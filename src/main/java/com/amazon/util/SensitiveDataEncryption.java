package com.amazon.util;

import org.mindrot.jbcrypt.BCrypt;

public class SensitiveDataEncryption {

    private static final int WORKLOAD = 12;


    public static String hashSensitiveData(String sensitiveData) {
        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(sensitiveData, salt);
    }

    public static boolean verifySensitiveData(String enteredSensitiveData, String storedSensitiveData) {
        if(storedSensitiveData == null || !storedSensitiveData.startsWith("$2a$")) {
            return false;
        }
        return BCrypt.checkpw(enteredSensitiveData, storedSensitiveData);
    }

}
