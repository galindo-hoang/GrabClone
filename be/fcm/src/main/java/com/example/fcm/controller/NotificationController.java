package com.example.fcm.controller;

import com.example.clients.feign.NotificationRequest.NotificationRequestDto;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.fcm.model.domain.*;
import com.example.fcm.model.dto.*;
import com.example.fcm.model.entity.*;
import com.example.fcm.service.*;
@CrossOrigin("localhost:3000")
@RequestMapping("/api/v1/fcm")
@RestController
@Slf4j
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TokenStoreService tokenStoreService;

    @Autowired
    private TopicNameService topicNameService;

    @PostMapping("/subscribe")
    public ResponseEntity<Object> subscribeToTopic(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService.findByUsername(subscriptionRequestDto.getUsername());
            if (tokenRecord != null) {
                SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
                        subscriptionRequestDto.getTopicName(), tokenRecord.getFcmToken());
                notificationService.subscribeToTopic(subscriptionRequest);
                if (topicNameService.findByTopicName(subscriptionRequestDto.getTopicName()) == null) {
                    topicNameService.save(new TopicNameRecord(null, subscriptionRequestDto.getTopicName()));
                    log.info("Create topic name: {}", subscriptionRequestDto.getTopicName());
                }
                log.info("Subscribe to topic: {} for userId: {} success", subscriptionRequestDto.getTopicName(),
                        subscriptionRequestDto.getUsername());
            } else {
                log.info("Subscribe to topic: {} for userId: {} failed, token not found",
                        subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUsername());
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Subscribe to topic: {} for userId: {} error", subscriptionRequestDto.getTopicName(),
                    subscriptionRequestDto.getUsername(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Object> unsubscribeFromTopic(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService.findByUsername(subscriptionRequestDto.getUsername());
            TopicNameRecord topicNameRecord = topicNameService.findByTopicName(subscriptionRequestDto.getTopicName());
            if (tokenRecord != null) {
                if (topicNameRecord != null) {
                    SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
                            subscriptionRequestDto.getTopicName(), tokenRecord.getFcmToken());
                    notificationService.unsubscribeFromTopic(subscriptionRequest);
                    log.info("Unsubscribe to topic: {} for userId: {} success", subscriptionRequestDto.getTopicName(),
                            subscriptionRequestDto.getUsername());
                } else {
                    log.info("Unsubscribe to topic: {} for userId: {} failed, topic not found",
                            subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUsername());
                }
            } else if (topicNameRecord != null) {
                log.info("Unsubscribe to topic: {} for userId: {} failed, token not found",
                        subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUsername());
            } else {
                log.info("Unsubscribe to topic: {} for userId: {} failed, token and topic not found",
                        subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUsername());
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Unsubscribe to topic: {} for userId: {} error", subscriptionRequestDto.getTopicName(),
                    subscriptionRequestDto.getUsername(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/user")
    public ResponseEntity<String> sendPnsToUser(@RequestBody NotificationRequestDto notificationRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService
                    .findByUsername(notificationRequestDto.getTarget());
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
                    return ResponseEntity.ok(response);
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
        return ResponseEntity.badRequest().body("Error");
    }

    @PostMapping("/topic")
    public ResponseEntity<String> sendPnsToTopic(@RequestBody NotificationRequestDto notificationRequestDto) {
        try {
            if (topicNameService.findByTopicName(notificationRequestDto.getTarget()) != null) {
                NotificationRequest notificationRequest = new NotificationRequest(
                        notificationRequestDto.getTarget(),
                        notificationRequestDto.getTitle(),
                        notificationRequestDto.getBody(),
                        notificationRequestDto.getData());
                log.info("Send PNS to topic: {} success", notificationRequestDto.getTarget());
                return ResponseEntity.ok(notificationService.sendPnsToTopic(notificationRequest));
            } else {
                log.info("No topic found for topicName: {}", notificationRequestDto.getTarget());
            }
        } catch (Exception e) {
            log.error("Send PNS to topic: {} error", notificationRequestDto.getTarget(), e);
        }
        return ResponseEntity.badRequest().body("Error");
    }
}
