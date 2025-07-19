package pro.hyundo.paymentsystem.application.service;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import pro.hyundo.paymentsystem.adapter.out.messaging.dto.PaymentFailureEvent;
import pro.hyundo.paymentsystem.adapter.out.messaging.dto.PaymentSuccessEvent;
import pro.hyundo.paymentsystem.application.port.in.ConfirmPaymentCommand;
import pro.hyundo.paymentsystem.application.port.in.CreatePurchaseOrderCommand;
import pro.hyundo.paymentsystem.application.port.in.PaymentUseCase;
import pro.hyundo.paymentsystem.application.port.out.LoadPurchaseOrderPort;
import pro.hyundo.paymentsystem.application.port.out.SavePaymentPort;
import pro.hyundo.paymentsystem.application.port.out.SavePurchaseOrderPort;
import pro.hyundo.paymentsystem.application.port.out.SendPaymentEventPort;
import pro.hyundo.paymentsystem.application.port.out.PaymentPort;
import pro.hyundo.paymentsystem.domain.CardPayment;
import pro.hyundo.paymentsystem.domain.OrderItem;
import pro.hyundo.paymentsystem.domain.OrderState;
import pro.hyundo.paymentsystem.domain.PaymentTransaction;
import pro.hyundo.paymentsystem.domain.PurchaseOrder;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService implements PaymentUseCase {

    private final SavePurchaseOrderPort savePurchaseOrderPort;
    private final LoadPurchaseOrderPort loadPurchaseOrderPort;
    private final SavePaymentPort savePaymentPort;
    private final SendPaymentEventPort sendPaymentEventPort;
    private final PaymentPort paymentPort;

    /**
     * 주문을 생성하고 데이터베이스에 저장합니다.
     * 초기 상태는 PENDING(주문 생성)입니다.
     */
    @Override
    @Transactional
    public PurchaseOrder createPurchaseOrder(CreatePurchaseOrderCommand command) {
        AtomicInteger index = new AtomicInteger(0);
        List<OrderItem> orderItems = command.getOrderItems().stream()
                .map(itemCmd -> OrderItem.builder()
                        .itemIdx(index.getAndIncrement())
                        .productId(itemCmd.getProductId())
                        .productName(itemCmd.getProductName())
                        .productPrice(itemCmd.getProductPrice())
                        .productSize(itemCmd.getProductSize())
                        .quantity(itemCmd.getQuantity())
                        .amount(itemCmd.getProductPrice() * itemCmd.getQuantity())
                        .orderState(OrderState.PENDING)
                        .build())
                .collect(Collectors.toList());

        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .orderId(UUID.randomUUID())
                .name(command.getCustomerName())
                .phoneNumber(command.getCustomerPhone())
                .totalPrice(command.getTotalPrice())
                .orderItems(orderItems)
                .orderState(OrderState.PENDING)
                .build();

        savePurchaseOrderPort.savePurchaseOrder(purchaseOrder);
        return purchaseOrder;
    }

    /**
     * 1. 주문 유효성 검증 (존재 여부, 가격 일치 여부)
     * 2. 외부 결제 API(Toss)에 결제 승인 요청
     * 3. 결제 결과에 따라 주문 상태 변경 (PAID 또는 FAILED)
     * 4. 결제 정보(Transaction, Card) 저장
     * 5. Kafka로 결제 완료/실패 이벤트 발행
     */
    @Override
    @Transactional
    public PurchaseOrder confirmPayment(ConfirmPaymentCommand command) {
        PurchaseOrder purchaseOrder = loadPurchaseOrderPort.loadPurchaseOrder(command.getOrderId());

        if (!purchaseOrder.getTotalPrice().equals(command.getAmount())) {
            throw new IllegalArgumentException("주문 금액이 일치하지 않습니다.");
        }

        try {
            // PG사 통신 및 성공 로직
            PaymentPort.TossPaymentConfirmationResult result = paymentPort.confirmPayment(
                    command.getPaymentKey(),
                    command.getOrderId(),
                    command.getAmount()
            );

            PaymentTransaction paymentTransaction = result.getPaymentTransaction();
            CardPayment cardPayment = result.getCardPayment();

            purchaseOrder.completePayment(paymentTransaction.getPaymentId());
            savePurchaseOrderPort.savePurchaseOrder(purchaseOrder);
            savePaymentPort.savePayment(paymentTransaction, cardPayment);

            sendSettlementEventsPerMerchant(purchaseOrder);

        } catch (Exception e) {
            // 실패 시, 별도의 트랜잭션으로 실패 처리를 위임.
            handlePaymentFailure(command.getOrderId());
            throw new RuntimeException("결제 승인에 실패했습니다.", e);
        }

        return purchaseOrder;
    }

    /**
     * 결제 실패 처리 로직. Propagation.REQUIRES_NEW 속성으로 새로운 트랜잭션을 보장.
     * 이 메소드는 반드시 public 이어야 Spring AOP가 적용.
     * @param orderId 실패한 주문의 ID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePaymentFailure(UUID orderId) {
        PurchaseOrder failedOrder = loadPurchaseOrderPort.loadPurchaseOrder(orderId);
        failedOrder.failOrder();
        savePurchaseOrderPort.savePurchaseOrder(failedOrder);

        // 구조화된 실패 이벤트 발행
        PaymentFailureEvent event = PaymentFailureEvent.builder()
                .eventType("PAYMENT_FAILED")
                .orderId(failedOrder.getOrderId())
                .paymentId(failedOrder.getPaymentId())
                .failureReason("PG사 승인 실패")
                .failureCode("INSUFFICIENT_BALANCE")
                .customerName(failedOrder.getName())
                .amount(failedOrder.getTotalPrice())
                .paymentMethod("CARD")
                .timestamp(LocalDateTime.now())
                .build();

        sendPaymentEventPort.sendPaymentFailureEvent(event);

        log.error("결제 실패 처리 완료 - orderId: {}, reason: {}",
                orderId, event.getFailureReason());
    }

    /**
     * 판매자별로 정산 이벤트를 발행합니다.
     */
    private void sendSettlementEventsPerMerchant(PurchaseOrder purchaseOrder) {
        Map<String, Integer> amountByMerchant = purchaseOrder.getAmountByMerchant();

        amountByMerchant.forEach((merchantId, amount) -> {
            PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                    .orderId(purchaseOrder.getOrderId())
                    .paymentId(purchaseOrder.getPaymentId())
                    .merchantId(merchantId)           // ← 개별 판매자
                    .totalAmount(amount)              // ← 해당 판매자 금액만
                    .customerName(purchaseOrder.getName())
                    .paymentMethod("CARD")
                    .timestamp(LocalDateTime.now())
                    .build();

            sendPaymentEventPort.sendPaymentSuccessEvent(event);

            log.info("판매자별 정산 이벤트 발행 - merchantId: {}, amount: {}",
                    merchantId, amount);
        });
    }
}

