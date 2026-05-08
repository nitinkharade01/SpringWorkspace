package com.nitin.payment.reconciliation.repository;

import com.nitin.payment.reconciliation.entity.ReconciliationSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReconciliationSummaryRepository extends JpaRepository<ReconciliationSummary, Long> {
    Optional<ReconciliationSummary> findByFileId(Long fileId);
}
