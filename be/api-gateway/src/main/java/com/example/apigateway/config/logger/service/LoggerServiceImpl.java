package com.example.apigateway.config.logger.service;


import com.example.apigateway.config.logger.model.Logger;
import com.example.apigateway.config.logger.repository.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoggerServiceImpl implements LoggerService {
    @Autowired
    private LoggerRepository loggerRepository;
    @Override
    public Logger saveLogger(Logger logger) {
        return loggerRepository.save(logger);
    }

    @Override
    public Optional<Logger> findLogger(Integer id) {
        return loggerRepository.findById(id);
    }

    @Override
    public Optional<Logger> findByRequestId(String requestId) {
        return loggerRepository.findByRequestId(requestId);
    }
}
