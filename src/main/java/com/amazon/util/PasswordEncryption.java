package com.amazon.util;

import com.amazon.exceptions.InvalidPasswordException;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryption {

    private static final int WORKLOAD = 12;


    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt(WORKLOAD);
        String hashedPassword = BCrypt.hashpw(password, salt);
        return(hashedPassword);
    }

    public static void verifyPassword(String loginPassword, String storedPassword) throws InvalidPasswordException {
        if(storedPassword == null || !storedPassword.startsWith("$2a$")) {
            throw new InvalidPasswordException("Invalid hash provided for comparison");
        }
        if(!BCrypt.checkpw(loginPassword, storedPassword)){
            throw new InvalidPasswordException("Invalid password!");
        }
    }

}
