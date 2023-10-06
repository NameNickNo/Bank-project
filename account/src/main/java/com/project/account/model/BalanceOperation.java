package com.project.account.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "balance_operation")
@NoArgsConstructor
public class BalanceOperation extends OperationEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "balance_operation_seq")
    @SequenceGenerator(name = "balance_operation_seq", sequenceName = "balance_operation_seq", allocationSize = 10)
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_number", referencedColumnName = "number")
    private Account account;

    @Column(name = "amount")
    private double amount;

    public BalanceOperation(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }
}
