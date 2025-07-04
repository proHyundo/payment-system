package pro.hyundo.paymentsystem.adapter.in.web.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderItemRequest {

    @NotNull
    private UUID productId;

    @NotEmpty
    private String productName;

    @NotNull
    private Integer productPrice;

    @NotEmpty
    private String productSize;

    @NotNull
    private Integer quantity;
}
