package com.amazon.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private boolean isAdmin;
    @OneToMany
    @JoinColumn(name = "user_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Address> addresses;
    @OneToOne
    private ShoppingCart shoppingCart;
    @OneToMany
    @JoinColumn(name = "user_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Seller> sellers;
    @OneToMany
    @JoinColumn(name = "user_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<CreditCard> creditCards;
    @OneToMany
    @JoinColumn(name = "user_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<GiftCard> giftCards;
    @OneToMany
    @JoinColumn(name = "user_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Order> orders;

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
