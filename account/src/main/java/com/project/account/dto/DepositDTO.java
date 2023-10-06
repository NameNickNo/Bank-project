package com.project.account.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DepositDTO extends BalanceOperationDTO {

    public DepositDTO(long accountNumber, double amount, int pin) {
        super.setAccountNumber(accountNumber);
        super.setAmount(amount);
        super.setPin(pin);
    }
}
