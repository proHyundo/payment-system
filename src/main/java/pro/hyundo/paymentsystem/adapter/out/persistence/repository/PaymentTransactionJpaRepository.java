package pro.hyundo.paymentsystem.adapter.out.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.hyundo.paymentsystem.adapter.out.persistence.entity.PaymentTransactionEntity;

import java.util.UUID;

@Repository
public interface PaymentTransactionJpaRepository extends JpaRepository<PaymentTransactionEntity, Long> {

    /**
     * 거래 번호(paymentId)를 기준으로 결제 거래 정보를 조회합니다.
     * @param paymentId 조회할 거래의 ID
     * @return Optional<PaymentTransactionEntity>
     */
    Optional<PaymentTransactionEntity> findByPaymentId(String paymentId);
}

