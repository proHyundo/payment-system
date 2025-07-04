package pro.hyundo.paymentsystem.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import pro.hyundo.paymentsystem.domain.*;
import pro.hyundo.paymentsystem.adapter.out.persistence.entity.*;

import java.util.List;
import java.util.stream.Collectors;

// =================== Mapper ===================

/**
 * 도메인 객체와 JPA 엔티티 간의 매핑을 담당하는 클래스
 */
@Component
public class PaymentPersistenceMapper {

    public PurchaseOrder mapToDomainEntity(PurchaseOrderEntity purchaseOrderEntity) {
        List<OrderItem> orderItems = purchaseOrderEntity.getOrderItems().stream()
                .map(this::mapToDomainEntity)
                .collect(Collectors.toList());

        return PurchaseOrder.builder()
                .id(purchaseOrderEntity.getId())
                .orderId(purchaseOrderEntity.getOrderId())
                .name(purchaseOrderEntity.getName())
                .phoneNumber(purchaseOrderEntity.getPhoneNumber())
                .orderState(purchaseOrderEntity.getOrderState())
                .paymentId(purchaseOrderEntity.getPaymentId())
                .totalPrice(purchaseOrderEntity.getTotalPrice())
                .orderItems(orderItems)
                .createdAt(purchaseOrderEntity.getCreatedAt())
                .updatedAt(purchaseOrderEntity.getUpdatedAt())
                .build();
    }

    OrderItem mapToDomainEntity(OrderItemEntity orderItemEntity) {
        return OrderItem.builder()
                .id(orderItemEntity.getId())
                .orderId(orderItemEntity.getPurchaseOrder().getOrderId())
                .itemIdx(orderItemEntity.getItemIdx())
                .productId(orderItemEntity.getProductId())
                .productName(orderItemEntity.getProductName())
                .productPrice(orderItemEntity.getProductPrice())
                .productSize(orderItemEntity.getProductSize())
                .quantity(orderItemEntity.getQuantity())
                .amount(orderItemEntity.getAmount())
                .orderState(orderItemEntity.getOrderState())
                .createdAt(orderItemEntity.getCreatedAt())
                .updatedAt(orderItemEntity.getUpdatedAt())
                .build();
    }

    public PaymentTransaction mapToDomainEntity(PaymentTransactionEntity paymentTransactionEntity) {
        return PaymentTransaction.builder()
                .id(paymentTransactionEntity.getId())
                .paymentId(paymentTransactionEntity.getPaymentId())
                .method(paymentTransactionEntity.getMethod())
                .paymentStatus(paymentTransactionEntity.getPaymentStatus())
                .totalAmount(paymentTransactionEntity.getTotalAmount())
                .balanceAmount(paymentTransactionEntity.getBalanceAmount())
                .canceledAmount(paymentTransactionEntity.getCanceledAmount())
                .createdAt(paymentTransactionEntity.getCreatedAt())
                .updatedAt(paymentTransactionEntity.getUpdatedAt())
                .build();
    }

    public CardPayment mapToDomainEntity(CardPaymentEntity cardPaymentEntity) {
        return CardPayment.builder()
                .paymentKey(cardPaymentEntity.getPaymentKey())
                .cardNumber(cardPaymentEntity.getCardNumber())
                .approveNo(cardPaymentEntity.getApproveNo())
                .acquireStatus(cardPaymentEntity.getAcquireStatus())
                .issuerCode(cardPaymentEntity.getIssuerCode())
                .acquirerCode(cardPaymentEntity.getAcquirerCode())
                .build();
    }

    public PurchaseOrderEntity mapToJpaEntity(PurchaseOrder purchaseOrder) {
        PurchaseOrderEntity entity = new PurchaseOrderEntity(
                purchaseOrder.getOrderId(),
                purchaseOrder.getName(),
                purchaseOrder.getPhoneNumber(),
                purchaseOrder.getOrderState(),
                purchaseOrder.getTotalPrice()
        );

        purchaseOrder.getOrderItems().stream()
                .map(this::mapToJpaEntity)
                .forEach(entity::addOrderItem);

        return entity;
    }

    OrderItemEntity mapToJpaEntity(OrderItem orderItem) {
        return new OrderItemEntity(
                orderItem.getItemIdx(),
                orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getProductPrice(),
                orderItem.getProductSize(),
                orderItem.getQuantity(),
                orderItem.getAmount(),
                orderItem.getOrderState()
        );
    }

    /**
     * 기존에 존재하는 엔티티의 상태를 도메인 객체의 상태로 업데이트합니다.
     * @param entity DB에서 불러온 영속성 상태의 엔티티
     * @param domain 업데이트할 상태를 가진 도메인 객체
     */
    public void updateEntityFromDomain(PurchaseOrderEntity entity, PurchaseOrder domain) {
        // 주문 상태와 결제 ID 등 변경 가능한 상태를 업데이트합니다.
        entity.setOrderState(domain.getOrderState());
        entity.setPaymentId(domain.getPaymentId());

        // 각 주문 항목의 상태도 동기화합니다.
        entity.getOrderItems().forEach(orderItemEntity -> {
            domain.getOrderItems().stream()
                    .filter(orderItemDomain -> orderItemEntity.getItemIdx().equals(orderItemDomain.getItemIdx()))
                    .findFirst()
                    .ifPresent(orderItemDomain -> orderItemEntity.setOrderState(orderItemDomain.getOrderState()));
        });
    }

}
