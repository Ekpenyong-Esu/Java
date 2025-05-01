package Electricity.controller;

import Electricity.dataAccessOutput.BillDAO;
import Electricity.dataAccessOutput.CustomerDAO;
import Electricity.model.Bill;
import Electricity.model.Customer;
import Electricity.model.Meter;
import Electricity.model.Tax;
import java.util.List;
import java.util.ArrayList;

/**
 * Controller class for bill management operations.
 * Handles the business logic for bill calculations, generation, and payment.
 */
public class BillController {

    private final BillDAO billDAO;
    private final CustomerDAO customerDAO;

    /**
     * Constructor initializing required DAOs
     */
    public BillController() {
        billDAO = new BillDAO();
        customerDAO = new CustomerDAO();
    }

    /**
     * Calculate bill amount based on units consumed
     * @param units Number of units consumed
     * @return Calculated bill amount
     */
    public double calculateBillAmount(int units) {
        if (units < 0) {
            return 0.0;
        }

        Tax tax = billDAO.getTaxRates();
        if (tax == null) {
            // Default calculations if tax rates not found
            double billAmount = units * 9.0; // Default cost per unit
            billAmount += 47.0; // Default meter rent
            billAmount += 22.0; // Default service charge
            return billAmount;
        }

        Bill bill = new Bill();
        bill.setUnits(units);
        return bill.calculateBillAmount(tax);
    }

    /**
     * Generate a new bill for a customer
     * @param meter Customer meter number
     * @param month Billing month
     * @param units Units consumed
     * @return true if bill generated successfully, false otherwise
     */
    public boolean generateBill(String meter, String month, int units) {
        // Input validation
        if (meter == null || meter.trim().isEmpty() ||
                month == null || month.trim().isEmpty() ||
                units < 0) {
            return false;
        }

        // Verify customer exists
        Customer customer = customerDAO.getCustomerByMeter(meter);
        if (customer == null) {
            return false;
        }

        // Check if bill already exists for this meter and month
        Bill existingBill = billDAO.getBill(meter, month);
        if (existingBill != null) {
            return false; // Bill already exists
        }

        // Calculate bill amount
        double totalBill = calculateBillAmount(units);

        // Create bill object
        Bill bill = new Bill(meter, month, units, totalBill, "Not Paid");

        return billDAO.saveBill(bill);
    }

    /**
     * Pay bill for a customer
     * @param meter Customer meter number
     * @param month Billing month
     * @return true if payment successful, false otherwise
     */
    public boolean payBill(String meter, String month) {
        if (meter == null || meter.trim().isEmpty() ||
                month == null || month.trim().isEmpty()) {
            return false;
        }

        return billDAO.updateBillStatus(meter, month, "Paid");
    }

    /**
     * Get bill for a specific meter and month
     * @param meter Customer meter number
     * @param month Billing month
     * @return Bill object if found, null otherwise
     */
    public Bill getBill(String meter, String month) {
        if (meter == null || meter.trim().isEmpty() ||
                month == null || month.trim().isEmpty()) {
            return null;
        }

        return billDAO.getBill(meter, month);
    }

    /**
     * Get all bills for a customer
     * @param meter Customer meter number
     * @return List of bills for the customer
     */
    public List<Bill> getBillsByMeter(String meter) {
        if (meter == null || meter.trim().isEmpty()) {
            return null;
        }

        return billDAO.getBillsByMeter(meter);
    }

    /**
     * Get current tax rates used for bill calculations
     * @return Tax object with current rates
     */
    public Tax getTaxRates() {
        return billDAO.getTaxRates();
    }

    /**
     * Get all bills for a specific month
     * @param month Month to get bills for
     * @return List of bills for the month
     */
    public List<Bill> getBillsByMonth(String month) {
        if (month == null || month.trim().isEmpty()) {
            return null;
        }

        return billDAO.getBillsByMonth(month);
    }

    /**
     * Get bill details in a formatted string for printing
     * @param meter Customer meter number
     * @param month Billing month
     * @return Formatted string with bill details
     */
    public String getBillDetails(String meter, String month) {
        if (meter == null || meter.trim().isEmpty() ||
                month == null || month.trim().isEmpty()) {
            return "Invalid meter number or month";
        }

        Bill bill = billDAO.getBill(meter, month);
        if (bill == null) {
            return "No bill found for meter " + meter + " for month " + month;
        }

        Customer customer = customerDAO.getCustomerByMeter(meter);
        if (customer == null) {
            return "Customer not found for meter " + meter;
        }

        Tax tax = billDAO.getTaxRates();
        if (tax == null) {
            return "Error retrieving tax rates";
        }

        StringBuilder details = new StringBuilder();
        details.append("ELECTRICITY BILL GENERATED FOR MONTH OF ").append(month.toUpperCase()).append("\n\n");
        details.append("Customer Name: ").append(customer.getName()).append("\n");
        details.append("Meter Number: ").append(meter).append("\n");
        details.append("Address: ").append(customer.getAddress()).append("\n");
        details.append("City: ").append(customer.getCity()).append("\n\n");
        details.append("Units Consumed: ").append(bill.getUnits()).append("\n");
        details.append("Cost per Unit: Rs ").append(tax.getCostPerUnit()).append("\n");
        details.append("Meter Rent: Rs ").append(tax.getMeterRent()).append("\n");
        details.append("Service Charge: Rs ").append(tax.getServiceCharge()).append("\n");
        details.append("Service Tax: ").append(tax.getServiceTax()).append("%\n");
        details.append("Swachh Bharat Cess: ").append(tax.getClimateChangeLevy()).append("%\n");
        details.append("Fixed Tax: Rs ").append(tax.getFixedTax()).append("\n\n");
        details.append("TOTAL BILL: Rs ").append(String.format("%.2f", bill.getTotalBill())).append("\n");
        details.append("Status: ").append(bill.getStatus());

        return details.toString();
    }

    /**
     * Update bill status (e.g., mark as paid or unpaid)
     * @param meter Customer meter number
     * @param month Billing month
     * @param status New status (e.g., "Paid", "Not Paid")
     * @return true if update successful, false otherwise
     */
    public boolean updateBillStatus(String meter, String month, String status) {
        if (meter == null || meter.trim().isEmpty() ||
                month == null || month.trim().isEmpty() ||
                status == null || status.trim().isEmpty()) {
            return false;
        }

        return billDAO.updateBillStatus(meter, month, status);
    }

    /**
     * Get all bills for reports
     * 
     * @return List of bills
     */
    public List<Bill> getAllBills() {
        return billDAO.getAllBills();
    }
    
    /**
     * Get all meter numbers from customer records
     * Used for UI components like dropdowns
     * 
     * @return List of meter numbers
     */
    public List<String> getAllMeterNumbers() {
        return customerDAO.getAllMeterNumbers();
    }
    
    /**
     * Get bills matching specific criteria as data for display in UI
     * 
     * @param meter Meter number or null for all meters
     * @param month Month or null for all months
     * @return List of bills matching the criteria
     */
    public List<Bill> getBillsForDisplay(String meter, String month) {
        if (meter != null && !meter.trim().isEmpty()) {
            if (month != null && !month.trim().isEmpty()) {
                // Both meter and month specified
                Bill bill = billDAO.getBill(meter, month);
                List<Bill> result = new ArrayList<>();
                if (bill != null) {
                    result.add(bill);
                }
                return result;
            } else {
                // Only meter specified
                return billDAO.getBillsByMeter(meter);
            }
        } else if (month != null && !month.trim().isEmpty()) {
            // Only month specified
            return billDAO.getBillsByMonth(month);
        } else {
            // No filters
            return billDAO.getAllBills();
        }
    }
    
    /**
     * Get bills formatted for UI display with additional information
     * This provides a more proper MVC implementation by preparing the data
     * for the view instead of letting the view access model properties directly
     * 
     * @param meter Meter number
     * @return Array of formatted display data for the UI table
     */
    public List<BillDisplayData> getBillDisplayData(String meter) {
        List<Bill> bills = getBillsByMeter(meter);
        List<BillDisplayData> displayData = new ArrayList<>();
        
        if (bills == null) {
            return displayData;
        }
        
        for (Bill bill : bills) {
            BillDisplayData data = new BillDisplayData();
            data.meterNumber = bill.getMeter();
            data.month = bill.getMonth();
            data.units = bill.getUnits();
            data.totalBill = String.format("%.2f", bill.getTotalBill());
            data.status = bill.getStatus();
            displayData.add(data);
        }
        
        return displayData;
    }
    
    /**
     * Inner class to represent bill data formatted for display
     * This creates a separation between the model and the view
     */
    public static class BillDisplayData {
        public String meterNumber;
        public String month;
        public int units;
        public String totalBill;
        public String status;
    }
    
    /**
     * Get comprehensive bill report data for a specific meter and month.
     * This combines data from Bill, Customer, and Meter entities for report display.
     * 
     * @param meter Customer meter number
     * @param month Billing month
     * @return BillReportData object or null if not found
     */
    public BillReportData getBillReport(String meter, String month) {
        if (meter == null || meter.trim().isEmpty() ||
                month == null || month.trim().isEmpty()) {
            return null;
        }

        // Get bill information
        Bill bill = billDAO.getBill(meter, month);
        if (bill == null) {
            return null;
        }

        // Get customer information
        Customer customer = customerDAO.getCustomerByMeter(meter);
        if (customer == null) {
            return null;
        }
        
        // Get meter information using MeterController
        MeterController meterController = new MeterController();
        Meter meterInfo = meterController.getMeterInfo(meter);
        if (meterInfo == null) {
            return null;
        }

        // Get tax information
        Tax tax = billDAO.getTaxRates();
        if (tax == null) {
            return null;
        }

        // Create and populate report data object
        BillReportData reportData = new BillReportData();
        
        // Customer information
        reportData.customerName = customer.getName();
        reportData.meterNumber = meter;
        reportData.address = customer.getAddress();
        reportData.city = customer.getCity();
        reportData.state = customer.getState();
        reportData.email = customer.getEmail();
        reportData.phoneNumber = customer.getPhone();
        
        // Meter information
        reportData.meterLocation = meterInfo.getMeterLocation();
        reportData.meterType = meterInfo.getMeterType();
        reportData.phaseCode = meterInfo.getPhaseCode();
        reportData.billType = meterInfo.getBillType();
        reportData.days = meterInfo.getDays();
        
        // Bill information
        reportData.month = month;
        reportData.unitsConsumed = bill.getUnits();
        reportData.totalBill = bill.getTotalBill();
        reportData.status = bill.getStatus();
        
        // Tax information
        reportData.costPerUnit = tax.getCostPerUnit();
        reportData.meterRent = tax.getMeterRent();
        reportData.serviceCharge = tax.getServiceCharge();
        reportData.serviceTax = tax.getServiceTax();
        reportData.climateLevy = tax.getClimateChangeLevy();
        reportData.fixedTax = tax.getFixedTax();
        
        reportData.totalPayable = bill.getTotalBill();
        reportData.year = "2025";  // Use current year
        
        return reportData;
    }
    
    /**
     * Inner class to represent complete bill report data for display
     * Combines information from multiple models for a comprehensive bill report
     */
    public static class BillReportData {
        // Customer information
        public String customerName;
        public String meterNumber;
        public String address;
        public String city;
        public String state;
        public String email;
        public String phoneNumber;
        
        // Meter information
        public String meterLocation;
        public String meterType;
        public String phaseCode;
        public String billType;
        public int days;
        
        // Bill information
        public String month;
        public String year;
        public int unitsConsumed;
        public double totalBill;
        public String status;
        
        // Tax information
        public double costPerUnit;
        public double meterRent;
        public double serviceCharge;
        public double serviceTax;
        public double climateLevy;
        public double fixedTax;
        
        public double totalPayable;
    }
    
    /**
     * Process payment for a specific meter and month
     * @param meter Customer meter number
     * @param month Billing month
     * @param amount Payment amount
     * @return true if payment is successful, false otherwise
     */
    public boolean processPayment(String meter, String month, double amount) {
        if (meter == null || meter.trim().isEmpty() ||
                month == null || month.trim().isEmpty() ||
                amount <= 0) {
            return false;
        }

        // Update bill status to "Paid"
        return updateBillStatus(meter, month, "Paid");
    }
    
    /**
     * Get all unpaid bills
     * @return List of bills with "UNPAID" status
     */
    public List<Bill> getUnpaidBills() {
        return billDAO.getUnpaidBills();
    }
    
    /**
     * Get unpaid bills formatted for UI display
     * @return List of formatted display data for unpaid bills
     */
    public List<BillDisplayData> getUnpaidBillsDisplayData() {
        List<Bill> unpaidBills = getUnpaidBills();
        List<BillDisplayData> displayData = new ArrayList<>();
        
        if (unpaidBills == null) {
            return displayData;
        }
        
        for (Bill bill : unpaidBills) {
            BillDisplayData data = new BillDisplayData();
            data.meterNumber = bill.getMeter();
            data.month = bill.getMonth();
            data.units = bill.getUnits();
            data.totalBill = String.format("%.2f", bill.getTotalBill());
            data.status = bill.getStatus();
            displayData.add(data);
        }
        
        return displayData;
    }
}