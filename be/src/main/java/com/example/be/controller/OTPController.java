package com.example.be.controller;

import com.example.be.exception.InvalidOTPException;
import com.example.be.model.payload.OTP.OTPStatus;
import com.example.be.model.payload.OTP.OTPRequest;
import com.example.be.model.payload.OTP.OTPResponse;
import com.example.be.service.twilio.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
public class OTPController {
    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<OTPResponse> sendOTP(@RequestBody OTPRequest OTPRequest) {
        OTPResponse OTPResponse = otpService.sendOTPForPasswordReset(OTPRequest);
        if (OTPResponse.getStatus().equals(OTPStatus.DELIVERED))
            return ResponseEntity.ok(OTPResponse);
        else return ResponseEntity.badRequest().body(OTPResponse);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateOTP(@RequestBody OTPRequest OTPRequest) throws InvalidOTPException {
        return ResponseEntity.ok(otpService.validateOTP(OTPRequest.getOnceTimePassword(), OTPRequest.getPhonenumber()));
    }
}
