package com.amazon.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Data
@NoArgsConstructor
@Table(name = "bank_accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotBlank(message = "IBAN can't be blank!")
    private String iban;
    @NotNull
    @PositiveOrZero(message  = "Bank account's balance can't be negative!")
    private Double balance;

    public BankAccount(String iban, Double balance) {
        this.iban = iban;
        this.balance = balance;
    }
}
