package pro.hyundo.paymentsystem.application.port.out;

import pro.hyundo.paymentsystem.adapter.out.messaging.dto.PaymentFailureEvent;
import pro.hyundo.paymentsystem.adapter.out.messaging.dto.PaymentSuccessEvent;

/**
 * 결제 관련 이벤트를 외부 메시징 시스템으로 전송하는 아웃바운드 포트입니다.
 */
public interface SendPaymentEventPort {

    /**
     * 결제 성공 이벤트를 발행합니다.
     * @param event 결제 성공 이벤트 객체
     */
    void sendPaymentSuccessEvent(PaymentSuccessEvent event);

    /**
     * 결제 실패 이벤트를 발행합니다.
     * @param event 결제 실패 이벤트 객체
     */
    void sendPaymentFailureEvent(PaymentFailureEvent event);
}
