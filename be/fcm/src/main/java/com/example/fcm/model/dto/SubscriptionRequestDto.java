package com.example.fcm.model.dto;

import lombok.Data;

@Data
public class SubscriptionRequestDto {
    private String topicName;
    private Integer userId;
}