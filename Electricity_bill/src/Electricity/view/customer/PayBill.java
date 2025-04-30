package Electricity.view.customer;

import Electricity.controller.BillController;
import Electricity.controller.CustomerController;
import Electricity.model.Bill;
import Electricity.model.Customer;
import Electricity.util.ValidationUtil;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * PayBill view component for bill payment.
 * Used by customers to pay their electricity bills.
 * Uses MVC architecture with BillController for payment processing.
 */
public class PayBill extends BaseCustomerView implements ActionListener {
    // Constants for UI
    private static final int FORM_WIDTH = 900;
    private static final int FORM_HEIGHT = 600;
    private static final int LABEL_X = 35;
    private static final int VALUE_X = 300;
    
    // UI components grouped by function
    // - Labels for field names
    private JLabel meterNumberLabel;
    private JLabel nameLabel;
    private JLabel monthLabel;
    private JLabel unitsLabel;
    private JLabel totalBillLabel;
    private JLabel paymentStatusLabel;
    
    // - Labels for field values
    private JLabel meterNumberValueLabel;
    private JLabel nameValueLabel;
    private JLabel unitsValueLabel;
    private JLabel totalBillValueLabel;
    private JLabel paymentStatusValueLabel;
    
    // - Input fields
    private Choice monthChoice;
    
    // - Buttons
    private JButton payButton;
    private JButton backButton;
    
    // Controllers
    private CustomerController customerController;
    private BillController billController;

    /**
     * Constructor to initialize the view
     * @param meterNumber The meter number of the customer
     */
    public PayBill(String meterNumber) {
        super("Pay Bill", meterNumber);

        // Initialize controllers
        initializeControllers();
        
        // Setup UI components
        initializeWindowSettings(FORM_WIDTH, FORM_HEIGHT, 550, 220);
        initializeTitle();
        initializeFormFields();
        initializeButtons();
        initializeDecorationImage();
        
        // Load data
        loadCustomerData();
        
        // Load initial bill data
        loadBillData(monthChoice.getSelectedItem());
        
        // Add listener to update bill data when month changes
        setupMonthChangeListener();
    }
    
    /**
     * Initialize controllers
     */
    private void initializeControllers() {
        customerController = new CustomerController();
        billController = new BillController();
    }
    
    /**
     * Initialize title label
     */
    private void initializeTitle() {
        createTitleLabel("Electricity Bill", 120, 5, 400);
    }
    
    /**
     * Initialize all form fields
     */
    private void initializeFormFields() {
        // Meter Number (read-only)
        meterNumberLabel = createLabel("Meter No", LABEL_X, 80, DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        meterNumberValueLabel = createLabel("", VALUE_X, 80, DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        
        // Name (read-only)
        nameLabel = createLabel("Name", LABEL_X, 140, DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        nameValueLabel = createLabel("", VALUE_X, 140, DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        
        // Month selection
        monthLabel = createLabel("Month", LABEL_X, 200, DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        
        monthChoice = new Choice();
        monthChoice.setBounds(VALUE_X, 200, DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        populateMonths();
        add(monthChoice);
        
        // Units (read-only)
        unitsLabel = createLabel("Units", LABEL_X, 260, DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        unitsValueLabel = createLabel("", VALUE_X, 260, DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        
        // Total Bill (read-only)
        totalBillLabel = createLabel("Total Bill", LABEL_X, 320, DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        totalBillValueLabel = createLabel("", VALUE_X, 320, DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        
        // Payment Status (read-only)
        paymentStatusLabel = createLabel("Status", LABEL_X, 380, DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        paymentStatusValueLabel = createLabel("", VALUE_X, 380, DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
        paymentStatusValueLabel.setForeground(STATUS_COLOR);
    }
    
    /**
     * Populate month selection dropdown
     */
    private void populateMonths() {
        String[] months = {
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        };
        
        for (String month : months) {
            monthChoice.add(month);
        }
    }
    
    /**
     * Initialize buttons
     */
    private void initializeButtons() {
        // Pay button
        payButton = createButton("Pay", 100, 460, 100, 25, null);
        payButton.addActionListener(this);
        
        // Back button
        backButton = createButton("Back", 230, 460, 100, 25, null);
        backButton.addActionListener(this);
    }
    
    /**
     * Add decorative image to form
     */
    private void initializeDecorationImage() {
        addDecorativeImage("bill.png", 400, 120, 600, 300);
    }
    
    /**
     * Setup listener to update data when month selection changes
     */
    private void setupMonthChangeListener() {
        monthChoice.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ae) {
                loadBillData(monthChoice.getSelectedItem());
            }
        });
    }

    /**
     * Load customer data from database
     */
    private void loadCustomerData() {
        Customer customer = customerController.getCustomerByMeter(meterNumber);
        if (customer != null) {
            meterNumberValueLabel.setText(customer.getMeter());
            nameValueLabel.setText(customer.getName());
        } else {
            showErrorMessage("Customer information not found", "Data Error");
        }
    }

    /**
     * Load bill data for the selected month
     */
    private void loadBillData(String month) {
        if (!ValidationUtil.isNotNullOrEmpty(month)) {
            showErrorMessage("Invalid month selected", "Input Error");
            return;
        }

        Bill bill = billController.getBill(meterNumber, month);

        if (bill != null) {
            updateBillDisplay(bill);
        } else {
            clearBillDisplay();
        }
    }
    
    /**
     * Update display with bill information
     * 
     * @param bill Bill object with data to display
     */
    private void updateBillDisplay(Bill bill) {
        unitsValueLabel.setText(String.valueOf(bill.getUnits()));
        totalBillValueLabel.setText(String.format("%.2f", bill.getTotalBill()));
        paymentStatusValueLabel.setText(bill.getStatus());

        // Disable pay button if bill is already paid
        payButton.setEnabled(!"Paid".equals(bill.getStatus()));
    }
    
    /**
     * Clear bill display when no bill is available
     */
    private void clearBillDisplay() {
        unitsValueLabel.setText("N/A");
        totalBillValueLabel.setText("N/A");
        paymentStatusValueLabel.setText("N/A");
        payButton.setEnabled(false);
    }

    /**
     * Handle button click events
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == payButton) {
            handlePayButtonAction();
        } else if (ae.getSource() == backButton) {
            this.setVisible(false);
        }
    }
    
    /**
     * Handle pay button logic
     */
    private void handlePayButtonAction() {
        String selectedMonth = monthChoice.getSelectedItem();
        Bill bill = billController.getBill(meterNumber, selectedMonth);

        if (bill != null && !bill.getStatus().equals("Paid")) {
            // Open payment gateway
            this.setVisible(false);
            new PaymentGateway(meterNumber, selectedMonth, bill.getTotalBill()).setVisible(true);
        } else {
            showErrorMessage("No unpaid bill found for selected month", "Payment Error");
        }
    }

    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        new PayBill("").setVisible(true);
    }
}