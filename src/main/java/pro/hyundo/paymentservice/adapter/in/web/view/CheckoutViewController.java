package pro.hyundo.paymentservice.adapter.in.web.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pro.hyundo.paymentservice.global.annotation.WebAdapter;
import reactor.core.publisher.Mono;

@WebAdapter
@Controller
@RequiredArgsConstructor
class CheckoutViewController {

    /**
     * 결제 페이지(checkout.html)를 렌더링합니다.
     */
    @GetMapping("/")
    public Mono<String> checkoutPage() {
        return Mono.just("checkout");
    }
}
