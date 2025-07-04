package pro.hyundo.paymentsystem.application.port.out;

import pro.hyundo.paymentsystem.domain.PurchaseOrder;

/**
 * 주문 정보를 저장하는 아웃바운드 포트입니다.
 */
public interface SavePurchaseOrderPort {
    void savePurchaseOrder(PurchaseOrder purchaseOrder);
}
