package com.project.account.service;

import com.project.account.dto.AccountWithOperationDTO;

public interface MessageProducer {
    void produce(AccountWithOperationDTO message);
}
