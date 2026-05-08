package com.nitin.payment.fraud.service;

import com.nitin.payment.common.event.TransactionCreatedEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class FraudRuleEngineTest {
    @Test
    void marksHighAmountAsHighRisk() {
        FraudRuleEngine engine = new FraudRuleEngine(new BigDecimal("50000"));
        var decision = engine.evaluate(new TransactionCreatedEvent("TXN", 1L, "Test", new BigDecimal("90000"), "INR", "UPI", "INITIATED", Instant.now()));
        assertThat(decision.status()).isEqualTo("HIGH_RISK");
    }
}
