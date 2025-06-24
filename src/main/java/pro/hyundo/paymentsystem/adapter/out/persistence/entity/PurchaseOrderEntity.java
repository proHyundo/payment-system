package pro.hyundo.paymentsystem.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.Comment;
import pro.hyundo.paymentsystem.domain.OrderState;

@Entity
@Table(name = "t_purchase_order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseOrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("주문 ID")
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "BINARY(16)")
    @Comment("주문번호")
    private UUID orderId;

    @Column(nullable = false)
    @Comment("주문자명")
    private String name;

    @Column(nullable = false)
    @Comment("주문자 휴대전화번호")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("주문상태")
    private OrderState orderState;

    @Comment("결제정보")
    private String paymentId;

    @Column(nullable = false)
    @Comment("상품 가격 * 주문 수량")
    private Integer totalPrice;

    // 양방향 연관관계 설정. CascadeType.ALL과 orphanRemoval=true로 생명주기를 일치시킴
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<OrderItemEntity> orderItems = new ArrayList<>();

    public PurchaseOrderEntity(UUID orderId, String name, String phoneNumber, OrderState orderState, Integer totalPrice) {
        this.orderId = orderId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.orderState = orderState;
        this.totalPrice = totalPrice;
    }

    public void addOrderItem(OrderItemEntity orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setPurchaseOrder(this);
    }
}
