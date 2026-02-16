package com.example.XLSXDataLoader.controller;

import com.example.XLSXDataLoader.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {
    
    private final ExcelService service;
    
    @PostMapping("/excel")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            String result = service.processExcel(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process Excel: " + e.getMessage(), e);
        }
    }
}
