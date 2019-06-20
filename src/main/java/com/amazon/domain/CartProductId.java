package com.amazon.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CartProductId implements Serializable {

    @Column(name = "shopping_cart_id")
    private Long shoppingCartId;
    @Column(name = "product_id")
    private Long productId;


}
