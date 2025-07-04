package pro.hyundo.paymentsystem.adapter.out.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pro.hyundo.paymentsystem.application.port.out.SendPaymentEventPort;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventKafkaProducer implements SendPaymentEventPort {
    // Kafka 메시지 발행
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${payment.kafka.topic.payment-success}")
    private String successTopic;

    @Value("${payment.kafka.topic.payment-failure}")
    private String failureTopic;

    @Override
    public void sendPaymentSuccessEvent(String message) {
        log.info("Sending payment success event to Kafka: {}", message);
        kafkaTemplate.send(successTopic, message);
    }

    @Override
    public void sendPaymentFailureEvent(String message) {
        log.info("Sending payment failure event to Kafka: {}", message);
        kafkaTemplate.send(failureTopic, message);
    }
}
