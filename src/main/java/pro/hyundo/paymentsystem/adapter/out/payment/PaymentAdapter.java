package pro.hyundo.paymentsystem.adapter.out.payment;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import pro.hyundo.paymentsystem.adapter.out.payment.dto.TossConfirmRequest;
import pro.hyundo.paymentsystem.adapter.out.payment.dto.TossPaymentResponse;
import pro.hyundo.paymentsystem.adapter.out.persistence.mapper.TossPaymentMapper;
import pro.hyundo.paymentsystem.application.port.out.PaymentPort;
import pro.hyundo.paymentsystem.global.annotation.WebAdapter;
import retrofit2.Response;

@WebAdapter // Spring Bean으로 등록하기 위한 어노테이션
@RequiredArgsConstructor
public class PaymentAdapter implements PaymentPort {

    private final TossApi tossApi;
    private final TossPaymentMapper tossPaymentMapper;

    @Override
    public TossPaymentConfirmationResult confirmPayment(String paymentKey, UUID orderId, Integer amount) {
        TossConfirmRequest request = TossConfirmRequest.builder()
                .paymentKey(paymentKey)
                .orderId(orderId)
                .amount(amount)
                .build();

        try {
            Response<TossPaymentResponse> response = tossApi.confirmPayment(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                return tossPaymentMapper.mapToDomain(response.body());
            } else {
                String errorMessage = response.errorBody() != null ? response.errorBody().string() : response.message();
                throw new IOException("Toss API call failed: " + errorMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to call Toss API", e);
        }
    }
}
