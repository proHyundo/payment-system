package pro.hyundo.paymentsystem.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
    private final Long id; // 데이터베이스 ID
    private final UUID orderId; // 고유 주문 번호 (비즈니스 키)
    private final String name; // 주문자명
    private final String phoneNumber; // 주문자 휴대전화번호
    private OrderState orderState; // 주문 상태
    private String paymentId; // 결제 정보 ID (FK)
    private final Integer totalPrice; // 총 주문 금액
    private final List<OrderItem> orderItems; // 주문 상품 목록
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
