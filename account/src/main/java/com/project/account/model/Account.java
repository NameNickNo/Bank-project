package com.project.account.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "account")
@NamedEntityGraph(name = "Account.balanceOperations", attributeNodes = @NamedAttributeNode("balanceOperations"))
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_num_seq")
    @SequenceGenerator(name = "account_num_seq", sequenceName = "account_num_seq",initialValue = 20, allocationSize = 10)
    @Column(name = "number")
    private long number;

    @Column(name = "username")
    private String username;

    @Column(name = "pin")
    private int pin;

    @Column(name = "balance")
    private double balance;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<BalanceOperation> balanceOperations;

    @Embedded
    private Profile profile;

    public Account(long number, String username, int pin, double balance) {
        this.number = number;
        this.username = username;
        this.pin = pin;
        this.balance = balance;
    }
}
