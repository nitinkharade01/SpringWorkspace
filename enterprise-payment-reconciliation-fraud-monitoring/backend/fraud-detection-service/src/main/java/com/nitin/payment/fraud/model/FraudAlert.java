package com.nitin.payment.fraud.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document("fraud_alerts")
public class FraudAlert {
    @Id
    private String id;
    @Indexed
    private String transactionId;
    private Long userId;
    private int riskScore;
    private String riskStatus;
    private String fraudReason;
    private String alertStatus = "OPEN";
    private Instant createdAt = Instant.now();
}
