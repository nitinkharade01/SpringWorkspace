package com.nitin.payment.reporting.controller;

import com.nitin.payment.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportingController {
    @GetMapping("/dashboard")
    ApiResponse<?> dashboard() {
        return ApiResponse.ok("Dashboard fetched", Map.of(
                "totalTransactions", 1250,
                "successCount", 1040,
                "failedCount", 86,
                "fraudCount", 18,
                "reconciliationPendingCount", 124,
                "dailyVolume", daily(),
                "paymentModeDistribution", paymentModes(),
                "fraudRisk", fraudRisk(),
                "recentTransactions", List.of(
                        Map.of("transactionId", "TXN-SAMPLE-001", "customerName", "Aarav Mehta", "amount", 1520, "status", "SUCCESS"),
                        Map.of("transactionId", "TXN-SAMPLE-003", "customerName", "Rohan Iyer", "amount", 76000, "status", "SUSPICIOUS"))));
    }

    @GetMapping("/transactions/daily")
    ApiResponse<?> dailyTransactions() {
        return ApiResponse.ok("Daily transactions fetched", daily());
    }

    @GetMapping("/payment-mode-distribution")
    ApiResponse<?> paymentModeDistribution() {
        return ApiResponse.ok("Payment mode distribution fetched", paymentModes());
    }

    @GetMapping("/fraud-risk-summary")
    ApiResponse<?> fraudRiskSummary() {
        return ApiResponse.ok("Fraud risk summary fetched", fraudRisk());
    }

    private List<Map<String, Object>> daily() {
        return List.of(
                Map.of("date", LocalDate.now().minusDays(4).toString(), "volume", new BigDecimal("120000")),
                Map.of("date", LocalDate.now().minusDays(3).toString(), "volume", new BigDecimal("155000")),
                Map.of("date", LocalDate.now().minusDays(2).toString(), "volume", new BigDecimal("98000")),
                Map.of("date", LocalDate.now().minusDays(1).toString(), "volume", new BigDecimal("210000")),
                Map.of("date", LocalDate.now().toString(), "volume", new BigDecimal("175000")));
    }

    private Map<String, Integer> paymentModes() {
        return Map.of("UPI", 52, "CARD", 23, "NEFT", 15, "IMPS", 10);
    }

    private Map<String, Integer> fraudRisk() {
        return Map.of("LOW_RISK", 980, "MEDIUM_RISK", 42, "HIGH_RISK", 18, "BLOCKED", 4);
    }
}
