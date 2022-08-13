package com.example.apigateway;

import com.example.apigateway.config.logger.model.Logger;
import com.example.apigateway.config.logger.repository.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = {
                "com.example.apigateway",
        }
)
@EnableFeignClients(
        basePackages = "com.example.clients"
)
@EnableEurekaClient

public class ApiGatewayApplication {
    private LoggerRepository loggerRepository;
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
