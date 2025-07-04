package pro.hyundo.paymentsystem.adapter.in.web.api.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConfirmPaymentResponse {
    private UUID orderId;
    private String status;
}
