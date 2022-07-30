package com.example.be.model.payload.OTP;

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
}
