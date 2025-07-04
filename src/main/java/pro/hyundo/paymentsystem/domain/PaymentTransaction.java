package pro.hyundo.paymentsystem.domain;


import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 결제 거래(PaymentTransaction) 도메인 모델
 * - 하나의 결제에 대한 거래 원장 정보를 관리합니다.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentTransaction {
    private final Long id; // 데이터베이스 ID
    private final String paymentId; // 거래 번호 (비즈니스 키)
    private final String method; // 결제 수단
    private PaymentStatus paymentStatus; // 거래 상태
    private final Integer totalAmount; // 최초 승인 금액
    private Integer balanceAmount; // 취소 가능 잔액
    private Integer canceledAmount; // 총 취소 금액
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 거래 상태를 업데이트합니다.
     *
     * @param newStatus 새로운 거래 상태
     */
    public void updateStatus(PaymentStatus newStatus) {
        this.paymentStatus = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 결제 취소를 처리합니다.
     *
     * @param cancelAmount 취소할 금액
     * @throws IllegalArgumentException 취소 요청 금액이 잔액을 초과할 경우 발생
     */
    public void cancel(int cancelAmount) {
        if (this.balanceAmount < cancelAmount) {
            throw new IllegalArgumentException("취소 요청 금액이 취소 가능 잔액보다 큽니다.");
        }
        this.balanceAmount -= cancelAmount;
        this.canceledAmount += cancelAmount;
        this.paymentStatus = (this.balanceAmount == 0) ? PaymentStatus.CANCELED : PaymentStatus.PARTIAL_CANCELED;
        this.updatedAt = LocalDateTime.now();
    }
}
