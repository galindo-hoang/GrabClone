package com.example.be.exception;

public class InvalidOTPException extends Exception {
    public InvalidOTPException(String message) {
        super(message);
    }
}