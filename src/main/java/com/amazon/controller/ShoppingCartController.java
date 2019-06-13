package com.amazon.controller;


import com.amazon.domain.User;
import com.amazon.dto.ResponseDTO;
import com.amazon.exceptions.InvalidProductException;
import com.amazon.exceptions.NotLoggedInException;
import com.amazon.service.ShoppingCartService;
import com.amazon.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShoppingCartController {
	
	private static final String USER_SESSION_ATTRIBUTE = "user";

	private final ShoppingCartService shoppingCartService;

	@GetMapping("/add-to-basket")
	public ResponseEntity<ResponseDTO> addProductToBasket(@RequestParam(name="productId", required=true) Long productId, @RequestParam(name="quantity", required=false, defaultValue = "1") Integer quantity, HttpServletRequest request) throws NotLoggedInException, InvalidProductException {
		UserValidator.validateLogIn(request);
		User loggedUser = (User) request.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
		this.shoppingCartService.addProduct(loggedUser, quantity, productId);
		return new ResponseEntity<ResponseDTO>(new ResponseDTO(HttpStatus.OK.value(), "Product added to basket successfully!"), HttpStatus.OK);
	}
	
//	@DeleteMapping("/remove-from-basket")
//	public ResponseEntity<ResponseDTO> removeProductFromBasket(@RequestParam(name="productId", required=true) Integer productId, HttpServletRequest request) throws NoSuchProductException, NotLoggedInException, BasketException {
//		Validation.validateLogIn(request);
//		User user = (User) request.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
//		this.basketDAO.removeProductFromUserBasket(productId, user.getBasket());
//		return new ResponseEntity<ResponseDTO>(new ResponseDTO(HttpStatus.OK.value(), "Product removed from basket successfully!"), HttpStatus.OK);
//	}
	

}
