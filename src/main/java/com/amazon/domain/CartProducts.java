package com.amazon.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "cart_has_products")
public class CartProducts implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn
    private ShoppingCart shoppingCart;
    @Id
    @ManyToOne
    @JoinColumn
    private Product product;
    private Integer quantity;



}
