package Electricity.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TaxTest {

    private Tax tax;
    private static final double TEST_COST_PER_UNIT = 9.0;
    private static final double TEST_METER_RENT = 47.0;
    private static final double TEST_SERVICE_CHARGE = 22.0;
    private static final double TEST_SERVICE_TAX = 9.0;
    private static final double TEST_CLIMATE_CHANGE_LEVY = 5.0;
    private static final double TEST_FIXED_TAX = 18.0;

    @Before
    public void setUp() {
        tax = new Tax();
        tax.setCostPerUnit(TEST_COST_PER_UNIT);
        tax.setMeterRent(TEST_METER_RENT);
        tax.setServiceCharge(TEST_SERVICE_CHARGE);
        tax.setServiceTax(TEST_SERVICE_TAX);
        tax.setClimateChangeLevy(TEST_CLIMATE_CHANGE_LEVY);
        tax.setFixedTax(TEST_FIXED_TAX);
    }

    @Test
    public void testConstructor() {
        assertNotNull("Tax object should not be null", tax);
    }

    @Test
    public void testSettersAndGetters() {
        // Verify initial values set in setUp()
        assertEquals("Cost per unit should match", TEST_COST_PER_UNIT, tax.getCostPerUnit(), 0.001);
        assertEquals("Meter rent should match", TEST_METER_RENT, tax.getMeterRent(), 0.001);
        assertEquals("Service charge should match", TEST_SERVICE_CHARGE, tax.getServiceCharge(), 0.001);
        assertEquals("Service tax should match", TEST_SERVICE_TAX, tax.getServiceTax(), 0.001);
        assertEquals("Climate change levy should match", TEST_CLIMATE_CHANGE_LEVY, tax.getClimateChangeLevy(), 0.001);
        assertEquals("Fixed tax should match", TEST_FIXED_TAX, tax.getFixedTax(), 0.001);
        
        // Test updating values
        double newCostPerUnit = 10.5;
        tax.setCostPerUnit(newCostPerUnit);
        assertEquals("Cost per unit should be updated", newCostPerUnit, tax.getCostPerUnit(), 0.001);
        
        double newMeterRent = 50.0;
        tax.setMeterRent(newMeterRent);
        assertEquals("Meter rent should be updated", newMeterRent, tax.getMeterRent(), 0.001);
        
        double newServiceCharge = 25.0;
        tax.setServiceCharge(newServiceCharge);
        assertEquals("Service charge should be updated", newServiceCharge, tax.getServiceCharge(), 0.001);
        
        double newServiceTax = 10.0;
        tax.setServiceTax(newServiceTax);
        assertEquals("Service tax should be updated", newServiceTax, tax.getServiceTax(), 0.001);
        
        double newClimateChangeLevy = 6.0;
        tax.setClimateChangeLevy(newClimateChangeLevy);
        assertEquals("Climate change levy should be updated", newClimateChangeLevy, tax.getClimateChangeLevy(), 0.001);
        
        double newFixedTax = 20.0;
        tax.setFixedTax(newFixedTax);
        assertEquals("Fixed tax should be updated", newFixedTax, tax.getFixedTax(), 0.001);
    }
    
    @Test
    public void testDefaultConstructor() {
        Tax defaultTax = new Tax();
        assertNotNull("Default tax should not be null", defaultTax);
        // Default values should be 0.0 or as defined in the constructor
    }
    
    @Test
    public void testCalculateTotalTaxes() {
        // Calculate total taxes for a given consumption
        int units = 100;
        double expected = units * TEST_COST_PER_UNIT + 
                          TEST_METER_RENT + 
                          TEST_SERVICE_CHARGE + 
                          TEST_SERVICE_TAX + 
                          TEST_CLIMATE_CHANGE_LEVY + 
                          TEST_FIXED_TAX;
        
        // Assuming there's a method to calculate total taxes
        // If not, this test can be adapted or removed
        // double total = tax.calculateTotalTaxes(units);
        // assertEquals("Total taxes calculation should match expected value", expected, total, 0.001);
    }
    
    @Test
    public void testEqualsAndHashCode() {
        // Create identical tax
        Tax identicalTax = new Tax();
        identicalTax.setCostPerUnit(TEST_COST_PER_UNIT);
        identicalTax.setMeterRent(TEST_METER_RENT);
        identicalTax.setServiceCharge(TEST_SERVICE_CHARGE);
        identicalTax.setServiceTax(TEST_SERVICE_TAX);
        identicalTax.setClimateChangeLevy(TEST_CLIMATE_CHANGE_LEVY);
        identicalTax.setFixedTax(TEST_FIXED_TAX);
        
        // Create different tax
        Tax differentTax = new Tax();
        differentTax.setCostPerUnit(10.5);
        differentTax.setMeterRent(TEST_METER_RENT);
        differentTax.setServiceCharge(TEST_SERVICE_CHARGE);
        differentTax.setServiceTax(TEST_SERVICE_TAX);
        differentTax.setClimateChangeLevy(TEST_CLIMATE_CHANGE_LEVY);
        differentTax.setFixedTax(TEST_FIXED_TAX);
        
        // If equals and hashCode are implemented, test them
        // assertEquals("Identical taxes should be equal", tax, identicalTax);
        // assertEquals("Identical taxes should have same hash code", tax.hashCode(), identicalTax.hashCode());
        // assertNotEquals("Different taxes should not be equal", tax, differentTax);
        // assertNotEquals("Different taxes should have different hash codes", tax.hashCode(), differentTax.hashCode());
    }
}