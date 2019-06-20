package com.amazon.service;

import com.amazon.domain.*;
import com.amazon.exceptions.InvalidProductException;
import com.amazon.repository.CartProductRepository;
import com.amazon.repository.ProductRepository;
import com.amazon.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShoppingCartService {

    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    public void addProduct(User loggedUser, Integer quantity, Long productId) throws InvalidProductException {
        Product product = getProduct(productId);
        ShoppingCart userShoppingCart = loggedUser.getShoppingCart();
        CartProductId cartProductId = new CartProductId(userShoppingCart.getId(), productId);
        linkProductToShoppingCart(quantity, product, userShoppingCart, cartProductId);
    }

    private void linkProductToShoppingCart(Integer quantity, Product product, ShoppingCart userShoppingCart, CartProductId cartProductId) {
        Optional<CartProduct> existingCartProduct = cartProductRepository.findById(cartProductId);
        if (existingCartProduct.isEmpty()){
            CartProduct cartProduct = new CartProduct(cartProductId,userShoppingCart, product, quantity);
            cartProductRepository.save(cartProduct);
        }else{
            CartProduct cartProduct = existingCartProduct.get();
            Integer newQuantity = quantity + cartProduct.getQuantity();
            cartProduct.setQuantity(newQuantity);
            cartProductRepository.save(cartProduct);
        }
    }

    private Product getProduct(Long productId) throws InvalidProductException {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()){
            throw new InvalidProductException("Product not found!");
        }
        return product.get();
    }


}
