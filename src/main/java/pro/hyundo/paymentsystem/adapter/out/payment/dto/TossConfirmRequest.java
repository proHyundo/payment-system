package pro.hyundo.paymentsystem.adapter.out.payment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class TossConfirmRequest {
    private final String paymentKey;
    private final UUID orderId;
    private final Integer amount;

    @Builder
    public TossConfirmRequest(String paymentKey, UUID orderId, Integer amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }
}
