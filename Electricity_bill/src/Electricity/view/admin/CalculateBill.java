package Electricity.view.admin;

import Electricity.controller.BillController;
import Electricity.controller.CustomerController;
import Electricity.model.Customer;
import Electricity.util.ResourceUtil;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.io.File;

/**
 * CalculateBill view component for generating bills.
 * Used by administrators to create electricity bills for customers.
 * Uses MVC architecture with BillController for billing operations.
 */
public class CalculateBill extends JFrame implements ActionListener {
    // Constants for UI
    private static final Color PANEL_BACKGROUND = new Color(173, 216, 230);
    private static final Color BUTTON_BG = Color.BLACK;
    private static final Color BUTTON_FG = Color.WHITE;
    private static final int FORM_WIDTH = 750;
    private static final int FORM_HEIGHT = 500;
    private static final int LABEL_WIDTH = 100;
    private static final int FIELD_WIDTH = 180;
    
    // UI components grouped by function
    // - Panel
    private JPanel mainPanel;
    
    // - Labels
    private JLabel titleLabel;
    private JLabel meterNumberLabel;
    private JLabel nameLabel;
    private JLabel addressLabel;
    private JLabel unitsConsumedLabel;
    private JLabel monthLabel;
    
    // - Input fields
    private JTextField customerNameField;
    private JTextField customerAddressField;
    private JTextField unitsConsumedField;
    private Choice meterNumberChoice;
    private Choice monthChoice;
    
    // - Buttons
    private JButton submitButton;
    private JButton cancelButton;
    
    // Controllers
    private CustomerController customerController;
    private BillController billController;

    /**
     * Constructor to initialize the view
     */
    public CalculateBill() {
        super("Calculate Bill");
        
        // Initialize controllers
        initializeControllers();
        
        // Setup UI components
        initializePanel();
        initializeTitle();
        initializeMeterSelection();
        initializeCustomerFields();
        initializeUnitsField();
        initializeMonthSelection();
        initializeButtons();
        
        // Add decorative image
        JLabel decorativeImageLabel = createDecorativeImage();
        
        // Set layout
        setLayout(new BorderLayout(30, 30));
        add(mainPanel, "Center");
        add(decorativeImageLabel, "West");
        
        // Final window settings
        getContentPane().setBackground(Color.WHITE);
        setSize(FORM_WIDTH, FORM_HEIGHT);
        setLocation(550, 220);
    }
    
    /**
     * Initialize controllers
     */
    private void initializeControllers() {
        customerController = new CustomerController();
        billController = new BillController();
    }
    
    /**
     * Initialize the main panel
     */
    private void initializePanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(PANEL_BACKGROUND);
    }
    
    /**
     * Initialize title label
     */
    private void initializeTitle() {
        titleLabel = new JLabel("Calculate Electricity Bill");
        titleLabel.setBounds(30, 10, 400, 30);
        titleLabel.setFont(new Font("Senserif", Font.PLAIN, 26));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel);
    }
    
    /**
     * Initialize meter number selection
     */
    private void initializeMeterSelection() {
        meterNumberLabel = new JLabel("Meter No");
        meterNumberLabel.setBounds(60, 70, LABEL_WIDTH, 30);
        
        meterNumberChoice = new Choice();
        meterNumberChoice.setBounds(200, 70, FIELD_WIDTH, 20);
        loadMeterNumbers();
        
        // Add item listener to update customer details when meter selection changes
        meterNumberChoice.addItemListener(e -> loadCustomerDetails(meterNumberChoice.getSelectedItem()));
        
        mainPanel.add(meterNumberLabel);
        mainPanel.add(meterNumberChoice);
    }
    
    /**
     * Initialize customer information fields
     */
    private void initializeCustomerFields() {
        // Customer name
        nameLabel = new JLabel("Name");
        nameLabel.setBounds(60, 120, LABEL_WIDTH, 30);
        
        customerNameField = new JTextField();
        customerNameField.setBounds(200, 120, FIELD_WIDTH, 20);
        customerNameField.setEditable(false);
        customerNameField.setBackground(Color.WHITE);
        customerNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Customer address
        addressLabel = new JLabel("Address");
        addressLabel.setBounds(60, 170, LABEL_WIDTH, 30);
        
        customerAddressField = new JTextField();
        customerAddressField.setBounds(200, 170, FIELD_WIDTH, 20);
        customerAddressField.setEditable(false);
        customerAddressField.setBackground(Color.WHITE);
        customerAddressField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Load initial customer details if there are meters available
        if (meterNumberChoice.getItemCount() > 0) {
            loadCustomerDetails(meterNumberChoice.getSelectedItem());
        }
        
        mainPanel.add(nameLabel);
        mainPanel.add(customerNameField);
        mainPanel.add(addressLabel);
        mainPanel.add(customerAddressField);
    }
    
    /**
     * Initialize units consumed field
     */
    private void initializeUnitsField() {
        unitsConsumedLabel = new JLabel("Units Consumed");
        unitsConsumedLabel.setBounds(60, 220, LABEL_WIDTH, 30);
        
        unitsConsumedField = new JTextField();
        unitsConsumedField.setBounds(200, 220, FIELD_WIDTH, 20);
        
        mainPanel.add(unitsConsumedLabel);
        mainPanel.add(unitsConsumedField);
    }
    
    /**
     * Initialize month selection dropdown
     */
    private void initializeMonthSelection() {
        monthLabel = new JLabel("Month");
        monthLabel.setBounds(60, 270, LABEL_WIDTH, 30);
        
        monthChoice = new Choice();
        monthChoice.setBounds(200, 270, FIELD_WIDTH, 20);
        populateMonths();
        
        mainPanel.add(monthLabel);
        mainPanel.add(monthChoice);
    }
    
    /**
     * Add month names to the choice component
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
     * Initialize submit and cancel buttons
     */
    private void initializeButtons() {
        submitButton = new JButton("Submit");
        submitButton.setBounds(100, 350, 100, 25);
        submitButton.setBackground(BUTTON_BG);
        submitButton.setForeground(BUTTON_FG);
        submitButton.addActionListener(this);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(230, 350, 100, 25);
        cancelButton.setBackground(BUTTON_BG);
        cancelButton.setForeground(BUTTON_FG);
        cancelButton.addActionListener(this);
        
        mainPanel.add(submitButton);
        mainPanel.add(cancelButton);
    }
    
    /**
     * Create and return the decorative image label
     */
    private JLabel createDecorativeImage() {
        try {
            File imageFile = ResourceUtil.getResourcePath("images/hicon2.jpg");
            
            if (imageFile != null) {
                ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
                Image scaledImage = icon.getImage().getScaledInstance(180, 270, Image.SCALE_DEFAULT);
                return new JLabel(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
        
        return new JLabel("Image not available");
    }

    /**
     * Load meter numbers from database into dropdown
     */
    private void loadMeterNumbers() {
        List<Customer> customers = customerController.getAllCustomers();
        for (Customer customer : customers) {
            meterNumberChoice.add(customer.getMeter());
        }
    }

    /**
     * Load customer details based on selected meter number
     */
    private void loadCustomerDetails(String meterNo) {
        if (meterNo == null || meterNo.isEmpty()) {
            clearCustomerFields();
            return;
        }

        Customer customer = customerController.getCustomerByMeter(meterNo);
        if (customer != null) {
            customerNameField.setText(customer.getName());
            customerAddressField.setText(customer.getAddress());
        } else {
            customerNameField.setText("Customer not found");
            customerAddressField.setText("");
        }
    }
    
    /**
     * Clear customer field values
     */
    private void clearCustomerFields() {
        customerNameField.setText("");
        customerAddressField.setText("");
    }

    /**
     * Validate inputs before bill calculation
     */
    private boolean validateInputs() {
        try {
            int units = Integer.parseInt(unitsConsumedField.getText());
            if (units <= 0) {
                JOptionPane.showMessageDialog(this, "Units must be greater than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for units");
            return false;
        }

        return true;
    }

    /**
     * Handle button click events
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitButton) {
            handleSubmitAction();
        } else if (ae.getSource() == cancelButton) {
            this.setVisible(false);
        }
    }
    
    /**
     * Handle submit button logic
     */
    private void handleSubmitAction() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        String meterNo = meterNumberChoice.getSelectedItem();
        String month = monthChoice.getSelectedItem();
        int unitsConsumed = Integer.parseInt(unitsConsumedField.getText());

        // Check if a bill already exists for this meter and month
        if (billController.getBill(meterNo, month) != null) {
            JOptionPane.showMessageDialog(this, "Bill already exists for " + meterNo + " for " + month);
            return;
        }

        // Generate bill using the controller
        boolean success = billController.generateBill(meterNo, month, unitsConsumed);

        if (success) {
            JOptionPane.showMessageDialog(this, "Customer Bill Generated Successfully");
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "Error generating bill. Please try again.");
        }
    }

    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        new CalculateBill().setVisible(true);
    }
}