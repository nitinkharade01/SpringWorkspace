package com.nitin.payment.transaction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "transaction_audit_logs")
public class TransactionAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private String action;
    private String oldValue;
    private String newValue;
    private String changedBy;
    private Instant createdAt = Instant.now();
}
