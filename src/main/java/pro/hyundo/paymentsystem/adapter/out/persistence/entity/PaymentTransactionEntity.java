package pro.hyundo.paymentsystem.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import pro.hyundo.paymentsystem.domain.PaymentStatus;

@Entity
@Table(name = "payment_transaction", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"payment_id", "method", "payment_status"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentTransactionEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("거래 ID")
    private Long id;

    @Column(nullable = false)
    @Comment("거래 번호(ID)")
    private String paymentId;

    @Column(nullable = false)
    @Comment("거래 수단")
    private String method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("거래 상태")
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    @Comment("최종 결제 금액")
    private Integer totalAmount;

    @Column(nullable = false)
    @Comment("취소 가능한 금액(잔고)")
    private Integer balanceAmount;

    @Column(nullable = false)
    @Comment("취소된 총 금액")
    private Integer canceledAmount;

    public PaymentTransactionEntity(String paymentId, String method, PaymentStatus paymentStatus, Integer totalAmount, Integer balanceAmount, Integer canceledAmount) {
        this.paymentId = paymentId;
        this.method = method;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.balanceAmount = balanceAmount;
        this.canceledAmount = canceledAmount;
    }
}
