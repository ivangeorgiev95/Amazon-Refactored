import com.amazon.domain.*;
import com.amazon.dto.LoginDTO;
import com.amazon.dto.UserRegistrationDTO;
import com.amazon.exceptions.InvalidEmailException;
import com.amazon.exceptions.InvalidPasswordException;
import com.amazon.exceptions.SellerException;
import com.amazon.exceptions.UserException;
import com.amazon.repository.*;
import com.amazon.service.UserService;
import com.amazon.util.SensitiveDataEncryption;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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
    private static final long TEST_ID = 1L;
    private static final String TEST_CREDIT_CARD_NUMBER = "4523182363972080";
    private static final String TEST_BUSINESS_DISPLAY_NAME = "Test Company LTD";
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
    @Spy
    private static SensitiveDataEncryption dataEncryption = new SensitiveDataEncryption();
    @InjectMocks
    private UserService userService;

    private UserRegistrationDTO testRegistrationForm;
    private LoginDTO testLoginForm;
    private User testUser;
    private static String hashedPassword ;
    private CreditCard testCreditCard;
    private static String hashedCardNumber ;
    private Seller testSeller;



    @BeforeClass
    public static void hash(){
        hashedPassword = dataEncryption.hashSensitiveData(TEST_PASSWORD);
        hashedCardNumber = dataEncryption.hashSensitiveData(TEST_CREDIT_CARD_NUMBER);
    }

    @Before
    public void init(){
        testRegistrationForm = new UserRegistrationDTO(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
        testLoginForm = new LoginDTO(TEST_EMAIL, TEST_PASSWORD);
        testUser = new User();
        testUser.setPassword(hashedPassword);
        testUser.setCreditCards(new HashSet<>());
    }

    @Test
    public void registerBusyEmail(){
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(new User());
        assertThrows(UserException.class, () -> userService.registerUser(testRegistrationForm));
    }

    @Test
    public void registerNewUser() throws UserException {
        testUser.setId(TEST_ID);
        doReturn(hashedPassword).when(dataEncryption).hashSensitiveData(TEST_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        assertEquals(TEST_ID, (long) userService.registerUser(testRegistrationForm));
    }

    @Test
    public void setNewShoppingCartWhenRegisterTest() throws UserException {
        doReturn(hashedPassword).when(dataEncryption).hashSensitiveData(TEST_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(new ShoppingCart());
        userService.registerUser(testRegistrationForm);
        assertNotNull(testUser.getShoppingCart());
    }

    @Test
    public void successfulLoginTest() throws InvalidEmailException, InvalidPasswordException {
        doReturn(true).when(dataEncryption).verifySensitiveData(TEST_PASSWORD, hashedPassword);
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
        doReturn(false).when(dataEncryption).verifySensitiveData(INVALID_PASSWORD, hashedPassword);
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
        initTestCreditCard();
        testUser.setCreditCards(new HashSet<>(Arrays.asList(new CreditCard())));
        doReturn(true).when(dataEncryption).verifySensitiveData(any(), any());
        testUser.getCreditCards().add(new CreditCard());
        assertThrows(UserException.class, () -> userService.addNewCreditCard(testUser,testCreditCard));
    }

    @Test
    public void addNonExistingCreditCard() throws UserException {
        initTestCreditCard();
        when(creditCardRepository.findAll()).thenReturn(new ArrayList<>());
        testCreditCard.setId(TEST_ID);
        when(creditCardRepository.save(any(CreditCard.class))).thenReturn(testCreditCard);
        doReturn(hashedCardNumber).when(dataEncryption).hashSensitiveData(TEST_CREDIT_CARD_NUMBER);
        assertEquals(TEST_ID,(long) userService.addNewCreditCard(testUser, testCreditCard));
    }

    @Test
    public void addExistingCreditCard() throws UserException {
        initTestCreditCard();
        CreditCard existingCreditCard = new CreditCard();
        existingCreditCard.setCardNumber(hashedCardNumber);
        existingCreditCard.setId(TEST_ID);
        doReturn(true).when(dataEncryption).verifySensitiveData(testCreditCard.getCardNumber(), hashedCardNumber);
        when(creditCardRepository.findAll()).thenReturn(Arrays.asList(existingCreditCard));
        assertEquals(TEST_ID,(long) userService.addNewCreditCard(testUser, testCreditCard));
    }

    @Test
    public void registerNewSellerWithBusyBusinessDisplayName(){
        initTestSeller();
        when(sellerRepository.findByBusinessDisplayName(TEST_BUSINESS_DISPLAY_NAME)).thenReturn(new Seller());
        assertThrows(SellerException.class, () -> userService.registerNewSeller(testUser, testSeller));
    }



    private void initTestCreditCard(){
        testCreditCard = new CreditCard();
        testCreditCard.setCardNumber(TEST_CREDIT_CARD_NUMBER);
    }

    private void initTestSeller(){
        testSeller = new Seller();
        testSeller.setBusinessDisplayName(TEST_BUSINESS_DISPLAY_NAME);
    }
}
