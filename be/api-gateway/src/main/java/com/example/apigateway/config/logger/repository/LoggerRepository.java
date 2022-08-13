package com.example.apigateway.config.logger.repository;


import com.example.apigateway.config.logger.model.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoggerRepository extends JpaRepository<Logger, Integer> {
    Optional<Logger> findByRequestId(String requestId);
}
