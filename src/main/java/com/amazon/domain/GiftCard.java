package com.amazon.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "gift_cards")
public class GiftCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double balance;



}
