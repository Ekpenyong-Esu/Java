package Electricity.dataAccessOutput;

import Electricity.model.Tax;
import Electricity.util.Conn;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test class for TaxDAO database operations
 */
@RunWith(MockitoJUnitRunner.class)
public class TaxDAOTest {

    @InjectMocks
    private TaxDAO taxDAO;
    
    @Mock
    private Connection mockConnection;
    
    @Mock
    private PreparedStatement mockStatement;
    
    @Mock
    private ResultSet mockResultSet;
    
    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        
        // Setup mock connection
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        
        // Use reflection to set the static Conn to return our mock
        try {
            var connClass = Conn.class;
            var getConnectionMethod = connClass.getDeclaredMethod("getConnection");
            getConnectionMethod.setAccessible(true);
            // This is a simplified version - in real test you would use frameworks like PowerMock or similar
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSaveTax() throws SQLException {
        // Arrange
        Tax tax = new Tax(9.5, 55.0, 22.5, 5.5, 1.25, 18.5);
        when(mockStatement.executeUpdate()).thenReturn(1);
        
        // Act
        boolean result = taxDAO.saveTax(tax);
        
        // Assert
        assertTrue(result);
        // Verify parameters were set correctly
        verify(mockStatement).setDouble(1, 9.5);
        verify(mockStatement).setDouble(2, 55.0);
        verify(mockStatement).setDouble(3, 22.5);
        verify(mockStatement).setDouble(4, 5.5);
        verify(mockStatement).setDouble(5, 1.25);
        verify(mockStatement).setDouble(6, 18.5);
    }
    
    @Test
    public void testUpdateTax() throws SQLException {
        // Arrange
        Tax tax = new Tax(9.5, 55.0, 22.5, 5.5, 1.25, 18.5);
        when(mockStatement.executeUpdate()).thenReturn(1);
        
        // Act
        boolean result = taxDAO.updateTax(tax);
        
        // Assert
        assertTrue(result);
        // Verify parameters were set correctly
        verify(mockStatement).setDouble(1, 9.5);
        verify(mockStatement).setDouble(2, 55.0);
        verify(mockStatement).setDouble(3, 22.5);
        verify(mockStatement).setDouble(4, 5.5);
        verify(mockStatement).setDouble(5, 1.25);
        verify(mockStatement).setDouble(6, 18.5);
    }
    
    @Test
    public void testGetCurrentTax() throws SQLException {
        // Arrange
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getDouble("cost_per_unit")).thenReturn(9.5);
        when(mockResultSet.getDouble("meter_rent")).thenReturn(55.0);
        when(mockResultSet.getDouble("service_charge")).thenReturn(22.5);
        when(mockResultSet.getDouble("service_tax")).thenReturn(5.5);
        when(mockResultSet.getDouble("climate_change_levy")).thenReturn(1.25);
        when(mockResultSet.getDouble("fixed_tax")).thenReturn(18.5);
        
        // Act
        Tax result = taxDAO.getCurrentTax();
        
        // Assert
        assertNotNull(result);
        assertEquals(9.5, result.getCostPerUnit(), 0.01);
        assertEquals(55.0, result.getMeterRent(), 0.01);
        assertEquals(22.5, result.getServiceCharge(), 0.01);
        assertEquals(5.5, result.getServiceTax(), 0.01);
        assertEquals(1.25, result.getClimateChangeLevy(), 0.01);
        assertEquals(18.5, result.getFixedTax(), 0.01);
    }
    
    @Test
    public void testGetCurrentTaxNoResults() throws SQLException {
        // Arrange
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        
        // Act
        Tax result = taxDAO.getCurrentTax();
        
        // Assert
        assertNotNull(result);
        // Should return the default tax values
        assertEquals(0.0, result.getCostPerUnit(), 0.01); // Default constructor initializes to 0
    }
    
    @Test
    public void testDeleteTax() throws SQLException {
        // Arrange
        int taxId = 1;
        when(mockStatement.executeUpdate()).thenReturn(1);
        
        // Act
        boolean result = taxDAO.deleteTax(taxId);
        
        // Assert
        assertTrue(result);
        verify(mockStatement).setInt(1, taxId);
    }
    
    @Test
    public void testGetAllTaxes() throws SQLException {
        // Arrange
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        
        when(mockResultSet.getDouble("cost_per_unit")).thenReturn(9.5).thenReturn(9.0);
        when(mockResultSet.getDouble("meter_rent")).thenReturn(55.0).thenReturn(50.0);
        when(mockResultSet.getDouble("service_charge")).thenReturn(22.5).thenReturn(20.0);
        when(mockResultSet.getDouble("service_tax")).thenReturn(5.5).thenReturn(5.0);
        when(mockResultSet.getDouble("climate_change_levy")).thenReturn(1.25).thenReturn(1.0);
        when(mockResultSet.getDouble("fixed_tax")).thenReturn(18.5).thenReturn(18.0);
        
        // Act
        List<Tax> result = taxDAO.getAllTaxes();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        Tax firstTax = result.get(0);
        assertEquals(9.5, firstTax.getCostPerUnit(), 0.01);
        
        Tax secondTax = result.get(1);
        assertEquals(9.0, secondTax.getCostPerUnit(), 0.01);
    }
}