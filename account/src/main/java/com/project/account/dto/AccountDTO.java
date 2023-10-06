package com.project.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO  {

    private long number;

    private String username;

    private double balance;

    private ProfileDTO profile;
}
