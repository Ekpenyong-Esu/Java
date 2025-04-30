package Electricity.view.admin;

import Electricity.controller.CustomerController;
import Electricity.controller.MeterController;
import Electricity.model.Customer;
import Electricity.util.ResourceUtil;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.io.File;
import Electricity.util.ValidationUtil;

/**
 * NewCustomer view component for adding new customers to the system.
 * Used by administrators to create customer accounts.
 * Uses MVC architecture with CustomerController for data operations.
 */
public class NewCustomer extends JFrame implements ActionListener {
    // Constants for UI
    private static final Color PANEL_BACKGROUND = new Color(173, 216, 230);
    private static final Color BUTTON_BG = Color.BLACK;
    private static final Color BUTTON_FG = Color.WHITE;
    private static final int FORM_WIDTH = 700;
    private static final int FORM_HEIGHT = 500;
    private static final int LABEL_X = 100;
    private static final int FIELD_X = 240;
    private static final int FIELD_WIDTH = 200;
    
    // UI components grouped by function
    // - Labels
    private JLabel nameLabel;
    private JLabel meterNumberLabel;
    private JLabel addressLabel;
    private JLabel cityLabel; 
    private JLabel stateLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    
    // - Input fields
    private JTextField nameField;
    private JTextField addressField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField emailField;
    private JTextField phoneField;
    private JLabel meterNumberValueLabel;
    
    // - Buttons
    private JButton submitButton;
    private JButton cancelButton;
    
    // - Panel
    private JPanel mainPanel;
    
    // Controllers
    private CustomerController customerController;
    private MeterController meterController;

    /**
     * Constructor to initialize the view
     */
    public NewCustomer() {
        super("New Customer");
        
        // Initialize controllers
        initializeControllers();
        
        // Setup UI components
        initializePanel();
        initializeTitle();
        initializeFormFields();
        initializeButtons();
        
        // Add decorative image
        JLabel decorativeImageLabel = createDecorativeImage();
        
        // Set layout
        setLayout(new BorderLayout());
        add(mainPanel, "Center");
        add(decorativeImageLabel, "West");
        
        // Final window settings
        getContentPane().setBackground(Color.WHITE);
        setSize(FORM_WIDTH, FORM_HEIGHT);
        setLocation(600, 200);
        
        // Generate random meter number
        generateMeterNumber();
    }
    
    /**
     * Initialize controllers
     */
    private void initializeControllers() {
        customerController = new CustomerController();
        meterController = new MeterController();
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
        JLabel titleLabel = new JLabel("New Customer");
        titleLabel.setBounds(180, 10, 200, 26);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
        mainPanel.add(titleLabel);
    }
    
    /**
     * Initialize all form input fields
     */
    private void initializeFormFields() {
        // Customer Name
        nameLabel = new JLabel("Customer Name");
        nameLabel.setBounds(LABEL_X, 80, 100, 20);
        nameField = new JTextField();
        nameField.setBounds(FIELD_X, 80, FIELD_WIDTH, 20);
        mainPanel.add(nameLabel);
        mainPanel.add(nameField);
        
        // Meter Number (read-only)
        meterNumberLabel = new JLabel("Meter Number");
        meterNumberLabel.setBounds(LABEL_X, 120, 100, 20);
        meterNumberValueLabel = new JLabel();
        meterNumberValueLabel.setBounds(FIELD_X, 120, FIELD_WIDTH, 20);
        mainPanel.add(meterNumberLabel);
        mainPanel.add(meterNumberValueLabel);
        
        // Address
        addressLabel = new JLabel("Address");
        addressLabel.setBounds(LABEL_X, 160, 100, 20);
        addressField = new JTextField();
        addressField.setBounds(FIELD_X, 160, FIELD_WIDTH, 20);
        mainPanel.add(addressLabel);
        mainPanel.add(addressField);
        
        // City
        cityLabel = new JLabel("City");
        cityLabel.setBounds(LABEL_X, 200, 100, 20);
        cityField = new JTextField();
        cityField.setBounds(FIELD_X, 200, FIELD_WIDTH, 20);
        mainPanel.add(cityLabel);
        mainPanel.add(cityField);
        
        // State
        stateLabel = new JLabel("State");
        stateLabel.setBounds(LABEL_X, 240, 100, 20);
        stateField = new JTextField();
        stateField.setBounds(FIELD_X, 240, FIELD_WIDTH, 20);
        mainPanel.add(stateLabel);
        mainPanel.add(stateField);
        
        // Email
        emailLabel = new JLabel("Email");
        emailLabel.setBounds(LABEL_X, 280, 100, 20);
        emailField = new JTextField();
        emailField.setBounds(FIELD_X, 280, FIELD_WIDTH, 20);
        mainPanel.add(emailLabel);
        mainPanel.add(emailField);
        
        // Phone
        phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(LABEL_X, 320, 100, 20);
        phoneField = new JTextField();
        phoneField.setBounds(FIELD_X, 320, FIELD_WIDTH, 20);
        mainPanel.add(phoneLabel);
        mainPanel.add(phoneField);
    }
    
    /**
     * Initialize submit and cancel buttons
     */
    private void initializeButtons() {
        submitButton = new JButton("Submit");
        submitButton.setBounds(120, 390, 100, 25);
        submitButton.setBackground(BUTTON_BG);
        submitButton.setForeground(BUTTON_FG);
        submitButton.addActionListener(this);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(250, 390, 100, 25);
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
            File imageFile = ResourceUtil.getResourcePath("images/hicon1.jpg");
            if (imageFile != null) {
                ImageIcon customerIcon = new ImageIcon(imageFile.getAbsolutePath());
                Image scaledImage = customerIcon.getImage().getScaledInstance(150, 300, Image.SCALE_DEFAULT);
                return new JLabel(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
        
        return new JLabel("Image not available");
    }

    /**
     * Generate a random meter number that doesn't exist in the system
     */
    private void generateMeterNumber() {
        Random ran = new Random();
        long first;
        String meter;

        // Generate unique meter number
        do {
            first = (ran.nextLong() % 1000000);
            meter = "" + Math.abs(first);
        } while (meterController.meterExists(meter));

        meterNumberValueLabel.setText(meter);
    }

    /**
     * Validate input fields
     * @return true if all mandatory fields are filled properly
     */
    private boolean validateInputs() {
        if (!ValidationUtil.isNotNullOrEmpty(nameField.getText())) {
            JOptionPane.showMessageDialog(this, "Customer name cannot be empty");
            nameField.requestFocus();
            return false;
        }

        if (!ValidationUtil.isNotNullOrEmpty(addressField.getText())) {
            JOptionPane.showMessageDialog(this, "Address cannot be empty");
            addressField.requestFocus();
            return false;
        }

        if (!ValidationUtil.isValidPhone(phoneField.getText())) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits");
            phoneField.requestFocus();
            return false;
        }

        if (!ValidationUtil.isValidEmail(emailField.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid email format");
            emailField.requestFocus();
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
        // Validate inputs before proceeding
        if (!validateInputs()) {
            return;
        }

        // Get form values
        String name = nameField.getText();
        String meter = meterNumberValueLabel.getText();
        String address = addressField.getText();
        String state = stateField.getText();
        String city = cityField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        // Add customer via controller
        boolean success = customerController.addCustomer(name, meter, address, city, state, email, phone);

        if (success) {
            JOptionPane.showMessageDialog(this, "Customer Details Added Successfully");
            this.setVisible(false);

            // Open meter info screen after adding customer
            new MeterInfo(meter).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add customer. Please try again.");
        }
    }

    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        new NewCustomer().setVisible(true);
    }
}