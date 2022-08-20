package com.example.clients.feign.NotificationRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("FCM")
public interface NotificationRequestClient {
    @PostMapping(path = "api/v1/fcm/topic")
    ResponseEntity<String> sendPnsToTopic(@RequestBody NotificationRequestDto notificationRequestDto);

    @PostMapping(path = "api/v1/fcm/user")
    ResponseEntity<String> sendPnsToUser(@RequestBody NotificationRequestDto notificationRequestDto);

    @PostMapping(path = "api/v1/fcm/subscribe")
    ResponseEntity<Object> subscribeToTopic(@RequestBody SubscriptionRequestDto notificationRequestDto);

    @PostMapping(path = "api/v1/fcm/unsubscribe")
    ResponseEntity<Object> unsubscribeFromTopic(@RequestBody SubscriptionRequestDto subscriptionRequestDto);
}