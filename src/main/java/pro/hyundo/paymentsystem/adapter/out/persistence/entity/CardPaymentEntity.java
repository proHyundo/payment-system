package pro.hyundo.paymentsystem.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import pro.hyundo.paymentsystem.domain.AcquireStatus;

@Entity
@Table(name = "card_payment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"payment_key", "card_number", "approve_no"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardPaymentEntity extends BaseTimeEntity{

    @Id
    @Column(name = "payment_key")
    @Comment("결제번호(paymentKey)")
    private String paymentKey;

    @Column(nullable = false)
    @Comment("카드번호")
    private String cardNumber;

    @Column(nullable = false)
    @Comment("카드 승인 번호")
    private String approveNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("카드결제 매입 상태")
    private AcquireStatus acquireStatus;

    @Comment("카드 발급사 코드")
    private String issuerCode;

    @Column(nullable = false)
    @Comment("카드 매입사 코드")
    private String acquirerCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("카드 결제의 상태")
    private AcquireStatus acquirerStatus; // 테이블 정의서에는 acquirer_status로 되어있음

    public CardPaymentEntity(String paymentKey, String cardNumber, String approveNo, AcquireStatus acquireStatus,
                             String issuerCode, String acquirerCode, AcquireStatus acquirerStatus) {
        this.paymentKey = paymentKey;
        this.cardNumber = cardNumber;
        this.approveNo = approveNo;
        this.acquireStatus = acquireStatus;
        this.issuerCode = issuerCode;
        this.acquirerCode = acquirerCode;
        this.acquirerStatus = acquirerStatus;
    }
}
