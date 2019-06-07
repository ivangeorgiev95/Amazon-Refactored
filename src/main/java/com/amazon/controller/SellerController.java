package com.amazon.controller;


import com.amazon.domain.User;
import com.amazon.dto.AddProductDTO;
import com.amazon.dto.CreationResponseDTO;
import com.amazon.exceptions.NotLoggedInException;
import com.amazon.exceptions.SellerException;
import com.amazon.service.SellerService;
import com.amazon.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SellerController {

	private static final String USER_SESSION_ATTRIBUTE = "user";

	private final SellerService sellerService;


	@PostMapping("/addProductForSell")
	public ResponseEntity<CreationResponseDTO> addProduct(@RequestBody @Valid AddProductDTO productForm, HttpServletRequest request) throws NotLoggedInException, SellerException {
		UserValidator.validateLogIn(request);
		User loggedUser = (User) request.getSession().getAttribute("user");
		Long newProductId = sellerService.addNewProduct(productForm, loggedUser);
		return new ResponseEntity<>(new CreationResponseDTO(HttpStatus.CREATED.value(), "New product successfully created!", newProductId), HttpStatus.CREATED);
	}
//	@DeleteMapping("/deleteProductForSell")
//	public ResponseEntity<ResponseDTO> deleteProductToSell(@RequestBody @Valid DeleteProductDTO product, HttpServletRequest request) throws SellerException, NotLoggedInException, SellerDoesNotExistException, NoSuchProductException {
//		Validation.validateLogIn(request);
//		sellerDao.validateSellerExistance(product.getSellerId());
//		sellerDao.validateSellerMatchUser(product.getSellerId(), (User) request.getSession().getAttribute("user"));
//		sellerDao.deleteProduct(product.getSellerId(), product.getProductId());
//		return new ResponseEntity<ResponseDTO>(new ResponseDTO(HttpStatus.OK.value(), "Product deleted successfully!"), HttpStatus.OK);
//	}
//	@PatchMapping("/updateProductInfo")
//	public ResponseEntity<ResponseDTO> updateProductToSell(@RequestBody @Valid ProductUpdateDTO product, HttpServletRequest request  ) throws NotLoggedInException, SellerDoesNotExistException, SellerException, NoSuchProductException{
//		Validation.validateLogIn(request);
//		sellerDao.validateSellerExistance(product.getSellerId());
//		sellerDao.validateSellerMatchUser(product.getSellerId(), (User) request.getSession().getAttribute("user"));
//		sellerDao.updateProduct(product);
//		return new ResponseEntity<ResponseDTO>(new ResponseDTO(HttpStatus.OK.value(), "Product updated successfully!"), HttpStatus.OK);
//	}
}
