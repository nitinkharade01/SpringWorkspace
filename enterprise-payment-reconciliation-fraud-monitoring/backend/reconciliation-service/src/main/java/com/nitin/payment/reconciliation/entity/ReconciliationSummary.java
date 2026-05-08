package com.nitin.payment.reconciliation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reconciliation_summary")
public class ReconciliationSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private Long fileId;
    private long totalRecords;
    private long matchedCount;
    private long mismatchedCount;
    private long missingCount;
    private long duplicateCount;
}
