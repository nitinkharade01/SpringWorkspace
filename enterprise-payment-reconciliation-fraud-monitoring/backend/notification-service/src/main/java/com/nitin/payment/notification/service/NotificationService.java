package com.nitin.payment.notification.service;

import com.nitin.payment.common.CommonConstants;
import com.nitin.payment.common.event.FraudAlertEvent;
import com.nitin.payment.common.event.ReconciliationCompletedEvent;
import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.common.exception.ResourceNotFoundException;
import com.nitin.payment.notification.entity.NotificationLog;
import com.nitin.payment.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;

    @KafkaListener(topics = CommonConstants.FRAUD_ALERT_TOPIC, groupId = "notification-service")
    public void fraud(FraudAlertEvent event) {
        save("risk@payment.com", "FRAUD_ALERT", "Fraud alert: " + event.riskStatus(), event.fraudReason());
    }

    @KafkaListener(topics = CommonConstants.TRANSACTION_STATUS_UPDATED_TOPIC, groupId = "notification-service")
    public void transaction(TransactionCreatedEvent event) {
        save("user-" + event.userId(), "PAYMENT_" + event.transactionStatus(), "Payment status updated", event.transactionId());
    }

    @KafkaListener(topics = CommonConstants.RECONCILIATION_COMPLETED_TOPIC, groupId = "notification-service")
    public void reconciliation(ReconciliationCompletedEvent event) {
        save("finance@payment.com", "RECONCILIATION_COMPLETED", "Reconciliation completed", "File " + event.fileId());
    }

    public List<NotificationLog> all() {
        return repository.findAll();
    }

    public NotificationLog markRead(Long id) {
        NotificationLog log = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));
        log.setRead(true);
        return repository.save(log);
    }

    private void save(String recipient, String type, String subject, String message) {
        NotificationLog logEntry = new NotificationLog();
        logEntry.setRecipient(recipient);
        logEntry.setType(type);
        logEntry.setSubject(subject);
        logEntry.setMessage(message);
        repository.save(logEntry);
        log.info("Mock notification stored type={} recipient={}", type, recipient);
    }
}
