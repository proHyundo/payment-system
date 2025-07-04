package pro.hyundo.paymentsystem.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.hyundo.paymentsystem.adapter.out.persistence.entity.OrderItemEntity;

import java.util.UUID;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItemEntity, UUID> {
}
