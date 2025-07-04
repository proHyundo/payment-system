package pro.hyundo.paymentsystem.adapter.out.payment;

import pro.hyundo.paymentsystem.adapter.out.payment.dto.TossConfirmRequest;
import pro.hyundo.paymentsystem.adapter.out.payment.dto.TossPaymentResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface TossApi {

    @POST("v1/payments/confirm")
    Call<TossPaymentResponse> confirmPayment(@Body TossConfirmRequest request);
}
