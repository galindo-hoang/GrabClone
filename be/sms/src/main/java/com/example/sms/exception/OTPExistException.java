package com.example.sms.exception;

public class OTPExistException extends Exception {
    public OTPExistException(String message) {
        super(message);
    }
}
