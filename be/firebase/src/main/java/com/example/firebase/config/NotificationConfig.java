package com.example.firebase.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {
    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.queue-subscribe-topic}")
    private String notificationQueueSubscribeTopic;

    @Value("${rabbitmq.queues.queue-unsubscribe-topic}")
    private String notificationQueueUnsubscribeTopic;

    @Value("${rabbitmq.queues.queue-send-notification-device}")
    private String notificationQueueSendDevice;

    @Value("${rabbitmq.queues.queue-send-notification-topic}")
    private String notificationQueueSendTopic;
    @Value("${rabbitmq.routing-keys.internal-subscribe-topic}")
    private String notificationSubscribeTopicRoutingKey;
    @Value("${rabbitmq.routing-keys.internal-unsubscribe-topic}")
    private String notificationUnsubscribeTopicRoutingKey;
    @Value("${rabbitmq.routing-keys.internal-send-notification-topic}")
    private String notificationSendTopicRoutingKey;
    @Value("${rabbitmq.routing-keys.internal-send-notification-device}")
    private String notificationSendDeviceRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue queueSubscribeTopic() {
        return new Queue(this.notificationQueueSubscribeTopic);
    }

    @Bean
    public Queue queueUnsubscribeTopic() {
        return new Queue(this.notificationQueueUnsubscribeTopic);
    }

    @Bean
    public Queue queueSendDevice() {
        return new Queue(this.notificationQueueSendDevice);
    }

    @Bean
    public Queue queueSendTopic() {
        return new Queue(this.notificationQueueSendTopic);
    }

    @Bean
    public Binding internalToNotificationSubscribeTopicBinding() {
        return BindingBuilder
                .bind(queueSubscribeTopic())
                .to(internalTopicExchange())
                .with(this.notificationSubscribeTopicRoutingKey);
    }

    @Bean
    public Binding internalToNotificationUnsubscribeTopicBinding() {
        return BindingBuilder
                .bind(queueUnsubscribeTopic())
                .to(internalTopicExchange())
                .with(this.notificationUnsubscribeTopicRoutingKey);
    }

    @Bean
    public Binding internalToNotificationSendDeviceBinding() {
        return BindingBuilder
                .bind(queueSendDevice())
                .to(internalTopicExchange())
                .with(this.notificationSendDeviceRoutingKey);
    }

    @Bean
    public Binding internalToNotificationSendTopicBinding() {
        return BindingBuilder
                .bind(queueSendTopic())
                .to(internalTopicExchange())
                .with(this.notificationSendTopicRoutingKey);
    }

}
