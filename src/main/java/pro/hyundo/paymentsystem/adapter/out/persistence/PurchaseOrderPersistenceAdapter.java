package pro.hyundo.paymentsystem.adapter.out.persistence;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pro.hyundo.paymentsystem.adapter.out.persistence.entity.PurchaseOrderEntity;
import pro.hyundo.paymentsystem.adapter.out.persistence.mapper.PaymentPersistenceMapper;
import pro.hyundo.paymentsystem.adapter.out.persistence.repository.PurchaseOrderJpaRepository;
import pro.hyundo.paymentsystem.application.port.out.LoadPurchaseOrderPort;
import pro.hyundo.paymentsystem.application.port.out.SavePurchaseOrderPort;
import pro.hyundo.paymentsystem.domain.PurchaseOrder;

@Component
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderPersistenceAdapter implements LoadPurchaseOrderPort, SavePurchaseOrderPort {

    private final PurchaseOrderJpaRepository purchaseOrderJpaRepository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public PurchaseOrder loadPurchaseOrder(UUID orderId) {
        return purchaseOrderJpaRepository.findByOrderId(orderId)
                .map(mapper::mapToDomainEntity)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with orderId: " + orderId));
    }

    @Override
    public void savePurchaseOrder(PurchaseOrder purchaseOrder) {
        // DB에 해당 orderId가 있는지 먼저 조회합니다.
        PurchaseOrderEntity entity = purchaseOrderJpaRepository.findByOrderId(purchaseOrder.getOrderId())
                .map(existingEntity -> {
                    // UPDATE 경로: 이미 존재하면, 기존 엔티티의 상태를 업데이트합니다.
                    mapper.updateEntityFromDomain(existingEntity, purchaseOrder);
                    return existingEntity;
                })
                .orElseGet(() -> {
                    // CREATE 경로: 존재하지 않으면, 새로운 엔티티를 생성합니다.
                    return mapper.mapToJpaEntity(purchaseOrder);
                });

        // save 메소드는 엔티티의 ID 존재 여부에 따라 INSERT 또는 UPDATE를 자동으로 수행합니다.
        purchaseOrderJpaRepository.save(entity);
    }
}
