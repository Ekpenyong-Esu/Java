package Electricity.view.common;

import Electricity.controller.LoginController;
import Electricity.util.ResourceUtil;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

/**
 * Login view component that provides the entry point to the application.
 * Uses MVC architecture with LoginController for authentication logic.
 */
public class Login extends JFrame implements ActionListener {
    // Constants for UI
    private static final int FORM_WIDTH = 640;
    private static final int FORM_HEIGHT = 300;
    private static final int LABEL_X = 300;
    private static final int FIELD_X = 400;
    private static final int FIELD_WIDTH = 150;
    private static final int FIELD_HEIGHT = 20;
    private static final int ICON_SIZE = 16;
    
    // UI components grouped by function
    // - Labels
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel userTypeLabel;
    
    // - Input fields
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Choice userTypeChoice;
    
    // - Buttons
    private JButton loginButton;
    private JButton cancelButton;
    private JButton signupButton;
    
    // Controller
    private LoginController loginController;

    /**
     * Constructor to initialize the login form
     */
    public Login() {
        super("Login Page");

        // Initialize controller
        initializeController();
        
        // Setup UI components
        initializeWindowSettings();
        initializeInputFields();
        initializeButtons();
        
        // Add decorative image
        addDecorativeImage();
    }
    
    /**
     * Initialize the controller
     */
    private void initializeController() {
        loginController = new LoginController();
    }
    
    /**
     * Initialize window settings
     */
    private void initializeWindowSettings() {
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        setSize(FORM_WIDTH, FORM_HEIGHT);
        setLocation(600, 300);
    }
    
    /**
     * Initialize input fields and labels
     */
    private void initializeInputFields() {
        // Username field
        usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(LABEL_X, 20, 100, FIELD_HEIGHT);
        add(usernameLabel);
        
        usernameField = new JTextField(15);
        usernameField.setBounds(FIELD_X, 20, FIELD_WIDTH, FIELD_HEIGHT);
        add(usernameField);
        
        // Password field
        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(LABEL_X, 60, 100, FIELD_HEIGHT);
        add(passwordLabel);
        
        passwordField = new JPasswordField(15);
        passwordField.setBounds(FIELD_X, 60, FIELD_WIDTH, FIELD_HEIGHT);
        add(passwordField);
        
        // User type selection
        userTypeLabel = new JLabel("Logging in as");
        userTypeLabel.setBounds(LABEL_X, 100, 100, FIELD_HEIGHT);
        add(userTypeLabel);
        
        userTypeChoice = new Choice();
        populateUserTypes();
        userTypeChoice.setBounds(FIELD_X, 100, FIELD_WIDTH, FIELD_HEIGHT);
        add(userTypeChoice);
    }
    
    /**
     * Populate user type options
     */
    private void populateUserTypes() {
        userTypeChoice.add("Admin");
        userTypeChoice.add("Customer");
    }
    
    /**
     * Initialize all buttons with their icons and event listeners
     */
    private void initializeButtons() {
        // Login button
        ImageIcon loginIcon = createIcon("images/login.png", "login icon");
        loginButton = new JButton("Login", loginIcon);
        loginButton.setBounds(330, 160, 100, FIELD_HEIGHT);
        loginButton.addActionListener(this);
        add(loginButton);
        
        // Cancel button
        ImageIcon cancelIcon = createIcon("images/cancel.jpg", "cancel icon");
        cancelButton = new JButton("Cancel", cancelIcon);
        cancelButton.setBounds(450, 160, 100, FIELD_HEIGHT);
        cancelButton.addActionListener(this);
        add(cancelButton);
        
        // Signup button
        ImageIcon signupIcon = createIcon("images/signup.png", "signup icon");
        signupButton = new JButton("Signup", signupIcon);
        signupButton.setBounds(380, 200, 130, FIELD_HEIGHT);
        signupButton.addActionListener(this);
        add(signupButton);
    }
    
    /**
     * Create scaled icon from resource path
     * 
     * @param resourcePath Path to the image resource
     * @param description Description for error message
     * @return Scaled ImageIcon or null if not found
     */
    private ImageIcon createIcon(String resourcePath, String description) {
        try {
            File iconFile = ResourceUtil.getResourcePath(resourcePath);
            if (iconFile != null) {
                ImageIcon icon = new ImageIcon(iconFile.getAbsolutePath());
                Image scaledImage = icon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_DEFAULT);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            System.out.println("Error loading " + description + ": " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Add decorative image to the left side of the login form
     */
    private void addDecorativeImage() {
        try {
            File imageFile = ResourceUtil.getResourcePath("images/second.jpg");
            if (imageFile != null) {
                ImageIcon image = new ImageIcon(imageFile.getAbsolutePath());
                Image scaledImage = image.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
                JLabel decorativeImageLabel = new JLabel(new ImageIcon(scaledImage));
                decorativeImageLabel.setBounds(0, 0, 250, 250);
                add(decorativeImageLabel);
            }
        } catch (Exception e) {
            System.out.println("Error loading decorative image: " + e.getMessage());
        }
        
        // Switch to BorderLayout after all components are added
        setLayout(new BorderLayout());
    }

    /**
     * Handle button click events
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == loginButton) {
            handleLoginAction();
        } else if (ae.getSource() == cancelButton) {
            handleCancelAction();
        } else if (ae.getSource() == signupButton) {
            handleSignupAction();
        }
    }
    
    /**
     * Handle login button action
     */
    private void handleLoginAction() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = userTypeChoice.getSelectedItem();

        // Authenticate using controller
        Electricity.model.Login loginUser = loginController.authenticateUser(username, password, userType);

        if (loginUser != null) {
            String meter = loginUser.getMeterNo();
            new Project(meter, userType).setVisible(true);
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid login");
            usernameField.setText("");
            passwordField.setText("");
        }
    }
    
    /**
     * Handle cancel button action
     */
    private void handleCancelAction() {
        this.setVisible(false);
        System.exit(0);
    }
    
    /**
     * Handle signup button action
     */
    private void handleSignupAction() {
        this.setVisible(false);
        new Signup().setVisible(true);
    }

    /**
     * Main method as entry point to the application
     */
    public static void main(String[] args) {
        new Login().setVisible(true);
    }
}