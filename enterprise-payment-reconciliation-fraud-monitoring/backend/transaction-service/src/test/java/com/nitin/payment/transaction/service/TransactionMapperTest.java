package com.nitin.payment.transaction.service;

import com.nitin.payment.transaction.entity.PaymentTransaction;
import com.nitin.payment.transaction.entity.ReconciliationStatus;
import com.nitin.payment.transaction.entity.RiskStatus;
import com.nitin.payment.transaction.entity.TransactionStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {
    @Test
    void mapsEntityToResponse() {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setTransactionId("TXN-1");
        tx.setUserId(1L);
        tx.setCustomerName("Test User");
        tx.setSourceAccount("A");
        tx.setDestinationAccount("B");
        tx.setAmount(BigDecimal.TEN);
        tx.setCurrency("INR");
        tx.setPaymentMode("UPI");
        tx.setTransactionStatus(TransactionStatus.INITIATED);
        tx.setRiskStatus(RiskStatus.LOW_RISK);
        tx.setReconciliationStatus(ReconciliationStatus.PENDING);

        assertThat(new TransactionMapper().toResponse(tx).transactionId()).isEqualTo("TXN-1");
    }
}
