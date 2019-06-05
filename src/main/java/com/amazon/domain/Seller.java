package com.amazon.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@NoArgsConstructor
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Seller's legal name can't be blank!")
    private String legalName;
    @Column(unique = true)
    @NotBlank(message = "Seller's business dislpay name can't be blank!")
    private String businessDisplayName;
    @OneToOne
    private BankAccount bankAccount;

    public Seller(String legalName, String businessDisplayName, BankAccount bankAccount) {
        this.legalName = legalName;
        this.businessDisplayName = businessDisplayName;
        this.bankAccount = bankAccount;
    }
}
