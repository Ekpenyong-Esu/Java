package Electricity.controller;

import Electricity.dataAccessOutput.TaxDAO;
import Electricity.model.Tax;
import Electricity.util.ValidationUtil;

import java.util.List;

/**
 * Controller class to handle business logic for Tax operations.
 */
public class TaxController {
    
    private final TaxDAO taxDAO;
    
    /**
     * Constructor initializing the TaxDAO
     */
    public TaxController() {
        this.taxDAO = new TaxDAO();
    }
    
    /**
     * Creates a new tax rate entry
     * @param costPerUnit base cost per unit of electricity
     * @param meterRent monthly meter rent fee
     * @param serviceCharge service charge amount
     * @param serviceTax service tax percentage
     * @param climateChangeLevy climate change levy amount
     * @param fixedTax fixed tax amount
     * @return true if tax rates were saved successfully
     */
    public boolean createTax(double costPerUnit, double meterRent, double serviceCharge, 
                          double serviceTax, double climateChangeLevy, double fixedTax) {
        
        // Validate all inputs are non-negative
        if (!ValidationUtil.isPositiveNumber(costPerUnit) ||
            !ValidationUtil.isPositiveNumber(meterRent) ||
            !ValidationUtil.isPositiveNumber(serviceCharge) || 
            !ValidationUtil.isPositiveNumber(serviceTax) ||
            !ValidationUtil.isPositiveNumber(climateChangeLevy) ||
            !ValidationUtil.isPositiveNumber(fixedTax)) {
            return false;
        }
        
        Tax tax = new Tax(costPerUnit, meterRent, serviceCharge, serviceTax, climateChangeLevy, fixedTax);
        return taxDAO.saveTax(tax);
    }
    
    /**
     * Updates existing tax rates
     * @param tax Tax object with updated rates
     * @return true if update was successful
     */
    public boolean updateTaxRates(Tax tax) {
        // Validate all inputs
        if (tax == null || 
            !ValidationUtil.isPositiveNumber(tax.getCostPerUnit()) ||
            !ValidationUtil.isPositiveNumber(tax.getMeterRent()) ||
            !ValidationUtil.isPositiveNumber(tax.getServiceCharge()) || 
            !ValidationUtil.isPositiveNumber(tax.getServiceTax()) ||
            !ValidationUtil.isPositiveNumber(tax.getClimateChangeLevy()) ||
            !ValidationUtil.isPositiveNumber(tax.getFixedTax())) {
            return false;
        }
        
        return taxDAO.updateTax(tax);
    }
    
    /**
     * Gets the current tax rates from the database
     * @return Tax object containing current rates
     */
    public Tax getCurrentTaxRates() {
        return taxDAO.getCurrentTax();
    }
    
    /**
     * Gets a specific tax record by ID
     * @param id ID of the tax record
     * @return Tax object if found, null otherwise
     */
    public Tax getTaxById(int id) {
        if (id <= 0) {
            return null;
        }
        return taxDAO.getTaxById(id);
    }
    
    /**
     * Gets all historical tax rates
     * @return List of Tax objects ordered by recency
     */
    public List<Tax> getTaxHistory() {
        return taxDAO.getAllTaxes();
    }
    
    /**
     * Calculates the total tax amount for a given bill
     * @param unitsConsumed number of electricity units consumed
     * @return total calculated tax amount
     */
    public double calculateBillTax(double unitsConsumed) {
        Tax currentTax = taxDAO.getCurrentTax();
        
        // Basic calculation for total amount with taxes
        double energyCharges = unitsConsumed * currentTax.getCostPerUnit();
        double totalTaxes = currentTax.getMeterRent() + 
                           currentTax.getServiceCharge() +
                           (energyCharges * (currentTax.getServiceTax() / 100)) +
                           currentTax.getClimateChangeLevy() +
                           currentTax.getFixedTax();
        
        return totalTaxes;
    }
    
    /**
     * Calculates the total bill amount including base charges and taxes
     * @param unitsConsumed number of electricity units consumed
     * @return total bill amount
     */
    public double calculateTotalBill(double unitsConsumed) {
        Tax currentTax = taxDAO.getCurrentTax();
        
        // Calculate base energy charges
        double energyCharges = unitsConsumed * currentTax.getCostPerUnit();
        
        // Calculate taxes
        double totalTaxes = calculateBillTax(unitsConsumed);
        
        // Return total bill amount
        return energyCharges + totalTaxes;
    }
    
    /**
     * Deletes a tax entry by ID
     * @param id the ID of the tax entry to delete
     * @return true if successfully deleted
     */
    public boolean deleteTaxEntry(int id) {
        if (id <= 0) {
            return false;
        }
        return taxDAO.deleteTax(id);
    }
    
    /**
     * Get a breakdown of tax components for a bill
     * @param unitsConsumed number of electricity units consumed
     * @return array of tax component values [meterRent, serviceCharge, serviceTaxAmount, climateLevy, fixedTax]
     */
    public double[] getTaxBreakdown(double unitsConsumed) {
        Tax currentTax = taxDAO.getCurrentTax();
        double energyCharges = unitsConsumed * currentTax.getCostPerUnit();
        double serviceTaxAmount = energyCharges * (currentTax.getServiceTax() / 100);
        
        double[] breakdown = new double[5];
        breakdown[0] = currentTax.getMeterRent();
        breakdown[1] = currentTax.getServiceCharge();
        breakdown[2] = serviceTaxAmount;
        breakdown[3] = currentTax.getClimateChangeLevy();
        breakdown[4] = currentTax.getFixedTax();
        
        return breakdown;
    }
}