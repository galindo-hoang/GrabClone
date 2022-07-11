package com.example.be.model.dto.OTPDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OtpRequestDto {
    private String phonenumber;
    private String onceTimePassword;
}
