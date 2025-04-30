package Electricity.view.customer;

import Electricity.controller.CustomerController;
import Electricity.model.Customer;
import Electricity.util.ResourceUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * ViewInformation view component for displaying customer information.
 * Used by customers to view their personal details.
 * Uses MVC architecture with CustomerController for data retrieval.
 */
public class ViewInformation extends JFrame implements ActionListener {
    // Constants for UI
    private static final Color BUTTON_BG = Color.BLACK;
    private static final Color BUTTON_FG = Color.WHITE;
    private static final int FORM_WIDTH = 850;
    private static final int FORM_HEIGHT = 650;
    private static final int LEFT_LABEL_X = 70;
    private static final int RIGHT_LABEL_X = 500;
    private static final int LEFT_VALUE_X = 250;
    private static final int RIGHT_VALUE_X = 650;
    
    // UI components grouped by function
    // - Labels for field names
    private JLabel nameLabel;
    private JLabel meterNumberLabel;
    private JLabel addressLabel;
    private JLabel cityLabel;
    private JLabel stateLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    
    // - Labels for field values
    private JLabel nameValueLabel;
    private JLabel meterNumberValueLabel;
    private JLabel addressValueLabel;
    private JLabel cityValueLabel;
    private JLabel stateValueLabel;
    private JLabel emailValueLabel;
    private JLabel phoneValueLabel;
    
    // - Buttons
    private JButton backButton;
    
    // Data
    private String meterNumber;
    
    // Controller
    private CustomerController customerController;

    /**
     * Constructor to initialize the view
     * @param meterNumber The meter number of the customer
     */
    public ViewInformation(String meterNumber) {
        super("View Information");
        this.meterNumber = meterNumber;

        // Initialize controller
        initializeController();
        
        // Setup UI components
        initializeWindowSettings();
        initializeTitle();
        initializeLeftColumnFields();
        initializeRightColumnFields();
        initializeBackButton();
        
        // Add decorative image
        addDecorativeImage();
        
        // Load customer data
        loadCustomerInformation();
    }
    
    /**
     * Initialize controller
     */
    private void initializeController() {
        customerController = new CustomerController();
    }
    
    /**
     * Initialize window settings
     */
    private void initializeWindowSettings() {
        setBounds(600, 250, FORM_WIDTH, FORM_HEIGHT);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
    }
    
    /**
     * Initialize title label
     */
    private void initializeTitle() {
        JLabel titleLabel = new JLabel("VIEW CUSTOMER INFORMATION");
        titleLabel.setBounds(250, 0, 500, 40);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(titleLabel);
    }
    
    /**
     * Initialize left column labels and values
     */
    private void initializeLeftColumnFields() {
        // Name
        nameLabel = new JLabel("Name");
        nameLabel.setBounds(LEFT_LABEL_X, 80, 100, 20);
        add(nameLabel);
        
        nameValueLabel = new JLabel();
        nameValueLabel.setBounds(LEFT_VALUE_X, 80, 200, 20);
        add(nameValueLabel);
        
        // Meter Number
        meterNumberLabel = new JLabel("Meter Number");
        meterNumberLabel.setBounds(LEFT_LABEL_X, 140, 100, 20);
        add(meterNumberLabel);
        
        meterNumberValueLabel = new JLabel();
        meterNumberValueLabel.setBounds(LEFT_VALUE_X, 140, 200, 20);
        add(meterNumberValueLabel);
        
        // Address
        addressLabel = new JLabel("Address");
        addressLabel.setBounds(LEFT_LABEL_X, 200, 100, 20);
        add(addressLabel);
        
        addressValueLabel = new JLabel();
        addressValueLabel.setBounds(LEFT_VALUE_X, 200, 200, 20);
        add(addressValueLabel);
        
        // City
        cityLabel = new JLabel("City");
        cityLabel.setBounds(LEFT_LABEL_X, 260, 100, 20);
        add(cityLabel);
        
        cityValueLabel = new JLabel();
        cityValueLabel.setBounds(LEFT_VALUE_X, 260, 200, 20);
        add(cityValueLabel);
    }
    
    /**
     * Initialize right column labels and values
     */
    private void initializeRightColumnFields() {
        // State
        stateLabel = new JLabel("State");
        stateLabel.setBounds(RIGHT_LABEL_X, 80, 100, 20);
        add(stateLabel);
        
        stateValueLabel = new JLabel();
        stateValueLabel.setBounds(RIGHT_VALUE_X, 80, 200, 20);
        add(stateValueLabel);
        
        // Email
        emailLabel = new JLabel("Email");
        emailLabel.setBounds(RIGHT_LABEL_X, 140, 100, 20);
        add(emailLabel);
        
        emailValueLabel = new JLabel();
        emailValueLabel.setBounds(RIGHT_VALUE_X, 140, 200, 20);
        add(emailValueLabel);
        
        // Phone
        phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(RIGHT_LABEL_X, 200, 100, 20);
        add(phoneLabel);
        
        phoneValueLabel = new JLabel();
        phoneValueLabel.setBounds(RIGHT_VALUE_X, 200, 200, 20);
        add(phoneValueLabel);
    }
    
    /**
     * Initialize back button
     */
    private void initializeBackButton() {
        backButton = new JButton("Back");
        backButton.setBackground(BUTTON_BG);
        backButton.setForeground(BUTTON_FG);
        backButton.setBounds(350, 340, 100, 25);
        backButton.addActionListener(this);
        add(backButton);
    }
    
    /**
     * Add decorative image to the form
     */
    private void addDecorativeImage() {
        try {
            File imageFile = ResourceUtil.getResourcePath("images/viewcustomer.jpg");
            if (imageFile != null) {
                ImageIcon viewIcon = new ImageIcon(imageFile.getAbsolutePath());
                Image scaledImage = viewIcon.getImage().getScaledInstance(600, 300, Image.SCALE_DEFAULT);
                JLabel decorativeImageLabel = new JLabel(new ImageIcon(scaledImage));
                decorativeImageLabel.setBounds(20, 350, 600, 300);
                add(decorativeImageLabel);
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load customer information from the database using the controller
     */
    private void loadCustomerInformation() {
        Customer customer = customerController.getCustomerByMeter(meterNumber);

        if (customer != null) {
            nameValueLabel.setText(customer.getName());
            meterNumberValueLabel.setText(customer.getMeter());
            addressValueLabel.setText(customer.getAddress());
            cityValueLabel.setText(customer.getCity());
            stateValueLabel.setText(customer.getState());
            emailValueLabel.setText(customer.getEmail());
            phoneValueLabel.setText(customer.getPhone());
        } else {
            JOptionPane.showMessageDialog(this, "Customer information not found for meter " + meterNumber);
            this.setVisible(false);
        }
    }

    /**
     * Handle button click events
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == backButton) {
            this.setVisible(false);
        }
    }

    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        new ViewInformation("").setVisible(true);
    }
}