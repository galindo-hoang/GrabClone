package com.example.be.controller;

import com.example.be.exception.InvalidOTPException;
import com.example.be.model.dto.OTPDto.OTPStatus;
import com.example.be.model.dto.OTPDto.OtpRequestDto;
import com.example.be.model.dto.OTPDto.OtpResponseDto;
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
    public ResponseEntity<OtpResponseDto> sendOTP(@RequestBody OtpRequestDto otpRequestDto) {
        OtpResponseDto otpResponseDto = otpService.sendOTPForPasswordReset(otpRequestDto);
        if (otpResponseDto.getStatus().equals(OTPStatus.DELIVERED))
            return ResponseEntity.ok(otpResponseDto);
        else return ResponseEntity.badRequest().body(otpResponseDto);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateOTP(@RequestBody OtpRequestDto otpRequestDto) throws InvalidOTPException {
        return ResponseEntity.ok(otpService.validateOTP(otpRequestDto.getOnceTimePassword(), otpRequestDto.getPhonenumber()));
    }
}
