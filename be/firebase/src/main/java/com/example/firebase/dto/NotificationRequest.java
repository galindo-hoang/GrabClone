package com.example.firebase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class NotificationRequest {
    private String target;
    private String title;
    private String body;
    private Map<String, String> data;
}