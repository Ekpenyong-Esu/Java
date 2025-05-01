package Electricity.util;

import java.util.regex.Pattern;

/**
 * Utility class for common validation logic.
 */
public class ValidationUtil {

    /**
     * Validates if a string is not null or empty.
     * @param value The string to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isNotNullOrEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Checks if a string is null or empty (inverse of isNotNullOrEmpty)
     * @param value The string to validate
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validates if an email is in a proper format.
     * @param email The email to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        if (!isNotNullOrEmpty(email)) {
            return false;
        }
        // Enhanced regex to handle more complex email cases
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email) && !email.contains(" ");
    }

    /**
     * Validates if a phone number contains only digits and has a valid length.
     * @param phone The phone number to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidPhone(String phone) {
        if (!isNotNullOrEmpty(phone)) {
            return false;
        }
        // Accept formats like: 123-456-7890, 1234567890, +1-234-567-8901, (123) 456-7890
        String phoneRegex = "^(\\+\\d{1,3}[-])?\\(?\\d{3}\\)?[- ]?\\d{3}[- ]?\\d{4}$";
        return Pattern.matches(phoneRegex, phone);
    }

    /**
     * Validates if a number is positive.
     * @param number The number to validate.
     * @return true if positive, false otherwise.
     */
    public static boolean isPositiveNumber(double number) {
        return number > 0;
    }
    
    /**
     * Validates if a username meets the requirements:
     * - At least 4 characters
     * - Alphanumeric characters and underscores only
     * @param username The username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (isNullOrEmpty(username)) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_]{4,}$");
    }
    
    /**
     * Validates if a password meets the security requirements:
     * - At least 6 characters
     * - Contains at least one uppercase letter
     * - Contains at least one lowercase letter
     * - Contains at least one digit
     * - Contains at least one special character
     * @param password The password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (isNullOrEmpty(password)) {
            return false;
        }
        // Check length
        if (password.length() < 6) {
            return false;
        }
        // Check for uppercase
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        // Check for lowercase
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        // Check for digits
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        // Check for special characters
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            return false;
        }
        return true;
    }
    
    /**
     * Validates if a meter number meets the required format:
     * - Starts with a letter (usually M or E)
     * - Followed by at least 4 digits
     * @param meterNumber The meter number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidMeterNumber(String meterNumber) {
        if (isNullOrEmpty(meterNumber)) {
            return false;
        }
        return meterNumber.matches("^[A-Za-z]\\d{4,}$");
    }
    
    /**
     * Checks if a string contains a valid numeric value
     * @param str The string to check
     * @return true if the string is a valid number, false otherwise
     */
    public static boolean isNumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
