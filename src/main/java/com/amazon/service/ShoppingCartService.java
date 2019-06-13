package com.amazon.service;

import com.amazon.domain.CartProduct;
import com.amazon.domain.Product;
import com.amazon.domain.ShoppingCart;
import com.amazon.domain.User;
import com.amazon.exceptions.InvalidProductException;
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
    private final ShoppingCartRepository shoppingCartRepository;

    public void addProduct(User loggedUser, Integer quantity, Long productId) throws InvalidProductException {
        Product product = getProduct(productId);
        ShoppingCart userShoppingCart = loggedUser.getShoppingCart();
        CartProduct cartProduct = new CartProduct(userShoppingCart, product, quantity);

        userShoppingCart.getProducts().add(cartProduct);

        userShoppingCart.getProducts().forEach(cartProduct1 -> {
            System.out.println("cart id: "+cartProduct1.getShoppingCart().getId());
            System.out.println("product id: "+cartProduct1.getProduct().getId());
            System.out.println("quantity: "+cartProduct1.getQuantity());
        });
        System.out.println("------------------------------      cart id: "+cartProduct.getShoppingCart().getId());
        System.out.println("------------------------------      product id: "+cartProduct.getProduct().getId());
        System.out.println("------------------------------      quantity: "+cartProduct.getQuantity());
        shoppingCartRepository.save(userShoppingCart);
    }

    private Product getProduct(Long productId) throws InvalidProductException {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()){
            throw new InvalidProductException("Product not found!");
        }
        return product.get();
    }


}
