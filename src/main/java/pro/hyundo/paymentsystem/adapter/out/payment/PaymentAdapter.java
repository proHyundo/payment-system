package pro.hyundo.paymentsystem.adapter.out.payment;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import pro.hyundo.paymentsystem.adapter.out.payment.dto.TossConfirmRequest;
import pro.hyundo.paymentsystem.adapter.out.payment.dto.TossPaymentResponse;
import pro.hyundo.paymentsystem.adapter.out.persistence.mapper.TossPaymentMapper;
import pro.hyundo.paymentsystem.application.port.out.PaymentPort;
import pro.hyundo.paymentsystem.global.annotation.WebAdapter;
import retrofit2.Response;

@Slf4j
@WebAdapter
@RequiredArgsConstructor
public class PaymentAdapter implements PaymentPort {

    private final TossApi tossApi;
    private final TossPaymentMapper tossPaymentMapper;

    @Retryable(retryFor = {IOException.class, RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 1.5, maxDelay = 2500) // 500 -> 750 -> 1125 -> 1687ms
    )
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
                log.info("PG 결제 승인 성공 - orderId: {}", orderId);
                return tossPaymentMapper.mapToDomain(response.body());
            } else {
                String errorMessage = response.errorBody() != null ? response.errorBody().string() : response.message();
                throw new IOException("Toss API call failed: " + errorMessage);
            }
        } catch (IOException e) {
            log.info("PG 결제 승인 실패 - orderId: {}, error: {}", orderId, e.getMessage());
            throw new RuntimeException("Failed to call Toss API", e);
        }
    }

    @Recover
    public TossPaymentConfirmationResult recover(Exception ex, String paymentKey, UUID orderId, Integer amount) {
        log.error("PG 결제 승인 최종 실패 - orderId: {}, 모든 재시도 완료", orderId);

        // TODO : 관리자 알림 로직 추가

        throw new RuntimeException("PG 결제 승인 최종 실패 (재시도 4회)", ex);
    }
}
