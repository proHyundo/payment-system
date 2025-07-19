package pro.hyundo.paymentsystem.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 주문(PurchaseOrder) 도메인 모델
 * - 주문 전체의 상태와 정보를 관리합니다.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PurchaseOrder {

    private final Long id;
    private final UUID orderId;
    private final String name;
    private final String phoneNumber;
    private OrderState orderState;
    private String paymentId;
    private final Integer totalPrice;
    private final List<OrderItem> orderItems;  // ← merchantId는 OrderItem에서 관리
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 주문에 포함된 모든 판매자 ID를 반환합니다.
     */
    public Set<String> getMerchantIds() {
        return orderItems.stream()
                .map(OrderItem::getMerchantId)
                .collect(Collectors.toSet());
    }

    /**
     * 특정 판매자의 주문 항목들을 반환합니다.
     */
    public List<OrderItem> getItemsByMerchant(String merchantId) {
        return orderItems.stream()
                .filter(item -> item.getMerchantId().equals(merchantId))
                .collect(Collectors.toList());
    }

    /**
     * 판매자별 주문 금액을 계산합니다.
     */
    public Map<String, Integer> getAmountByMerchant() {
        return orderItems.stream()
                .collect(Collectors.groupingBy(
                        OrderItem::getMerchantId,
                        Collectors.summingInt(OrderItem::getAmount)
                ));
    }

    /**
     * 판매자별 주문 항목 수를 계산합니다.
     */
    public Map<String, Long> getItemCountByMerchant() {
        return orderItems.stream()
                .collect(Collectors.groupingBy(
                        OrderItem::getMerchantId,
                        Collectors.counting()
                ));
    }

    /**
     * 결제가 완료되었을 때 호출됩니다.
     * 주문 및 하위 항목들의 상태를 '결제 완료'로 변경합니다.
     * @param paymentId 외부 결제 시스템의 ID
     */
    public void completePayment(String paymentId) {
        this.paymentId = paymentId;
        this.orderState = OrderState.PAID;
        this.updatedAt = LocalDateTime.now();
        this.orderItems.forEach(item -> item.updateOrderState(OrderState.PAID));
    }

    /**
     * 주문이 실패했을 때 호출됩니다.
     * 주문 및 하위 항목들의 상태를 '주문 실패'로 변경합니다.
     */
    public void failOrder() {
        this.orderState = OrderState.FAILED;
        this.updatedAt = LocalDateTime.now();
        this.orderItems.forEach(item -> item.updateOrderState(OrderState.FAILED));
    }
}
