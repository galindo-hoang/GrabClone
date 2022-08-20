package com.example.clients.feign.NotificationRequest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionRequestDto {
    private String topicName;
    private String username;
}