package com.nitin.payment.fraud.service;

import com.nitin.payment.common.CommonConstants;
import com.nitin.payment.common.event.FraudAlertEvent;
import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.common.exception.ResourceNotFoundException;
import com.nitin.payment.fraud.model.FraudAlert;
import com.nitin.payment.fraud.repository.FraudAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FraudAlertService {
    private final FraudAlertRepository repository;
    private final FraudRuleEngine ruleEngine;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = CommonConstants.TRANSACTION_CREATED_TOPIC, groupId = "fraud-detection-service")
    public void consume(TransactionCreatedEvent event) {
        FraudRuleEngine.FraudDecision decision = ruleEngine.evaluate(event);
        if (!"LOW_RISK".equals(decision.status())) {
            FraudAlert alert = new FraudAlert();
            alert.setTransactionId(event.transactionId());
            alert.setUserId(event.userId());
            alert.setRiskScore(decision.score());
            alert.setRiskStatus(decision.status());
            alert.setFraudReason(decision.reason());
            FraudAlert saved = repository.save(alert);
            kafkaTemplate.send(CommonConstants.FRAUD_ALERT_TOPIC, event.transactionId(),
                    new FraudAlertEvent(event.transactionId(), event.userId(), decision.score(), decision.status(), decision.reason(), Instant.now()));
            log.info("Fraud alert created id={} transactionId={}", saved.getId(), event.transactionId());
        }
    }

    public List<FraudAlert> findAll() {
        return repository.findAll();
    }

    public FraudAlert find(String id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fraud alert not found: " + id));
    }

    public FraudAlert updateStatus(String id, String status) {
        FraudAlert alert = find(id);
        alert.setAlertStatus(status);
        return repository.save(alert);
    }
}
