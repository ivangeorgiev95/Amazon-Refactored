package com.amazon.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "bank_accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String iban;
    private Double balance;

}
