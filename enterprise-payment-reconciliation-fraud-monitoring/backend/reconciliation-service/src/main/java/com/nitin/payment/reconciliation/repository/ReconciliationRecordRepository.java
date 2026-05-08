package com.nitin.payment.reconciliation.repository;

import com.nitin.payment.reconciliation.entity.ReconciliationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReconciliationRecordRepository extends JpaRepository<ReconciliationRecord, Long> {
    List<ReconciliationRecord> findByFileId(Long fileId);
    List<ReconciliationRecord> findByFileIdAndReconciliationStatusNot(Long fileId, String status);
}
