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
public class PaymentFailureEvent {

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("orderId")
    private UUID orderId;

    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("failureReason")
    private String failureReason;

    @JsonProperty("failureCode")
    private String failureCode;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}
