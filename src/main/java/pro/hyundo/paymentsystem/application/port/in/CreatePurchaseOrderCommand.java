package pro.hyundo.paymentsystem.application.port.in;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 주문 생성을 위한 Command 객체.
 * Use Case에 필요한 데이터를 전달합니다.
 */
@Getter
@Builder
public class CreatePurchaseOrderCommand {
    private final String customerName;
    private final String customerPhone;
    private final List<OrderItemCommand> orderItems;
    private final Integer totalPrice;
}
