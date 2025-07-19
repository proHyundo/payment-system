package pro.hyundo.paymentsystem.adapter.out.messaging.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.hyundo.paymentsystem.adapter.out.messaging.entity.EventOutboxEntity;
import pro.hyundo.paymentsystem.adapter.out.messaging.entity.EventStatus;

public interface EventOutboxJpaRepository extends JpaRepository<EventOutboxEntity, Long> {
    
    List<EventOutboxEntity> findByStatus(EventStatus status);

    @Query(value = """
        SELECT * FROM t_event_outbox 
        WHERE status = 'PENDING' 
        ORDER BY created_at ASC 
        LIMIT :limit 
        FOR UPDATE SKIP LOCKED
        """, nativeQuery = true)
    List<EventOutboxEntity> findPendingEventsWithSkipLocked(@Param("limit") int limit);
    
    // 재시도 가능한 실패 이벤트 조회
    List<EventOutboxEntity> findByStatusAndRetryCountLessThan(EventStatus status, int maxRetryCount);
}
