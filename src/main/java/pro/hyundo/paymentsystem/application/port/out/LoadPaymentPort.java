package pro.hyundo.paymentsystem.application.port.out;

import pro.hyundo.paymentsystem.domain.CardPayment;
import pro.hyundo.paymentsystem.domain.PaymentTransaction;

/**
 * 결제 관련 정보를 불러오는 아웃바운드 포트입니다.
 */
public interface LoadPaymentPort {

    /**
     * 거래 ID를 기준으로 결제 거래 원장 정보를 조회합니다.
     * @param paymentId 조회할 거래의 ID
     * @return PaymentTransaction
     */
    PaymentTransaction loadPaymentTransaction(String paymentId);

    /**
     * 결제 키를 기준으로 카드 결제 상세 정보를 조회합니다.
     * @param paymentKey 조회할 카드 결제의 키
     * @return CardPayment
     */
    CardPayment loadCardPayment(String paymentKey);

}
