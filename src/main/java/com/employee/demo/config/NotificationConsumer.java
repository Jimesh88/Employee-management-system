package com.employee.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(NotificationConsumer.class);

    @RabbitListener(queues = RabbitConfig.NOTIFICATION_QUEUE)
    public void receiveMessage(String message) {
        try {
            log.info("Received notification message: {}", message);

            // Simulate email sending
            log.info("Simulating email sending for message: {}", message);

        } catch (Exception e) {
            log.error("Error while processing notification message", e);
        }
    }
}
