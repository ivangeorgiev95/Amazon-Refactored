package com.amazon.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_has_products")
public class CartProduct implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private ShoppingCart shoppingCart;
    @Id
    @ManyToOne
    @JoinColumn
    private Product product;
    private Integer quantity;



}
