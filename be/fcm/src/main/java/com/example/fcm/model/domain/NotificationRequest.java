package com.example.fcm.model.domain;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationRequest {
    private String target;
    private String title;
    private String body;
    private Map<String, String> data;
}