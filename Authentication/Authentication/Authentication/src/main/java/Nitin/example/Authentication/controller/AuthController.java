package Nitin.example.Authentication.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Nitin.example.Authentication.dto.LoginRequest;
import Nitin.example.Authentication.dto.RegisterRequest;
import Nitin.example.Authentication.dto.ResetPasswordRequest;
import Nitin.example.Authentication.dto.VerifyOtpRequest;
import Nitin.example.Authentication.entity.User;
import Nitin.example.Authentication.repository.ForgotPasswordRequest;
import Nitin.example.Authentication.security.JwtUtil;
import Nitin.example.Authentication.service.OtpService;
import Nitin.example.Authentication.service.UserService;
import jakarta.validation.Valid;

@Controller      // Changed from @RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, OtpService otpService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.otpService = otpService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok("User Registered Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    @ResponseBody    // Added for JSON response
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        User user = userService.login(req.getEmail(), req.getPassword());
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }

    // NEW OTP FORGOT PASSWORD ENDPOINTS
    @PostMapping("/forgot-password")
    @ResponseBody    // Added for JSON response
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            otpService.generateAndSendOtp(request.getEmail());
            return ResponseEntity.ok("OTP sent to email");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    @ResponseBody    // Added for JSON response
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        if (otpService.validateOtp(request.getEmail(), request.getOtp())) {
            return ResponseEntity.ok("OTP valid");
        }
        return ResponseEntity.badRequest().body("Invalid or expired OTP");
    }

    @PostMapping("/reset-password")
    @ResponseBody    // Added for JSON response
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            otpService.resetPassword(request.getEmail(), request.getNewPassword());
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

