package com.example.be.service.twilio;

import com.example.be.exception.InvalidOTPException;
import com.example.be.model.dto.OTPDto.OTPStatus;
import com.example.be.model.dto.OTPDto.OtpRequestDto;
import com.example.be.model.dto.OTPDto.OtpResponseDto;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {
    @Autowired
    private OtpConfig otpConfig;
    private final Map<String, String> otpMap = new HashMap<>();
    public OtpResponseDto sendOTPForPasswordReset(OtpRequestDto otpRequestDto) {
        OtpResponseDto otpResponseDto = null;
        try{
            PhoneNumber to = new PhoneNumber(otpRequestDto.getPhonenumber());
            PhoneNumber from = new PhoneNumber(otpConfig.getTrialNumber());
            String otp = generateOTP();
            String otpMsg = String.format("Your OTP is %s", otp);
            Message.creator(to, from, otpMsg).create();
            otpMap.put(otpRequestDto.getPhonenumber(), otp);
            otpResponseDto = new OtpResponseDto(OTPStatus.DELIVERED, otpMsg);
        }catch (Exception e){
            otpResponseDto = new OtpResponseDto(OTPStatus.FAILED, e.getMessage());
        }
        return otpResponseDto;
    }
    public String validateOTP(String userInputOtp, String userName) throws InvalidOTPException {
        if (userInputOtp.equals(otpMap.get(userName))) {
            otpMap.remove(userName,userInputOtp);
            return "Valid OTP please proceed with your transaction !";
        }
        throw new InvalidOTPException("Invalid otp please retry !");
    }
    private String generateOTP(){
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}
