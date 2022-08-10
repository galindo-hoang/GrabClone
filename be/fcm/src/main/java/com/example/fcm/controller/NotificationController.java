package com.example.fcm.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fcm.model.domain.*;
import com.example.fcm.model.dto.*;
import com.example.fcm.model.entity.*;
import com.example.fcm.service.*;

@RequestMapping("/fcm")
@RestController
@Slf4j
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TokenStoreService tokenStoreService;

    @Autowired
    private TopicNameService topicNameService;

    @PostMapping("/register")
    public void register(@RequestBody RegistrationRequestDto registrationRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService.findByFcmToken(registrationRequestDto.getFcmToken());
            if (tokenRecord == null) {
                tokenRecord = tokenStoreService.findByUserId(registrationRequestDto.getUserId());
                if (tokenRecord != null) {
                    tokenRecord.setFcmToken(registrationRequestDto.getFcmToken());
                    tokenRecord.setCreatedAt(new Date());
                    tokenStoreService.save(tokenRecord);
                    log.info("Update token: {} for userId: {} success", registrationRequestDto.getFcmToken(),
                            registrationRequestDto.getUserId());
                } else {
                    tokenRecord = new FcmTokenRecord(null, registrationRequestDto.getUserId(),
                            registrationRequestDto.getFcmToken(), new Date());
                    tokenStoreService.save(tokenRecord);
                    log.info("Register token: {} for userId: {} success", registrationRequestDto.getFcmToken(),
                            registrationRequestDto.getUserId());
                }
            } else {
                if (tokenRecord.getUserId().equals(registrationRequestDto.getUserId())) {
                    log.info("Token: {} not changed for userId: {}, token is the same as before",
                            registrationRequestDto.getFcmToken(),
                            registrationRequestDto.getUserId());
                } else {
                    log.info("Register token: {} for userId: {} failed, token already registered by userId: {}",
                            registrationRequestDto.getFcmToken(), registrationRequestDto.getUserId(),
                            tokenRecord.getUserId());
                }
            }
        } catch (Exception e) {
            log.error("Store token: {} for userId: {} error", registrationRequestDto.getFcmToken(),
                    registrationRequestDto.getUserId(), e);
        }
    }

    @PostMapping("/subscribe")
    public void subscribeToTopic(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService.findByUserId(subscriptionRequestDto.getUserId());
            if (tokenRecord != null) {
                SubscriptionRequest subscriptionRequest = new SubscriptionRequest(tokenRecord.getFcmToken(),
                        subscriptionRequestDto.getTopicName());
                notificationService.subscribeToTopic(subscriptionRequest);
                if (topicNameService.findByTopicName(subscriptionRequestDto.getTopicName()) == null) {
                    topicNameService.save(new TopicNameRecord(null, subscriptionRequestDto.getTopicName()));
                    log.info("Create topic name: {}", subscriptionRequestDto.getTopicName());
                }
                log.info("Subscribe to topic: {} for userId: {} success", subscriptionRequestDto.getTopicName(),
                        subscriptionRequestDto.getUserId());
            } else {
                log.info("Subscribe to topic: {} for userId: {} failed, token not found",
                        subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUserId());
            }
        } catch (Exception e) {
            log.error("Subscribe to topic: {} for userId: {} error", subscriptionRequestDto.getTopicName(),
                    subscriptionRequestDto.getUserId(), e);
        }
    }

    @PostMapping("/unsubscribe")
    public void unsubscribeFromTopic(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService.findByUserId(subscriptionRequestDto.getUserId());
            TopicNameRecord topicNameRecord = topicNameService.findByTopicName(subscriptionRequestDto.getTopicName());
            if (tokenRecord != null) {
                if (topicNameRecord != null) {
                    SubscriptionRequest subscriptionRequest = new SubscriptionRequest(tokenRecord.getFcmToken(),
                            subscriptionRequestDto.getTopicName());
                    notificationService.unsubscribeFromTopic(subscriptionRequest);
                    log.info("Unsubscribe to topic: {} for userId: {} success", subscriptionRequestDto.getTopicName(),
                            subscriptionRequestDto.getUserId());
                } else {
                    log.info("Unsubscribe to topic: {} for userId: {} failed, topic not found",
                            subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUserId());
                }
            } else if (topicNameRecord != null) {
                log.info("Unsubscribe to topic: {} for userId: {} failed, token not found",
                        subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUserId());
            } else {
                log.info("Unsubscribe to topic: {} for userId: {} failed, token and topic not found",
                        subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUserId());
            }
        } catch (Exception e) {
            log.error("Unsubscribe to topic: {} for userId: {} error", subscriptionRequestDto.getTopicName(),
                    subscriptionRequestDto.getUserId(), e);
        }
    }

    @PostMapping("/user")
    public String sendPnsToUser(@RequestBody NotificationRequestDto notificationRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService
                    .findByUserId(Integer.parseInt(notificationRequestDto.getTarget()));
            if (tokenRecord != null) {
                NotificationRequest notificationRequest = new NotificationRequest(
                        tokenRecord.getFcmToken(),
                        notificationRequestDto.getTitle(),
                        notificationRequestDto.getBody(),
                        notificationRequestDto.getData());
                String response = notificationService.sendPnsToDevice(notificationRequest);
                if (response != null) {
                    log.info("Send PNS to device: {} for userId: {}, title: {}, body: {}, data: {} success",
                            tokenRecord.getFcmToken(), notificationRequestDto.getTarget(),
                            notificationRequestDto.getTitle(), notificationRequestDto.getBody(),
                            notificationRequestDto.getData());
                    return response;
                } else {
                    log.info("No response from FCM for userId: {}", notificationRequestDto.getTarget());
                }
            } else {
                log.info("No token found for userId: {}", notificationRequestDto.getTarget());
            }
        } catch (Exception e) {
            log.error("Send PNS to device: {} for userId: {} error", notificationRequestDto.getTarget(),
                    notificationRequestDto.getTarget(), e);
        }
        return "Error";
    }

    @PostMapping("/topic")
    public String sendPnsToTopic(@RequestBody NotificationRequestDto notificationRequestDto) {
        try {
            if (topicNameService.findByTopicName(notificationRequestDto.getTarget()) != null) {
                NotificationRequest notificationRequest = new NotificationRequest(
                        notificationRequestDto.getTarget(),
                        notificationRequestDto.getTitle(),
                        notificationRequestDto.getBody(),
                        notificationRequestDto.getData());
                log.info("Send PNS to topic: {} success", notificationRequestDto.getTarget());
                return notificationService.sendPnsToTopic(notificationRequest);
            } else {
                log.info("No topic found for topicName: {}", notificationRequestDto.getTarget());
            }
        } catch (Exception e) {
            log.error("Send PNS to topic: {} error", notificationRequestDto.getTarget(), e);
        }
        return "Error";
    }
}
