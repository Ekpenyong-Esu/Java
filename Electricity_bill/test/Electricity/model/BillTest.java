package Electricity.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BillTest {
    
    private Bill bill;
    private static final String TEST_METER = "M12345";
    private static final String TEST_MONTH = "January";
    private static final int TEST_UNITS = 100;
    private static final double TEST_TOTAL_BILL = 950.50;
    private static final String TEST_STATUS = "UNPAID";
    
    @Before
    public void setUp() {
        bill = new Bill(TEST_METER, TEST_MONTH, TEST_UNITS, TEST_TOTAL_BILL, TEST_STATUS);
    }
    
    @Test
    public void testBillConstructor() {
        assertNotNull("Bill object should not be null", bill);
        assertEquals("Meter should match constructor parameter", TEST_METER, bill.getMeter());
        assertEquals("Month should match constructor parameter", TEST_MONTH, bill.getMonth());
        assertEquals("Units should match constructor parameter", TEST_UNITS, bill.getUnits());
        assertEquals("Total bill should match constructor parameter", TEST_TOTAL_BILL, bill.getTotalBill(), 0.001);
        assertEquals("Status should match constructor parameter", TEST_STATUS, bill.getStatus());
    }
    
    @Test
    public void testSettersAndGetters() {
        // Test setters
        String newMeter = "M67890";
        bill.setMeter(newMeter);
        assertEquals("Meter should be updated", newMeter, bill.getMeter());
        
        String newMonth = "February";
        bill.setMonth(newMonth);
        assertEquals("Month should be updated", newMonth, bill.getMonth());
        
        int newUnits = 200;
        bill.setUnits(newUnits);
        assertEquals("Units should be updated", newUnits, bill.getUnits());
        
        double newTotalBill = 1850.75;
        bill.setTotalBill(newTotalBill);
        assertEquals("Total bill should be updated", newTotalBill, bill.getTotalBill(), 0.001);
        
        String newStatus = "PAID";
        bill.setStatus(newStatus);
        assertEquals("Status should be updated", newStatus, bill.getStatus());
    }
    
    @Test
    public void testDefaultConstructor() {
        Bill defaultBill = new Bill();
        assertNotNull("Default bill should not be null", defaultBill);
        // Values should be null, 0, or default as set by the default constructor
    }
    
    @Test
    public void testCalculateBillAmount() {
        // Assuming Tax constants for calculation
        Tax tax = new Tax();
        tax.setCostPerUnit(9.0);
        tax.setMeterRent(47.0);
        tax.setServiceCharge(22.0);
        tax.setServiceTax(9.0);
        tax.setClimateChangeLevy(5.0);
        tax.setFixedTax(18.0);
        
        int units = 100;
        double costPerUnit = tax.getCostPerUnit();
        double meterRent = tax.getMeterRent();
        double serviceCharge = tax.getServiceCharge();
        double serviceTax = tax.getServiceTax();
        double climateLevy = tax.getClimateChangeLevy();
        double fixedTax = tax.getFixedTax();
        
        // Manual calculation based on formula
        double expectedBill = units * costPerUnit + meterRent + serviceCharge + 
                             serviceTax + climateLevy + fixedTax;
        
        // Verify if this calculation matches your actual calculation in the code
        // This is a placeholder test that you may need to adapt based on your actual implementation
        Bill testBill = new Bill("M12345", "January", units, 0.0, "UNPAID");
        // Assuming there's a calculateBill method, which might not exist in your actual code
        // double calculatedBill = testBill.calculateBill(tax);
        // assertEquals("Bill calculation should match expected formula", expectedBill, calculatedBill, 0.001);
        
        // If there's no calculateBill method, this test can be modified to test the controller instead
    }
}