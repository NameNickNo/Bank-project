package com.project.account.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class BalanceOperationDTO {

    @Min(value = 1, message = "must be more 0")
    @Max(value = Long.MAX_VALUE, message = "not supported value")
    private long accountNumber;

    @Positive(message = "must be more 0")
    @Max(value = Long.MAX_VALUE, message = "not supported value")
    private double amount;

    @Min(value = 1000, message = "must be 4 symbols!")
    @Max(value = 9999, message = "must be 4 symbols!")
    private int pin;
}
