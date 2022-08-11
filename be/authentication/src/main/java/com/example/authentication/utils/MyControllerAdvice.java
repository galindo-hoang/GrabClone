package com.example.authentication.utils;


import com.example.authentication.exception.UsernameAlreadyExistException;
import com.example.authentication.exception.UsernameNotFoundException;
import com.example.authentication.model.payload.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class MyControllerAdvice extends ResponseEntityExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(MyControllerAdvice.class);

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ZonedDateTime.now().format(DateTimeFormatter.formatter),
                "com.UsernameNotFoundException", ex.getMessage()
        );
        log.error("UsernameNotFoundException: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ResponseEntity<Object> handleUsernameAlreadyExistException(UsernameAlreadyExistException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ZonedDateTime.now().format(DateTimeFormatter.formatter),
                "com.UsernameAlreadyExistException", ex.getMessage());
        log.error("UsernameAlreadyExistException: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                ZonedDateTime.now().format(DateTimeFormatter.formatter),
                "com.Exception", exception.getMessage());
        log.error("Exception: {}", exception.getMessage());
        return new ResponseEntity<>
                (errorResponse,
                        HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
