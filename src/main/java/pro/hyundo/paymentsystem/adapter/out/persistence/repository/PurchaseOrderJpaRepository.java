package pro.hyundo.paymentsystem.adapter.out.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.hyundo.paymentsystem.adapter.out.persistence.entity.PurchaseOrderEntity;

import java.util.UUID;

@Repository
public interface PurchaseOrderJpaRepository extends JpaRepository<PurchaseOrderEntity, UUID> {

    /**
     * 주문번호(UUID)를 기준으로 주문 정보를 조회합니다.
     * @param orderId 조회할 주문의 UUID
     * @return Optional<PurchaseOrderEntity>
     */
    Optional<PurchaseOrderEntity> findByOrderId(UUID orderId);

}
