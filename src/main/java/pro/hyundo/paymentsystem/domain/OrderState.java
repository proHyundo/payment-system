package pro.hyundo.paymentsystem.domain;

import lombok.*;

/**
 * 주문 상태를 나타내는 Enum
 */
@Getter
@RequiredArgsConstructor
public enum OrderState {
    PENDING("주문 생성"),
    PAID("결제 완료"),
    CANCELED("주문 취소"),
    FAILED("주문 실패");

    private final String description;
}
