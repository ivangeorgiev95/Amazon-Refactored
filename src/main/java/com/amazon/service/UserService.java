package com.amazon.service;

import com.amazon.domain.*;
import com.amazon.dto.LoginDTO;
import com.amazon.dto.OrderPaymentDTO;
import com.amazon.dto.UserRegistrationDTO;
import com.amazon.exceptions.*;
import com.amazon.repository.*;
import com.amazon.util.SensitiveDataEncryption;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CreditCardRepository creditCardRepository;
    private final SellerRepository sellerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public Long registerUser(UserRegistrationDTO registrationDTO) throws UserException {
        if (isMailBusy(registrationDTO.getEmail())){
            throw new UserException("Email is unavailable!");
        }
        String hashedPassword = SensitiveDataEncryption.hashSensitiveData(registrationDTO.getPassword());
        User newUser = userRepository.save(new User(registrationDTO.getEmail(), registrationDTO.getName(), hashedPassword));
        setNewShoppingCart(newUser);
        return newUser.getId();
    }

    private boolean isMailBusy(String email){
        return userRepository.findByEmail(email) != null;
    }
    
    private void setNewShoppingCart(User user){
        ShoppingCart newShoppingCart = shoppingCartRepository.save(new ShoppingCart());
        user.setShoppingCart(newShoppingCart);
        userRepository.save(user);
    }
    
    public User logUser(LoginDTO loginForm) throws InvalidPasswordException, InvalidEmailException {
        User loggedUser = userRepository.findByEmail(loginForm.getEmail());
        verifyEmail(loggedUser);
        verifyPassword(loginForm.getPassword(), loggedUser.getPassword());
        return loggedUser;
    }

    private void verifyEmail(User loggedUser) throws InvalidEmailException {
        if(loggedUser == null){
            throw new InvalidEmailException("Invalid email!");
        }
    }

    private void verifyPassword(String loginPassword, String storedPassword) throws InvalidPasswordException {
        if (!SensitiveDataEncryption.verifySensitiveData(loginPassword, storedPassword)){
            throw new InvalidPasswordException("Invalid password!");
        }
    }

    public Long addNewAddress(Address newAddress, User user){
        Address addedAddress = addressRepository.save(newAddress);
        user.getAddresses().add(addedAddress);
        userRepository.save(user);
        return addedAddress.getId();
    }


    public Long addNewCreditCard(User user, CreditCard creditCard) throws UserException {
        verifyUserDoesNotHaveCreditCard(creditCard.getCardNumber(), user);
        Optional<CreditCard> existingCreditCard = findCreditCard(creditCard.getCardNumber());
        if (existingCreditCard.isPresent()){
            CreditCard newCreditCard = existingCreditCard.get();
            user.getCreditCards().add(newCreditCard);
            userRepository.save(user);
            return newCreditCard.getId();
        } else {
            String hashedCardNumber = SensitiveDataEncryption.hashSensitiveData(creditCard.getCardNumber());
            creditCard.setCardNumber(hashedCardNumber);
            CreditCard newCreditCard = creditCardRepository.save(creditCard);
            user.getCreditCards().add(newCreditCard);
            userRepository.save(user);
            return newCreditCard.getId();
        }
    }

    private void verifyUserDoesNotHaveCreditCard(String cardNumber, User user) throws UserException {
        if (user.getCreditCards().stream().anyMatch(creditCard -> SensitiveDataEncryption.verifySensitiveData(cardNumber, creditCard.getCardNumber()))){
            throw new UserException("User already has this credit card");
        }
    }

    private Optional<CreditCard> findCreditCard(String cardNumber){
        List<CreditCard> cardList = creditCardRepository.findAll();
        return cardList.stream().filter(creditCard -> SensitiveDataEncryption.verifySensitiveData(cardNumber, creditCard.getCardNumber())).findFirst();
    }

    @Transactional(rollbackOn = Exception.class)
    public Long registerNewSeller(User user, Seller seller) throws SellerException {
        verifyBusinessDisplayNameAvailability(seller.getBusinessDisplayName());
        BankAccount sellerBankAccount = addBankAccount(seller.getBankAccount());
        Seller addedSeller = sellerRepository.save(seller);
        addedSeller.setBankAccount(sellerBankAccount);
        user.getSellers().add(addedSeller);
        userRepository.save(user);
        return addedSeller.getId();
    }

    private void verifyBusinessDisplayNameAvailability(String businessDisplayName) throws SellerException {
        Seller availableSeller = sellerRepository.findByBusinessDisplayName(businessDisplayName);
        if (availableSeller != null){
            throw new SellerException("Business display name already taken");
        }
    }

    private BankAccount addBankAccount(BankAccount bankAccount){
        BankAccount bankAccountToAdd = bankAccountRepository.findByIban(bankAccount.getIban());
        if (bankAccountToAdd == null){
           bankAccountToAdd = bankAccountRepository.save(bankAccount);
        }
        return bankAccountToAdd;
    }

    @Transactional(rollbackOn = Exception.class)
    public Order makeOrder(User user, OrderPaymentDTO orderPaymentForm) throws EmptyBasketException, UserException, NotEnoughQuantityException {
        verifyShoppingCartNotEmpty(user.getShoppingCart());
        CreditCard creditCard = findUserCreditCard(user, orderPaymentForm);
        Double orderPrice = calculateOrderPrice(user.getShoppingCart());
        verifyUserHasMoney(creditCard.getBalance(), orderPrice);
        payProducts(user.getShoppingCart());
        decreaseCreditCardBalance(creditCard, orderPrice);
        return submitOrder(user);
    }

    private void verifyShoppingCartNotEmpty(ShoppingCart shoppingCart) throws EmptyBasketException {
        if(shoppingCart.getProducts().isEmpty()){
            throw new EmptyBasketException("Basket is empty!");
        }
    }

    private CreditCard findUserCreditCard(User user, OrderPaymentDTO orderPaymentForm) throws UserException {
        Optional<CreditCard> creditCard = user.getCreditCards().stream().filter(card -> card.getId().equals(orderPaymentForm.getCreditCardId())).findFirst();
        if (creditCard.isEmpty()){
            throw new UserException("User credit card could not be found!");
        }
        return creditCard.get();
    }

    private Double calculateOrderPrice(ShoppingCart shoppingCart){
        Set<CartProduct> orderProducts = shoppingCart.getProducts();
        return orderProducts.stream().map(cartProduct -> cartProduct.getQuantity()*cartProduct.getProduct().getPrice()).reduce(0.0, Double::sum);
    }

    private void verifyUserHasMoney(Double balance, Double price) throws UserException {
        if (balance < price){
            throw new UserException("Not enough money in credit card!");
        }
    }

    private void payProducts(ShoppingCart shoppingCart) throws NotEnoughQuantityException {
        Set<CartProduct> cartProducts = shoppingCart.getProducts();
        for (CartProduct cartProduct : cartProducts){
            Product product = cartProduct.getProduct();
            Integer availableQuantity = product.getQuantity();
            Integer orderedQuantity = cartProduct.getQuantity();
            if (availableQuantity < orderedQuantity){
                throw new NotEnoughQuantityException("Insufficient quantity of product " + cartProduct.getProduct());
            } else {
                product.setQuantity(availableQuantity-orderedQuantity);
                BankAccount sellerBankAccount = product.getSeller().getBankAccount();
                Double balance = sellerBankAccount.getBalance();
                Double price = product.getPrice()*orderedQuantity;
                balance += price;
                sellerBankAccount.setBalance(balance);
                productRepository.save(product);
                bankAccountRepository.save(sellerBankAccount);
            }
        }
    }

    private void decreaseCreditCardBalance(CreditCard creditCard, Double orderPrice){
        Double balance = creditCard.getBalance();
        balance -= orderPrice;
        creditCard.setBalance(balance);
        creditCardRepository.save(creditCard);
    }

    private Order submitOrder(User user){
        Order order = orderRepository.save(new Order(user.getShoppingCart()));
        ShoppingCart emptyShoppingCart = shoppingCartRepository.save(new ShoppingCart());
        user.setShoppingCart(emptyShoppingCart);
        user.getOrders().add(order);
        userRepository.save(user);
        return order;
    }

    public ShoppingCart findUserShoppingCart(User user){
        return shoppingCartRepository.findById(user.getShoppingCart().getId()).get();
    }

}
