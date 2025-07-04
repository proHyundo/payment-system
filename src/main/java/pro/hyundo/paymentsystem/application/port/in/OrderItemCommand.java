package pro.hyundo.paymentsystem.application.port.in;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

/**
 * 주문 항목을 위한 Command 객체.
 */
@Getter
@Builder
public class OrderItemCommand {
    private final UUID productId;
    private final String productName;
    private final Integer productPrice;
    private final String productSize;
    private final Integer quantity;
}
