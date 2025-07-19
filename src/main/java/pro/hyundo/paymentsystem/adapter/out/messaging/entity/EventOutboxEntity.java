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
        
        // 재시도 관련 필드 추가
        private int retryCount = 0;
        private String errorMessage;
        private LocalDateTime lastRetryAt;

        @Builder
        public EventOutboxEntity(String aggregateId, String aggregateType, String eventType, String payload) {
            this.aggregateId = aggregateId;
            this.aggregateType = aggregateType;
            this.eventType = eventType;
            this.payload = payload;
        }

        /**
         * 발행 완료로 상태 변경
         */
        public void markAsPublished() {
            this.status = EventStatus.PUBLISHED;
            this.publishedAt = LocalDateTime.now();
        }

        /**
         * 재시도 실패 처리
         */
        public void markRetryFailed(String errorMessage) {
            this.retryCount++;
            this.errorMessage = errorMessage;
            this.lastRetryAt = LocalDateTime.now();
            
            if (this.retryCount >= 3) {
                this.status = EventStatus.FAILED; // 최종 실패 상태 유지
            } else {
                this.status = EventStatus.FAILED; // 재시도 가능한 실패
            }
        }
}
