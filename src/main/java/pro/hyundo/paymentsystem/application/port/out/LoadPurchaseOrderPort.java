package pro.hyundo.paymentsystem.application.port.out;

import java.util.UUID;
import pro.hyundo.paymentsystem.domain.PurchaseOrder;

public interface LoadPurchaseOrderPort {
    // 주문 조회 포트
    PurchaseOrder loadPurchaseOrder(UUID id);
}
