package pro.hyundo.paymentsystem.application.port.out;

/**
 * 결제 관련 이벤트를 외부 메시징 시스템으로 전송하는 아웃바운드 포트입니다.
 */
public interface SendPaymentEventPort {

    /**
     * 결제 성공 이벤트를 발행합니다.
     * @param message 발행할 메시지 (주로 주문 ID나 관련 정보를 포함)
     */
    void sendPaymentSuccessEvent(String message);

    /**
     * 결제 실패 이벤트를 발행합니다.
     * @param message 발행할 메시지
     */
    void sendPaymentFailureEvent(String message);
}
