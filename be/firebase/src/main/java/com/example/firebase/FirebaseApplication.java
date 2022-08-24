package com.example.firebase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = {
                "com.example.firebase",
                "com.example.rabbitmq"
        }
)
@EnableEurekaClient
@EnableFeignClients(
        basePackages = "com.example.clients")
public class FirebaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirebaseApplication.class, args);
    }

}
