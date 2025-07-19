package pro.hyundo.paymentsystem.adapter.out.messaging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessEvent {

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("orderId")
    private UUID orderId;

    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("totalAmount")
    private Integer totalAmount;

    @JsonProperty("merchantId")
    private String merchantId;

    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}
