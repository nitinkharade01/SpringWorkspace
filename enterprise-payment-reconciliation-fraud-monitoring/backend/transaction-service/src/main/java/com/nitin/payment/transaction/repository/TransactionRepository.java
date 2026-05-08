package com.nitin.payment.transaction.repository;

import com.nitin.payment.transaction.entity.PaymentTransaction;
import com.nitin.payment.transaction.entity.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findByTransactionId(String transactionId);
    Page<PaymentTransaction> findByTransactionStatus(TransactionStatus status, Pageable pageable);
    Page<PaymentTransaction> findByCustomerNameContainingIgnoreCaseOrTransactionIdContainingIgnoreCase(String customerName, String transactionId, Pageable pageable);
    long countByTransactionStatus(TransactionStatus status);
    long countByReconciliationStatus(com.nitin.payment.transaction.entity.ReconciliationStatus status);
    List<PaymentTransaction> findByUserIdAndCreatedAtAfter(Long userId, Instant after);
}
