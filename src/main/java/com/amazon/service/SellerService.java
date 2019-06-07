package com.amazon.service;

import com.amazon.domain.Category;
import com.amazon.domain.Product;
import com.amazon.domain.Seller;
import com.amazon.domain.User;
import com.amazon.dto.AddProductDTO;
import com.amazon.exceptions.SellerException;
import com.amazon.repository.CategoryRepository;
import com.amazon.repository.ProductRepository;
import com.amazon.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SellerService {

    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    public Long addNewProduct(AddProductDTO productForm, User loggedUser) throws SellerException {
        Seller seller = findSeller(loggedUser, productForm.getSellerId());
        Category productCategory = findCategory(productForm.getCategoryId());
        Product newProduct = new Product(productForm.getName(), productForm.getDescription(), productForm.getPrice(), productForm.getQuantity());
        newProduct.setSeller(seller);
        newProduct.setCategory(productCategory);
        Product addedProduct = productRepository.save(newProduct);
        seller.getProducts().add(addedProduct);
        return addedProduct.getId();
    }

    private Category findCategory(Long categoryId) throws SellerException {
        Category category = categoryRepository.findById(categoryId);
        if (category == null){
            throw new SellerException("Product category does not exist!");
        }
        return category;
    }

    private Seller findSeller(User user, Long sellerId) throws SellerException {
        Optional<Seller> seller =sellerRepository.findById(sellerId);
        if(seller.isEmpty()){
            throw new SellerException("Seller does not exist!");
        }
        if (user.getSellers().stream().noneMatch(seller1 -> seller1.equals(seller.get()))){
            throw new SellerException("Seller does not match the user!");
        }
        return seller.get();
    }

}
