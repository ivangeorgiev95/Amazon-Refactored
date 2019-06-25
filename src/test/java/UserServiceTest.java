import com.amazon.domain.Address;
import com.amazon.domain.CreditCard;
import com.amazon.domain.ShoppingCart;
import com.amazon.domain.User;
import com.amazon.dto.LoginDTO;
import com.amazon.dto.UserRegistrationDTO;
import com.amazon.exceptions.InvalidEmailException;
import com.amazon.exceptions.InvalidPasswordException;
import com.amazon.exceptions.UserException;
import com.amazon.repository.*;
import com.amazon.service.UserService;
import com.amazon.util.SensitiveDataEncryption;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    private static final String TEST_EMAIL = "test@amazon.com";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_NAME = "Test";
    private static final String INVALID_PASSWORD = "654321";
    public static final long TEST_ID = 1L;
    public static final String TEST_CREDIT_CARD_NUMBER = "4523182363972080";
    @Mock
    private UserRepository userRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CreditCardRepository creditCardRepository;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private UserService userService;

    private UserRegistrationDTO testRegistrationForm;
    private LoginDTO testLoginForm;
    private User testUser;
    private String hashedPassword = SensitiveDataEncryption.hashSensitiveData(TEST_PASSWORD);
    @Before
    public void init(){
        testRegistrationForm = new UserRegistrationDTO(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        testLoginForm = new LoginDTO(TEST_EMAIL, TEST_PASSWORD);
        testUser = new User();
        testUser.setPassword(hashedPassword);
    }

    @Test
    public void registerBusyEmail(){
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(new User());
        assertThrows(UserException.class, () -> userService.registerUser(testRegistrationForm));
    }

    @Test
    public void registerNewUser() throws UserException {
        testUser.setId(TEST_ID);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        assertEquals(TEST_ID, (long) userService.registerUser(testRegistrationForm));
    }

    @Test
    public void setNewShoppingCartWhenRegisterTest() throws UserException {
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(new ShoppingCart());
        userService.registerUser(testRegistrationForm);
        assertNotNull(testUser.getShoppingCart());
    }

    @Test
    public void successfulLoginTest() throws InvalidEmailException, InvalidPasswordException {
        when(userRepository.findByEmail(testLoginForm.getEmail())).thenReturn(testUser);
        assertEquals(testUser, userService.logUser(testLoginForm));
    }

    @Test
    public void loginWithInvalidEmailTest(){
        when(userRepository.findByEmail(testLoginForm.getEmail())).thenReturn(null);
        assertThrows(InvalidEmailException.class, () -> userService.logUser(testLoginForm));
    }

    @Test
    public void loginWithInvalidPasswordTest(){
        testLoginForm.setPassword(INVALID_PASSWORD);
        when(userRepository.findByEmail(testLoginForm.getEmail())).thenReturn(testUser);
        assertThrows(InvalidPasswordException.class, () -> userService.logUser(testLoginForm));
    }

    @Test
    public void addNewAddressTest(){
        Address testAddress = new Address();
        testAddress.setId(TEST_ID);
        testUser.setAddresses(new ArrayList<Address>());
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);
        assertTrue(userService.addNewAddress(new Address(), testUser) == TEST_ID && testUser.getAddresses().contains(testAddress));
    }

    @Test
    public void addCreditCardWhichUserAlreadyContains(){
        CreditCard existingCreditCard = new CreditCard();
        String hashedCreditCardNumber = SensitiveDataEncryption.hashSensitiveData(TEST_CREDIT_CARD_NUMBER);
        existingCreditCard.setCardNumber(hashedCreditCardNumber);
        testUser.setCreditCards(new HashSet<CreditCard>(Arrays.asList(existingCreditCard)));
        CreditCard testCreditCard = new CreditCard();
        testCreditCard.setCardNumber(TEST_CREDIT_CARD_NUMBER);
        assertThrows(UserException.class, () -> userService.addNewCreditCard(testUser,testCreditCard));
    }

    @Test
    public void addNonExistingCreditCard(){

    }

}
