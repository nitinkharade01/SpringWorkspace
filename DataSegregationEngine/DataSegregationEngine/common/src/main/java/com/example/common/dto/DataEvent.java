package com.example.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataEvent {
    
    @NotBlank private String eventId;
    @NotBlank private String source;  // ✅ ADD THIS
    @NotBlank private String data;
    
    @NotNull private DataClassification classification;
    
    @NotNull private DataEventStatus status;
    @NotNull private Priority priority;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    // ✅ ADD PENDING to match ingestion-service
    public enum DataEventStatus {
        RAW, PENDING, PROCESSED, FAILED, DLQ  // ✅ ADDED PENDING
    }
    
    public enum DataClassification {
        SENSOR, LOG, METRIC, EVENT
    }
    
    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}

