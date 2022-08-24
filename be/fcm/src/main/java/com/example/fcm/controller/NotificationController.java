package com.example.fcm.controller;

import com.example.clients.feign.DriverLocation.DriverLocationClients;
import com.example.clients.feign.DriverLocation.DriverLocationDto;
import com.example.clients.feign.NotificationRequest.NotificationRequestDto;
import com.example.rabbitmq.RabbitMQMessageProducer;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.fcm.model.domain.*;
import com.example.fcm.model.dto.*;
import com.example.fcm.model.entity.*;
import com.example.fcm.service.*;

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

    @Autowired
    private DriverLocationClients driverLocationClients;

    @Autowired
    private UserTopicService userTopicService;
    @Autowired
    private RabbitMQMessageProducer rabbitMQMessageProducer;

    @PostMapping("/subscribe")
    public ResponseEntity<Object> subscribeToTopic(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        try {
            FcmTokenRecord tokenRecord = tokenStoreService.findByUsername(subscriptionRequestDto.getUsername());
            TopicNameRecord topicNameRecord = null;
            if (tokenRecord != null) {
                SubscriptionRequest subscriptionRequest = new SubscriptionRequest(subscriptionRequestDto.getTopicName()
                        , tokenRecord.getFcmToken());
                //notificationService.subscribeToTopic(subscriptionRequest);
                rabbitMQMessageProducer.publish(subscriptionRequest,
                        "internal.exchange",
                        "internal.subscribe-topic.routing-key");
                if (topicNameService.findByTopicName(subscriptionRequestDto.getTopicName()) == null) {
                    TopicNameRecord topicNameRc = new TopicNameRecord();
                    topicNameRc.setTopicName(subscriptionRequestDto.getTopicName());
                    topicNameRecord = topicNameService.save(topicNameRc);
                    log.info("Create topic name: {}", subscriptionRequestDto.getTopicName());
                } else {
                    topicNameRecord = topicNameService.findByTopicName(subscriptionRequestDto.getTopicName());
                    log.info("Add user: {} to topic name: {}", subscriptionRequestDto.getUsername(), subscriptionRequestDto.getTopicName());
                }
                UserTopicRecord userTopicRecord = userTopicService.findByUsernameAndTopicNameRecord(subscriptionRequestDto.getUsername(), topicNameRecord);
                if (userTopicRecord == null) {
                    UserTopicRecord userTopicRecordNew = new UserTopicRecord();
                    userTopicRecordNew.setUsername(subscriptionRequestDto.getUsername());
                    userTopicRecordNew.setTopicState(TopicState.SUBSCRIBE);
                    userTopicRecordNew.setTopicNameRecord(topicNameRecord);
                    userTopicRecordNew.setCreatedAt(new Date());
                    userTopicService.save(userTopicRecordNew);
                    log.info("Subscribe to topic: {} for userId: {} success", subscriptionRequestDto.getTopicName(),
                            subscriptionRequestDto.getUsername());
                } else {
                    userTopicRecord.setTopicState(TopicState.SUBSCRIBE);
                    userTopicRecord.setCreatedAt(new Date());
                    userTopicService.save(userTopicRecord);
                    log.info("User topic record: {} already exists", userTopicRecord);
                }

            } else {
                log.info("Subscribe to topic: {} for userId: {} failed, token not found",
                        subscriptionRequestDto.getTopicName(), subscriptionRequestDto.getUsername());
            }


            //Create driver record for location
            driverLocationClients.updateLocation(DriverLocationDto.builder()
                    .username(subscriptionRequestDto.getUsername())
                    .build());


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
                    SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
                            subscriptionRequestDto.getTopicName(), tokenRecord.getFcmToken());
                    rabbitMQMessageProducer.publish(subscriptionRequest,
                            "internal.exchange",
                            "internal.unsubscribe-topic.routing-key");
                    //notificationService.unsubscribeFromTopic(subscriptionRequest);
                    TopicNameRecord topicNameRc = topicNameService.findByTopicName(subscriptionRequestDto.getTopicName());
                    UserTopicRecord userTopicRecord = userTopicService.findByUsernameAndTopicNameRecord(subscriptionRequestDto.getUsername(), topicNameRc);
                    userTopicRecord.setTopicState(TopicState.UNSUBSCRIBE);
                    userTopicRecord.setCreatedAt(new Date());
                    userTopicService.save(userTopicRecord);
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

            return ResponseEntity.ok(topicNameRecord);
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
                rabbitMQMessageProducer.publish(notificationRequest,"internal.exchange",
                        "internal.send-notification-device.routing-key");
                //String response = notificationService.sendPnsToDevice(notificationRequest);
                log.info("Send PNS to device: {} for userId: {}, title: {}, body: {}, data: {} success",
                        tokenRecord.getFcmToken(), notificationRequestDto.getTarget(),
                        notificationRequestDto.getTitle(), notificationRequestDto.getBody(),
                        notificationRequestDto.getData());
                return ResponseEntity.ok().build();
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
                rabbitMQMessageProducer.publish(notificationRequest,"internal.exchange",
                        "internal.send-notification-topic.routing-key");
                return ResponseEntity.ok().build();
            } else {
                log.info("No topic found for topicName: {}", notificationRequestDto.getTarget());
            }
        } catch (Exception e) {
            log.error("Send PNS to topic: {} error", notificationRequestDto.getTarget(), e);
        }
        return ResponseEntity.badRequest().body("Error");
    }
}
