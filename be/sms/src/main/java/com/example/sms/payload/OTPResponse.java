package com.example.sms.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTPResponse {
    private OTPStatus status;
    private String message;
    private String timestamp;
    public String getOTP(){
        return this.message.substring(this.message.length()-6);
    }
}
