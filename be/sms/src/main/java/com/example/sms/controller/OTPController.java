package com.example.sms.controller;

import com.example.sms.TwilioService.OtpService;
import com.example.sms.exception.InvalidOTPException;
import com.example.sms.exception.OTPExistException;
import com.example.sms.model.OTP;
import com.example.sms.payload.OTPRequest;
import com.example.sms.payload.OTPResponse;
import com.example.sms.payload.OTPStatus;
import com.example.sms.utils.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/v1/sms")
@CrossOrigin(origins = "http://localhost:3000")
public class OTPController {
    @Autowired
    private OtpService otpService;

    @GetMapping()
    public String get() {
        return "Hello World";
    }

    @PostMapping("/register")
    public ResponseEntity<OTPResponse> sendOTP(@RequestBody OTPRequest OTPRequest) {
        OTPResponse OTPResponse = otpService.sendOTPForPasswordReset(OTPRequest);
        if (OTPResponse.getStatus().equals(OTPStatus.DELIVERED)) {
            try {
                otpService.saveOtp(new OTP(OTPRequest.getPhonenumber(), OTPResponse.getOTP()));
            } catch (OTPExistException e) {
                return ResponseEntity.badRequest().body(new OTPResponse(OTPStatus.FAILED, "OTP already exist !",
                        ZonedDateTime.now().format(DateTimeFormatter.formatter)));
            }
            return ResponseEntity.ok(OTPResponse);
        } else return ResponseEntity.badRequest().body(OTPResponse);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateOTP(@RequestBody OTPRequest OTPRequest) {
        try {
            OTPResponse otpResponse = otpService.validateOTP(OTPRequest.getOnceTimePassword(), OTPRequest.getPhonenumber());
            return ResponseEntity.ok(otpResponse);
        } catch (InvalidOTPException e) {
            throw new RuntimeException(e);
        }
    }
}