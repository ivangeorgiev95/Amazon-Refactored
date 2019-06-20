package com.amazon.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_has_products")
public class CartProduct {

//    @Id
//    @ManyToOne
//    @JoinColumn
//    @JsonIgnore
//    private ShoppingCart shoppingCart;
//    @Id
//    @ManyToOne
//    @JoinColumn
//    private Product product;

    @EmbeddedId
    @JsonIgnore
    private CartProductId id;
    @ManyToOne
    @MapsId("shoppingCartId")
    @JsonIgnore
    private ShoppingCart shoppingCart;
    @ManyToOne
    @MapsId("productId")
    private Product product;
    private Integer quantity;



}
