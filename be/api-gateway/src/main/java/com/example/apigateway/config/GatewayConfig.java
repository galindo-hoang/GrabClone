package com.example.apigateway.config;

import com.example.apigateway.config.logger.model.Logger;
import com.example.apigateway.config.logger.repository.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private RequestRecordFilter filter;
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("sms", r -> r.path("/api/v1/sms/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://SMS"))
                .route("sms", r -> r.path("/api/v1/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://AUTHENTICATION")).build();
    }

}