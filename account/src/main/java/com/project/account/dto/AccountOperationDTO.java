package com.project.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperationDTO implements Serializable {
    private long id;
    private double amount;
    private LocalDateTime created;
}
