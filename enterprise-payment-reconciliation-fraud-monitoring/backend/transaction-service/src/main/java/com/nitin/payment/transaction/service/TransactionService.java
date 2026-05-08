package com.nitin.payment.transaction.service;

import com.nitin.payment.common.CommonConstants;
import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.common.exception.ResourceNotFoundException;
import com.nitin.payment.transaction.dto.CreateTransactionRequest;
import com.nitin.payment.transaction.dto.TransactionResponse;
import com.nitin.payment.transaction.dto.UpdateStatusRequest;
import com.nitin.payment.transaction.entity.*;
import com.nitin.payment.transaction.repository.TransactionAuditLogRepository;
import com.nitin.payment.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;
    private final TransactionAuditLogRepository auditRepository;
    private final TransactionMapper mapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public TransactionResponse create(CreateTransactionRequest request) {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        tx.setUserId(request.userId());
        tx.setCustomerName(request.customerName());
        tx.setSourceAccount(request.sourceAccount());
        tx.setDestinationAccount(request.destinationAccount());
        tx.setAmount(request.amount());
        tx.setCurrency(request.currency());
        tx.setPaymentMode(request.paymentMode());
        tx.setTransactionStatus(TransactionStatus.INITIATED);
        tx.setRiskStatus(RiskStatus.LOW_RISK);
        tx.setReconciliationStatus(ReconciliationStatus.PENDING);
        tx.setRemarks(request.remarks());
        PaymentTransaction saved = repository.save(tx);
        kafkaTemplate.send(CommonConstants.TRANSACTION_CREATED_TOPIC, saved.getTransactionId(),
                new TransactionCreatedEvent(saved.getTransactionId(), saved.getUserId(), saved.getCustomerName(), saved.getAmount(),
                        saved.getCurrency(), saved.getPaymentMode(), saved.getTransactionStatus().name(), saved.getCreatedAt()));
        log.info("Published transaction-created event transactionId={}", saved.getTransactionId());
        return mapper.toResponse(saved);
    }

    public Page<TransactionResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    public TransactionResponse findByTransactionId(String transactionId) {
        return mapper.toResponse(load(transactionId));
    }

    public Page<TransactionResponse> byStatus(TransactionStatus status, Pageable pageable) {
        return repository.findByTransactionStatus(status, pageable).map(mapper::toResponse);
    }

    public Page<TransactionResponse> search(String keyword, Pageable pageable) {
        return repository.findByCustomerNameContainingIgnoreCaseOrTransactionIdContainingIgnoreCase(keyword, keyword, pageable).map(mapper::toResponse);
    }

    @Transactional
    public TransactionResponse updateStatus(String transactionId, UpdateStatusRequest request) {
        PaymentTransaction tx = load(transactionId);
        TransactionStatus old = tx.getTransactionStatus();
        tx.setTransactionStatus(request.status());
        tx.setRemarks(request.remarks());
        TransactionAuditLog logEntry = new TransactionAuditLog();
        logEntry.setTransactionId(transactionId);
        logEntry.setAction("STATUS_CHANGED");
        logEntry.setOldValue(old.name());
        logEntry.setNewValue(request.status().name());
        auditRepository.save(logEntry);
        kafkaTemplate.send(CommonConstants.TRANSACTION_STATUS_UPDATED_TOPIC, transactionId,
                new TransactionCreatedEvent(tx.getTransactionId(), tx.getUserId(), tx.getCustomerName(), tx.getAmount(), tx.getCurrency(), tx.getPaymentMode(), tx.getTransactionStatus().name(), tx.getCreatedAt()));
        return mapper.toResponse(tx);
    }

    private PaymentTransaction load(String transactionId) {
        return repository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionId));
    }
}
