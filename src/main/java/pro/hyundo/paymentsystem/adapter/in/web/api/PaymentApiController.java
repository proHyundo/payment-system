package pro.hyundo.paymentsystem.adapter.in.web.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import pro.hyundo.paymentsystem.adapter.in.web.api.dto.request.ConfirmPaymentRequest;
import pro.hyundo.paymentsystem.adapter.in.web.api.dto.request.CreateOrderRequest;
import pro.hyundo.paymentsystem.adapter.in.web.api.dto.response.ConfirmPaymentResponse;
import pro.hyundo.paymentsystem.adapter.in.web.api.dto.response.CreateOrderResponse;
import pro.hyundo.paymentsystem.application.port.in.ConfirmPaymentCommand;
import pro.hyundo.paymentsystem.application.port.in.CreatePurchaseOrderCommand;
import pro.hyundo.paymentsystem.application.port.in.OrderItemCommand;
import pro.hyundo.paymentsystem.application.port.in.PaymentUseCase;
import pro.hyundo.paymentsystem.domain.PurchaseOrder;
import pro.hyundo.paymentsystem.global.annotation.WebAdapter;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import pro.hyundo.paymentsystem.global.response.ApiResponse;

@Slf4j
@WebAdapter
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentApiController {

    private final PaymentUseCase paymentUseCase;

    /**
     * 주문을 생성하고, 프론트엔드에서 결제창을 호출하는 데 필요한 정보를 반환합니다.
     */
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        log.info("Creating order for customer: {}", request.getCustomerName());
        List<OrderItemCommand> orderItemCommands = request.getOrderItems().stream()
                .map(item -> OrderItemCommand.builder()
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .productPrice(item.getProductPrice())
                        .productSize(item.getProductSize())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        CreatePurchaseOrderCommand command = CreatePurchaseOrderCommand.builder()
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhone())
                .orderItems(orderItemCommands)
                .totalPrice(request.getTotalPrice())
                .build();

        PurchaseOrder purchaseOrder = paymentUseCase.createPurchaseOrder(command);

        CreateOrderResponse responseDto = CreateOrderResponse.builder()
                .orderId(purchaseOrder.getOrderId())
                .orderName("상품 " + purchaseOrder.getOrderItems().size() + "종") // Toss orderName 예시
                .totalPrice(purchaseOrder.getTotalPrice())
                .build();

        log.info("Order created successfully: {}", responseDto);
        return ResponseEntity.ok(new ApiResponse<>("success", responseDto));
    }

    /**
     * 결제 승인 요청을 받아 처리합니다.
     */
    @PostMapping("/payments/confirm")
    public ResponseEntity<ApiResponse<ConfirmPaymentResponse>> confirmPayment(@RequestBody @Valid ConfirmPaymentRequest request) {
        ConfirmPaymentCommand command = ConfirmPaymentCommand.builder()
                .paymentKey(request.getPaymentKey())
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .build();

        PurchaseOrder purchaseOrder = paymentUseCase.confirmPayment(command);

        ConfirmPaymentResponse responseDto = ConfirmPaymentResponse.builder()
                .orderId(purchaseOrder.getOrderId())
                .status(purchaseOrder.getOrderState().name())
                .build();

        return ResponseEntity.ok(new ApiResponse<>("success", responseDto));
    }
}
