package com.example.persistence.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.example.common.dto.DataEvent;
// Remove RedisTemplate for now
// import org.springframework.data.redis.core.RedisTemplate;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    
    // @Autowired
    // private RedisTemplate<String, DataEvent> redisTemplate;
    
    @GetMapping("/event/{eventId}")
    public ResponseEntity<DataEvent> getEvent(@PathVariable String eventId) {
        // TODO: Fetch from cache/DB
        return ResponseEntity.ok(null);
    }
}
