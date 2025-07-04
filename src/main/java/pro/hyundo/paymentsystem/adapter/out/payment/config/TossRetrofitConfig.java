package pro.hyundo.paymentsystem.adapter.out.payment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.hyundo.paymentsystem.adapter.out.payment.TossApi;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Toss Payments API 연동을 위한 Retrofit 설정 클래스.
 * PG사(Toss)에 특화된 설정을 담당하며, 해당 어댑터 패키지 내에 위치합니다.
 */
@Configuration
public class TossRetrofitConfig {

    @Value("${payment.toss.api.baseUrl}")
    private String baseUrl;

    @Value("${payment.toss.api.secretKey}")
    private String secretKey;

    @Bean
    public OkHttpClient tossOkHttpClient() { // Bean 이름 명확화
        // Toss Payments API는 Basic 인증을 사용합니다.
        byte[] encodedKey = Base64.getEncoder().encode((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorization = "Basic " + new String(encodedKey);

        // 인증 헤더를 추가하는 인터셉터 설정
        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", authorization)
                            .header("Content-Type", "application/json");
                    Request newRequest = requestBuilder.build();
                    return chain.proceed(newRequest);
                })
                .build();
    }

    @Bean
    public TossApi tossApi(OkHttpClient tossOkHttpClient) { // 명확화된 Bean 이름을 받도록 수정
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Java 8 날짜/시간 타입 지원

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(tossOkHttpClient)
                .build();

        return retrofit.create(TossApi.class);
    }
}
