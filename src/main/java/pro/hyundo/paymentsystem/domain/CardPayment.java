package pro.hyundo.paymentsystem.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 카드 결제(CardPayment) 도메인 모델
 * - 카드 결제와 관련된 상세 정보를 관리합니다.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CardPayment {
    private final String paymentKey; // 결제 키 (PK)
    private final String cardNumber; // 마스킹된 카드 번호
    private final String approveNo; // 카드 승인 번호
    private AcquireStatus acquireStatus; // 카드 매입 상태
    private final String issuerCode; // 카드 발급사 코드
    private final String acquirerCode; // 카드 매입사 코드

    /**
     * 카드 매입 상태를 업데이트합니다.
     * @param newStatus 새로운 매입 상태
     */
    public void updateAcquireStatus(AcquireStatus newStatus) {
        this.acquireStatus = newStatus;
    }
}
