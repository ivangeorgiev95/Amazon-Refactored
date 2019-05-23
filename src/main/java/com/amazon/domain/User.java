package com.amazon.domain;


import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private boolean isAdmin;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Address> addresses;
//    private Basket basket;
//    private Set<CreditCard> creditCards;
//    private Set<GiftCard> giftCards;
//    private Set<Order> orders;



}
