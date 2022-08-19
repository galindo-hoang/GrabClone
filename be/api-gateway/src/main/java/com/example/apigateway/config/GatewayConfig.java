package com.example.apigateway.config;

import com.example.apigateway.config.logger.model.Logger;
import com.example.apigateway.config.logger.repository.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@CrossOrigin(origins = "http://localhost:3000")
public class GatewayConfig {

    @Autowired
    private RequestRecordFilter filter;
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("sms", r -> r.path("/api/v1/sms/**")
                        .filters(f -> f
                                .setResponseHeader("Access-Control-Allow-Origin", "http://localhost:3000")
                                .filter(filter))
                        .uri("lb://SMS"))
                .route("authentication", r -> r.path("/api/v1/users/**")
                        .filters(f -> f
                                .setResponseHeader("Access-Control-Allow-Origin", "http://localhost:3000")
                                .filter(filter))
                        .uri("lb://AUTHENTICATION"))
                .route("booking", r -> r.path("/api/v1/booking/**")
                        .filters(f -> f
                                .setResponseHeader("Access-Control-Allow-Origin", "http://localhost:3000")
                                .filter(filter))
                        .uri("lb://BOOKING"))
                .route("fcm", r -> r.path("/api/v1/fcm-publish/**")
                        .filters(f -> f
                                .setResponseHeader("Access-Control-Allow-Origin", "http://localhost:3000")
                                .filter(filter))
                        .uri("lb://FCM"))
                .build();

    }

}