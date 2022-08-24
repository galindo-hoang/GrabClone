package com.example.firebase.service;

import com.example.firebase.dto.NotificationRequest;
import com.example.firebase.dto.SubscriptionRequest;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class NotificationService {

    @Value("${app.firebase-config}")
    private String firebaseConfig;

    private FirebaseApp firebaseApp;

    @PostConstruct
    private void initialize() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfig).getInputStream())).build();

            if (FirebaseApp.getApps().isEmpty()) {
                this.firebaseApp = FirebaseApp.initializeApp(options);
            } else {
                this.firebaseApp = FirebaseApp.getInstance();
            }
        } catch (IOException e) {
            log.error("Create FirebaseApp Error", e);
        }
    }

    public void subscribeToTopic(SubscriptionRequest subscriptionRequest) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).subscribeToTopic(List.of(subscriptionRequest.getFcmToken()),
                    subscriptionRequest.getTopicName());
        } catch (FirebaseMessagingException e) {
            log.error("Firebase subscribe to topic fail", e);
        }
    }

    public void unsubscribeFromTopic(SubscriptionRequest subscriptionRequest) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).unsubscribeFromTopic(List.of(subscriptionRequest.getFcmToken()),
                    subscriptionRequest.getTopicName());
        } catch (FirebaseMessagingException e) {
            log.error("Firebase unsubscribe from topic fail", e);
        }
    }

    public void sendPnsToDevice(NotificationRequest notificationRequest) {
        Message message = Message.builder()
                .setToken(notificationRequest.getTarget())
                .setNotification(new Notification(notificationRequest.getTitle(), notificationRequest.getBody()))
                .putData("content", notificationRequest.getTitle())
                .putData("body", notificationRequest.getBody())
                .putAllData(notificationRequest.getData())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e);
        }
    }

    public void sendPnsToTopic(NotificationRequest notificationRequest) {
        Message message = Message.builder()
                .setTopic(notificationRequest.getTarget())
                .setNotification(new Notification(notificationRequest.getTitle(), notificationRequest.getBody()))
                .putData("content", notificationRequest.getTitle())
                .putData("body", notificationRequest.getBody())
                .putAllData(notificationRequest.getData())
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e);
        }
    }
}