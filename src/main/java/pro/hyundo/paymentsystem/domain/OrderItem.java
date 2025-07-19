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

    private final Long id;
    private final UUID orderId;
    private final Integer itemIdx;
    private final UUID productId;
    private final String productName;
    private final Integer productPrice;
    private final String productSize;
    private final Integer quantity;
    private final Integer amount;
    private final String merchantId;        // ← 핵심 추가!
    private OrderState orderState;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 특정 판매자의 상품인지 확인합니다.
     */
    public boolean belongsToMerchant(String merchantId) {
        return this.merchantId.equals(merchantId);
    }

    /**
     * 상품 정보 요약을 반환합니다.
     */
    public String getProductSummary() {
        return String.format("%s (%s) x%d - %s",
                productName, productSize, quantity, merchantId);
    }

    void updateOrderState(OrderState orderState) {
        this.orderState = orderState;
        this.updatedAt = LocalDateTime.now();
    }
}
