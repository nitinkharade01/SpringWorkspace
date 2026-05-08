package com.nitin.payment.notification.controller;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService service;

    @GetMapping
    ApiResponse<?> all() {
        return ApiResponse.ok("Notifications fetched", service.all());
    }

    @PutMapping("/{id}/read")
    ApiResponse<?> read(@PathVariable Long id) {
        return ApiResponse.ok("Notification marked read", service.markRead(id));
    }
}
