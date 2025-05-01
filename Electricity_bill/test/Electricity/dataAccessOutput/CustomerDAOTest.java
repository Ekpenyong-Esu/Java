package Electricity.dataAccessOutput;

import Electricity.model.Customer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CustomerDAOTest {

    private CustomerDAO customerDAO;
    
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResultSet;
    
    private Customer testCustomer;
    
    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        
        // Create a CustomerDAO subclass that overrides getConnection to return our mock
        customerDAO = new CustomerDAO() {
            @Override
            protected Connection getConnection() throws SQLException {
                return mockConnection;
            }
        };
        
        // Set up common mock behaviors
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        // Create a test customer
        testCustomer = new Customer("John Doe", "M12345", "123 Electric Avenue", 
                                  "Powertown", "Voltage State", "john.doe@example.com", 
                                  "123-456-7890");
    }

    @Test
    public void testAddCustomer() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 row affected
        
        // Act
        boolean result = customerDAO.addCustomer(testCustomer);
        
        // Assert
        assertTrue("Adding customer should return true for success", result);
        verify(mockConnection).prepareStatement(contains("INSERT INTO customer"));
        verify(mockPreparedStatement).setString(1, testCustomer.getName());
        verify(mockPreparedStatement).setString(2, testCustomer.getMeter());
        verify(mockPreparedStatement).setString(3, testCustomer.getAddress());
        verify(mockPreparedStatement).setString(4, testCustomer.getCity());
        verify(mockPreparedStatement).setString(5, testCustomer.getState());
        verify(mockPreparedStatement).setString(6, testCustomer.getEmail());
        verify(mockPreparedStatement).setString(7, testCustomer.getPhone());
        verify(mockPreparedStatement).executeUpdate();
    }
    
    @Test
    public void testAddCustomerFailure() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // 0 rows affected
        
        // Act
        boolean result = customerDAO.addCustomer(testCustomer);
        
        // Assert
        assertFalse("Adding customer should return false for failure", result);
    }
    
    @Test
    public void testAddCustomerSQLException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Test exception"));
        
        // Act
        boolean result = customerDAO.addCustomer(testCustomer);
        
        // Assert
        assertFalse("Adding customer should return false for SQL exception", result);
    }
    
    @Test
    public void testGetCustomerByMeter() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // One result
        when(mockResultSet.getString("name")).thenReturn(testCustomer.getName());
        when(mockResultSet.getString("meter_no")).thenReturn(testCustomer.getMeter());
        when(mockResultSet.getString("address")).thenReturn(testCustomer.getAddress());
        when(mockResultSet.getString("city")).thenReturn(testCustomer.getCity());
        when(mockResultSet.getString("state")).thenReturn(testCustomer.getState());
        when(mockResultSet.getString("email")).thenReturn(testCustomer.getEmail());
        when(mockResultSet.getString("phone")).thenReturn(testCustomer.getPhone());
        
        // Act
        Customer result = customerDAO.getCustomerByMeter(testCustomer.getMeter());
        
        // Assert
        assertNotNull("Retrieved customer should not be null", result);
        assertEquals("Retrieved name should match", testCustomer.getName(), result.getName());
        assertEquals("Retrieved meter should match", testCustomer.getMeter(), result.getMeter());
        assertEquals("Retrieved address should match", testCustomer.getAddress(), result.getAddress());
        assertEquals("Retrieved city should match", testCustomer.getCity(), result.getCity());
        assertEquals("Retrieved state should match", testCustomer.getState(), result.getState());
        assertEquals("Retrieved email should match", testCustomer.getEmail(), result.getEmail());
        assertEquals("Retrieved phone should match", testCustomer.getPhone(), result.getPhone());
        
        verify(mockConnection).prepareStatement(contains("SELECT * FROM customer WHERE meter_no = ?"));
        verify(mockPreparedStatement).setString(1, testCustomer.getMeter());
    }
    
    @Test
    public void testGetCustomerByMeterNotFound() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false); // No results
        
        // Act
        Customer result = customerDAO.getCustomerByMeter(testCustomer.getMeter());
        
        // Assert
        assertNull("Customer should be null when not found", result);
    }
    
    @Test
    public void testGetAllCustomers() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false); // Two results
        
        // First result
        when(mockResultSet.getString("name")).thenReturn("John Doe").thenReturn("Jane Smith");
        when(mockResultSet.getString("meter_no")).thenReturn("M12345").thenReturn("M67890");
        when(mockResultSet.getString("address")).thenReturn("123 Electric Ave").thenReturn("456 Power St");
        when(mockResultSet.getString("city")).thenReturn("Powertown").thenReturn("Circuit City");
        when(mockResultSet.getString("state")).thenReturn("Voltage State").thenReturn("Current State");
        when(mockResultSet.getString("email")).thenReturn("john@example.com").thenReturn("jane@example.com");
        when(mockResultSet.getString("phone")).thenReturn("123-456-7890").thenReturn("098-765-4321");
        
        // Act
        List<Customer> results = customerDAO.getAllCustomers();
        
        // Assert
        assertNotNull("Customer list should not be null", results);
        assertEquals("Customer list should have 2 items", 2, results.size());
        assertEquals("First customer name should be John Doe", "John Doe", results.get(0).getName());
        assertEquals("Second customer name should be Jane Smith", "Jane Smith", results.get(1).getName());
        
        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery(contains("SELECT * FROM customer"));
    }
    
    @Test
    public void testGetAllCustomersSQLException() throws SQLException {
        // Arrange
        when(mockStatement.executeQuery(anyString())).thenThrow(new SQLException("Test exception"));
        
        // Act
        List<Customer> results = customerDAO.getAllCustomers();
        
        // Assert
        assertNotNull("Customer list should not be null on exception", results);
        assertTrue("Customer list should be empty on exception", results.isEmpty());
    }
    
    @Test
    public void testGetAllMeterNumbers() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false); // Two results
        when(mockResultSet.getString("meter_no")).thenReturn("M12345").thenReturn("M67890");
        
        // Act
        List<String> results = customerDAO.getAllMeterNumbers();
        
        // Assert
        assertNotNull("Meter number list should not be null", results);
        assertEquals("Meter number list should have 2 items", 2, results.size());
        assertEquals("First meter number should be M12345", "M12345", results.get(0));
        assertEquals("Second meter number should be M67890", "M67890", results.get(1));
        
        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery(contains("SELECT meter_no FROM customer"));
    }
    
    @Test
    public void testUpdateCustomer() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 row affected
        
        // Act
        boolean result = customerDAO.updateCustomer(testCustomer);
        
        // Assert
        assertTrue("Updating customer should return true for success", result);
        verify(mockConnection).prepareStatement(contains("UPDATE customer SET"));
        verify(mockPreparedStatement).setString(1, testCustomer.getName());
        verify(mockPreparedStatement).setString(2, testCustomer.getAddress());
        verify(mockPreparedStatement).setString(3, testCustomer.getCity());
        verify(mockPreparedStatement).setString(4, testCustomer.getState());
        verify(mockPreparedStatement).setString(5, testCustomer.getEmail());
        verify(mockPreparedStatement).setString(6, testCustomer.getPhone());
        verify(mockPreparedStatement).setString(7, testCustomer.getMeter());
        verify(mockPreparedStatement).executeUpdate();
    }
}