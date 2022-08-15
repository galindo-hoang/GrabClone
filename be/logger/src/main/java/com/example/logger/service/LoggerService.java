package com.example.logger.service;

import com.example.logger.model.Logger;
import org.springframework.stereotype.Service;

@Service
public interface LoggerService {
    Logger saveLogger(Logger logger);
}
