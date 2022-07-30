package com.example.fcm.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubscriptionRequestDto {

    String topicName;
    String tokens;
}
