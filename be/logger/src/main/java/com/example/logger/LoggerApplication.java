package com.example.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class LoggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoggerApplication.class, args);
    }

}
