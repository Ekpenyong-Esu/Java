package Electricity.controller;

import Electricity.dataAccessOutput.BillDAO;
import Electricity.dataAccessOutput.TaxDAO;
import Electricity.model.Bill;
import Electricity.model.Tax;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BillControllerTest {

    @Mock
    private BillDAO billDAO;
    
    @Mock
    private TaxDAO taxDAO;

    private BillController billController;
    private Bill testBill;
    private Tax testTax;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Create a controller with a mocked DAOs
        billController = new BillController();
        
        // Use reflection to replace the DAOs with our mocks
        try {
            java.lang.reflect.Field billDaoField = BillController.class.getDeclaredField("billDAO");
            billDaoField.setAccessible(true);
            billDaoField.set(billController, billDAO);
            
            java.lang.reflect.Field taxDaoField = BillController.class.getDeclaredField("taxDAO");
            taxDaoField.setAccessible(true);
            taxDaoField.set(billController, taxDAO);
        } catch (Exception e) {
            fail("Failed to inject mock DAOs: " + e.getMessage());
        }
        
        // Setup test data
        testBill = new Bill("M12345", "January", 100, 950.50, "UNPAID");
        
        testTax = new Tax();
        testTax.setCostPerUnit(9.0);
        testTax.setMeterRent(47.0);
        testTax.setServiceCharge(22.0);
        testTax.setServiceTax(9.0);
        testTax.setClimateChangeLevy(5.0);
        testTax.setFixedTax(18.0);
    }

    @Test
    public void testGenerateBill() {
        // Configure mocks
        when(taxDAO.getCurrentTax()).thenReturn(testTax);
        when(billDAO.addBill(any(Bill.class))).thenReturn(true);
        
        // Call the controller method
        boolean result = billController.generateBill("M12345", "January", 100);
        
        // Verify the result and DAO interactions
        assertTrue("Bill should be generated successfully", result);
        verify(taxDAO).getCurrentTax();
        verify(billDAO).addBill(any(Bill.class));
    }
    
    @Test
    public void testGenerateBillWithInvalidData() {
        // Call with invalid data (null meter)
        boolean result = billController.generateBill(null, "January", 100);
        
        // Verify the result
        assertFalse("Generating bill with null meter should fail", result);
        verify(billDAO, never()).addBill(any(Bill.class));
    }
    
    @Test
    public void testGetBill() {
        // Configure mock
        when(billDAO.getBill("M12345", "January")).thenReturn(testBill);
        
        // Call the controller method
        Bill result = billController.getBill("M12345", "January");
        
        // Verify the result and DAO interaction
        assertNotNull("Retrieved bill should not be null", result);
        assertEquals("Retrieved bill should match the expected one", testBill.getMeter(), result.getMeter());
        assertEquals("Retrieved bill month should match", testBill.getMonth(), result.getMonth());
        verify(billDAO).getBill("M12345", "January");
    }
    
    @Test
    public void testGetBillWithInvalidData() {
        // Configure mock
        when(billDAO.getBill("INVALID", "January")).thenReturn(null);
        
        // Call the controller method
        Bill result = billController.getBill("INVALID", "January");
        
        // Verify the result
        assertNull("Bill with invalid meter should not be found", result);
        verify(billDAO).getBill("INVALID", "January");
    }
    
    @Test
    public void testGetBillsByMeter() {
        // Create a list of bills
        List<Bill> bills = new ArrayList<>();
        bills.add(testBill);
        bills.add(new Bill("M12345", "February", 120, 1100.60, "UNPAID"));
        
        // Configure mock
        when(billDAO.getBillsByMeter("M12345")).thenReturn(bills);
        
        // Call the controller method
        List<Bill> result = billController.getBillsByMeter("M12345");
        
        // Verify the result and DAO interaction
        assertNotNull("Bill list should not be null", result);
        assertEquals("Bill list size should match", 2, result.size());
        assertEquals("First bill month should match", "January", result.get(0).getMonth());
        verify(billDAO).getBillsByMeter("M12345");
    }
    
    @Test
    public void testProcessPayment() {
        // Configure mock
        when(billDAO.updateBillStatus("M12345", "January", "PAID")).thenReturn(true);
        
        // Call the controller method
        boolean result = billController.processPayment("M12345", "January", 950.50);
        
        // Verify the result and DAO interaction
        assertTrue("Payment should be processed successfully", result);
        verify(billDAO).updateBillStatus("M12345", "January", "PAID");
    }
    
    @Test
    public void testProcessPaymentWithInvalidData() {
        // Call with invalid data (null meter)
        boolean result = billController.processPayment(null, "January", 950.50);
        
        // Verify the result
        assertFalse("Processing payment with null meter should fail", result);
        verify(billDAO, never()).updateBillStatus(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testCalculateBillAmount() {
        // Configure mock
        when(taxDAO.getCurrentTax()).thenReturn(testTax);
        
        // Call the controller method
        double result = billController.calculateBillAmount(100);
        
        // Calculate expected bill amount based on tax values
        double expectedAmount = 100 * testTax.getCostPerUnit() + 
                               testTax.getMeterRent() + 
                               testTax.getServiceCharge() +
                               testTax.getServiceTax() +
                               testTax.getClimateChangeLevy() +
                               testTax.getFixedTax();
        
        // Verify the result and DAO interaction
        assertEquals("Bill amount should match expected calculation", expectedAmount, result, 0.001);
        verify(taxDAO).getCurrentTax();
    }
}