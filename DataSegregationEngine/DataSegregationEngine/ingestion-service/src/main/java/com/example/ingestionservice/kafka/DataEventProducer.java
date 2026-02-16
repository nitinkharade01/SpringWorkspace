package com.example.ingestionservice.kafka;

import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;
import lombok.extern.slf4j.Slf4j;
import com.example.common.dto.DataEvent;

@Component
@Slf4j
public class DataEventProducer {
    
    private final KafkaTemplate<String, DataEvent> kafkaTemplate;
    
    public DataEventProducer(KafkaTemplate<String, DataEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void sendEvent(DataEvent event) {
        kafkaTemplate.send("data-events", event.getEventId(), event)
        .whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Event sent successfully: {}", event.getEventId());
            } else {
                log.error("Failed to send event", ex);
            }
        });
    }
}
