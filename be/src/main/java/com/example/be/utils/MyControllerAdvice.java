package com.example.be.utils;


import com.example.be.exception.InvalidOTPException;
import com.example.be.exception.UsernameAlreadyExistException;
import com.example.be.exception.UsernameNotFoundException;
import com.example.be.model.payload.ErrorResponse;
import com.example.be.model.payload.OTP.OTPRequest;
import com.example.be.model.payload.OTP.OTPResponse;
import com.example.be.model.payload.OTP.OTPStatus;
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
                "com.UsernameNotFoundException",ex.getMessage()
        );
        log.error("UsernameNotFoundException: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ResponseEntity<Object> handleUsernameAlreadyExistException(UsernameAlreadyExistException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ZonedDateTime.now().format(DateTimeFormatter.formatter),
                "com.UsernameAlreadyExistException",ex.getMessage());
        log.error("UsernameAlreadyExistException: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(InvalidOTPException.class)
    public ResponseEntity<Object> handleInvalidOTPException(InvalidOTPException ex) {
        OTPResponse errorResponse = new OTPResponse(
                OTPStatus.FAILED,
                ex.getMessage(),
                ZonedDateTime.now().format(DateTimeFormatter.formatter)
        );
        log.error("InvalidOTPException: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                ZonedDateTime.now().format(DateTimeFormatter.formatter),
                "com.Exception",exception.getMessage());
        log.error("Exception: {}", exception.getMessage());
        return new ResponseEntity<>
                (errorResponse,
                        HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
