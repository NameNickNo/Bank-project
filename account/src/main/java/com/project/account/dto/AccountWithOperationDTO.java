package com.project.account.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class AccountWithOperationDTO implements Serializable {
    private long number;
    private List<AccountOperationDTO> balanceOperations;
}
