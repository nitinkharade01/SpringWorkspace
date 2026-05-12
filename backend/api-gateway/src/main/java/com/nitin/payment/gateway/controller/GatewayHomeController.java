/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.nitin.payment.gateway.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayHomeController {
    @GetMapping(value={"/"})
    public Map<String, Object> home() {
        return Map.of("status", "UP", "message", "Enterprise Payment API Gateway is running", "frontend", "http://localhost:5173", "health", "http://localhost:8080/actuator/health", "apiRoutes", Map.of("auth", "/api/auth/**", "transactions", "/api/transactions/**", "fraudAlerts", "/api/fraud-alerts/**", "reconciliation", "/api/reconciliation/**", "notifications", "/api/notifications/**", "reports", "/api/reports/**"));
    }
}
