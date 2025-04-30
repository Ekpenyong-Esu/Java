package Electricity.view.customer;

import Electricity.controller.BillController;
import Electricity.util.ResourceUtil;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

/**
 * PaymentGateway view component for processing bill payments.
 * Used by customers to make payments for their electricity bills.
 * Uses MVC architecture with BillController for payment processing.
 */
public class PaymentGateway extends BaseCustomerView implements ActionListener {
    private JButton payNowButton, cancelButton;
    private String billingMonth;
    private double billAmount;
    private BillController billController;
    private JTextField cardNumberField, nameField, cvvField;
    private JComboBox<String> expiryMonthCombo, expiryYearCombo;

    /**
     * Constructor to initialize the view
     * @param meterNumber The meter number
     * @param billingMonth The billing month
     * @param billAmount The bill amount
     */
    public PaymentGateway(String meterNumber, String billingMonth, double billAmount) {
        super("Payment Gateway", meterNumber);
        this.billingMonth = billingMonth;
        this.billAmount = billAmount;

        // Initialize controller
        billController = new BillController();

        // Set up UI
        initializeWindowSettings(800, 600, 550, 220);
        initializePaymentForm();
    }

    /**
     * Initialize the payment form UI components
     */
    private void initializePaymentForm() {
        // Payment Gateway title
        createTitleLabel("Payment Gateway", 300, 5, 400);

        // Amount to pay
        JLabel amountLabel = createLabel("Amount to Pay: $" + String.format("%.2f", billAmount), 300, 60, 300, 30);
        amountLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        amountLabel.setForeground(SUCCESS_COLOR);

        // Payment method section
        JLabel methodLabel = createLabel("Payment Method", 50, 120, 200, 25);
        methodLabel.setFont(new Font("Tahoma", Font.BOLD, 16));

        // Credit Card Number
        createLabel("Card Number", 50, 170, 100, 25);
        cardNumberField = createTextField(200, 170, 300, 25);

        // Name on Card
        createLabel("Name on Card", 50, 220, 100, 25);
        nameField = createTextField(200, 220, 300, 25);

        // Expiry Date
        createLabel("Expiry Date", 50, 270, 100, 25);

        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        expiryMonthCombo = new JComboBox<>(months);
        expiryMonthCombo.setBounds(200, 270, 100, 25);
        add(expiryMonthCombo);

        String[] years = new String[10];
        int startYear = 2024;
        for (int i = 0; i < 10; i++) {
            years[i] = String.valueOf(startYear + i);
        }
        expiryYearCombo = new JComboBox<>(years);
        expiryYearCombo.setBounds(320, 270, 100, 25);
        add(expiryYearCombo);

        // CVV
        createLabel("CVV", 50, 320, 100, 25);
        cvvField = createTextField(200, 320, 100, 25);

        // Pay button
        payNowButton = createButton("Pay Now", 150, 400, 150, 35, new Color(0, 102, 0));
        payNowButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        payNowButton.addActionListener(this);

        // Cancel button
        cancelButton = createButton("Cancel", 350, 400, 150, 35, Color.RED);
        cancelButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        cancelButton.addActionListener(this);

        // Add security icons
        File cardImageFile = ResourceUtil.getResourcePath("images/icon9.png");
        if (cardImageFile != null) {
            ImageIcon cardIcon = new ImageIcon(cardImageFile.getAbsolutePath());
            Image scaledCardImage = cardIcon.getImage().getScaledInstance(100, 80, Image.SCALE_DEFAULT);
            ImageIcon scaledCardIcon = new ImageIcon(scaledCardImage);
            JLabel cardImageLabel = new JLabel(scaledCardIcon);
            cardImageLabel.setBounds(450, 320, 300, 80);
            add(cardImageLabel);
        }

        // Add secure payment text
        JLabel secureLabel = createLabel("100% Secure Payments", 550, 400, 200, 25);
        secureLabel.setForeground(SUCCESS_COLOR);
        secureLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
    }

    /**
     * Validate payment form input
     * @return true if all inputs are valid
     */
    private boolean validatePayment() {
        String cardNumber = cardNumberField.getText().trim();
        String name = nameField.getText().trim();
        String cvv = cvvField.getText().trim();

        if (cardNumber.isEmpty()) {
            showErrorMessage("Please enter card number", "Validation Error");
            return false;
        }

        if (cardNumber.length() < 15 || cardNumber.length() > 16) {
            showErrorMessage("Card number should be 15 or 16 digits", "Validation Error");
            return false;
        }

        if (name.isEmpty()) {
            showErrorMessage("Please enter name on card", "Validation Error");
            return false;
        }

        if (cvv.isEmpty()) {
            showErrorMessage("Please enter CVV", "Validation Error");
            return false;
        }

        if (cvv.length() != 3) {
            showErrorMessage("CVV should be 3 digits", "Validation Error");
            return false;
        }

        // Check if all inputs are numeric where required
        try {
            Long.parseLong(cardNumber);
            Integer.parseInt(cvv);
        } catch (NumberFormatException e) {
            showErrorMessage("Card number and CVV should contain only digits", "Validation Error");
            return false;
        }

        return true;
    }

    /**
     * Handle button click events
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == payNowButton) {
            // Validate payment form
            if (!validatePayment()) {
                return;
            }

            // Delegate payment processing to the controller
            boolean success = billController.processPayment(meterNumber, billingMonth, billAmount);

            if (success) {
                showSuccessMessage("Payment Successful!\nThank you for your payment.");
                this.setVisible(false);
            } else {
                showErrorMessage("Payment processing failed. Please try again.", "Payment Error");
            }
        } else if (ae.getSource() == cancelButton) {
            this.setVisible(false);
        }
    }

    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        new PaymentGateway("", "", 1000.0).setVisible(true);
    }
}