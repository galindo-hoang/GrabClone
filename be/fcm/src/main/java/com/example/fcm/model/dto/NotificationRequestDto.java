package com.example.fcm.model.dto;

import java.util.Map;

import lombok.Data;

@Data
public class NotificationRequestDto {
    private String target;
    private String title;
    private String body;
    private Map<String, String> data;
}