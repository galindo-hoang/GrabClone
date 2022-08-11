package com.example.fcm.model.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequest {
    private String topicName;
    private String fcmToken;
}