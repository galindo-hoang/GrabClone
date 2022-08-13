package com.example.clients.feign.logger;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("logger")
public interface LoggerClient {
    @PostMapping(path = "/api/v1/logger")
    Logger saveLogger(@RequestBody Logger logger);
}
