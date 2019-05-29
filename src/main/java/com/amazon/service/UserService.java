package com.amazon.service;

import com.amazon.domain.Address;
import com.amazon.domain.User;
import com.amazon.dto.LoginDTO;
import com.amazon.dto.UserRegistrationDTO;
import com.amazon.exceptions.InvalidEmailException;
import com.amazon.exceptions.InvalidPasswordException;
import com.amazon.exceptions.UserException;
import com.amazon.repository.AddressRepository;
import com.amazon.repository.UserRepository;
import com.amazon.util.PasswordEncryption;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public Long registerUser(UserRegistrationDTO registrationDTO) throws UserException {
        if (isMailBusy(registrationDTO.getEmail())){
            throw new UserException("Email is unavailable!");
        }
        String hashedPassword = PasswordEncryption.hashPassword(registrationDTO.getPassword());
        User newUser = new User(registrationDTO.getEmail(), registrationDTO.getName(), hashedPassword);
        return userRepository.save(newUser).getId();
    }

    private boolean isMailBusy(String email){
        return userRepository.findByEmail(email) != null;
    }

    public User logUser(LoginDTO loginForm) throws InvalidPasswordException, InvalidEmailException {
        User loggedUser = userRepository.findByEmail(loginForm.getEmail());
        verifyEmail(loggedUser);
        PasswordEncryption.verifyPassword(loginForm.getPassword(), loggedUser.getPassword());
        return loggedUser;
    }

    private void verifyEmail(User loggedUser) throws InvalidEmailException {
        if(loggedUser == null){
            throw new InvalidEmailException("Invalid email!");
        }
    }

    public Long addNewAddress(Address newAddress, User user){
        Address addedAddress = addressRepository.save(newAddress);
        user.getAddresses().add(addedAddress);
        userRepository.save(user);
        return addedAddress.getId();
    }


}
