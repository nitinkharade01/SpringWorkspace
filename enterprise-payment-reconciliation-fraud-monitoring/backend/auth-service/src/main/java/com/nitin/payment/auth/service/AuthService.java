package com.nitin.payment.auth.service;

import com.nitin.payment.auth.dto.*;
import com.nitin.payment.auth.entity.Role;
import com.nitin.payment.auth.entity.RoleName;
import com.nitin.payment.auth.entity.User;
import com.nitin.payment.auth.repository.RoleRepository;
import com.nitin.payment.auth.repository.UserRepository;
import com.nitin.payment.auth.security.JwtService;
import com.nitin.payment.common.exception.DuplicateResourceException;
import com.nitin.payment.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered");
        }
        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(loadRoles(request.roles()));
        return toResponse(userRepository.save(user));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid email or password");
        }
        return new AuthResponse(jwtService.generate(user), user.getId(), user.getFullName(), user.getEmail(),
                user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));
    }

    public UserResponse profile(String email) {
        return userRepository.findByEmail(email).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));
    }

    public java.util.List<UserResponse> users() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    private Set<Role> loadRoles(Set<RoleName> roleNames) {
        return roleNames.stream()
                .map(name -> roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Role not found: " + name)))
                .collect(Collectors.toSet());
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.isActive(),
                user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));
    }
}
