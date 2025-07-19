package pro.hyundo.paymentsystem.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import pro.hyundo.paymentsystem.domain.OrderState;

@Entity
@Table(name = "t_order_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"order_id", "item_idx", "product_id"})
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("주문 상세 ID")
    private Long id;

    // 단방향 연관관계로 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "orderId", nullable = false)
    @Comment("주문번호 - FK")
    private PurchaseOrderEntity purchaseOrder;

    @Column(nullable = false)
    @Comment("같은 주문 내에서 몇 번째 항목인지")
    private Integer itemIdx;

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    @Comment("상품번호")
    private UUID productId;

    @Column(nullable = false)
    @Comment("상품명")
    private String productName;

    @Column(nullable = false)
    @Comment("상품 가격")
    private Integer productPrice;

    @Column(nullable = false)
    @Comment("상품 사이즈")
    private String productSize;

    @Column(nullable = false)
    @Comment("주문 수량")
    private Integer quantity;

    @Column(nullable = false)
    @Comment("총 가격(상품 가격 * 주문 수량)")
    private Integer amount;

    @Column(nullable = false)
    @Comment("상품 판매자 ID")
    private String merchantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("개별 주문상태")
    private OrderState orderState;

    public OrderItemEntity(Integer itemIdx, UUID productId, String productName, Integer productPrice,
                           String productSize, Integer quantity, Integer amount, String merchantId,
                           OrderState orderState) {
        this.itemIdx = itemIdx;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productSize = productSize;
        this.quantity = quantity;
        this.amount = amount;
        this.merchantId = merchantId;
        this.orderState = orderState;
    }

    public void setPurchaseOrder(PurchaseOrderEntity purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
}
