package com.nitin.payment.transaction.entity;

import com.nitin.payment.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_transactions_transaction_id", columnList = "transactionId"),
        @Index(name = "idx_transactions_user_status", columnList = "userId, transactionStatus")
})
public class PaymentTransaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String transactionId;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String customerName;
    @Column(nullable = false)
    private String sourceAccount;
    @Column(nullable = false)
    private String destinationAccount;
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private String paymentMode;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
    @Enumerated(EnumType.STRING)
    private RiskStatus riskStatus;
    @Enumerated(EnumType.STRING)
    private ReconciliationStatus reconciliationStatus;
    private String remarks;
}
