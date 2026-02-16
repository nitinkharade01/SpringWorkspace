package com.example.segregation.kafka;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;  // ✅ FIXED
import com.example.common.dto.DataEvent;
import com.example.common.dto.DataEvent.DataClassification;
import com.example.common.dto.DataEvent.DataEventStatus;

@Service
@Slf4j
public class DataSegregationConsumer {
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    @KafkaListener(topics = "raw-data-topic", groupId = "segregation-group")
    public void processRawData(DataEvent event, Acknowledgment ack) {
        try {
            DataClassification classification = classifyData(event);
            
            DataEvent processedEvent = DataEvent.builder()  // ✅ Use DataEvent.builder()
                .eventId(event.getEventId())
                .data(event.getData())
                .classification(classification)
                .status(DataEventStatus.PROCESSED)
                .priority(event.getPriority())
                .timestamp(event.getTimestamp())
                .build();
            
            kafkaTemplate.send("processed-data-topic", processedEvent);
            ack.acknowledge();
            log.info("Processed event: {}", event.getEventId());
            
        } catch (Exception e) {
            log.error("Processing failed: {}", event.getEventId(), e);
        }
    }
    
    private DataClassification classifyData(DataEvent event) {
        if (event.getData().contains("temp")) return DataClassification.SENSOR;
        if (event.getData().contains("ERROR")) return DataClassification.LOG;
        return DataClassification.EVENT;
    }
}

