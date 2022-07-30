package com.example.fcm.dto;

import lombok.Data;

@Data
public class NotificationRequestDto {

    private String target;
    private String title;
    private String body;
}
