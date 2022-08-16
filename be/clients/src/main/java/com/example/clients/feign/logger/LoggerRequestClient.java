package com.example.clients.feign.logger;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("logger")
public interface LoggerRequestClient {
    @PostMapping(path = "api/v1/logger")
    ResponseEntity<LoggerRequest> saveLogger(@RequestBody LoggerRequest loggerRequest);
}
