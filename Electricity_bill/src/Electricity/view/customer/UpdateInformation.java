package Electricity.view.customer;

import Electricity.controller.CustomerController;
import Electricity.model.Customer;
import Electricity.util.ValidationUtil;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * UpdateInformation view component for updating customer information.
 * Used by customers to update their personal details.
 * Uses MVC architecture with CustomerController for data operations.
 */
public class UpdateInformation extends BaseCustomerView implements ActionListener {
    // Constants for UI
    private static final int FORM_WIDTH = 1050;
    private static final int FORM_HEIGHT = 450;
    private static final int LABEL_X = 30;
    private static final int VALUE_X = 230;
    
    // UI components 
    private JTextField addressField, cityField, stateField, emailField, phoneField;
    private JLabel nameValueLabel, meterNumberValueLabel;
    private JButton updateButton, cancelButton;
    
    // Controller
    private CustomerController customerController;

    /**
     * Constructor to initialize the view
     * @param meterNumber The meter number of the customer
     */
    public UpdateInformation(String meterNumber) {
        super("Update Information", meterNumber);

        // Initialize controller
        customerController = new CustomerController();

        // Setup UI components
        initializeWindowSettings(FORM_WIDTH, FORM_HEIGHT, 500, 220);
        initializeTitle();
        initializeFormFields();
        initializeButtons();
        initializeDecorationImage();
        
        // Load customer information
        loadCustomerInformation();
    }

    /**
     * Initialize title label
     */
    private void initializeTitle() {
        createTitleLabel("UPDATE CUSTOMER INFORMATION", 110, 0, 400);
    }

    /**
     * Initialize form fields
     */
    private void initializeFormFields() {
        // Name (non-editable)
        createLabel("Name", LABEL_X, 70, 100, 20);
        nameValueLabel = createLabel("", VALUE_X, 70, 200, 20);

        // Meter Number (non-editable)
        createLabel("Meter Number", LABEL_X, 110, 100, 20);
        meterNumberValueLabel = createLabel("", VALUE_X, 110, 200, 20);

        // Address (editable)
        createLabel("Address", LABEL_X, 150, 100, 20);
        addressField = createTextField(VALUE_X, 150, 200, 20);

        // City (editable)
        createLabel("City", LABEL_X, 190, 100, 20);
        cityField = createTextField(VALUE_X, 190, 200, 20);

        // State (editable)
        createLabel("State", LABEL_X, 230, 100, 20);
        stateField = createTextField(VALUE_X, 230, 200, 20);

        // Email (editable)
        createLabel("Email", LABEL_X, 270, 100, 20);
        emailField = createTextField(VALUE_X, 270, 200, 20);

        // Phone (editable)
        createLabel("Phone", LABEL_X, 310, 100, 20);
        phoneField = createTextField(VALUE_X, 310, 200, 20);
    }

    /**
     * Initialize buttons
     */
    private void initializeButtons() {
        // Update button
        updateButton = createButton("Update", 70, 360, 100, 25, BUTTON_BG);
        updateButton.addActionListener(this);

        // Cancel button
        cancelButton = createButton("Cancel", 230, 360, 100, 25, BUTTON_BG);
        cancelButton.addActionListener(this);
    }
    
    /**
     * Add decorative image to form
     */
    private void initializeDecorationImage() {
        addDecorativeImage("update.jpg", 550, 50, 400, 300);
    }

    /**
     * Load customer information from the database using the controller
     */
    private void loadCustomerInformation() {
        Customer customer = customerController.getCustomerByMeter(meterNumber);

        if (customer != null) {
            nameValueLabel.setText(customer.getName());
            meterNumberValueLabel.setText(customer.getMeter());
            addressField.setText(customer.getAddress());
            cityField.setText(customer.getCity());
            stateField.setText(customer.getState());
            emailField.setText(customer.getEmail());
            phoneField.setText(customer.getPhone());
        } else {
            showErrorMessage("Customer information not found", "Data Error");
            this.setVisible(false);
        }
    }

    /**
     * Validate input fields before updating
     */
    private boolean validateInputs() {
        if (!ValidationUtil.isNotNullOrEmpty(addressField.getText())) {
            showErrorMessage("Address cannot be empty", "Validation Error");
            addressField.requestFocus();
            return false;
        }

        if (!ValidationUtil.isNotNullOrEmpty(cityField.getText())) {
            showErrorMessage("City cannot be empty", "Validation Error");
            cityField.requestFocus();
            return false;
        }

        if (!ValidationUtil.isValidPhone(phoneField.getText())) {
            showErrorMessage("Phone number must be 10 digits", "Validation Error");
            phoneField.requestFocus();
            return false;
        }

        if (!ValidationUtil.isValidEmail(emailField.getText())) {
            showErrorMessage("Invalid email format", "Validation Error");
            emailField.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Handle button click events
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateButton) {
            // Validate inputs
            if (!validateInputs()) {
                return;
            }

            // Get form values
            String name = nameValueLabel.getText();
            String meterNo = meterNumberValueLabel.getText();
            String address = addressField.getText();
            String city = cityField.getText();
            String state = stateField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();

            // Update customer via controller
            boolean success = customerController.updateCustomerInfo(meterNo, name, address, city, state, email, phone);

            if (success) {
                showSuccessMessage("Details Updated Successfully");
                this.setVisible(false);
            } else {
                showErrorMessage("Failed to update details. Please try again.", "Update Error");
            }

        } else if (ae.getSource() == cancelButton) {
            this.setVisible(false);
        }
    }

    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        new UpdateInformation("").setVisible(true);
    }
}