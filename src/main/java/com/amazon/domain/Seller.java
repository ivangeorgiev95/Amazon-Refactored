package com.amazon.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String legalName;
    private String businessDisplayName;
    @OneToMany
    @JoinColumn(name = "seller_id")
    private List<BankAccount> bankAccounts;
}
