package org.gfigueras.bizum.model.util;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class Cipher {
    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
    public static byte[] hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return hashedPassword;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyPassword(String enteredPassword, byte[] salt, byte[] storedHashedPassword) {
        byte[] calculatedHash = hashPassword(enteredPassword, salt);
        return MessageDigest.isEqual(calculatedHash, storedHashedPassword);
    }

    public static String generateAccountNumber(){
        String prefix = "ES24";
        for(int i = 0; i < 20; i++){
            prefix += (int)(Math.random() * 10);
        }
        return prefix;
    }
}