package com.amazon.controller;

import com.amazon.domain.*;
import com.amazon.dto.CreationResponseDTO;
import com.amazon.dto.LoginDTO;
import com.amazon.dto.ResponseDTO;
import com.amazon.dto.UserRegistrationDTO;
import com.amazon.exceptions.*;
import com.amazon.service.UserService;
import com.amazon.util.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private static final String USER_SESSION_ATTRIBUTE = "user";
    private static final int SESSION_DURATION = 300;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<CreationResponseDTO> register(@RequestBody @Valid UserRegistrationDTO registrationDTO) throws UserException {
        Validation.validateUserPassword(registrationDTO.getPassword(), registrationDTO.getReEnteredPassword());
        Long newUserId = userService.registerUser(registrationDTO);
        return new ResponseEntity<CreationResponseDTO>(new CreationResponseDTO(HttpStatus.CREATED.value(), "User registered successfully!", newUserId), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid LoginDTO loginForm , HttpServletRequest request) throws  UserException, InvalidEmailException, InvalidPasswordException{
        User loggedUser = userService.logUser(loginForm);
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(SESSION_DURATION);
        session.setAttribute(USER_SESSION_ATTRIBUTE, loggedUser);
        return new ResponseEntity<ResponseDTO>(new ResponseDTO(HttpStatus.OK.value(), "Log in successful!"), HttpStatus.OK);
    }
//    @GetMapping("/profile")
//    public User getUserProfile(HttpServletRequest request) throws NotLoggedInException {
//        Validation.validateLogIn(request);
//        return (User) request.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
//    }
    @GetMapping("/orders")
    public Set<Order> getUserOrders(HttpServletRequest request) throws NotLoggedInException {
        Validation.validateLogIn(request);
        User loggedUser = (User)request.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
        return loggedUser.getOrders();
    }
    @GetMapping("/addresses")
    public List<Address> getUserAddresses(HttpServletRequest request) throws NotLoggedInException {
        Validation.validateLogIn(request);
        User loggedUser = (User)request.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
        return loggedUser.getAddresses();
    }
    @GetMapping("/creditCards")
    public Set<CreditCard> getUserCreditCards(HttpServletRequest request) throws NotLoggedInException {
        Validation.validateLogIn(request);
        return ((User)request.getSession().getAttribute(USER_SESSION_ATTRIBUTE)).getCreditCards();
    }
    @GetMapping("/giftCards")
    public Set<GiftCard> getUserGiftCards(HttpServletRequest request) throws NotLoggedInException {
        Validation.validateLogIn(request);
        return ((User)request.getSession().getAttribute(USER_SESSION_ATTRIBUTE)).getGiftCards();
    }
//    @GetMapping("/basket")
//    public Basket getUserBasket(HttpServletRequest request) throws NotLoggedInException {
//        Validation.validateLogIn(request);
//        return ((User)request.getSession().getAttribute(USER_SESSION_ATTRIBUTE)).getBasket();
//    }
//    @GetMapping("/signout")
//    public ResponseEntity<ResponseDTO> logout(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        session.invalidate();
//        return new ResponseEntity<ResponseDTO>(new ResponseDTO(HttpStatus.OK.value(), "Sign out successful!"), HttpStatus.OK);
//    }
//
//    @PostMapping("/makeOrder")
//    public Order makeOrder(@RequestBody @Valid CreditCardOrderDTO creditCard ,HttpServletRequest request) throws EmptyBasketException, NotLoggedInException, CreditCardException, NotEnoughMoneyInCreditCardException, ProductException, NoSuchProductException, NotEnoughQuantityException, OrderException, UserException{
//        Validation.validateLogIn(request);
//        Validation.validateBasketNotEmpty(request);
//        return userService.makeOrder((User) request.getSession().getAttribute(USER_SESSION_ATTRIBUTE), creditCard);
//    }
//
    @PostMapping("/add-new-address")
    public ResponseEntity<CreationResponseDTO> addNewAddress(@RequestBody @Valid Address newAddress, HttpServletRequest request) throws NotLoggedInException{
        Validation.validateLogIn(request);
        Long newAddressId = this.userService.addNewAddress(newAddress, (User)request.getSession().getAttribute(USER_SESSION_ATTRIBUTE));
        return new ResponseEntity<CreationResponseDTO>(new CreationResponseDTO(HttpStatus.CREATED.value(), "Address created successfully!", newAddressId), HttpStatus.CREATED);
    }
//
//    @PostMapping("/registerSeller")
//    public ResponseEntity<CreateResponseDTO> registerNewSeller(@RequestBody @Valid SellerRegistrationDTO seller, HttpServletRequest request) throws NotLoggedInException, BankAccountException, SellerException {
//        Validation.validateLogIn(request);
//        Integer newSellerId = userService.registerNewSeller((User) request.getSession().getAttribute(USER_SESSION_ATTRIBUTE), seller, seller.getAccount());
//        return new ResponseEntity<CreateResponseDTO>(new CreateResponseDTO(HttpStatus.CREATED.value(), "Seller created successfully!", newSellerId), HttpStatus.CREATED);
//    }
//
//    @PostMapping("/addcreditCard")
//    public ResponseEntity<CreateResponseDTO> addNewCreditcard(@RequestBody @Valid AddCreditCardDTO creditCard, HttpServletRequest request) throws NotLoggedInException, CreditCardException, UserException {
//        Validation.validateLogIn(request);
//        Integer newCreditCardId = userService.addNewCreditCard((User) request.getSession().getAttribute(USER_SESSION_ATTRIBUTE), creditCard);
//        return new ResponseEntity<CreateResponseDTO>(new CreateResponseDTO(HttpStatus.CREATED.value(), "Credit card created successfully!", newCreditCardId), HttpStatus.CREATED);
//    }

}
