package Nitin.example.Authentication.controller;

import java.util.Map;


import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Nitin.example.Authentication.dto.LoginRequest;
import Nitin.example.Authentication.dto.RegisterRequest;
import Nitin.example.Authentication.security.JwtUtil;
import Nitin.example.Authentication.service.UserService;
import Nitin.example.Authentication.entity.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService service;
	private final JwtUtil jwtUtil;

	public AuthController(UserService service, JwtUtil jwtUtil) {
		this.service = service;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/register")
	public String register(@Valid @RequestBody RegisterRequest req) {
		User user = new User();
		user.setUsername(req.username);
		user.setEmail(req.email);
		user.setPassword(req.password);
		service.register(user);
		return "User Registered Successfully";
	}

	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {

	    User user = service.login(req.getEmail(), req.getPassword());

	    String token = jwtUtil.generateToken(user.getUsername());

	    return ResponseEntity.ok(Map.of("token", token));
	}

}
