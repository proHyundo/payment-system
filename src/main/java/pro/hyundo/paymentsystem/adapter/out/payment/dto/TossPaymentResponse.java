package pro.hyundo.paymentsystem.adapter.out.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class TossPaymentResponse {
    private String version;
    private String paymentKey;
    private String type;
    private String orderId;
    private String orderName;
    private String currency;
    private String method;
    private Integer totalAmount;
    private Integer balanceAmount;
    private String status;
    private ZonedDateTime requestedAt;
    private ZonedDateTime approvedAt;
    private Card card;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    public static class Card {
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private String installmentPlanMonths;
        private String approveNo;
        private String cardType;
        private String ownerType;
    }
}
