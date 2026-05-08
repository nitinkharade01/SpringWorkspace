package com.nitin.payment.fraud.repository;

import com.nitin.payment.fraud.model.FraudAlert;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FraudAlertRepository extends MongoRepository<FraudAlert, String> {
    long countByRiskStatus(String riskStatus);
}
