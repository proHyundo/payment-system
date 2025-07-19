package pro.hyundo.paymentsystem.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
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
    private final SendPaymentEventPort sendPaymentEventPort;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void relayEvents() {
        List<EventOutboxEntity> pendingEvents = eventOutboxRepository.findByStatus(EventStatus.PENDING);

        for (EventOutboxEntity event : pendingEvents) {
            try {
                // 이벤트 타입에 따라 적절한 메서드 호출
                if ("PAYMENT_SUCCESS".equals(event.getEventType())) {
                    PaymentSuccessEvent successEvent = objectMapper.readValue(
                            event.getPayload(), PaymentSuccessEvent.class);
                    sendPaymentEventPort.sendPaymentSuccessEvent(successEvent);

                } else if ("PAYMENT_FAILED".equals(event.getEventType())) {
                    PaymentFailureEvent failureEvent = objectMapper.readValue(
                            event.getPayload(), PaymentFailureEvent.class);
                    sendPaymentEventPort.sendPaymentFailureEvent(failureEvent);
                }

                event.markAsPublished();
                eventOutboxRepository.save(event);

                log.info("이벤트 발행 성공 - eventId: {}, type: {}",
                        event.getId(), event.getEventType());

            } catch (Exception e) {
                log.error("이벤트 발행 실패 - eventId: {}", event.getId(), e);
                event.markRetryFailed();
            }
        }
    }
}
