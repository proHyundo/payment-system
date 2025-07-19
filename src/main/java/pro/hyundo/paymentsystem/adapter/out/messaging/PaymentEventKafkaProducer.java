package pro.hyundo.paymentsystem.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pro.hyundo.paymentsystem.adapter.out.messaging.dto.PaymentFailureEvent;
import pro.hyundo.paymentsystem.adapter.out.messaging.dto.PaymentSuccessEvent;
import pro.hyundo.paymentsystem.application.port.out.SendPaymentEventPort;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventKafkaProducer implements SendPaymentEventPort {

    // Kafka 메시지 발행
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${payment.kafka.topic.payment-success}")
    private String successTopic;

    @Value("${payment.kafka.topic.payment-failure}")
    private String failureTopic;

    @Override
    public void sendPaymentSuccessEvent(PaymentSuccessEvent event) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(event);
            log.info("Sending payment success event to Kafka: {}", jsonMessage);
            kafkaTemplate.send(successTopic, jsonMessage);
        } catch (Exception e) {
            log.error("Failed to send payment success event", e);
            throw new RuntimeException("Failed to send event", e);
        }
    }

    @Override
    public void sendPaymentFailureEvent(PaymentFailureEvent event) {

    }

}
