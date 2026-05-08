package com.nitin.payment.fraud.controller;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.fraud.service.FraudAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud-alerts")
@RequiredArgsConstructor
public class FraudAlertController {
    private final FraudAlertService service;

    @GetMapping
    ApiResponse<?> all() {
        return ApiResponse.ok("Fraud alerts fetched", service.findAll());
    }

    @GetMapping("/{id}")
    ApiResponse<?> find(@PathVariable String id) {
        return ApiResponse.ok("Fraud alert fetched", service.find(id));
    }

    @PutMapping("/{id}/status")
    ApiResponse<?> update(@PathVariable String id, @RequestParam String status) {
        return ApiResponse.ok("Fraud alert updated", service.updateStatus(id, status));
    }
}
