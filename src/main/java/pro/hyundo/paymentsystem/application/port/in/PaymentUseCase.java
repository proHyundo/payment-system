package pro.hyundo.paymentsystem.application.port.in;

import pro.hyundo.paymentsystem.domain.PurchaseOrder;

/**
 * 인바운드 포트 (Inbound Port / UseCase)
 * 애플리케이션의 핵심 비즈니스 로직을 정의합니다.
 */
public interface PaymentUseCase {
    /**
     * 새로운 주문을 생성합니다. (결제 전)
     * @param command 주문 생성에 필요한 정보
     * @return 생성된 주문 정보
     */
    PurchaseOrder createPurchaseOrder(CreatePurchaseOrderCommand command);

    /**
     * 결제 승인을 요청하고 처리합니다.
     * @param command 결제 승인에 필요한 정보
     * @return 처리된 주문 정보
     */
    PurchaseOrder confirmPayment(ConfirmPaymentCommand command);
}


