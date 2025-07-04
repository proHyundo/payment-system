package pro.hyundo.paymentsystem.application.port.out;

import pro.hyundo.paymentsystem.domain.CardPayment;
import pro.hyundo.paymentsystem.domain.PaymentTransaction;

/**
 * 결제 관련 정보를 저장하는 아웃바운드 포트입니다.
 */
public interface SavePaymentPort {

    /**
     * 결제 거래 원장과 카드 결제 상세 정보를 저장합니다.
     * @param paymentTransaction 저장할 결제 거래 원장 도메인 객체
     * @param cardPayment 저장할 카드 결제 상세 도메인 객체 (카드 결제가 아닌 경우 null일 수 있음)
     */
    void savePayment(PaymentTransaction paymentTransaction, CardPayment cardPayment);
}

