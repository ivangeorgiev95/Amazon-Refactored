package com.amazon.controller;

import com.amazon.domain.*;
import com.amazon.dto.*;
import com.amazon.exceptions.*;
import com.amazon.service.UserService;
import com.amazon.util.UserValidator;
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
        UserValidator.validateUserPassword(registrationDTO.getPassword(), registrationDTO.getReEnteredPassword());
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

    @GetMapping("/orders")
    public Set<Order> getUserOrders(HttpServletRequest request) throws NotLoggedInException {
        UserValidator.validateLogIn(request);
        User loggedUser = (User)request.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
        return loggedUser.getOrders();
    }

    @GetMapping("/addresses")
    public List<Address> getUserAddresses(HttpServletRequest request) throws NotLoggedInException {
        UserValidator.validateLogIn(request);
        User loggedUser = (User)request.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
        return loggedUser.getAddresses();
    }

    @GetMapping("/creditCards")
    public Set<CreditCard> getUserCreditCards(HttpServletRequest request) throws NotLoggedInException {
        UserValidator.validateLogIn(request);
        User loggedUser = (User)request.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
        return loggedUser.getCreditCards();
    }

    @GetMapping("/giftCards")
    public Set<GiftCard> getUserGiftCards(HttpServletRequest request) throws NotLoggedInException {
        UserValidator.validateLogIn(request);
        User loggedUser = (User)request.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
        return loggedUser.getGiftCards();
    }

    @GetMapping("/shoppingCart")
    public ShoppingCart getUserShoppingCart(HttpServletRequest request) throws NotLoggedInException {
        UserValidator.validateLogIn(request);
        User loggedUser = (User)request.getSession().getAttribute(USER_SESSION_ATTRIBUTE);
        return userService.findUserShoppingCart(loggedUser);
    }

    @GetMapping("/signout")
    public ResponseEntity<ResponseDTO> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return new ResponseEntity<ResponseDTO>(new ResponseDTO(HttpStatus.OK.value(), "Sign out successful!"), HttpStatus.OK);
    }

    @PostMapping("/makeOrder")
    public Order makeOrder(@RequestBody @Valid OrderPaymentDTO orderPaymentForm, HttpServletRequest request) throws EmptyBasketException, NotLoggedInException, UserException, NotEnoughQuantityException {
        UserValidator.validateLogIn(request);
        return userService.makeOrder((User) request.getSession().getAttribute(USER_SESSION_ATTRIBUTE), orderPaymentForm);
    }

    @PostMapping("/add-new-address")
    public ResponseEntity<CreationResponseDTO> addNewAddress(@RequestBody @Valid Address newAddress, HttpServletRequest request) throws NotLoggedInException{
        UserValidator.validateLogIn(request);
        Long newAddressId = this.userService.addNewAddress(newAddress, (User)request.getSession().getAttribute(USER_SESSION_ATTRIBUTE));
        return new ResponseEntity<CreationResponseDTO>(new CreationResponseDTO(HttpStatus.CREATED.value(), "Address created successfully!", newAddressId), HttpStatus.CREATED);
    }

    @PostMapping("/registerSeller")
    public ResponseEntity<CreationResponseDTO> registerNewSeller(@RequestBody @Valid Seller seller, HttpServletRequest request) throws NotLoggedInException, SellerException {
        UserValidator.validateLogIn(request);
        Long newSellerId = userService.registerNewSeller((User) request.getSession().getAttribute(USER_SESSION_ATTRIBUTE), seller);
        return new ResponseEntity<CreationResponseDTO>(new CreationResponseDTO(HttpStatus.CREATED.value(), "Seller created successfully!", newSellerId), HttpStatus.CREATED);
    }

    @PostMapping("/addcreditCard")
    public ResponseEntity<CreationResponseDTO> addNewCreditcard(@RequestBody @Valid CreditCard creditCard, HttpServletRequest request) throws NotLoggedInException, UserException {
        UserValidator.validateLogIn(request);
        Long newCreditCardId = userService.addNewCreditCard((User) request.getSession().getAttribute(USER_SESSION_ATTRIBUTE), creditCard);
        return new ResponseEntity<CreationResponseDTO>(new CreationResponseDTO(HttpStatus.CREATED.value(), "Credit card created successfully!", newCreditCardId), HttpStatus.CREATED);
    }

}
