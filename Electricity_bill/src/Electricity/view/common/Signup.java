package Electricity.view.common;

import Electricity.controller.LoginController;
import Electricity.util.ResourceUtil;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.File;

/**
 * Signup view component that provides user registration functionality.
 * Uses MVC architecture with LoginController for registration logic.
 */
public class Signup extends JFrame implements ActionListener {
    // Constants for UI
    private static final Color BUTTON_BG = Color.BLACK;
    private static final Color BUTTON_FG = Color.WHITE;
    private static final Color LABEL_FG = Color.DARK_GRAY;
    private static final Color BORDER_COLOR = new Color(173, 216, 230);
    private static final Color PANEL_BG = Color.WHITE;
    private static final int FORM_WIDTH = 700;
    private static final int FORM_HEIGHT = 400;
    private static final int LABEL_X = 100;
    private static final int FIELD_X = 260;
    private static final int FIELD_WIDTH = 150;
    private static final int FIELD_HEIGHT = 20;
    private static final Font LABEL_FONT = new Font("Tahoma", Font.BOLD, 14);
    
    // UI components grouped by function
    // - Labels
    private JLabel usernameLabel;
    private JLabel nameLabel;
    private JLabel passwordLabel;
    private JLabel userTypeLabel;
    private JLabel meterNumberLabel;
    
    // - Input fields
    private JTextField usernameField;
    private JTextField nameField;
    private JTextField passwordField;
    private JTextField meterNumberField;
    private Choice userTypeChoice;
    
    // - Buttons
    private JButton createAccountButton;
    private JButton backButton;
    
    // - Panels
    private JPanel mainPanel;
    
    // Controller
    private LoginController loginController;

    /**
     * Constructor to initialize the signup form
     */
    public Signup() {
        super("Signup");

        // Initialize controller
        initializeController();
        
        // Setup UI components
        initializeWindowSettings();
        initializeMainPanel();
        initializeInputFields();
        initializeButtons();
        setupUserTypeListener();
        addDecorativeImage();
    }
    
    /**
     * Initialize controller
     */
    private void initializeController() {
        loginController = new LoginController();
    }
    
    /**
     * Initialize window settings
     */
    private void initializeWindowSettings() {
        setBounds(600, 250, FORM_WIDTH, FORM_HEIGHT);
        setLayout(null);
    }
    
    /**
     * Initialize the main panel with border
     */
    private void initializeMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBounds(30, 30, 650, 300);
        mainPanel.setLayout(null);
        mainPanel.setBackground(PANEL_BG);
        mainPanel.setForeground(new Color(34, 139, 34));
        mainPanel.setBorder(new TitledBorder(
                new LineBorder(BORDER_COLOR, 2), 
                "Create-Account",
                TitledBorder.LEADING, 
                TitledBorder.TOP, 
                null, 
                BORDER_COLOR));
        add(mainPanel);
    }
    
    /**
     * Initialize all input fields and labels
     */
    private void initializeInputFields() {
        // Username field
        usernameLabel = createLabel("Username", 50);
        usernameField = createTextField(50);
        
        // Name field
        nameLabel = createLabel("Name", 90);
        nameField = createTextField(90);
        
        // Password field
        passwordLabel = createLabel("Password", 130);
        passwordField = createTextField(130);
        
        // User type selection
        userTypeLabel = createLabel("Create Account As", 170);
        userTypeChoice = new Choice();
        populateUserTypes();
        userTypeChoice.setBounds(FIELD_X, 170, FIELD_WIDTH, FIELD_HEIGHT);
        mainPanel.add(userTypeChoice);
        
        // Meter number field (only visible for Customer)
        meterNumberLabel = createLabel("Meter Number", 210);
        meterNumberField = createTextField(210);
        
        // Initially hide meter number field for Admin
        meterNumberLabel.setVisible(false);
        meterNumberField.setVisible(false);
    }
    
    /**
     * Create a standardized label
     * 
     * @param text Label text
     * @param y Y position
     * @return Configured JLabel
     */
    private JLabel createLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setForeground(LABEL_FG);
        label.setFont(LABEL_FONT);
        label.setBounds(LABEL_X, y, 140, FIELD_HEIGHT);
        mainPanel.add(label);
        return label;
    }
    
    /**
     * Create a standardized text field
     * 
     * @param y Y position
     * @return Configured JTextField
     */
    private JTextField createTextField(int y) {
        JTextField field = new JTextField();
        field.setBounds(FIELD_X, y, FIELD_WIDTH, FIELD_HEIGHT);
        mainPanel.add(field);
        return field;
    }
    
    /**
     * Populate user type choices
     */
    private void populateUserTypes() {
        userTypeChoice.add("Admin");
        userTypeChoice.add("Customer");
    }
    
    /**
     * Initialize create account and back buttons
     */
    private void initializeButtons() {
        // Create Account button
        createAccountButton = new JButton("Create");
        createAccountButton.setBackground(BUTTON_BG);
        createAccountButton.setForeground(BUTTON_FG);
        createAccountButton.setBounds(140, 260, 120, 30);
        createAccountButton.addActionListener(this);
        mainPanel.add(createAccountButton);
        
        // Back button
        backButton = new JButton("Back");
        backButton.setBackground(BUTTON_BG);
        backButton.setForeground(BUTTON_FG);
        backButton.setBounds(300, 260, 120, 30);
        backButton.addActionListener(this);
        mainPanel.add(backButton);
    }
    
    /**
     * Setup listener to show/hide meter number field based on user type
     */
    private void setupUserTypeListener() {
        userTypeChoice.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ae) {
                String userType = userTypeChoice.getSelectedItem();
                toggleMeterNumberVisibility(userType.equals("Customer"));
            }
        });
    }
    
    /**
     * Toggle visibility of meter number field and label
     * 
     * @param visible Whether fields should be visible
     */
    private void toggleMeterNumberVisibility(boolean visible) {
        meterNumberLabel.setVisible(visible);
        meterNumberField.setVisible(visible);
    }
    
    /**
     * Add decorative image to form
     */
    private void addDecorativeImage() {
        try {
            File imageFile = ResourceUtil.getResourcePath("images/signupImage.png");
            if (imageFile != null) {
                ImageIcon signupIcon = new ImageIcon(imageFile.getAbsolutePath());
                Image scaledImage = signupIcon.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
                JLabel decorativeImageLabel = new JLabel(new ImageIcon(scaledImage));
                decorativeImageLabel.setBounds(430, 30, 250, 250);
                mainPanel.add(decorativeImageLabel);
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle button click events
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == createAccountButton) {
            handleCreateAccount();
        } else if (ae.getSource() == backButton) {
            navigateToLogin();
        }
    }
    
    /**
     * Handle create account button click
     */
    private void handleCreateAccount() {
        // Get form values
        String username = usernameField.getText();
        String name = nameField.getText();
        String password = passwordField.getText();
        String userType = userTypeChoice.getSelectedItem();
        String meterNumber = meterNumberField.getText();

        // Validate inputs
        if (!validateInputs(username, name, password, userType, meterNumber)) {
            return;
        }

        // Register user with controller
        boolean success = registerUser(username, name, password, userType, meterNumber);

        // Handle result
        if (success) {
            JOptionPane.showMessageDialog(this, "Account Created Successfully");
            navigateToLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Account creation failed. Please try again.");
        }
    }
    
    /**
     * Validate user inputs
     * 
     * @param username Username
     * @param name Name
     * @param password Password
     * @param userType User type
     * @param meterNumber Meter number
     * @return True if inputs are valid, false otherwise
     */
    private boolean validateInputs(String username, String name, 
            String password, String userType, String meterNumber) {
        
        if (username.isEmpty() || name.isEmpty() || password.isEmpty() ||
                (userType.equals("Customer") && meterNumber.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return false;
        }
        return true;
    }
    
    /**
     * Register user with the controller
     * 
     * @param username Username
     * @param name Name
     * @param password Password
     * @param userType User type
     * @param meterNumber Meter number (if customer)
     * @return True if registration successful, false otherwise
     */
    private boolean registerUser(String username, String name, 
            String password, String userType, String meterNumber) {
        
        if (userType.equals("Admin")) {
            // For admin, generate a dummy meter number
            return loginController.registerUser(
                    "ADMIN" + System.currentTimeMillis(),
                    username, name, password, userType);
        } else {
            // For customer, use provided meter number
            return loginController.registerUser(
                    meterNumber, username, name, password, userType);
        }
    }
    
    /**
     * Navigate back to login screen
     */
    private void navigateToLogin() {
        this.setVisible(false);
        new Login().setVisible(true);
    }

    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        new Signup().setVisible(true);
    }
}