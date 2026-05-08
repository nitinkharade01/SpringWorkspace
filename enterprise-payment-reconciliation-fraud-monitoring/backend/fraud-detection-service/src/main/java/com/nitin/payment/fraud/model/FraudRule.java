package com.nitin.payment.fraud.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("fraud_rules")
public class FraudRule {
    @Id
    private String id;
    private String name;
    private String expression;
    private boolean active = true;
}
