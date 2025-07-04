package pro.hyundo.paymentsystem.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 주문 항목(OrderItem) 도메인 모델
 * - 주문에 포함된 개별 상품의 정보를 나타냅니다.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItem {
    private final Long id; // 데이터베이스 ID
    private final UUID orderId; // PurchaseOrder의 ID (FK)
    private final Integer itemIdx; // 주문 내 상품 순서
    private final UUID productId; // 상품 ID
    private final String productName; // 상품명
    private final Integer productPrice; // 상품 가격
    private final String productSize; // 상품 사이즈
    private final Integer quantity; // 수량
    private final Integer amount; // 금액 (가격 * 수량)
    private OrderState orderState; // 개별 주문 상태
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 상위 주문의 상태 변경에 따라 개별 항목의 상태를 업데이트합니다.
     * @param orderState 새로운 주문 상태
     */
    void updateOrderState(OrderState orderState) {
        this.orderState = orderState;
        this.updatedAt = LocalDateTime.now();
    }
}
