package Electricity.controller;

import Electricity.dataAccessOutput.TaxDAO;
import Electricity.model.Tax;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test class for TaxController functionality
 */
@RunWith(MockitoJUnitRunner.class)
public class TaxControllerTest {

    @Mock
    private TaxDAO taxDAO;

    @InjectMocks
    private TaxController taxController;

    private Tax sampleTax;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        // Create sample tax object for testing
        sampleTax = new Tax(9.5, 55.0, 22.5, 5.5, 1.25, 18.5);
        
        // Mock the getCurrentTax method to return our sample tax
        when(taxDAO.getCurrentTax()).thenReturn(sampleTax);
    }

    @Test
    public void testCreateTax() {
        // Arrange
        when(taxDAO.saveTax(any(Tax.class))).thenReturn(true);
        
        // Act
        boolean result = taxController.createTax(9.5, 55.0, 22.5, 5.5, 1.25, 18.5);
        
        // Assert
        assertTrue(result);
        verify(taxDAO, times(1)).saveTax(any(Tax.class));
    }

    @Test
    public void testCreateTaxWithInvalidValues() {
        // Act - test with negative value
        boolean result = taxController.createTax(-9.5, 55.0, 22.5, 5.5, 1.25, 18.5);
        
        // Assert
        assertFalse(result);
        // Verify that DAO was not called with invalid inputs
        verify(taxDAO, never()).saveTax(any(Tax.class));
    }

    @Test
    public void testUpdateTaxRates() {
        // Arrange
        when(taxDAO.updateTax(any(Tax.class))).thenReturn(true);
        
        // Act
        boolean result = taxController.updateTaxRates(sampleTax);
        
        // Assert
        assertTrue(result);
        verify(taxDAO, times(1)).updateTax(any(Tax.class));
    }

    @Test
    public void testGetCurrentTaxRates() {
        // Act
        Tax result = taxController.getCurrentTaxRates();
        
        // Assert
        assertEquals(sampleTax, result);
        verify(taxDAO, times(1)).getCurrentTax();
    }

    @Test
    public void testGetTaxHistory() {
        // Arrange
        List<Tax> taxHistory = new ArrayList<>();
        taxHistory.add(sampleTax);
        taxHistory.add(new Tax(9.0, 50.0, 20.0, 5.0, 1.0, 18.0));
        when(taxDAO.getAllTaxes()).thenReturn(taxHistory);
        
        // Act
        List<Tax> result = taxController.getTaxHistory();
        
        // Assert
        assertEquals(2, result.size());
        verify(taxDAO, times(1)).getAllTaxes();
    }

    @Test
    public void testCalculateBillTax() {
        // Arrange
        double unitsConsumed = 100.0;
        
        // Expected calculation:
        // Energy charges = 100 * 9.5 = 950.0
        // Total taxes = 55.0 (meter rent) + 22.5 (service charge) + 
        //               (950.0 * 5.5/100) (service tax) + 1.25 (climate change levy) + 18.5 (fixed tax)
        // = 55.0 + 22.5 + 52.25 + 1.25 + 18.5 = 149.5
        double expectedTax = 149.5;
        
        // Act
        double result = taxController.calculateBillTax(unitsConsumed);
        
        // Assert
        assertEquals(expectedTax, result, 0.01); // Using delta for double comparison
    }

    @Test
    public void testCalculateTotalBill() {
        // Arrange
        double unitsConsumed = 100.0;
        
        // Expected:
        // Energy charges = 100 * 9.5 = 950.0
        // Total taxes = 149.5 (from calculateBillTax test)
        // Total = 950.0 + 149.5 = 1099.5
        double expectedTotal = 1099.5;
        
        // Act
        double result = taxController.calculateTotalBill(unitsConsumed);
        
        // Assert
        assertEquals(expectedTotal, result, 0.01);
    }

    @Test
    public void testDeleteTaxEntry() {
        // Arrange
        int taxId = 1;
        when(taxDAO.deleteTax(taxId)).thenReturn(true);
        
        // Act
        boolean result = taxController.deleteTaxEntry(taxId);
        
        // Assert
        assertTrue(result);
        verify(taxDAO, times(1)).deleteTax(taxId);
    }
}