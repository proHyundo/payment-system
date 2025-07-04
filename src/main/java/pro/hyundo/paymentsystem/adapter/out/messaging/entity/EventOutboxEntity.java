package pro.hyundo.paymentsystem.adapter.out.messaging.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_event_outbox")
@Getter
@NoArgsConstructor
public class EventOutboxEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String aggregateId;
        private String aggregateType;
        private String eventType;

        @Column(columnDefinition = "JSON")
        private String payload;

        @Enumerated(EnumType.STRING)
        private EventStatus status = EventStatus.PENDING;

        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime publishedAt;

        @Builder
        public EventOutboxEntity(String aggregateId, String aggregateType, String eventType, String payload) {
            this.aggregateId = aggregateId;
            this.aggregateType = aggregateType;
            this.eventType = eventType;
            this.payload = payload;
        }

        public void markAsPublished() {
            this.status = EventStatus.PUBLISHED;
            this.publishedAt = LocalDateTime.now();
        }
}
