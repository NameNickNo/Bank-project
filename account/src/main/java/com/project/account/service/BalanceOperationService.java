package com.project.account.service;

import com.project.account.dto.DepositDTO;
import com.project.account.dto.WithdrawDTO;

public interface BalanceOperationService {
    void createDepositOperation(DepositDTO depositDTO);

    void createWithdrawOperation(WithdrawDTO withdrawDTO);
}
