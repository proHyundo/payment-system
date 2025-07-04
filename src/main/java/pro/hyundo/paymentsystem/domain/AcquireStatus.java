package pro.hyundo.paymentsystem.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 카드 매입 상태를 나타내는 Enum
 */
@Getter
@RequiredArgsConstructor
public enum AcquireStatus {
    READY("매입 대기"),
    REQUESTED("매입 요청됨"),
    COMPLETED("매입 완료"),
    CANCEL_REQUESTED("취소 요청됨"),
    CANCELED("취소 완료");

    private final String description;
}
