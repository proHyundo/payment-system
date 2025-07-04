package pro.hyundo.paymentsystem.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pro.hyundo.paymentsystem.adapter.out.persistence.entity.CardPaymentEntity;
import pro.hyundo.paymentsystem.adapter.out.persistence.entity.PaymentTransactionEntity;
import pro.hyundo.paymentsystem.adapter.out.persistence.mapper.PaymentPersistenceMapper;
import pro.hyundo.paymentsystem.adapter.out.persistence.repository.CardPaymentJpaRepository;
import pro.hyundo.paymentsystem.adapter.out.persistence.repository.PaymentTransactionJpaRepository;
import pro.hyundo.paymentsystem.application.port.out.LoadPaymentPort;
import pro.hyundo.paymentsystem.application.port.out.SavePaymentPort;
import pro.hyundo.paymentsystem.domain.CardPayment;
import pro.hyundo.paymentsystem.domain.PaymentTransaction;

@Component
@RequiredArgsConstructor
@Transactional
class PaymentTransactionPersistenceAdapter implements LoadPaymentPort, SavePaymentPort {

    private final PaymentTransactionJpaRepository paymentTransactionJpaRepository;
    private final CardPaymentJpaRepository cardPaymentJpaRepository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public PaymentTransaction loadPaymentTransaction(String paymentId) {
        return paymentTransactionJpaRepository.findByPaymentId(paymentId)
                .map(mapper::mapToDomainEntity)
                .orElse(null);
    }

    @Override
    public CardPayment loadCardPayment(String paymentKey) {
        return cardPaymentJpaRepository.findById(paymentKey)
                .map(mapper::mapToDomainEntity)
                .orElse(null);
    }

    @Override
    public void savePayment(PaymentTransaction paymentTransaction, CardPayment cardPayment) {
        PaymentTransactionEntity transactionEntity = new PaymentTransactionEntity(
                paymentTransaction.getPaymentId(),
                paymentTransaction.getMethod(),
                paymentTransaction.getPaymentStatus(),
                paymentTransaction.getTotalAmount(),
                paymentTransaction.getBalanceAmount(),
                paymentTransaction.getCanceledAmount()
        );
        paymentTransactionJpaRepository.save(transactionEntity);

        if (cardPayment != null) {
            CardPaymentEntity cardPaymentEntity = new CardPaymentEntity(
                    cardPayment.getPaymentKey(),
                    cardPayment.getCardNumber(),
                    cardPayment.getApproveNo(),
                    cardPayment.getAcquireStatus(),
                    cardPayment.getIssuerCode(),
                    cardPayment.getAcquirerCode(),
                    cardPayment.getAcquireStatus() // acquirerStatus 필드 추가
            );
            cardPaymentJpaRepository.save(cardPaymentEntity);
        }
    }
}

