package com.nitin.payment.reconciliation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "reconciliation_records")
public class ReconciliationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String internalTransactionId;
    private String bankTransactionId;
    private BigDecimal internalAmount;
    private BigDecimal bankAmount;
    private String internalStatus;
    private String bankStatus;
    private String reconciliationStatus;
    private String mismatchReason;
    private Long fileId;
    private Instant createdAt = Instant.now();
}
