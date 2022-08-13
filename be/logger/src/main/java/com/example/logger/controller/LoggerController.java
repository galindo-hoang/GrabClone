package com.example.logger.controller;

import com.example.logger.model.Logger;
import com.example.logger.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logger")
public class LoggerController {
    @Autowired
    private LoggerService loggerService;

    @PostMapping()
    public ResponseEntity<Logger> saveLogger(@RequestBody Logger logger) {
        return ResponseEntity.ok(loggerService.saveLogger(logger));
    }
}
