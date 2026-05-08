package com.nitin.payment.transaction.controller;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.transaction.dto.CreateTransactionRequest;
import com.nitin.payment.transaction.dto.UpdateStatusRequest;
import com.nitin.payment.transaction.entity.TransactionStatus;
import com.nitin.payment.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping
    ApiResponse<?> create(@Valid @RequestBody CreateTransactionRequest request) {
        return ApiResponse.created("Transaction created", service.create(request));
    }

    @GetMapping
    ApiResponse<?> all(Pageable pageable) {
        return ApiResponse.ok("Transactions fetched", service.findAll(pageable));
    }

    @GetMapping("/{transactionId}")
    ApiResponse<?> details(@PathVariable String transactionId) {
        return ApiResponse.ok("Transaction fetched", service.findByTransactionId(transactionId));
    }

    @GetMapping("/status/{status}")
    ApiResponse<?> byStatus(@PathVariable TransactionStatus status, Pageable pageable) {
        return ApiResponse.ok("Transactions fetched", service.byStatus(status, pageable));
    }

    @PutMapping("/{transactionId}/status")
    ApiResponse<?> updateStatus(@PathVariable String transactionId, @Valid @RequestBody UpdateStatusRequest request) {
        return ApiResponse.ok("Transaction status updated", service.updateStatus(transactionId, request));
    }

    @GetMapping("/search")
    ApiResponse<?> search(@RequestParam String keyword, Pageable pageable) {
        return ApiResponse.ok("Transactions fetched", service.search(keyword, pageable));
    }
}
