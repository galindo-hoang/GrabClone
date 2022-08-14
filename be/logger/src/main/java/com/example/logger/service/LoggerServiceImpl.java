package com.example.logger.service;

import com.example.logger.model.Logger;
import com.example.logger.repository.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoggerServiceImpl implements LoggerService {
    @Autowired
    private LoggerRepository loggerRepository;
    @Override
    public Logger saveLogger(Logger logger) {
        return loggerRepository.save(logger);
    }
}
