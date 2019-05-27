package com.amazon.domain;


import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String password;
    private boolean isAdmin;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Address> addresses;
    @OneToOne
    private ShoppingCart shoppingCart;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Seller> sellers;
    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<CreditCard> creditCards;
    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<GiftCard> giftCards;
    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<Order> orders;



}
