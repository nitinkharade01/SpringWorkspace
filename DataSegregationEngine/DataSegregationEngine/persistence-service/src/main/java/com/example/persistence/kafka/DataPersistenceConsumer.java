package com.example.persistence.kafka;

import com.example.common.dto.DataEvent;
import com.example.persistence.entity.DataEventEntity;
import com.example.persistence.repository.DataEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataPersistenceConsumer {

    private final DataEventRepository dataEventRepository;
    private final ObjectMapper objectMapper;

    // ✅ NOW WORKS WITH YOUR DataEvent DTO!
    @KafkaListener(
        topics = "${app.kafka.input-topic:processed-data-topic}",
        groupId = "${spring.kafka.consumer.group-id:persistence-group}"
    )
    public void persistData(DataEvent dataEvent) {
        log.info("✅ Received DataEvent: eventId={}, status={}", 
                dataEvent.getEventId(), dataEvent.getStatus());
        
        try {
            // 1. Convert DTO → Entity
            DataEventEntity entity = DataEventEntity.builder()
                .eventId(dataEvent.getEventId())
                .data(objectMapper.writeValueAsString(dataEvent))
                .status(DataEventEntity.DataEventStatus.valueOf(dataEvent.getStatus().name()))
                .timestamp(dataEvent.getTimestamp())
                .build();
            
            // 2. Save to MySQL
            dataEventRepository.save(entity);
            log.info("✅ Saved DataEventEntity: {}", entity.getEventId());
            
        } catch (Exception e) {
            log.error("❌ Failed to persist: {}", e.getMessage());
            // TODO: Send to DLQ
        }
    }
}
