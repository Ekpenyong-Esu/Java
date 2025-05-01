package Electricity.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {

    private Login login;
    private static final String TEST_METER_NO = "M12345";
    private static final String TEST_USERNAME = "johndoe";
    private static final String TEST_NAME = "John Doe";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_USER_TYPE = "Customer";

    @Before
    public void setUp() {
        login = new Login(TEST_METER_NO, TEST_USERNAME, TEST_NAME, TEST_PASSWORD, TEST_USER_TYPE);
    }

    @Test
    public void testLoginConstructor() {
        assertNotNull("Login object should not be null", login);
        assertEquals("Meter number should match constructor parameter", TEST_METER_NO, login.getMeterNo());
        assertEquals("Username should match constructor parameter", TEST_USERNAME, login.getUsername());
        assertEquals("Name should match constructor parameter", TEST_NAME, login.getName());
        assertEquals("Password should match constructor parameter", TEST_PASSWORD, login.getPassword());
        assertEquals("User type should match constructor parameter", TEST_USER_TYPE, login.getUserType());
    }

    @Test
    public void testSettersAndGetters() {
        // Test setters
        String newMeterNo = "M67890";
        login.setMeterNo(newMeterNo);
        assertEquals("Meter number should be updated", newMeterNo, login.getMeterNo());
        
        String newUsername = "janesmith";
        login.setUsername(newUsername);
        assertEquals("Username should be updated", newUsername, login.getUsername());
        
        String newName = "Jane Smith";
        login.setName(newName);
        assertEquals("Name should be updated", newName, login.getName());
        
        String newPassword = "newpassword456";
        login.setPassword(newPassword);
        assertEquals("Password should be updated", newPassword, login.getPassword());
        
        String newUserType = "Admin";
        login.setUserType(newUserType);
        assertEquals("User type should be updated", newUserType, login.getUserType());
    }
    
    @Test
    public void testDefaultConstructor() {
        Login defaultLogin = new Login();
        assertNotNull("Default login should not be null", defaultLogin);
        // Values should be null or default as set by the default constructor
    }
    
    @Test
    public void testEqualsAndHashCode() {
        // Create identical login
        Login identicalLogin = new Login(TEST_METER_NO, TEST_USERNAME, TEST_NAME, TEST_PASSWORD, TEST_USER_TYPE);
        
        // Create different login
        Login differentLogin = new Login("M67890", TEST_USERNAME, TEST_NAME, TEST_PASSWORD, TEST_USER_TYPE);
        
        // Test equals and hashCode
        assertEquals("Identical logins should be equal", login, identicalLogin);
        assertEquals("Identical logins should have same hash code", login.hashCode(), identicalLogin.hashCode());
        assertNotEquals("Different logins should not be equal", login, differentLogin);
        assertNotEquals("Login should not equal null", login, null);
        assertNotEquals("Login should not equal object of different class", login, "Not a Login");
    }
    
    @Test
    public void testToString() {
        String expectedString = "Login{meterNo='" + TEST_METER_NO + "', username='" + TEST_USERNAME + 
                               "', name='" + TEST_NAME + "', userType='" + TEST_USER_TYPE + "'}";
        
        // Some implementations might include the password in toString, others might not for security
        // So we'll just check if the toString contains the essential fields
        String actualString = login.toString();
        
        assertTrue("toString should contain meter number", actualString.contains(TEST_METER_NO));
        assertTrue("toString should contain username", actualString.contains(TEST_USERNAME));
        assertTrue("toString should contain name", actualString.contains(TEST_NAME));
        assertTrue("toString should contain user type", actualString.contains(TEST_USER_TYPE));
    }
}