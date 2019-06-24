import com.amazon.domain.User;
import com.amazon.dto.UserRegistrationDTO;
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
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {

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

    private UserRegistrationDTO registrationDTO;

    @Before
    public void init(){
        registrationDTO = new UserRegistrationDTO("Test", "test@amazon.com","123456","123456");
    }

    @Test
    public void registerBusyEmail(){
        when(userRepository.findByEmail("test@amazon.com")).thenReturn(new User());
        assertThrows(UserException.class, () -> userService.registerUser(registrationDTO));
    }

    @Test
    public void registerNewUser() throws UserException {
        User testUser = new User();
        testUser.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        assertEquals((long) 1, (long) userService.registerUser(registrationDTO));
    }

    @Test
    public void loginTest(){
        String hashedPassword = SensitiveDataEncryption.hashSensitiveData(registrationDTO.getPassword());
        User testUser = new User();
        testUser.setPassword(hashedPassword);
        when(userRepository.findByEmail(registrationDTO.getEmail())).thenReturn(testUser);
//        assertEquals(testUser, userService.logUser());

    }

}
