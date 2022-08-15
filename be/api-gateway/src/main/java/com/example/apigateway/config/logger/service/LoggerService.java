package com.example.apigateway.config.logger.service;

import com.example.apigateway.config.logger.model.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface LoggerService {
    Logger saveLogger(Logger logger);
    Optional<Logger> findLogger(Integer id);
    Optional<Logger> findByRequestId(String requestId);
}
