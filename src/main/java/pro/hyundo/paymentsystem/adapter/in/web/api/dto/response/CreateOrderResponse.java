package pro.hyundo.paymentsystem.adapter.in.web.api.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class CreateOrderResponse {
    private UUID orderId;
    private String orderName;
    private Integer totalPrice;
}
