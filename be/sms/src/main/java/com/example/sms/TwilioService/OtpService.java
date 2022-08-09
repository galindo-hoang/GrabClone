package com.example.sms.TwilioService;


import com.example.sms.exception.InvalidOTPException;
import com.example.sms.exception.OTPExistException;
import com.example.sms.model.OTP;
import com.example.sms.payload.OTPRequest;
import com.example.sms.payload.OTPResponse;
import com.example.sms.payload.OTPStatus;
import com.example.sms.repository.OTPRepository;
import com.example.sms.utils.DateTimeFormatter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Transactional
@Service
public class OtpService {
    @Autowired
    private OtpConfig otpConfig;
    @Autowired
    private OTPRepository otpRepository;
    private final Map<String, String> otpMap = new HashMap<>();

    public OTPResponse sendOTPForPasswordReset(OTPRequest OTPRequest) {
        OTPResponse OTPResponse;
        try {
            PhoneNumber to = new PhoneNumber(OTPRequest.getPhonenumber());
            PhoneNumber from = new PhoneNumber(otpConfig.getTrialNumber());
            String otp = generateOTP();
            String otpMsg = String.format("Your OTP is %s", otp);
//            Message.creator(to, from, otpMsg).create();
            otpMap.put(OTPRequest.getPhonenumber(), otp);
            OTPResponse = new OTPResponse(OTPStatus.DELIVERED, otpMsg, ZonedDateTime.now().format(DateTimeFormatter.formatter));
        } catch (Exception e) {
            OTPResponse = new OTPResponse(OTPStatus.FAILED, e.getMessage(), ZonedDateTime.now().format(DateTimeFormatter.formatter));
        }
        return OTPResponse;
    }

    public OTPResponse validateOTP(String userInputOtp, String phoneNumber) throws InvalidOTPException {
        if (userInputOtp.equals(otpMap.get(phoneNumber))) {
            otpMap.remove(phoneNumber, userInputOtp);
            return new OTPResponse(OTPStatus.DELIVERED, "Valid OTP please proceed with your transaction !",
                    ZonedDateTime.now().format(DateTimeFormatter.formatter));
        }
        throw new InvalidOTPException("Invalid otp please retry !");
    }
    public OTP saveOtp(OTP otp) throws OTPExistException {
        return otpRepository.save(otp);
    }
    private String generateOTP() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}
