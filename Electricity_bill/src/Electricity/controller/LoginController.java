package Electricity.controller;

import Electricity.dataAccessOutput.LoginDAO;
import Electricity.model.Login;
// Import the LoginView class with fully qualified name to avoid conflicts

/**
 * Controller class for authentication and user management operations.
 * Handles the business logic between the view and data access layers.
 */
public class LoginController {

    private final LoginDAO loginDAO;

    /**
     * Constructor initializing the LoginDAO
     */
    public LoginController() {
        loginDAO = new LoginDAO();
    }

    /**
     * Authenticate a user with username, password and user type
     * @param username The username entered
     * @param password The password entered
     * @param userType The selected user type (Admin/Customer)
     * @return Login object if authenticated, null otherwise
     */
    public Login authenticateUser(String username, String password, String userType) {
        // Input validation
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                userType == null || userType.trim().isEmpty()) {
            return null;
        }

        return loginDAO.authenticate(username, password, userType);
    }

    /**
     * Register a new user
     * @param meterNo Meter number
     * @param username Username
     * @param name Full name
     * @param password Password
     * @param userType User type (Admin/Customer)
     * @return true if registration successful, false otherwise
     */
    public boolean registerUser(String meterNo, String username, String name, String password, String userType) {
        // Input validation
        if (meterNo == null || meterNo.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                name == null || name.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                userType == null || userType.trim().isEmpty()) {
            return false;
        }

        // Check if user already exists with this meter number
        Login existingUser = loginDAO.getLoginByMeterNo(meterNo);
        if (existingUser != null) {
            return false;
        }

        // Create new login object
        Login newLogin = new Login(meterNo, username, name, password, userType);

        return loginDAO.createUser(newLogin);
    }

    /**
     * Update user information
     * @param login Login object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateUser(Login login) {
        if (login == null) {
            return false;
        }

        return loginDAO.updateUser(login);
    }

    /**
     * Update specific user details
     * @param meterNo Meter number to identify the user
     * @param username Updated username
     * @param name Updated full name
     * @param password Updated password
     * @param userType Updated user type
     * @return true if update successful, false otherwise
     */
    public boolean updateUserDetails(String meterNo, String username, String name, String password, String userType) {
        // Input validation
        if (meterNo == null || meterNo.trim().isEmpty()) {
            return false;
        }

        // Get existing user
        Login user = getUserByMeterNo(meterNo);
        if (user == null) {
            return false;
        }

        // Update fields if provided
        if (username != null && !username.trim().isEmpty()) {
            user.setUsername(username);
        }
        if (name != null && !name.trim().isEmpty()) {
            user.setName(name);
        }
        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(password);
        }
        if (userType != null && !userType.trim().isEmpty()) {
            user.setUserType(userType);
        }

        return updateUser(user);
    }

    /**
     * Get user information by meter number
     * @param meterNo Meter number
     * @return Login object with user details if found, null otherwise
     */
    public Login getUserByMeterNo(String meterNo) {
        if (meterNo == null || meterNo.trim().isEmpty()) {
            return null;
        }

        return loginDAO.getLoginByMeterNo(meterNo);
    }

    /**
     * Delete a user by meter number
     * @param meterNo Meter number
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteUser(String meterNo) {
        if (meterNo == null || meterNo.trim().isEmpty()) {
            return false;
        }

        return loginDAO.deleteUser(meterNo);
    }

    /**
     * Navigate to the main dashboard after successful authentication
     * Following MVC pattern by handling navigation through controller
     * @param currentView The current login view that will be closed
     * @param username The authenticated username
     * @param password The authenticated password
     * @param userType The user type (Admin/Customer)
     * @return boolean indicating whether navigation was successful
     */
    public boolean showMainDashboard(javax.swing.JFrame currentView, String username, String password, String userType) {
        // Authenticate user
        Login loginUser = authenticateUser(username, password, userType);
        
        if (loginUser != null) {
            String meter = loginUser.getMeterNo();
            
            // Close the login view
            if (currentView != null) {
                currentView.setVisible(false);
            }
            
            // Open the main dashboard
            new Electricity.view.common.Project(meter, userType).setVisible(true);
            return true;
        }
        
        return false;
    }

    /**
     * Navigate to the login screen
     * Following MVC pattern by handling navigation through controller
     * @param currentView The current view that will be closed
     */
    public void showLoginScreen(javax.swing.JFrame currentView) {
        if (currentView != null) {
            currentView.setVisible(false);
        }
        // Using fully qualified name to avoid conflicts with model.Login
        new Electricity.view.common.Login().setVisible(true);
    }
}