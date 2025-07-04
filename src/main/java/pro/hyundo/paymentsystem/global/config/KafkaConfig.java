package pro.hyundo.paymentsystem.global.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka Topic 자동 생성을 위한 설정
 */
@Configuration
public class KafkaConfig {

    @Value("${payment.kafka.topic.payment-success}")
    private String successTopic;

    @Value("${payment.kafka.topic.payment-failure}")
    private String failureTopic;

    @Bean
    public NewTopic paymentSuccessTopic() {
        return TopicBuilder.name(successTopic).build();
    }

    @Bean
    public NewTopic paymentFailureTopic() {
        return TopicBuilder.name(failureTopic).build();
    }
}
