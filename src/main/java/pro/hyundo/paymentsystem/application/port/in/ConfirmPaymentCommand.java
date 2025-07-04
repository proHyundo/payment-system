package pro.hyundo.paymentsystem.application.port.in;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

/**
 * 결제 승인을 위한 Command 객체.
 */
@Getter
@Builder
public class ConfirmPaymentCommand {
    private final String paymentKey;
    private final UUID orderId;
    private final Integer amount;
}
