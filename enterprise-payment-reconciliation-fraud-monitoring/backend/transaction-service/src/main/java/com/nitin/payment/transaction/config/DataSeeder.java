package com.nitin.payment.transaction.config;

import com.nitin.payment.transaction.entity.*;
import com.nitin.payment.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {
    @Bean
    CommandLineRunner seedTransactions(TransactionRepository repository) {
        return args -> {
            if (repository.count() > 0) return;
            sample(repository, "TXN-SAMPLE-001", 2L, "Aarav Mehta", "UPI", "SUCCESS", "1520.00");
            sample(repository, "TXN-SAMPLE-002", 2L, "Priya Shah", "NEFT", "FAILED", "880.00");
            sample(repository, "TXN-SAMPLE-003", 3L, "Rohan Iyer", "CARD", "PENDING", "76000.00");
        };
    }

    private void sample(TransactionRepository repository, String id, Long userId, String name, String mode, String status, String amount) {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setTransactionId(id);
        tx.setUserId(userId);
        tx.setCustomerName(name);
        tx.setSourceAccount("SRC-" + userId);
        tx.setDestinationAccount("DST-" + userId);
        tx.setAmount(new BigDecimal(amount));
        tx.setCurrency("INR");
        tx.setPaymentMode(mode);
        tx.setTransactionStatus(TransactionStatus.valueOf(status));
        tx.setRiskStatus(RiskStatus.LOW_RISK);
        tx.setReconciliationStatus(ReconciliationStatus.PENDING);
        repository.save(tx);
    }
}
