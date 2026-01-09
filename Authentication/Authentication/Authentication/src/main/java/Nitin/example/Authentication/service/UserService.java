package Nitin.example.Authentication.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;


import Nitin.example.Authentication.entity.User;
import Nitin.example.Authentication.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

	/*
	 * public User login(String email, String rawPassword) { User user =
	 * repo.findByEmail(email).orElseThrow(); if (!encoder.matches(rawPassword,
	 * user.getPassword())) { throw new RuntimeException("Invalid credentials"); }
	 * return user; }
	 */
    
    public User login(String email, String password) {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }
}
