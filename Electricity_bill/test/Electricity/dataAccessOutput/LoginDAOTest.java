package Electricity.dataAccessOutput;

import Electricity.model.Login;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginDAOTest {

    private LoginDAO loginDAO;
    
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;
    
    private Login testLogin;
    
    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        
        // Create a LoginDAO subclass that overrides getConnection to return our mock
        loginDAO = new LoginDAO() {
            @Override
            protected Connection getConnection() throws SQLException {
                return mockConnection;
            }
        };
        
        // Set up common mock behaviors
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        // Create a test login
        testLogin = new Login("M12345", "johndoe", "John Doe", "password123", "Customer");
    }

    @Test
    public void testAuthenticate() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // One result
        when(mockResultSet.getString("meter_no")).thenReturn(testLogin.getMeterNo());
        when(mockResultSet.getString("username")).thenReturn(testLogin.getUsername());
        when(mockResultSet.getString("name")).thenReturn(testLogin.getName());
        when(mockResultSet.getString("password")).thenReturn(testLogin.getPassword());
        when(mockResultSet.getString("user_type")).thenReturn(testLogin.getUserType());
        
        // Act
        Login result = loginDAO.authenticate(testLogin.getUsername(), testLogin.getPassword(), testLogin.getUserType());
        
        // Assert
        assertNotNull("Authentication result should not be null", result);
        assertEquals("Retrieved meter number should match", testLogin.getMeterNo(), result.getMeterNo());
        assertEquals("Retrieved username should match", testLogin.getUsername(), result.getUsername());
        assertEquals("Retrieved name should match", testLogin.getName(), result.getName());
        assertEquals("Retrieved password should match", testLogin.getPassword(), result.getPassword());
        assertEquals("Retrieved user type should match", testLogin.getUserType(), result.getUserType());
        
        verify(mockConnection).prepareStatement(contains("SELECT * FROM login WHERE username = ? AND password = ? AND user_type = ?"));
        verify(mockPreparedStatement).setString(1, testLogin.getUsername());
        verify(mockPreparedStatement).setString(2, testLogin.getPassword());
        verify(mockPreparedStatement).setString(3, testLogin.getUserType());
    }
    
    @Test
    public void testAuthenticateInvalidCredentials() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false); // No results
        
        // Act
        Login result = loginDAO.authenticate(testLogin.getUsername(), "wrong_password", testLogin.getUserType());
        
        // Assert
        assertNull("Authentication should fail with invalid credentials", result);
    }
    
    @Test
    public void testAuthenticateSQLException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test exception"));
        
        // Act
        Login result = loginDAO.authenticate(testLogin.getUsername(), testLogin.getPassword(), testLogin.getUserType());
        
        // Assert
        assertNull("Authentication should return null on SQL exception", result);
    }
    
    @Test
    public void testCreateUser() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 row affected
        
        // Act
        boolean result = loginDAO.createUser(testLogin);
        
        // Assert
        assertTrue("Creating user should return true for success", result);
        verify(mockConnection).prepareStatement(contains("INSERT INTO login"));
        verify(mockPreparedStatement).setString(1, testLogin.getMeterNo());
        verify(mockPreparedStatement).setString(2, testLogin.getUsername());
        verify(mockPreparedStatement).setString(3, testLogin.getName());
        verify(mockPreparedStatement).setString(4, testLogin.getPassword());
        verify(mockPreparedStatement).setString(5, testLogin.getUserType());
        verify(mockPreparedStatement).executeUpdate();
    }
    
    @Test
    public void testCreateUserFailure() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // 0 rows affected
        
        // Act
        boolean result = loginDAO.createUser(testLogin);
        
        // Assert
        assertFalse("Creating user should return false for failure", result);
    }
    
    @Test
    public void testCreateUserSQLException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Test exception"));
        
        // Act
        boolean result = loginDAO.createUser(testLogin);
        
        // Assert
        assertFalse("Creating user should return false for SQL exception", result);
    }
    
    @Test
    public void testGetLoginByMeterNo() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // One result
        when(mockResultSet.getString("meter_no")).thenReturn(testLogin.getMeterNo());
        when(mockResultSet.getString("username")).thenReturn(testLogin.getUsername());
        when(mockResultSet.getString("name")).thenReturn(testLogin.getName());
        when(mockResultSet.getString("password")).thenReturn(testLogin.getPassword());
        when(mockResultSet.getString("user_type")).thenReturn(testLogin.getUserType());
        
        // Act
        Login result = loginDAO.getLoginByMeterNo(testLogin.getMeterNo());
        
        // Assert
        assertNotNull("Retrieved login should not be null", result);
        assertEquals("Retrieved username should match", testLogin.getUsername(), result.getUsername());
        assertEquals("Retrieved meter number should match", testLogin.getMeterNo(), result.getMeterNo());
        
        verify(mockConnection).prepareStatement(contains("SELECT * FROM login WHERE meter_no = ?"));
        verify(mockPreparedStatement).setString(1, testLogin.getMeterNo());
    }
    
    @Test
    public void testGetLoginByMeterNoNotFound() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false); // No results
        
        // Act
        Login result = loginDAO.getLoginByMeterNo(testLogin.getMeterNo());
        
        // Assert
        assertNull("Login should be null when not found", result);
    }
    
    @Test
    public void testUpdateUser() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 row affected
        
        // Act
        boolean result = loginDAO.updateUser(testLogin);
        
        // Assert
        assertTrue("Updating user should return true for success", result);
        verify(mockConnection).prepareStatement(contains("UPDATE login SET"));
        verify(mockPreparedStatement).setString(1, testLogin.getUsername());
        verify(mockPreparedStatement).setString(2, testLogin.getName());
        verify(mockPreparedStatement).setString(3, testLogin.getPassword());
        verify(mockPreparedStatement).setString(4, testLogin.getUserType());
        verify(mockPreparedStatement).setString(5, testLogin.getMeterNo());
        verify(mockPreparedStatement).executeUpdate();
    }
    
    @Test
    public void testUpdateUserFailure() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // 0 rows affected
        
        // Act
        boolean result = loginDAO.updateUser(testLogin);
        
        // Assert
        assertFalse("Updating user should return false for failure", result);
    }
}