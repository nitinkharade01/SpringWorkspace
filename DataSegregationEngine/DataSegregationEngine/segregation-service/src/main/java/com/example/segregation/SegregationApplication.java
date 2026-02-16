package com.example.segregation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SegregationApplication {
    public static void main(String[] args) {
        SpringApplication.run(SegregationApplication.class, args);
    }
}
