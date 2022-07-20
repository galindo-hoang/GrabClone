package com.example.be.service.twilio;

import com.example.be.exception.InvalidOTPException;
import com.example.be.model.payload.OTP.OTPStatus;
import com.example.be.model.payload.OTP.OTPRequest;
import com.example.be.model.payload.OTP.OTPResponse;
import com.example.be.utils.DateTimeFormatter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {
    @Autowired
    private OtpConfig otpConfig;
    private final Map<String, String> otpMap = new HashMap<>();
    public OTPResponse sendOTPForPasswordReset(OTPRequest OTPRequest) {
        OTPResponse OTPResponse = null;
        try{
            PhoneNumber to = new PhoneNumber(OTPRequest.getPhonenumber());
            PhoneNumber from = new PhoneNumber(otpConfig.getTrialNumber());
            String otp = generateOTP();
            String otpMsg = String.format("Your OTP is %s", otp);
            Message.creator(to, from, otpMsg).create();
            otpMap.put(OTPRequest.getPhonenumber(), otp);
            OTPResponse = new OTPResponse(OTPStatus.DELIVERED, otpMsg, ZonedDateTime.now().format(DateTimeFormatter.formatter));
        }catch (Exception e){
            OTPResponse = new OTPResponse(OTPStatus.FAILED, e.getMessage(),ZonedDateTime.now().format(DateTimeFormatter.formatter));
        }
        return OTPResponse;
    }
    public OTPResponse validateOTP(String userInputOtp, String userName) throws InvalidOTPException {
        if (userInputOtp.equals(otpMap.get(userName))) {
            otpMap.remove(userName,userInputOtp);
            return new OTPResponse(OTPStatus.DELIVERED,"Valid OTP please proceed with your transaction !",
                    ZonedDateTime.now().format(DateTimeFormatter.formatter));
        }
        throw new InvalidOTPException("Invalid otp please retry !");
    }
    private String generateOTP(){
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}
