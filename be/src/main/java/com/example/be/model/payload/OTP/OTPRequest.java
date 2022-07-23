package com.example.be.model.payload.OTP;

import com.example.be.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OTPRequest {
    private String phonenumber;
    private String onceTimePassword;
    private String username;
    private String password;
}
