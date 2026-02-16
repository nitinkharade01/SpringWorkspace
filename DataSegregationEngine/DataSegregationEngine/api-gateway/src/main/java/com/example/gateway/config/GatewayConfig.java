package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class GatewayConfig {
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("ingestion", r -> r.path("/ingestion/**")
                .uri("lb://INGESTION-SERVICE"))
            .route("segregation", r -> r.path("/segregation/**")
                .uri("lb://SEGREGATION-SERVICE"))
            .route("persistence", r -> r.path("/persistence/**")
                .uri("lb://PERSISTENCE-SERVICE"))
            .build();
    }
}
