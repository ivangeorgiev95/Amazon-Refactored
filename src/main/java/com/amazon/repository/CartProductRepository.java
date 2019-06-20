package com.amazon.repository;

import com.amazon.domain.CartProduct;
import com.amazon.domain.CartProductId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, CartProductId> {
}
