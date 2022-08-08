package com.example.fcm.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.fcm.model.domain.*;
import com.example.fcm.model.dto.*;
import com.example.fcm.model.entity.*;
import com.example.fcm.service.NotificationService;
import com.example.fcm.service.TokenStoreService;

@RestController
@Slf4j
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TokenStoreService tokenStoreService;

    @PostMapping("/register")
    public void register(@RequestBody RegistrationRequestDto registrationRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService.findByUserId(registrationRequestDto.getUserId());
            if (tokenRecord != null) {
                if (!tokenRecord.getFcmToken().equals(registrationRequestDto.getFcmToken())) {
                    tokenRecord.setFcmToken(registrationRequestDto.getFcmToken());
                    tokenRecord.setCreatedAt(new Date());
                    tokenStoreService.save(tokenRecord);
                    log.info("Update token: {} for userId: {}", registrationRequestDto.getFcmToken(), registrationRequestDto.getUserId());
                } else {
                    log.info("UserId: {} with token: {} is already registered", registrationRequestDto.getUserId(), registrationRequestDto.getFcmToken());
                }
            } else {
                tokenRecord = tokenStoreService.findByFcmToken(registrationRequestDto.getFcmToken());
                if (tokenRecord != null) {
                    log.info("Request token registration: {} from userId: {} is already registered for userId: {}",
                        registrationRequestDto.getFcmToken(), registrationRequestDto.getUserId(), tokenRecord.getUserId());
                } else {
                    tokenRecord = new FcmTokenRecord(null, registrationRequestDto.getUserId(), registrationRequestDto.getFcmToken(), new Date());
                    tokenStoreService.save(tokenRecord);
                    log.info("Register token: {} for userId: {} success", registrationRequestDto.getFcmToken(), registrationRequestDto.getUserId());
                }
            }
        } catch (Exception e) {
            log.error("Store token: {} for userId: {} error", registrationRequestDto.getFcmToken(), registrationRequestDto.getUserId(), e);
        }
    }

    @PostMapping("/subscribe")
    public void subscribeToTopic(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService.findByUserId(subscriptionRequestDto.getUserId());
            if (tokenRecord != null) {
                SubscriptionRequest subscriptionRequest = new SubscriptionRequest(tokenRecord.getFcmToken(), subscriptionRequestDto.getTopicName());
                notificationService.subscribeToTopic(subscriptionRequest);
                log.info("Subscribe to topic: {} for userId: {} success", subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUserId());
            } else {
                log.info("No token found for userId: {}", subscriptionRequestDto.getUserId());
            }
        } catch (Exception e) {
            log.error("Subscribe to topic: {} for userId: {} error", subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUserId(), e);
        }
    }

    @PostMapping("/unsubscribe")
    public void unsubscribeFromTopic(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService.findByUserId(subscriptionRequestDto.getUserId());
            if (tokenRecord != null) {
                SubscriptionRequest subscriptionRequest = new SubscriptionRequest(tokenRecord.getFcmToken(), subscriptionRequestDto.getTopicName());
                notificationService.unsubscribeFromTopic(subscriptionRequest);
                log.info("Unsubscribe to topic: {} for userId: {} success", subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUserId());
            } else {
                log.info("No token found for userId: {}", subscriptionRequestDto.getUserId());
            }
        } catch (Exception e) {
            log.error("Unsubscribe to topic: {} for userId: {} error", subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUserId(), e);
        }
    }

    @PostMapping("/user")
    public String sendPnsToUser(@RequestBody NotificationRequestDto notificationRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService.findByUserId(Integer.parseInt(notificationRequestDto.getTarget()));
            if (tokenRecord != null) {
                NotificationRequest notificationRequest = new NotificationRequest(tokenRecord.getFcmToken(), notificationRequestDto.getTitle(), notificationRequestDto.getBody());
                String response = notificationService.sendPnsToDevice(notificationRequest);
                if (response != null) {
                    log.info("Send PNS to device: {} for userId: {}, title: {}, body: {} success", tokenRecord.getFcmToken(), notificationRequestDto.getTarget(), notificationRequestDto.getTitle(), notificationRequestDto.getBody());
                    return response;
                } else {
                    log.info("No response from FCM for userId: {}", notificationRequestDto.getTarget());
                }
            } else {
                log.info("No token found for userId: {}", notificationRequestDto.getTarget());
            }
        } catch (Exception e) {
            log.error("Send PNS to device: {} for userId: {} error", notificationRequestDto.getTarget(), notificationRequestDto.getTarget(), e);
        }
        return "Error";
    }

    @PostMapping("/topic")
    public String sendPnsToTopic(@RequestBody NotificationRequestDto notificationRequestDto) {
        NotificationRequest notificationRequest = new NotificationRequest(
            notificationRequestDto.getTarget(),
            notificationRequestDto.getTitle(),
            notificationRequestDto.getBody()
            );
        log.info("Send PNS to topic: {} success", notificationRequestDto.getTarget());
        return notificationService.sendPnsToTopic(notificationRequest);
    }
}
