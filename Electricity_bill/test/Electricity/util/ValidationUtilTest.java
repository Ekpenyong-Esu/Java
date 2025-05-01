package Electricity.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ValidationUtilTest {

    @Test
    public void testIsValidEmail() {
        // Valid email addresses
        assertTrue(ValidationUtil.isValidEmail("test@example.com"), "Should accept standard email format");
        assertTrue(ValidationUtil.isValidEmail("user123@domain.com"), "Should accept email with numbers");
        assertTrue(ValidationUtil.isValidEmail("first.last@example.com"), "Should accept email with dots");
        assertTrue(ValidationUtil.isValidEmail("test+filter@example.com"), "Should accept email with plus sign");
        assertTrue(ValidationUtil.isValidEmail("test@sub.domain.co.uk"), "Should accept email with subdomain");
        
        // Invalid email addresses
        assertFalse(ValidationUtil.isValidEmail("testatexample.com"), "Should reject email without @ symbol");
        assertFalse(ValidationUtil.isValidEmail("test@"), "Should reject email without domain");
        assertFalse(ValidationUtil.isValidEmail("@example.com"), "Should reject email without username");
        assertFalse(ValidationUtil.isValidEmail("test user@example.com"), "Should reject email with spaces");
        assertFalse(ValidationUtil.isValidEmail(null), "Should reject null email");
        assertFalse(ValidationUtil.isValidEmail(""), "Should reject empty email");
    }
    
    @Test
    public void testIsValidPhoneNumber() {
        // Valid phone numbers
        assertTrue(ValidationUtil.isValidPhone("123-456-7890"), "Should accept standard format with dashes");
        assertTrue(ValidationUtil.isValidPhone("1234567890"), "Should accept standard format without separators");
        assertTrue(ValidationUtil.isValidPhone("+1-234-567-8901"), "Should accept international format");
        assertTrue(ValidationUtil.isValidPhone("(123) 456-7890"), "Should accept format with parentheses");
        
        // Invalid phone numbers
        assertFalse(ValidationUtil.isValidPhone("123-abc-7890"), "Should reject phone with letters");
        assertFalse(ValidationUtil.isValidPhone("123-456"), "Should reject too short phone number");
        assertFalse(ValidationUtil.isValidPhone(null), "Should reject null phone");
        assertFalse(ValidationUtil.isValidPhone(""), "Should reject empty phone");
    }
    
    @Test
    public void testIsValidUsername() {
        // Valid usernames
        assertTrue(ValidationUtil.isValidUsername("user123"), "Should accept alphanumeric username");
        assertTrue(ValidationUtil.isValidUsername("user_name"), "Should accept username with underscore");
        assertTrue(ValidationUtil.isValidUsername("usr12"), "Should accept username with minimum length");
        
        // Invalid usernames
        assertFalse(ValidationUtil.isValidUsername("usr"), "Should reject username that's too short");
        assertFalse(ValidationUtil.isValidUsername("user@123"), "Should reject username with special characters");
        assertFalse(ValidationUtil.isValidUsername("user name"), "Should reject username with spaces");
        assertFalse(ValidationUtil.isValidUsername(null), "Should reject null username");
        assertFalse(ValidationUtil.isValidUsername(""), "Should reject empty username");
    }
    
    @Test
    public void testIsValidPassword() {
        // Valid passwords
        assertTrue(ValidationUtil.isValidPassword("Password123!"), "Should accept strong password");
        assertTrue(ValidationUtil.isValidPassword("Pass12!"), "Should accept password with minimum requirements");
        
        // Invalid passwords
        assertFalse(ValidationUtil.isValidPassword("Pas1!"), "Should reject password that's too short");
        assertFalse(ValidationUtil.isValidPassword("password123!"), "Should reject password without uppercase");
        assertFalse(ValidationUtil.isValidPassword("PASSWORD123!"), "Should reject password without lowercase");
        assertFalse(ValidationUtil.isValidPassword("Password!"), "Should reject password without digits");
        assertFalse(ValidationUtil.isValidPassword("Password123"), "Should reject password without special char");
        assertFalse(ValidationUtil.isValidPassword(null), "Should reject null password");
        assertFalse(ValidationUtil.isValidPassword(""), "Should reject empty password");
    }
    
    @Test
    public void testIsValidMeterNumber() {
        // Valid meter numbers
        assertTrue(ValidationUtil.isValidMeterNumber("M12345"), "Should accept standard meter number");
        assertTrue(ValidationUtil.isValidMeterNumber("E98765"), "Should accept meter number with different prefix");
        
        // Invalid meter numbers
        assertFalse(ValidationUtil.isValidMeterNumber("12345"), "Should reject meter number without prefix");
        assertFalse(ValidationUtil.isValidMeterNumber("M123$5"), "Should reject meter number with invalid characters");
        assertFalse(ValidationUtil.isValidMeterNumber("M123"), "Should reject meter number that's too short");
        assertFalse(ValidationUtil.isValidMeterNumber(null), "Should reject null meter number");
        assertFalse(ValidationUtil.isValidMeterNumber(""), "Should reject empty meter number");
    }
    
    @Test
    public void testIsNumeric() {
        // Valid numeric values
        assertTrue(ValidationUtil.isNumeric("123"), "Should accept integer");
        assertTrue(ValidationUtil.isNumeric("123.45"), "Should accept decimal");
        assertTrue(ValidationUtil.isNumeric("-123"), "Should accept negative number");
        assertTrue(ValidationUtil.isNumeric("0"), "Should accept zero");
        
        // Invalid numeric values
        assertFalse(ValidationUtil.isNumeric("abc"), "Should reject text");
        assertFalse(ValidationUtil.isNumeric("123abc"), "Should reject mixed content");
        assertFalse(ValidationUtil.isNumeric(null), "Should reject null input");
        assertFalse(ValidationUtil.isNumeric(""), "Should reject empty string");
    }
    
    @Test
    public void testIsNullOrEmpty() {
        // Null or empty values
        assertTrue(ValidationUtil.isNullOrEmpty(null), "Should identify null as empty");
        assertTrue(ValidationUtil.isNullOrEmpty(""), "Should identify empty string as empty");
        assertTrue(ValidationUtil.isNullOrEmpty("   "), "Should identify whitespace as empty");
        
        // Non-empty values
        assertFalse(ValidationUtil.isNullOrEmpty("text"), "Should identify text as not empty");
        assertFalse(ValidationUtil.isNullOrEmpty(" text "), "Should identify space-padded text as not empty");
    }
}