-- 주문 정보 : 장바구니에서 주문하기 버튼 클릭 시 생성 → 체크아웃
CREATE TABLE `t_purchase_order` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '주문 ID',
    `order_id` BINARY(16) DEFAULT (uuid_to_bin(uuid())) NOT NULL COMMENT '주문번호', -- PSP 에서 결제 주문을 유일하게 식별할 수 있는 주문번호
    `name` VARCHAR(255) NOT NULL COMMENT '주문자명',
    `phone_number` VARCHAR(255) NOT NULL COMMENT '주문자 휴대전화번호',
    `order_state` VARCHAR(255) NOT NULL COMMENT '주문상태',
    `payment_id` VARCHAR(255) NULL COMMENT '결제정보', -- PSP에서 결제 승인
    `total_price` INT NOT NULL COMMENT '상품 가격 * 주문 수량',
    `created_at` DATETIME DEFAULT NOW() NOT NULL,
    `updated_at` DATETIME DEFAULT NOW() NOT NULL,
    PRIMARY KEY (`id`)
);

-- 주문 상세 정보 : 실제 결제 대, 구매자가 구매하는 물품
CREATE TABLE `t_order_items` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '주문 상세 ID',
    `order_id` BINARY(16) NOT NULL COMMENT '주문번호 - FK',     -- 1 : N 관계
    `item_idx` INTEGER(10) NOT NULL COMMENT '같은 주문 내에서 몇 번째 항목인지',
    `product_id` BINARY(16) NOT NULL COMMENT '상품번호',
    `product_name` VARCHAR(255) NOT NULL COMMENT '상품명',
    `product_price` INT NOT NULL COMMENT '상품 가격',
    `product_size` VARCHAR(255) NOT NULL COMMENT '상품 사이즈',
    `quantity` INT NOT NULL COMMENT '주문 수량',
    `amount` INT NOT NULL COMMENT '총 가격(상품 가격 * 주문 수량)',
    `merchant_id` VARCHAR(255) NOT NULL COMMENT '상품 판매자 ID', -- ← 핵심 추가!
    `order_state` VARCHAR(255) NOT NULL COMMENT '개별 주문상태',
    `created_at` DATETIME DEFAULT NOW() NOT NULL,
    `updated_at` DATETIME DEFAULT NOW() NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`order_id`, `item_idx`, `product_id`)
);

-- 거래 원장 테이블
CREATE TABLE `payment_transaction` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '거래 ID',
    `payment_id` VARCHAR(255) NOT NULL COMMENT '거래 번호(ID)',
    `method` VARCHAR(255) NOT NULL COMMENT '거래 수단',
    `payment_status` VARCHAR(255) NOT NULL COMMENT '거래 상태',
    `total_amount` INT NOT NULL COMMENT '최종 결제 금액(즉시 할인 금액 포함)',
    `balance_amount` INT NOT NULL COMMENT '취소 가능한 금액(잔고)',
    `canceled_amount` INT NOT NULL COMMENT '취소된 총 금액',
    `created_at` DATETIME DEFAULT NOW() NOT NULL,
    `updated_at` DATETIME DEFAULT NOW() NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`payment_id`, `method`, `payment_status`)
);

-- 카드 결제 정보 테이블
CREATE TABLE `card_payment` (
    `payment_key` VARCHAR(255) NOT NULL COMMENT '결제번호(paymentKey)',
    `card_number` VARCHAR(255) NOT NULL COMMENT '카드번호',
    `approve_no` VARCHAR(10) NOT NULL COMMENT '카드 승인 번호',
    `acquire_status` VARCHAR(255) NOT NULL COMMENT '카드결제 매입 상태',
    `issuer_code` VARCHAR(255) NULL COMMENT '카드 발급사 코드',
    `acquirer_code` VARCHAR(255) NOT NULL COMMENT '카드 매입사 코드',
    `acquirer_status` VARCHAR(255) NOT NULL COMMENT '카드 결제의 상태',
    PRIMARY KEY (`payment_key`),
    UNIQUE KEY (`payment_key`, `card_number`, `approve_no`)
);
