package com.example.firebase.rabbitmq;

import com.example.clients.feign.DriverLocation.DriverLocationDto;
import com.example.firebase.dto.NotificationRequest;
import com.example.firebase.dto.SubscriptionRequest;
import com.example.firebase.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queues.queue-subscribe-topic}")
    void subscribeToTopic(SubscriptionRequest subscriptionRequest) {
        try {
            log.info("Consumed {} from queue", subscriptionRequest);
            notificationService.subscribeToTopic(subscriptionRequest);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    @RabbitListener(queues = "${rabbitmq.queues.queue-unsubscribe-topic}")
    void unsubscribeToTopic(SubscriptionRequest subscriptionRequest) {
        try {
            log.info("Consumed {} from queue", subscriptionRequest);
            notificationService.unsubscribeFromTopic(subscriptionRequest);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    @RabbitListener(queues = "${rabbitmq.queues.queue-send-notification-device}")
    void sendPnsToDevice(NotificationRequest notificationRequest) {
        try {
            log.info("Consumed {} from queue", notificationRequest);
            notificationService.sendPnsToDevice(notificationRequest);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    @RabbitListener(queues = "${rabbitmq.queues.queue-send-notification-topic}")
    void sendPnsToTopic(NotificationRequest notificationRequest) {
        try {
            log.info("Consumed {} from queue", notificationRequest);
            notificationService.sendPnsToTopic(notificationRequest);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}
