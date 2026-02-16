package com.example.ProfileUploadApplication.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ProfileUploadApplication.entity.User;
import com.example.ProfileUploadApplication.repository.UserRepository;
import com.example.ProfileUploadApplication.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserRepository userRepository;
    private final FileUploadService fileService;
    
    @PostMapping("/{userId}/profile-image")
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            
            String imagePath = fileService.storeProfileImage(file, userId);
            user.setProfileImagePath(imagePath);
            userRepository.save(user);
            
            return ResponseEntity.ok(Map.of(
                "success", "true",
                "message", "Profile image uploaded successfully",
                "imagePath", imagePath
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", "false", "error", e.getMessage()));
        }
    }
    
    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<Map<String, String>> getProfileImage(@PathVariable Long userId) {
        return userRepository.findById(userId)
            .map(user -> ResponseEntity.ok(Map.of(
                "imageUrl", user.getProfileImagePath() != null ? user.getProfileImagePath() : "",
                "exists", user.getProfileImagePath() != null ? "true" : "false"
            )))
            .orElse(ResponseEntity.notFound().build());
    }
}

