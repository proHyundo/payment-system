package pro.hyundo.paymentsystem.adapter.in.web.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pro.hyundo.paymentsystem.global.annotation.WebAdapter;
import reactor.core.publisher.Mono;

@WebAdapter
@Controller
@RequiredArgsConstructor
public class PaymentViewController {
    // 결제 성공/실패 페이지 컨트롤러

    /**
     * 결제 성공 페이지(success.html)를 렌더링합니다.
     */
    @GetMapping("/success")
    public Mono<String> successPage() {
        return Mono.just("success");
    }

    /**
     * 결제 실패 페이지(fail.html)를 렌더링합니다.
     */
    @GetMapping("/fail")
    public Mono<String> failPage() {
        return Mono.just("fail");
    }

}
