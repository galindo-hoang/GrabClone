package com.example.clients.feign.NotificationRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("FCM")
public interface NotificationRequestClient {
    @PostMapping(path = "api/v1/topic")
    String sendPnsToUser(@RequestBody NotificationRequestDto notificationRequestDto);
}