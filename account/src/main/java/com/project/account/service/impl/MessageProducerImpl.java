package com.project.account.service.impl;

import com.project.account.dto.AccountWithOperationDTO;
import com.project.account.service.MessageProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class MessageProducerImpl implements MessageProducer {

    @Value("${rabbit.notification.queue.name}")
    private String notificationQueueName;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void produce(AccountWithOperationDTO message) {
        rabbitTemplate.convertAndSend(notificationQueueName, message);
    }
}
