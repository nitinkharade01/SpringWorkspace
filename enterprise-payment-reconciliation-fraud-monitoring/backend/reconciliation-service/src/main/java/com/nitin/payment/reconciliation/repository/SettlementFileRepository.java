package com.nitin.payment.reconciliation.repository;

import com.nitin.payment.reconciliation.entity.SettlementFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementFileRepository extends JpaRepository<SettlementFile, Long> {
}
