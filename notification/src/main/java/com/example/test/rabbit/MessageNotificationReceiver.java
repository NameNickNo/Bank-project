package com.example.test.rabbit;


import com.example.test.dto.AccountWithOperationDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableRabbit
public class MessageNotificationReceiver {

    @SneakyThrows
    @RabbitListener(queues = "${rabbit.notification.queue.name}")
    public void receiveMessage(AccountWithOperationDTO accountWithOperationDTO) {
        log.info("Account number: {} ", accountWithOperationDTO.getNumber());
        log.info(accountWithOperationDTO.getBalanceOperations().toString(), "{}");
    }
}
