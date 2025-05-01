package Electricity.dataAccessOutput;

import Electricity.model.Meter;
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

public class MeterDAOTest {

    private MeterDAO meterDAO;
    
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResultSet;
    
    private Meter testMeter;
    
    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        
        // Create a MeterDAO subclass that overrides getConnection to return our mock
        meterDAO = new MeterDAO() {
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
        
        // Create a test meter
        testMeter = new Meter("M12345", "Inside", "Electric", "PC001", "Normal", 30);
    }

    @Test
    public void testAddMeter() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 row affected
        
        // Act
        boolean result = meterDAO.addMeter(testMeter);
        
        // Assert
        assertTrue("Adding meter should return true for success", result);
        verify(mockConnection).prepareStatement(contains("INSERT INTO meter"));
        verify(mockPreparedStatement).setString(1, testMeter.getMeterNumber());
        verify(mockPreparedStatement).setString(2, testMeter.getMeterLocation());
        verify(mockPreparedStatement).setString(3, testMeter.getMeterType());
        verify(mockPreparedStatement).setString(4, testMeter.getPhaseCode());
        verify(mockPreparedStatement).setString(5, testMeter.getBillType());
        verify(mockPreparedStatement).setInt(6, testMeter.getDays());
        verify(mockPreparedStatement).executeUpdate();
    }
    
    @Test
    public void testAddMeterFailure() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // 0 rows affected
        
        // Act
        boolean result = meterDAO.addMeter(testMeter);
        
        // Assert
        assertFalse("Adding meter should return false for failure", result);
    }
    
    @Test
    public void testAddMeterSQLException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Test exception"));
        
        // Act
        boolean result = meterDAO.addMeter(testMeter);
        
        // Assert
        assertFalse("Adding meter should return false for SQL exception", result);
    }
    
    @Test
    public void testGetMeterByNumber() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // One result
        when(mockResultSet.getString("meter_no")).thenReturn(testMeter.getMeterNumber());
        when(mockResultSet.getString("meter_location")).thenReturn(testMeter.getMeterLocation());
        when(mockResultSet.getString("meter_type")).thenReturn(testMeter.getMeterType());
        when(mockResultSet.getString("phase_code")).thenReturn(testMeter.getPhaseCode());
        when(mockResultSet.getString("bill_type")).thenReturn(testMeter.getBillType());
        when(mockResultSet.getInt("days")).thenReturn(testMeter.getDays());
        
        // Act
        Meter result = meterDAO.getMeterByNumber(testMeter.getMeterNumber());
        
        // Assert
        assertNotNull("Retrieved meter should not be null", result);
        assertEquals("Retrieved meter number should match", testMeter.getMeterNumber(), result.getMeterNumber());
        assertEquals("Retrieved meter location should match", testMeter.getMeterLocation(), result.getMeterLocation());
        assertEquals("Retrieved meter type should match", testMeter.getMeterType(), result.getMeterType());
        assertEquals("Retrieved phase code should match", testMeter.getPhaseCode(), result.getPhaseCode());
        assertEquals("Retrieved bill type should match", testMeter.getBillType(), result.getBillType());
        assertEquals("Retrieved days should match", testMeter.getDays(), result.getDays());
        
        verify(mockConnection).prepareStatement(contains("SELECT * FROM meter WHERE meter_no = ?"));
        verify(mockPreparedStatement).setString(1, testMeter.getMeterNumber());
    }
    
    @Test
    public void testGetMeterByNumberNotFound() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false); // No results
        
        // Act
        Meter result = meterDAO.getMeterByNumber(testMeter.getMeterNumber());
        
        // Assert
        assertNull("Meter should be null when not found", result);
    }
    
    @Test
    public void testGetMeterByNumberSQLException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Test exception"));
        
        // Act
        Meter result = meterDAO.getMeterByNumber(testMeter.getMeterNumber());
        
        // Assert
        assertNull("Meter should be null on SQL exception", result);
    }
    
    @Test
    public void testGetAllMeters() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false); // Two results
        
        // First result, then second result
        when(mockResultSet.getString("meter_no")).thenReturn("M12345").thenReturn("M67890");
        when(mockResultSet.getString("meter_location")).thenReturn("Inside").thenReturn("Outside");
        when(mockResultSet.getString("meter_type")).thenReturn("Electric").thenReturn("Solar");
        when(mockResultSet.getString("phase_code")).thenReturn("PC001").thenReturn("PC002");
        when(mockResultSet.getString("bill_type")).thenReturn("Normal").thenReturn("Industrial");
        when(mockResultSet.getInt("days")).thenReturn(30).thenReturn(31);
        
        // Act
        List<Meter> results = meterDAO.getAllMeters();
        
        // Assert
        assertNotNull("Meter list should not be null", results);
        assertEquals("Meter list should have 2 items", 2, results.size());
        assertEquals("First meter number should be M12345", "M12345", results.get(0).getMeterNumber());
        assertEquals("Second meter number should be M67890", "M67890", results.get(1).getMeterNumber());
        
        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery(contains("SELECT * FROM meter"));
    }
    
    @Test
    public void testGetAllMetersSQLException() throws SQLException {
        // Arrange
        when(mockStatement.executeQuery(anyString())).thenThrow(new SQLException("Test exception"));
        
        // Act
        List<Meter> results = meterDAO.getAllMeters();
        
        // Assert
        assertNotNull("Meter list should not be null on exception", results);
        assertTrue("Meter list should be empty on exception", results.isEmpty());
    }
    
    @Test
    public void testGetMetersByLocation() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // One result
        when(mockResultSet.getString("meter_no")).thenReturn(testMeter.getMeterNumber());
        when(mockResultSet.getString("meter_location")).thenReturn(testMeter.getMeterLocation());
        when(mockResultSet.getString("meter_type")).thenReturn(testMeter.getMeterType());
        when(mockResultSet.getString("phase_code")).thenReturn(testMeter.getPhaseCode());
        when(mockResultSet.getString("bill_type")).thenReturn(testMeter.getBillType());
        when(mockResultSet.getInt("days")).thenReturn(testMeter.getDays());
        
        // Act
        List<Meter> results = meterDAO.getMetersByLocation(testMeter.getMeterLocation());
        
        // Assert
        assertNotNull("Meter list should not be null", results);
        assertEquals("Meter list should have 1 item", 1, results.size());
        assertEquals("Meter location should match", testMeter.getMeterLocation(), results.get(0).getMeterLocation());
        
        verify(mockConnection).prepareStatement(contains("SELECT * FROM meter WHERE meter_location = ?"));
        verify(mockPreparedStatement).setString(1, testMeter.getMeterLocation());
    }
    
    @Test
    public void testUpdateMeter() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 row affected
        
        // Act
        boolean result = meterDAO.updateMeter(testMeter);
        
        // Assert
        assertTrue("Updating meter should return true for success", result);
        verify(mockConnection).prepareStatement(contains("UPDATE meter SET"));
        verify(mockPreparedStatement).setString(1, testMeter.getMeterLocation());
        verify(mockPreparedStatement).setString(2, testMeter.getMeterType());
        verify(mockPreparedStatement).setString(3, testMeter.getPhaseCode());
        verify(mockPreparedStatement).setString(4, testMeter.getBillType());
        verify(mockPreparedStatement).setInt(5, testMeter.getDays());
        verify(mockPreparedStatement).setString(6, testMeter.getMeterNumber());
        verify(mockPreparedStatement).executeUpdate();
    }
    
    @Test
    public void testUpdateMeterFailure() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // 0 rows affected
        
        // Act
        boolean result = meterDAO.updateMeter(testMeter);
        
        // Assert
        assertFalse("Updating meter should return false for failure", result);
    }
}