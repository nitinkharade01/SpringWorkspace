package com.example.ProfileUploadApplication.service;

import com.example.ProfileUploadApplication.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadService {
    
    private static final String UPLOAD_DIR = "uploads/profile/";
    private static final long MAX_SIZE = 5 * 1024 * 1024L; // 5MB
    private static final Set<String> ALLOWED_TYPES = Set.of(
        "image/jpeg", "image/jpg", "image/png"
    );
    
    public String storeProfileImage(MultipartFile file, Long userId) throws IOException {
        // Validation
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("File too large (max 5MB)");
        }
        String contentType = file.getContentType();
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Only JPG/PNG allowed");
        }
        
        // Create directory if not exists
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String ext = Objects.requireNonNull(contentType).split("/")[1];
        String filename = UUID.randomUUID() + "." + ext;
        Path filePath = uploadPath.resolve(filename);
        
        // Save file
        file.transferTo(filePath);
        log.info("Uploaded profile image for user {}: {}", userId, filename);
        
        return "/uploads/profile/" + filename;
    }
}
