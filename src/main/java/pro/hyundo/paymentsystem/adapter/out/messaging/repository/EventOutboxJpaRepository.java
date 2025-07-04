package pro.hyundo.paymentsystem.adapter.out.messaging.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pro.hyundo.paymentsystem.adapter.out.messaging.entity.EventOutboxEntity;
import pro.hyundo.paymentsystem.adapter.out.messaging.entity.EventStatus;

public interface EventOutboxJpaRepository extends JpaRepository<EventOutboxEntity, Long> {
    List<EventOutboxEntity> findByStatus(EventStatus status);
}
