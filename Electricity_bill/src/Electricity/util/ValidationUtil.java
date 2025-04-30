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
     * Validates if an email is in a proper format.
     * @param email The email to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        if (!isNotNullOrEmpty(email)) {
            return false;
        }
        String emailRegex = "^[\\w-\\.+]*[\\w-\\.]\\@[\\w]+\\.[a-z]{2,3}$";
        return Pattern.matches(emailRegex, email);
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
        return phone.matches("\\d{10}");
    }

    /**
     * Validates if a number is positive.
     * @param number The number to validate.
     * @return true if positive, false otherwise.
     */
    public static boolean isPositiveNumber(int number) {
        return number > 0;
    }
}
