package com.example.be.controller;

import com.example.be.exception.InvalidOTPException;
import com.example.be.model.entity.User;
import com.example.be.model.payload.OTP.OTPStatus;
import com.example.be.model.payload.OTP.OTPRequest;
import com.example.be.model.payload.OTP.OTPResponse;
import com.example.be.service.UserService;
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
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<OTPResponse> sendOTP(@RequestBody OTPRequest OTPRequest) {
        OTPResponse OTPResponse = otpService.sendOTPForPasswordReset(OTPRequest);
        if (OTPResponse.getStatus().equals(OTPStatus.DELIVERED)) {
            User user = new User();
            user.setUsername(OTPRequest.getUsername());
            user.setPassword(OTPRequest.getPassword());
            user.setPhonenumber(OTPRequest.getPhonenumber());
            user.setStatus(false);
            userService.saveUser(user);
            return ResponseEntity.ok(OTPResponse);
        } else return ResponseEntity.badRequest().body(OTPResponse);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateOTP(@RequestBody OTPRequest OTPRequest) {
        try {
            OTPResponse otpResponse = otpService.validateOTP(OTPRequest.getOnceTimePassword(), OTPRequest.getPhonenumber());
            User user = userService.findByUserByPhoneNumber(OTPRequest.getPhonenumber());
            System.out.println(OTPRequest.getPhonenumber());
            user.setStatus(true);
            userService.saveUser(user);
            return ResponseEntity.ok(otpResponse);
        } catch (InvalidOTPException e) {
            throw new RuntimeException(e);
        }
    }
}