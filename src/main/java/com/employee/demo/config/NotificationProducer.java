package com.employee.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(String message) {
        try {
            log.info("Publishing message to RabbitMQ: {}", message);
            rabbitTemplate.convertAndSend(RabbitConfig.NOTIFICATION_QUEUE, message);
        } catch (Exception e) {
            log.error("Failed to publish message to RabbitMQ", e);
        }    }
}
