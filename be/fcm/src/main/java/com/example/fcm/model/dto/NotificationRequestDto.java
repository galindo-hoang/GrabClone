package com.example.fcm.model.dto;

import lombok.Data;

@Data
public class NotificationRequestDto {
    private String target;
    private String title;
    private String body;
}