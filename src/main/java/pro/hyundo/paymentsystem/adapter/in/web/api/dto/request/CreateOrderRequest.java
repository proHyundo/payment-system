package pro.hyundo.paymentsystem.adapter.in.web.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

@Getter
public class CreateOrderRequest {

    @NotEmpty
    private String customerName;

    @NotEmpty
    private String customerPhone;

    @NotNull
    @Valid // 리스트 내부의 객체들도 유효성 검사를 하도록 설정합니다.
    private List<OrderItemRequest> orderItems;

    @NotNull
    private Integer totalPrice;
}
