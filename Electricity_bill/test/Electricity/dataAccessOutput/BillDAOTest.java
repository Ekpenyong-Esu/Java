package Electricity.dataAccessOutput;

import Electricity.model.Bill;
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

public class BillDAOTest {

    private BillDAO billDAO;
    
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResultSet;
    
    private Bill testBill;
    
    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        
        // Create a BillDAO subclass that overrides getConnection to return our mock
        billDAO = new BillDAO() {
            @Override
            protected Connection getConnection() throws SQLException {
                return mockConnection;
            }
        };
        
        // Set up common mock behaviors
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        
        // Create a test bill
        testBill = new Bill("M12345", "January", 100, 950.50, "UNPAID");
    }

    @Test
    public void testAddBill() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 row affected
        
        // Act
        boolean result = billDAO.addBill(testBill);
        
        // Assert
        assertTrue("Adding bill should return true for success", result);
        verify(mockConnection).prepareStatement(contains("INSERT INTO bill"));
        verify(mockPreparedStatement).setString(1, testBill.getMeter());
        verify(mockPreparedStatement).setString(2, testBill.getMonth());
        verify(mockPreparedStatement).setInt(3, testBill.getUnits());
        verify(mockPreparedStatement).setDouble(4, testBill.getTotalBill());
        verify(mockPreparedStatement).setString(5, testBill.getStatus());
        verify(mockPreparedStatement).executeUpdate();
    }
    
    @Test
    public void testAddBillFailure() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // 0 rows affected
        
        // Act
        boolean result = billDAO.addBill(testBill);
        
        // Assert
        assertFalse("Adding bill should return false for failure", result);
    }
    
    @Test
    public void testAddBillSQLException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Test exception"));
        
        // Act
        boolean result = billDAO.addBill(testBill);
        
        // Assert
        assertFalse("Adding bill should return false for SQL exception", result);
    }
    
    @Test
    public void testGetBill() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // One result
        when(mockResultSet.getString("meter")).thenReturn(testBill.getMeter());
        when(mockResultSet.getString("month")).thenReturn(testBill.getMonth());
        when(mockResultSet.getInt("units")).thenReturn(testBill.getUnits());
        when(mockResultSet.getDouble("total_bill")).thenReturn(testBill.getTotalBill());
        when(mockResultSet.getString("status")).thenReturn(testBill.getStatus());
        
        // Act
        Bill result = billDAO.getBill(testBill.getMeter(), testBill.getMonth());
        
        // Assert
        assertNotNull("Retrieved bill should not be null", result);
        assertEquals("Retrieved meter should match", testBill.getMeter(), result.getMeter());
        assertEquals("Retrieved month should match", testBill.getMonth(), result.getMonth());
        assertEquals("Retrieved units should match", testBill.getUnits(), result.getUnits());
        assertEquals("Retrieved total bill should match", testBill.getTotalBill(), result.getTotalBill(), 0.001);
        assertEquals("Retrieved status should match", testBill.getStatus(), result.getStatus());
        
        verify(mockConnection).prepareStatement(contains("SELECT * FROM bill WHERE meter = ? AND month = ?"));
        verify(mockPreparedStatement).setString(1, testBill.getMeter());
        verify(mockPreparedStatement).setString(2, testBill.getMonth());
    }
    
    @Test
    public void testGetBillNotFound() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false); // No results
        
        // Act
        Bill result = billDAO.getBill(testBill.getMeter(), testBill.getMonth());
        
        // Assert
        assertNull("Bill should be null when not found", result);
    }
    
    @Test
    public void testGetBillSQLException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test exception"));
        
        // Act
        Bill result = billDAO.getBill(testBill.getMeter(), testBill.getMonth());
        
        // Assert
        assertNull("Bill should be null on SQL exception", result);
    }
    
    @Test
    public void testGetBillsByMeter() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false); // Two results
        // First result
        when(mockResultSet.getString("meter")).thenReturn(testBill.getMeter());
        when(mockResultSet.getString("month")).thenReturn("January").thenReturn("February");
        when(mockResultSet.getInt("units")).thenReturn(100).thenReturn(120);
        when(mockResultSet.getDouble("total_bill")).thenReturn(950.50).thenReturn(1100.75);
        when(mockResultSet.getString("status")).thenReturn("UNPAID").thenReturn("PAID");
        
        // Act
        List<Bill> results = billDAO.getBillsByMeter(testBill.getMeter());
        
        // Assert
        assertNotNull("Bill list should not be null", results);
        assertEquals("Bill list should have 2 items", 2, results.size());
        assertEquals("First bill month should be January", "January", results.get(0).getMonth());
        assertEquals("Second bill month should be February", "February", results.get(1).getMonth());
        
        verify(mockConnection).prepareStatement(contains("SELECT * FROM bill WHERE meter = ?"));
        verify(mockPreparedStatement).setString(1, testBill.getMeter());
    }
    
    @Test
    public void testUpdateBillStatus() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 row affected
        
        // Act
        boolean result = billDAO.updateBillStatus(testBill.getMeter(), testBill.getMonth(), "PAID");
        
        // Assert
        assertTrue("Updating bill status should return true for success", result);
        verify(mockConnection).prepareStatement(contains("UPDATE bill SET status = ? WHERE meter = ? AND month = ?"));
        verify(mockPreparedStatement).setString(1, "PAID");
        verify(mockPreparedStatement).setString(2, testBill.getMeter());
        verify(mockPreparedStatement).setString(3, testBill.getMonth());
        verify(mockPreparedStatement).executeUpdate();
    }
    
    @Test
    public void testGetUnpaidBills() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false); // Two results
        // Result data setup
        when(mockResultSet.getString("meter")).thenReturn("M12345").thenReturn("M67890");
        when(mockResultSet.getString("month")).thenReturn("January").thenReturn("February");
        when(mockResultSet.getInt("units")).thenReturn(100).thenReturn(120);
        when(mockResultSet.getDouble("total_bill")).thenReturn(950.50).thenReturn(1100.75);
        when(mockResultSet.getString("status")).thenReturn("UNPAID").thenReturn("UNPAID");
        
        // Act
        List<Bill> results = billDAO.getUnpaidBills();
        
        // Assert
        assertNotNull("Unpaid bill list should not be null", results);
        assertEquals("Unpaid bill list should have 2 items", 2, results.size());
        assertEquals("All bills should have UNPAID status", "UNPAID", results.get(0).getStatus());
        assertEquals("All bills should have UNPAID status", "UNPAID", results.get(1).getStatus());
        
        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery(contains("SELECT * FROM bill WHERE status = 'UNPAID'"));
    }
}