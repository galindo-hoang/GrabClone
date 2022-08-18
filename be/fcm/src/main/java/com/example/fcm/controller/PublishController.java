package com.example.fcm.controller;

import com.example.fcm.model.domain.SubscriptionRequest;
import com.example.fcm.model.dto.RegistrationRequestDto;
import com.example.fcm.model.dto.SubscriptionRequestDto;
import com.example.fcm.model.entity.FcmTokenRecord;
import com.example.fcm.model.entity.TopicNameRecord;
import com.example.fcm.service.NotificationService;
import com.example.fcm.service.TokenStoreService;
import com.example.fcm.service.TopicNameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/v1/fcm-publish")
@Slf4j
public class PublishController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TokenStoreService tokenStoreService;

    @Autowired
    private TopicNameService topicNameService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegistrationRequestDto registrationRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService.findByFcmToken(registrationRequestDto.getFcmToken());
            if (tokenRecord == null) {
                tokenRecord = tokenStoreService.findByUsername(registrationRequestDto.getUsername());
                if (tokenRecord != null) {
                    tokenRecord.setFcmToken(registrationRequestDto.getFcmToken());
                    tokenRecord.setCreatedAt(new Date());
                    log.info("Update token: {} for userId: {} success", registrationRequestDto.getFcmToken(),
                            registrationRequestDto.getUsername());
                    return ResponseEntity.ok(tokenStoreService.save(tokenRecord));
                } else {
                    tokenRecord = new FcmTokenRecord(null, registrationRequestDto.getUsername(),
                            registrationRequestDto.getFcmToken(), new Date());
                    log.info("Register token: {} for userId: {} success", registrationRequestDto.getFcmToken(),
                            registrationRequestDto.getUsername());
                    return ResponseEntity.ok(tokenStoreService.save(tokenRecord));
                }
            } else {
                if (tokenRecord.getUsername().equals(registrationRequestDto.getUsername())) {
                    log.info("Token: {} not changed for userId: {}, token is the same as before",
                            registrationRequestDto.getFcmToken(),
                            registrationRequestDto.getUsername());
                } else {
                    log.info("Register token: {} for userId: {} failed, token already registered by userId: {}",
                            registrationRequestDto.getFcmToken(), registrationRequestDto.getUsername(),
                            tokenRecord.getUsername());
                }
                return ResponseEntity.ok(tokenRecord);
            }
        } catch (Exception e) {
            log.error("Store token: {} for userId: {} error", registrationRequestDto.getFcmToken(),
                    registrationRequestDto.getUsername(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Object> subscribeToTopic(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService.findByUsername(subscriptionRequestDto.getUsername());
            TopicNameRecord topicNameRecord = null;
            if (tokenRecord != null) {
                SubscriptionRequest subscriptionRequest = new SubscriptionRequest(subscriptionRequestDto.getTopicName(),
                        tokenRecord.getFcmToken());
                notificationService.subscribeToTopic(subscriptionRequest);
                if (topicNameService.findByTopicName(subscriptionRequestDto.getTopicName()) == null) {
                    topicNameRecord = topicNameService.save(new TopicNameRecord(null, subscriptionRequestDto.getTopicName()));
                    log.info("Create topic name: {}", subscriptionRequestDto.getTopicName());
                }
                log.info("Subscribe to topic: {} for userId: {} success", subscriptionRequestDto.getTopicName(),
                        subscriptionRequestDto.getUsername());
            } else {
                log.info("Subscribe to topic: {} for userId: {} failed, token not found",
                        subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUsername());
            }
            return ResponseEntity.ok(topicNameRecord);
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
                    SubscriptionRequest subscriptionRequest = new SubscriptionRequest(tokenRecord.getFcmToken(),
                            subscriptionRequestDto.getTopicName());
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
}
