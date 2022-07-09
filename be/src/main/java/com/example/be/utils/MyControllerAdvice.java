package com.example.be.utils;


import com.example.be.exception.UsernameAlreadyExistException;
import com.example.be.exception.UsernameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MyControllerAdvice extends ResponseEntityExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(MyControllerAdvice.class);

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(null, ex.getMessage());
        log.error("UsernameNotFoundException: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ResponseEntity<Object> handleUsernameAlreadyExistException(UsernameAlreadyExistException ex) {
        ErrorResponse errorResponse = new ErrorResponse(null, ex.getMessage());
        log.error("UsernameAlreadyExistException: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(null, exception.getMessage());
        log.debug(exception.getMessage());
        return new ResponseEntity<>
                (errorResponse,
                        HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
