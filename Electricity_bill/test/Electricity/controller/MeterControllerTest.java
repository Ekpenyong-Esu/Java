package Electricity.controller;

import Electricity.dataAccessOutput.MeterDAO;
import Electricity.model.Meter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MeterControllerTest {

    @Mock
    private MeterDAO meterDAO;

    private MeterController meterController;
    private Meter testMeter;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Create a controller with a mocked DAO
        meterController = new MeterController();
        
        // Use reflection to replace the DAO with our mock
        try {
            java.lang.reflect.Field daoField = MeterController.class.getDeclaredField("meterDAO");
            daoField.setAccessible(true);
            daoField.set(meterController, meterDAO);
        } catch (Exception e) {
            fail("Failed to inject mock DAO: " + e.getMessage());
        }
        
        // Setup test meter data
        testMeter = new Meter("M12345", "Inside", "Electric", "PC001", "Normal", 30);
    }

    @Test
    public void testRegisterMeter() {
        // Configure mock to return true when addMeter is called
        when(meterDAO.addMeter(any(Meter.class))).thenReturn(true);
        
        // Call the controller method
        boolean result = meterController.registerMeter(
            testMeter.getMeterNumber(),
            testMeter.getMeterLocation(),
            testMeter.getMeterType(),
            testMeter.getPhaseCode(),
            testMeter.getBillType(),
            testMeter.getDays()
        );
        
        // Verify the result and DAO interaction
        assertTrue("Meter should be registered successfully", result);
        verify(meterDAO).addMeter(any(Meter.class));
    }
    
    @Test
    public void testRegisterMeterWithInvalidData() {
        // Call with invalid data (null meter number)
        boolean result = meterController.registerMeter(
            null,
            testMeter.getMeterLocation(),
            testMeter.getMeterType(),
            testMeter.getPhaseCode(),
            testMeter.getBillType(),
            testMeter.getDays()
        );
        
        // Verify the result
        assertFalse("Registering meter with null meter number should fail", result);
        verify(meterDAO, never()).addMeter(any(Meter.class));
    }
    
    @Test
    public void testGetMeterByNumber() {
        // Configure mock to return a meter when getMeterByNumber is called
        when(meterDAO.getMeterByNumber(testMeter.getMeterNumber())).thenReturn(testMeter);
        
        // Call the controller method
        Meter result = meterController.getMeterByNumber(testMeter.getMeterNumber());
        
        // Verify the result and DAO interaction
        assertNotNull("Retrieved meter should not be null", result);
        assertEquals("Retrieved meter should match the expected one", testMeter.getMeterNumber(), result.getMeterNumber());
        verify(meterDAO).getMeterByNumber(testMeter.getMeterNumber());
    }
    
    @Test
    public void testGetMeterByInvalidNumber() {
        // Configure mock to return null for a non-existent meter
        when(meterDAO.getMeterByNumber("INVALID")).thenReturn(null);
        
        // Call the controller method
        Meter result = meterController.getMeterByNumber("INVALID");
        
        // Verify the result and DAO interaction
        assertNull("Meter with invalid number should not be found", result);
        verify(meterDAO).getMeterByNumber("INVALID");
    }
    
    @Test
    public void testUpdateMeter() {
        // Configure mock to return true when updateMeter is called
        when(meterDAO.updateMeter(any(Meter.class))).thenReturn(true);
        
        // Call the controller method
        boolean result = meterController.updateMeter(
            testMeter.getMeterNumber(),
            testMeter.getMeterLocation(),
            testMeter.getMeterType(),
            testMeter.getPhaseCode(),
            testMeter.getBillType(),
            testMeter.getDays()
        );
        
        // Verify the result and DAO interaction
        assertTrue("Meter should be updated successfully", result);
        verify(meterDAO).updateMeter(any(Meter.class));
    }
    
    @Test
    public void testGetAllMeters() {
        // Create a list of meters to return
        List<Meter> meters = new ArrayList<>();
        meters.add(testMeter);
        meters.add(new Meter("M67890", "Outside", "Solar", "PC002", "Industrial", 30));
        
        // Configure mock to return the meter list
        when(meterDAO.getAllMeters()).thenReturn(meters);
        
        // Call the controller method
        List<Meter> result = meterController.getAllMeters();
        
        // Verify the result and DAO interaction
        assertNotNull("Meter list should not be null", result);
        assertEquals("Meter list size should match", 2, result.size());
        assertEquals("First meter number should match", "M12345", result.get(0).getMeterNumber());
        verify(meterDAO).getAllMeters();
    }
    
    @Test
    public void testGetMetersByLocation() {
        // Create a list of meters to return
        List<Meter> meters = new ArrayList<>();
        meters.add(testMeter);
        
        // Configure mock to return the meter list
        when(meterDAO.getMetersByLocation("Inside")).thenReturn(meters);
        
        // Call the controller method
        List<Meter> result = meterController.getMetersByLocation("Inside");
        
        // Verify the result and DAO interaction
        assertNotNull("Meter list should not be null", result);
        assertEquals("Meter list size should match", 1, result.size());
        assertEquals("Meter location should match", "Inside", result.get(0).getMeterLocation());
        verify(meterDAO).getMetersByLocation("Inside");
    }
}