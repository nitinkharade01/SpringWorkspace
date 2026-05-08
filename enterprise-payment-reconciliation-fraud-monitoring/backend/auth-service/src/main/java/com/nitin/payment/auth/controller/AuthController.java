package com.nitin.payment.auth.controller;

import com.nitin.payment.auth.dto.LoginRequest;
import com.nitin.payment.auth.dto.RegisterRequest;
import com.nitin.payment.auth.service.AuthService;
import com.nitin.payment.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    ApiResponse<?> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.created("User registered", authService.register(request));
    }

    @PostMapping("/login")
    ApiResponse<?> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok("Login successful", authService.login(request));
    }

    @GetMapping("/profile")
    ApiResponse<?> profile(@RequestHeader("X-Authenticated-User") String email) {
        return ApiResponse.ok("Profile fetched", authService.profile(email));
    }

    @GetMapping("/users")
    ApiResponse<?> users() {
        return ApiResponse.ok("Users fetched", authService.users());
    }
}
