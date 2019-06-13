package com.amazon.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderDate;
    @OneToOne
    private ShoppingCart shoppingCart;

    public Order() {
        this.orderDate = LocalDateTime.now();
    }

    public Order(ShoppingCart shoppingCart) {
        this();
        this.shoppingCart = shoppingCart;
    }
}
