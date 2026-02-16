package com.example.ingestionservice.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.example.ingestionservice.service.DataIngestionService;
import com.example.ingestionservice.dto.DataIngestionRequest;
import com.example.common.dto.DataEvent;

@RestController
@RequestMapping("/api/ingestion")
@Slf4j
public class IngestionController {
    
    private final DataIngestionService dataIngestionService;
    
    public IngestionController(DataIngestionService dataIngestionService) {
        this.dataIngestionService = dataIngestionService;
    }
    
    // ✅ FIXED: Added 'public' modifier
    @PostMapping
    public DataEvent ingest(@RequestBody DataIngestionRequest request) {
        log.info("Received ingestion request from tenant: {}", request.getTenantId());
        return dataIngestionService.ingestData(request);
    }
}
