package pro.hyundo.paymentsystem.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import pro.hyundo.paymentsystem.adapter.out.payment.dto.TossPaymentResponse;
import pro.hyundo.paymentsystem.application.port.out.PaymentPort.TossPaymentConfirmationResult;
import pro.hyundo.paymentsystem.domain.AcquireStatus;
import pro.hyundo.paymentsystem.domain.CardPayment;
import pro.hyundo.paymentsystem.domain.PaymentStatus;
import pro.hyundo.paymentsystem.domain.PaymentTransaction;

@Component // Spring Bean으로 등록하기 위한 어노테이션
public class TossPaymentMapper {

    public TossPaymentConfirmationResult mapToDomain(TossPaymentResponse response) {
        CardPayment cardPayment = null;
        if (response.getCard() != null) {
            cardPayment = CardPayment.builder()
                    .paymentKey(response.getPaymentKey())
                    .cardNumber(response.getCard().getNumber())
                    .approveNo(response.getCard().getApproveNo())
                    .acquireStatus(AcquireStatus.READY)
                    .issuerCode(response.getCard().getIssuerCode())
                    .acquirerCode(response.getCard().getAcquirerCode())
                    .build();
        }

        PaymentTransaction paymentTransaction = PaymentTransaction.builder()
                .paymentId(response.getPaymentKey())
                .method(response.getMethod())
                .paymentStatus(mapToPaymentStatus(response.getStatus()))
                .totalAmount(response.getTotalAmount())
                .balanceAmount(response.getBalanceAmount())
                .canceledAmount(0)
                .build();

        return TossPaymentConfirmationResult.builder()
                .paymentTransaction(paymentTransaction)
                .cardPayment(cardPayment)
                .build();
    }

    private PaymentStatus mapToPaymentStatus(String tossStatus) {
        return switch (tossStatus) {
            case "DONE" -> PaymentStatus.SUCCESS;
            case "CANCELED" -> PaymentStatus.CANCELED;
            case "PARTIAL_CANCELED" -> PaymentStatus.PARTIAL_CANCELED;
            default -> PaymentStatus.FAILURE;
        };
    }
}
