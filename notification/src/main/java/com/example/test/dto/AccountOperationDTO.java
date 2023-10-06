package com.example.test.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AccountOperationDTO implements Serializable {
    private long id;
    private double amount;
    private LocalDateTime created;
}
