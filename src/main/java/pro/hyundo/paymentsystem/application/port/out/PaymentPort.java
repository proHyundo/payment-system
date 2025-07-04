package pro.hyundo.paymentsystem.application.port.out;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import pro.hyundo.paymentsystem.domain.CardPayment;
import pro.hyundo.paymentsystem.domain.PaymentTransaction;

/**
 * 외부 결제 게이트웨이(Toss Payments)와 통신하는 아웃바운드 포트입니다.
 * PG사가 변경되더라도 이 인터페이스는 변경되지 않습니다.
 */
public interface PaymentPort {

    /**
     * Toss Payments API에 결제 승인을 요청합니다.
     *
     * @param paymentKey 결제 위젯에서 발급된 결제 식별 키
     * @param orderId    주문 시스템에서 생성된 고유 주문 번호
     * @param amount     승인할 총 결제 금액
     * @return Toss API의 응답을 애플리케이션 도메인 모델로 변환한 결과 객체
     */
    TossPaymentConfirmationResult confirmPayment(String paymentKey, UUID orderId, Integer amount);

    /**
     * Toss API의 결제 승인 응답을 애플리케이션의 도메인 모델로 변환한 결과물입니다.
     */
    @Builder
    @Getter
    class TossPaymentConfirmationResult {
        private final PaymentTransaction paymentTransaction;
        private final CardPayment cardPayment; // 카드 결제가 아닐 경우 null일 수 있습니다.
    }
}
