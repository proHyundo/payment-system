package pro.hyundo.paymentsystem.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 결제 거래 상태를 나타내는 Enum
 */
@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    NOT_STARTED("결제 시작 전"),
    EXECUTING("결제 승인중"),
    SUCCESS("결제 성공"),
    FAILURE("결제 실패"),
    CANCELED("결제 취소"),
    PARTIAL_CANCELED("부분 취소");

    private final String description;
}
