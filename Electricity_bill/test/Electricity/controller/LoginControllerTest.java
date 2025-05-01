package Electricity.controller;

import Electricity.dataAccessOutput.LoginDAO;
import Electricity.model.Login;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    @Mock
    private LoginDAO loginDAO;

    private LoginController loginController;
    private Login testLogin;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Create a controller with a mocked DAO
        loginController = new LoginController();
        
        // Use reflection to replace the DAO with our mock
        try {
            java.lang.reflect.Field daoField = LoginController.class.getDeclaredField("loginDAO");
            daoField.setAccessible(true);
            daoField.set(loginController, loginDAO);
        } catch (Exception e) {
            fail("Failed to inject mock DAO: " + e.getMessage());
        }
        
        // Setup test login data
        testLogin = new Login("M12345", "johndoe", "John Doe", "password123", "Customer");
    }

    @Test
    public void testAuthenticateUser() {
        // Configure mock to return a login when authenticate is called
        when(loginDAO.authenticate("johndoe", "password123", "Customer")).thenReturn(testLogin);
        
        // Call the controller method
        Login result = loginController.authenticateUser("johndoe", "password123", "Customer");
        
        // Verify the result and DAO interaction
        assertNotNull("Authenticated login should not be null", result);
        assertEquals("Login username should match", "johndoe", result.getUsername());
        assertEquals("Login user type should match", "Customer", result.getUserType());
        verify(loginDAO).authenticate("johndoe", "password123", "Customer");
    }
    
    @Test
    public void testAuthenticateUserWithInvalidCredentials() {
        // Configure mock to return null for invalid credentials
        when(loginDAO.authenticate("johndoe", "wrongpassword", "Customer")).thenReturn(null);
        
        // Call the controller method
        Login result = loginController.authenticateUser("johndoe", "wrongpassword", "Customer");
        
        // Verify the result and DAO interaction
        assertNull("Authentication with invalid credentials should fail", result);
        verify(loginDAO).authenticate("johndoe", "wrongpassword", "Customer");
    }
    
    @Test
    public void testRegisterUser() {
        // Configure mocks
        when(loginDAO.getLoginByMeterNo("M12345")).thenReturn(null); // No existing user with this meter
        when(loginDAO.createUser(any(Login.class))).thenReturn(true);
        
        // Call the controller method
        boolean result = loginController.registerUser(
            testLogin.getMeterNo(),
            testLogin.getUsername(),
            testLogin.getName(),
            testLogin.getPassword(),
            testLogin.getUserType()
        );
        
        // Verify the result and DAO interactions
        assertTrue("User registration should be successful", result);
        verify(loginDAO).getLoginByMeterNo("M12345");
        verify(loginDAO).createUser(any(Login.class));
    }
    
    @Test
    public void testRegisterExistingUser() {
        // Configure mock to return an existing user
        when(loginDAO.getLoginByMeterNo("M12345")).thenReturn(testLogin);
        
        // Call the controller method
        boolean result = loginController.registerUser(
            testLogin.getMeterNo(),
            testLogin.getUsername(),
            testLogin.getName(),
            testLogin.getPassword(),
            testLogin.getUserType()
        );
        
        // Verify the result and DAO interactions
        assertFalse("Registration of existing user should fail", result);
        verify(loginDAO).getLoginByMeterNo("M12345");
        verify(loginDAO, never()).createUser(any(Login.class));
    }
    
    @Test
    public void testRegisterUserWithInvalidData() {
        // Call with invalid data (null meter)
        boolean result = loginController.registerUser(
            null,
            testLogin.getUsername(),
            testLogin.getName(),
            testLogin.getPassword(),
            testLogin.getUserType()
        );
        
        // Verify the result and DAO interactions
        assertFalse("Registration with null meter should fail", result);
        verify(loginDAO, never()).getLoginByMeterNo(anyString());
        verify(loginDAO, never()).createUser(any(Login.class));
    }
    
    @Test
    public void testUpdateUser() {
        // Configure mock
        when(loginDAO.updateUser(any(Login.class))).thenReturn(true);
        
        // Call the controller method
        boolean result = loginController.updateUserDetails(
            testLogin.getMeterNo(),
            testLogin.getUsername(),
            testLogin.getName(),
            testLogin.getPassword(),
            testLogin.getUserType()
        );
        
        // Verify the result and DAO interaction
        assertTrue("User update should be successful", result);
        verify(loginDAO).updateUser(any(Login.class));
    }
}