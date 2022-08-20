package com.example.clients.feign.NotificationRequest;

import lombok.Data;

@Data
public class RegistrationRequestDto {
    private String fcmToken;
    private String username;
}
