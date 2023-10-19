package com.example.test.rabbit;


import com.example.test.dto.AccountWithOperationDTO;
import com.example.test.mail.PdfGenerateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableRabbit
public class MessageNotificationReceiver {

    private final PdfGenerateService pdfGenerateService;

    @Autowired
    public MessageNotificationReceiver(PdfGenerateService pdfGenerateService) {
        this.pdfGenerateService = pdfGenerateService;
    }

    @RabbitListener(queues = "${rabbit.notification.queue.name}")
    public void receiveMessage(AccountWithOperationDTO accountWithOperationDTO) {
        log.info(accountWithOperationDTO.getBalanceOperations().toString(), "{}");
        pdfGenerateService.generate();
    }
}
