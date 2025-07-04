package pro.hyundo.paymentsystem.adapter.out.messaging;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pro.hyundo.paymentsystem.adapter.out.messaging.entity.EventOutboxEntity;
import pro.hyundo.paymentsystem.adapter.out.messaging.entity.EventStatus;
import pro.hyundo.paymentsystem.adapter.out.messaging.repository.EventOutboxJpaRepository;
import pro.hyundo.paymentsystem.application.port.out.SendPaymentEventPort;

@Component
@RequiredArgsConstructor
public class EventRelayScheduler {

    private final EventOutboxJpaRepository eventOutboxRepository;
    private final SendPaymentEventPort sendPaymentEventPort; // 실제 Kafka 발행은 기존 포트 재사용

//    @Scheduled(fixedDelay = 10000) // 10초마다 실행
//    @Transactional
//    public void relayEvents() {
//        List<EventOutboxEntity> pendingEvents = eventOutboxRepository.findByStatus(EventStatus.PENDING);
//
//        for (EventOutboxEntity event : pendingEvents) {
//            try {
//                // 이벤트 타입에 따라 적절한 토픽으로 발행
//                // 예시: PAYMENT_SUCCESS -> payment.success.v1 토픽
//                String topic = "payment." + event.getEventType().toLowerCase().replace('_', '.') + ".v1";
//                sendPaymentEventPort.sendPaymentSuccessEvent(topic, event.getPayload()); // topic과 payload로 발행
//
//                event.markAsPublished(); // 발행 성공 시 상태 변경
//                // 변경된 상태는 트랜잭션 종료 시 자동으로 DB에 반영 (save 호출 필요 없음)
//            } catch (Exception e) {
//                // TODO: 발행 실패 시 재시도 횟수 기록 또는 별도 처리 로직 추가
//                // 현재는 다음 주기에 재시도됨
//            }
//        }
//    }
}
