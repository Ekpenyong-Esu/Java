package Electricity.view.reports;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Electricity.controller.BillController;

/**
 * View class for generating bill reports.
 * Uses MVC architecture with BillController for generating comprehensive bill reports.
 */
public class GenerateBill extends JFrame implements ActionListener {
    // Constants for UI
    private static final int FORM_WIDTH = 500;
    private static final int FORM_HEIGHT = 900;
    private static final Font BILL_FONT = new Font("Senserif", Font.ITALIC, 18);
    
    // UI components grouped by function
    // - Labels
    private JLabel titleLabel;
    private JLabel meterNumberLabel;
    
    // - Input and display components
    private JTextArea billTextArea;
    private Choice monthChoice;
    
    // - Buttons
    private JButton generateButton;
    
    // - Panels
    private JPanel headerPanel;
    
    // Data
    private String meterNumber;
    
    // Controller
    private BillController billController;

    /**
     * Constructor to initialize the view
     * @param meterNumber The meter number to generate bill for
     */
    public GenerateBill(String meterNumber) {
        super("Generate Bill");
        this.meterNumber = meterNumber;
        
        // Initialize controller
        initializeController();
        
        // Setup UI components
        initializeWindowSettings();
        initializeHeaderPanel();
        initializeBillTextArea();
    }
    
    /**
     * Initialize controller
     */
    private void initializeController() {
        billController = new BillController();
    }
    
    /**
     * Initialize window settings
     */
    private void initializeWindowSettings() {
        setSize(FORM_WIDTH, FORM_HEIGHT);
        setLayout(new BorderLayout());
        setLocation(350, 40);
    }
    
    /**
     * Initialize the header panel with controls
     */
    private void initializeHeaderPanel() {
        // Create panel
        headerPanel = new JPanel();
        
        // Add title label
        titleLabel = new JLabel("Generate Bill");
        headerPanel.add(titleLabel);
        
        // Add meter number label
        meterNumberLabel = new JLabel(meterNumber);
        headerPanel.add(meterNumberLabel);
        
        // Add month selection
        monthChoice = new Choice();
        populateMonths();
        headerPanel.add(monthChoice);
        
        // Add generate button
        generateButton = new JButton("Generate Bill");
        generateButton.addActionListener(this);
        headerPanel.add(generateButton);
        
        // Add panel to frame
        add(headerPanel, BorderLayout.NORTH);
    }
    
    /**
     * Initialize bill text area with scrolling
     */
    private void initializeBillTextArea() {
        billTextArea = new JTextArea(50, 15);
        billTextArea.setFont(BILL_FONT);
        
        JScrollPane scrollPane = new JScrollPane(billTextArea);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Populate months in the dropdown
     */
    private void populateMonths() {
        String[] months = {
            "January", "February", "March", "April", "May", "June", 
            "July", "August", "September", "October", "November", "December"
        };
        
        for (String month : months) {
            monthChoice.add(month);
        }
    }

    /**
     * Handle button click events
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == generateButton) {
            generateBillReport();
        }
    }
    
    /**
     * Generate bill report for the selected month
     */
    private void generateBillReport() {
        try {
            String month = monthChoice.getSelectedItem();
            
            // Get comprehensive bill report data using controller
            BillController.BillReportData report = billController.getBillReport(meterNumber, month);

            if (report != null) {
                billTextArea.setText(formatBillReport(month, report));
            } else {
                showNoBillMessage(month);
            }
        } catch (Exception e) {
            showErrorMessage(e);
        }
    }
    
    /**
     * Format bill report data into a nicely formatted string
     * 
     * @param month Selected month
     * @param report Bill report data
     * @return Formatted bill text
     */
    private String formatBillReport(String month, BillController.BillReportData report) {
        StringBuilder billText = new StringBuilder();
        
        // Header
        billText.append("\tReliance Power Limited\n");
        billText.append("ELECTRICITY BILL FOR THE MONTH OF ")
                .append(month.toUpperCase())
                .append(", ")
                .append(report.year)
                .append("\n\n");
        
        // Customer information
        appendCustomerInfo(billText, report);
        billText.append("\n--------------------------------------------------------------");
        
        // Meter information
        appendMeterInfo(billText, report);
        billText.append("\n");
        
        // Billing information
        appendBillingInfo(billText, report);
        billText.append("\n---------------------------------------------------------------");
        
        // Total
        billText.append("\n    TOTAL PAYABLE: Rs ")
                .append(String.format("%.2f", report.totalPayable));
        billText.append("\n    Status: ")
                .append(report.status);
        
        return billText.toString();
    }
    
    /**
     * Append customer information to bill text
     * 
     * @param billText StringBuilder for the bill text
     * @param report Bill report data
     */
    private void appendCustomerInfo(StringBuilder billText, BillController.BillReportData report) {
        billText.append("\n    Customer Name: ").append(report.customerName);
        billText.append("\n    Meter Number: ").append(report.meterNumber);
        billText.append("\n    Address: ").append(report.address);
        billText.append("\n    State: ").append(report.state);
        billText.append("\n    City: ").append(report.city);
        billText.append("\n    Email: ").append(report.email);
        billText.append("\n    Phone Number: ").append(report.phoneNumber);
    }
    
    /**
     * Append meter information to bill text
     * 
     * @param billText StringBuilder for the bill text
     * @param report Bill report data
     */
    private void appendMeterInfo(StringBuilder billText, BillController.BillReportData report) {
        billText.append("\n    Meter Location: ").append(report.meterLocation);
        billText.append("\n    Meter Type: ").append(report.meterType);
        billText.append("\n    Phase Code: ").append(report.phaseCode);
        billText.append("\n    Bill Type: ").append(report.billType);
        billText.append("\n    Days: ").append(report.days);
    }
    
    /**
     * Append billing information to bill text
     * 
     * @param billText StringBuilder for the bill text
     * @param report Bill report data
     */
    private void appendBillingInfo(StringBuilder billText, BillController.BillReportData report) {
        billText.append("\n    Units Consumed: ").append(report.unitsConsumed);
        billText.append("\n    Cost per Unit: Rs ").append(String.format("%.2f", report.costPerUnit));
        billText.append("\n    Meter Rent: Rs ").append(String.format("%.2f", report.meterRent));
        billText.append("\n    Service Charge: Rs ").append(String.format("%.2f", report.serviceCharge));
        
        // Calculate taxes
        double serviceTaxAmount = report.totalBill * report.serviceTax / 100;
        double climateLevyAmount = report.totalBill * report.climateLevy / 100;
        
        billText.append("\n    Service Tax (")
                .append(String.format("%.1f", report.serviceTax))
                .append("%): Rs ")
                .append(String.format("%.2f", serviceTaxAmount));
        
        billText.append("\n    Climate Levy (")
                .append(String.format("%.1f", report.climateLevy))
                .append("%): Rs ")
                .append(String.format("%.2f", climateLevyAmount));
                
        billText.append("\n    Fixed Tax: Rs ").append(String.format("%.2f", report.fixedTax));
    }
    
    /**
     * Show message when no bill is found
     * 
     * @param month Selected month
     */
    private void showNoBillMessage(String month) {
        billTextArea.setText("No bill found for meter " + meterNumber + 
                " for the month of " + month);
    }
    
    /**
     * Show error message in the text area
     * 
     * @param e Exception that occurred
     */
    private void showErrorMessage(Exception e) {
        billTextArea.setText("Error generating bill: " + e.getMessage());
        e.printStackTrace();
    }
    
    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        new GenerateBill("").setVisible(true);
    }
}