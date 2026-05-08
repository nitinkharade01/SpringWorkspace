package com.nitin.payment.reconciliation.controller;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.reconciliation.service.ReconciliationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/reconciliation")
@RequiredArgsConstructor
public class ReconciliationController {
    private final ReconciliationService service;

    @PostMapping("/upload")
    ApiResponse<?> upload(@RequestParam MultipartFile file) {
        return ApiResponse.created("Reconciliation completed", service.upload(file));
    }

    @GetMapping("/summary/{fileId}")
    ApiResponse<?> summary(@PathVariable Long fileId) {
        return ApiResponse.ok("Summary fetched", service.summary(fileId));
    }

    @GetMapping("/mismatches/{fileId}")
    ApiResponse<?> mismatches(@PathVariable Long fileId) {
        return ApiResponse.ok("Mismatches fetched", service.mismatches(fileId));
    }

    @GetMapping("/download/{fileId}")
    ResponseEntity<String> download(@PathVariable Long fileId) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reconciliation-" + fileId + ".csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(service.report(fileId));
    }
}
