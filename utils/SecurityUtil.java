package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityUtil {

    // Hash a plaintext password using SHA-256
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Verify a plaintext password against a hashed password
    public static boolean check(String plainPassword, String hashedPassword) {
        String hashedAttempt = hash(plainPassword);
        return hashedAttempt != null && hashedAttempt.equals(hashedPassword);
    }
}
