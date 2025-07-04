package pro.hyundo.paymentsystem.adapter.in.web.api.dto.request;

import java.util.UUID;
import lombok.Getter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Getter
public class ConfirmPaymentRequest {

    @NotEmpty private String paymentKey;
    @NotNull private UUID orderId;
    @NotNull private Integer amount;
}
