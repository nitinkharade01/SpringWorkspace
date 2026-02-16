package com.example.ingestionservice.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import com.example.ingestionservice.kafka.DataEventProducer;
import lombok.extern.slf4j.Slf4j;
import com.example.ingestionservice.dto.DataIngestionRequest;
import com.example.common.dto.DataEvent;
import com.example.common.dto.DataEvent.Priority;                    // ✅ ADDED
import java.time.LocalDateTime;                                    // ✅ ADDED
import com.example.common.dto.DataEvent.DataClassification;       // ✅ ADDED

@Service
@Slf4j
public class DataIngestionService {
    
    @Autowired
    private DataEventProducer dataEventProducer;
    
    @CircuitBreaker(name = "dataEventProducer", fallbackMethod = "fallback")
    public DataEvent ingestData(DataIngestionRequest request) {
        log.info("Processing ingestion request: {}", request.getSource());
        
        DataEvent event = DataEvent.builder()
            .eventId("evt-" + System.currentTimeMillis())
            .source("ingestion-api")
            .data(request.getPayload())
            .status(DataEvent.DataEventStatus.RAW)
            .priority(Priority.MEDIUM)           // ✅ Now compiles
            .timestamp(LocalDateTime.now())      // ✅ Now compiles
            .classification(DataClassification.LOG)  // ✅ Now compiles
            .build();

        dataEventProducer.sendEvent(event);
        log.info("Event sent with ID: {}", event.getEventId());
        return event;
    }
    
    public DataEvent fallback(DataIngestionRequest request, Exception ex) {
        log.error("Circuit breaker opened for ingestion: {}", ex.getMessage());
        return null;
    }
}

