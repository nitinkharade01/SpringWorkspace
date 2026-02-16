package com.example.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "data_events")
@Data
@Builder  // ← ADD THIS
@NoArgsConstructor
@AllArgsConstructor
public class DataEventEntity {
    
    @Id
    private String eventId;
    
    private String data;
    
    @Enumerated(EnumType.STRING)
    private DataEventStatus status;
    
    @Column
    private LocalDateTime timestamp;
    
    public enum DataEventStatus {
        RAW, PENDING, PROCESSED, FAILED  // Match your DTO
    }
}
