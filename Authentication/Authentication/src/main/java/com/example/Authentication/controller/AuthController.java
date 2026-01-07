package com.example.Authentication.controller;

//package com.example.auth.controller;
import com.example.Authentication.dto.AuthDtos.*;
import com.example.Authentication.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;  // no = new AuthService()

    // Spring will inject AuthService here
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
 @PostMapping("/register")
 public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
     authService.register(request);
     return ResponseEntity.status(HttpStatus.CREATED)
             .body(Map.of("message", "User registered successfully"));
 }

 @PostMapping("/login")
 public ResponseEntity<AuthResponse> login(
         @Valid @RequestBody LoginRequest request
 ) {
     AuthResponse response = authService.login(request);
     return ResponseEntity.ok(response); // JWT in response body.
 }

 // Sample secured endpoint
 @GetMapping("/me")
 public ResponseEntity<?> me() {
     return ResponseEntity.ok(Map.of("message", "You are authenticated"));
 }

 // Validation errors
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<Map<String,String>> handleValidationExceptions(
         MethodArgumentNotValidException ex
 ) {
     Map<String,String> errors = new HashMap<>();
     ex.getBindingResult().getFieldErrors()
             .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
     return ResponseEntity.badRequest().body(errors);
 }

 // Generic errors (e.g., IllegalArgumentException, BadCredentialsException)
 @ExceptionHandler({IllegalArgumentException.class, org.springframework.security.authentication.BadCredentialsException.class})
 public ResponseEntity<Map<String,String>> handleAuthExceptions(RuntimeException ex) {
     Map<String,String> body = new HashMap<>();
     body.put("error", ex.getMessage());
     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
 }
}
