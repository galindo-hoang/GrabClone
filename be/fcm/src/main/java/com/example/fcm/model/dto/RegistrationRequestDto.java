package com.example.fcm.model.dto;

import lombok.Data;

@Data
public class RegistrationRequestDto {
    private String fcmToken;
    private String username;
}
