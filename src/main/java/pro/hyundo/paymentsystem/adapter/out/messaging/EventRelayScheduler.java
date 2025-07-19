package pro.hyundo.paymentsystem.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pro.hyundo.paymentsystem.adapter.out.messaging.dto.PaymentFailureEvent;
import pro.hyundo.paymentsystem.adapter.out.messaging.dto.PaymentSuccessEvent;
import pro.hyundo.paymentsystem.adapter.out.messaging.entity.EventOutboxEntity;
import pro.hyundo.paymentsystem.adapter.out.messaging.entity.EventStatus;
import pro.hyundo.paymentsystem.adapter.out.messaging.repository.EventOutboxJpaRepository;
import pro.hyundo.paymentsystem.application.port.out.SendPaymentEventPort;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventRelayScheduler {

    private final EventOutboxJpaRepository eventOutboxRepository;
    private final PaymentEventKafkaProducer kafkaProducer;
    private final SendPaymentEventPort sendPaymentEventPort;
    private final ObjectMapper objectMapper;

    /**
     * PENDING 이벤트를 Kafka로 발행 (동시성 문제 해결: SKIP LOCKED 사용)
     */
    @Scheduled(fixedDelay = 5000) // 5초마다 실행
    @Transactional
    public void relayPendingEvents() {
        List<EventOutboxEntity> pendingEvents = eventOutboxRepository.findPendingEventsWithSkipLocked(10);
        
        if (pendingEvents.isEmpty()) {
            return;
        }

        for (EventOutboxEntity event : pendingEvents) {
            try {
                publishEventToKafka(event);
                event.markAsPublished();
                log.info("이벤트 발행 성공");
            } catch (Exception e) {
                log.error("이벤트 발행 실패 - eventId: {}, type: {}",  event.getId(), event.getEventType(), e);
                markEventFailed(event, e.getMessage());
            }
        }
    }

    /**
     * FAILED 이벤트 재시도 처리
     */
    @Scheduled(fixedDelay = 30000) // 30초마다 실행
    @Transactional
    public void retryFailedEvents() {
        List<EventOutboxEntity> failedEvents = eventOutboxRepository
            .findByStatusAndRetryCountLessThan(EventStatus.FAILED, 3);
        
        if (failedEvents.isEmpty()) {
            return;
        }
        
        log.info("실패 이벤트 재시도 시작 - 이벤트 수: {}", failedEvents.size());

        for (EventOutboxEntity event : failedEvents) {
            try {
                publishEventToKafka(event);
                event.markAsPublished();
                
                log.info("재시도 성공 - eventId: {}, retryCount: {}", 
                        event.getId(), event.getRetryCount());
                        
            } catch (Exception e) {
                log.warn("재시도 실패 - eventId: {}, retryCount: {}", 
                        event.getId(), event.getRetryCount(), e);
                        
                markEventFailed(event, e.getMessage());
                
                if (event.getStatus() == EventStatus.FAILED && event.getRetryCount() >= 3) {
                    handlePublishRetryFailedEvent(event);
                }
            }
        }
    }

    /**
     * 이벤트를 Kafka로 발행
     */
    private void publishEventToKafka(EventOutboxEntity event) throws Exception {
        switch (event.getEventType()) {
            case "PAYMENT_SUCCESS":
                kafkaProducer.sendPaymentSuccessEvent(
                        objectMapper.readValue(event.getPayload(), PaymentSuccessEvent.class));
                break;
                
            case "PAYMENT_FAILED":
                kafkaProducer.sendPaymentFailureEvent(
                        objectMapper.readValue(event.getPayload(), PaymentFailureEvent.class)
                );
                break;
                
            default:
                throw new IllegalArgumentException("Unknown event type: " + event.getEventType());
        }
    }

    /**
     * 이벤트 실패 처리
     */
    private void markEventFailed(EventOutboxEntity event, String errorMessage) {
        event.markRetryFailed(errorMessage);
    }

    /**
     * 재시도 실패 이벤트 처리 (최종 실패)
     */
    private void handlePublishRetryFailedEvent(EventOutboxEntity event) {
        log.error("재시도 최종 실패 - eventId: {}, aggregateId: {}, eventType: {}, error: {}",
                event.getId(), event.getAggregateId(), event.getEventType(), event.getErrorMessage());
        
        // TODO: 관리자 알림 발송
    }
}
