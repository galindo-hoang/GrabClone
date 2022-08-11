package com.example.sms.utils;

import com.example.sms.exception.InvalidOTPException;
import com.example.sms.exception.OTPExistException;
import com.example.sms.payload.OTPResponse;
import com.example.sms.payload.OTPStatus;
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

    @ExceptionHandler(OTPExistException.class)
    public ResponseEntity<Object> handleOTPExist(OTPExistException ex) {
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
        log.error("Exception: {}", exception.getMessage());
        return new ResponseEntity<>
                ("Exception: " + exception.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
