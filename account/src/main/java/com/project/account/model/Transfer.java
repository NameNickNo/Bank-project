package com.project.account.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "transfer")
@NoArgsConstructor
public class Transfer extends OperationEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "transfer_seq")
    @SequenceGenerator(name = "transfer_seq", sequenceName = "transfer_seq", allocationSize = 10)
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_number_from", referencedColumnName = "number")
    private Account accountNumberFrom;

    @ManyToOne
    @JoinColumn(name = "account_number_to", referencedColumnName = "number")
    private Account accountNumberTo;

    @Column(name = "amount")
    private double amount;

    public Transfer(Account accountNumberFrom, Account accountNumberTo, double amount) {
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.amount = amount;
    }
}
