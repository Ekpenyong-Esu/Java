package Electricity.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MeterTest {

    private Meter meter;
    private static final String TEST_METER_NUMBER = "M12345";
    private static final String TEST_METER_LOCATION = "Inside";
    private static final String TEST_METER_TYPE = "Electric";
    private static final String TEST_PHASE_CODE = "PC001";
    private static final String TEST_BILL_TYPE = "Normal";
    private static final int TEST_DAYS = 30;

    @Before
    public void setUp() {
        meter = new Meter(TEST_METER_NUMBER, TEST_METER_LOCATION, TEST_METER_TYPE, 
                          TEST_PHASE_CODE, TEST_BILL_TYPE, TEST_DAYS);
    }

    @Test
    public void testMeterConstructor() {
        assertNotNull("Meter object should not be null", meter);
        assertEquals("Meter number should match constructor parameter", TEST_METER_NUMBER, meter.getMeterNumber());
        assertEquals("Meter location should match constructor parameter", TEST_METER_LOCATION, meter.getMeterLocation());
        assertEquals("Meter type should match constructor parameter", TEST_METER_TYPE, meter.getMeterType());
        assertEquals("Phase code should match constructor parameter", TEST_PHASE_CODE, meter.getPhaseCode());
        assertEquals("Bill type should match constructor parameter", TEST_BILL_TYPE, meter.getBillType());
        assertEquals("Days should match constructor parameter", TEST_DAYS, meter.getDays());
    }

    @Test
    public void testSettersAndGetters() {
        // Test setters
        String newMeterNumber = "M67890";
        meter.setMeterNumber(newMeterNumber);
        assertEquals("Meter number should be updated", newMeterNumber, meter.getMeterNumber());
        
        String newMeterLocation = "Outside";
        meter.setMeterLocation(newMeterLocation);
        assertEquals("Meter location should be updated", newMeterLocation, meter.getMeterLocation());
        
        String newMeterType = "Solar";
        meter.setMeterType(newMeterType);
        assertEquals("Meter type should be updated", newMeterType, meter.getMeterType());
        
        String newPhaseCode = "PC002";
        meter.setPhaseCode(newPhaseCode);
        assertEquals("Phase code should be updated", newPhaseCode, meter.getPhaseCode());
        
        String newBillType = "Industrial";
        meter.setBillType(newBillType);
        assertEquals("Bill type should be updated", newBillType, meter.getBillType());
        
        int newDays = 31;
        meter.setDays(newDays);
        assertEquals("Days should be updated", newDays, meter.getDays());
    }
    
    @Test
    public void testDefaultConstructor() {
        Meter defaultMeter = new Meter();
        assertNotNull("Default meter should not be null", defaultMeter);
        // Values should be null or default as set by the default constructor
    }
    
    @Test
    public void testEqualsAndHashCode() {
        // Create identical meter
        Meter identicalMeter = new Meter(TEST_METER_NUMBER, TEST_METER_LOCATION, TEST_METER_TYPE, 
                                       TEST_PHASE_CODE, TEST_BILL_TYPE, TEST_DAYS);
        
        // Create different meter
        Meter differentMeter = new Meter("M67890", TEST_METER_LOCATION, TEST_METER_TYPE, 
                                       TEST_PHASE_CODE, TEST_BILL_TYPE, TEST_DAYS);
        
        // Test equals and hashCode
        assertEquals("Identical meters should be equal", meter, identicalMeter);
        assertEquals("Identical meters should have same hash code", meter.hashCode(), identicalMeter.hashCode());
        assertNotEquals("Different meters should not be equal", meter, differentMeter);
        assertNotEquals("Different meters should have different hash codes", meter.hashCode(), differentMeter.hashCode());
        assertNotEquals("Meter should not equal null", meter, null);
        assertNotEquals("Meter should not equal object of different class", meter, "Not a Meter");
    }
}