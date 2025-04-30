package Electricity.view.customer;

import Electricity.util.ResourceUtil;
import java.awt.*;
import java.io.File;
import javax.swing.*;

/**
 * BaseCustomerView class providing common functionality for customer views.
 * Contains shared methods for UI initialization, validation, and error handling.
 */
public abstract class BaseCustomerView extends JFrame {
    // Common UI constants
    protected static final Color BUTTON_BG = Color.BLACK;
    protected static final Color BUTTON_FG = Color.WHITE;
    protected static final Color SUCCESS_COLOR = new Color(0, 102, 0);
    protected static final Color ERROR_COLOR = Color.RED;
    protected static final Color STATUS_COLOR = Color.RED;
    protected static final Font TITLE_FONT = new Font("Tahoma", Font.BOLD, 24);
    protected static final Font LABEL_FONT = new Font("Tahoma", Font.PLAIN, 14);
    protected static final Font BUTTON_FONT = new Font("Tahoma", Font.PLAIN, 14);
    
    // Common field dimensions
    protected static final int DEFAULT_FIELD_WIDTH = 200;
    protected static final int DEFAULT_FIELD_HEIGHT = 20;
    
    // Customer data
    protected String meterNumber;
    
    /**
     * Constructor for the base customer view
     * 
     * @param title The window title
     * @param meterNumber The customer's meter number
     */
    public BaseCustomerView(String title, String meterNumber) {
        super(title);
        this.meterNumber = meterNumber;
    }
    
    /**
     * Initialize common window settings
     * 
     * @param width The window width
     * @param height The window height
     * @param x The window x position (or -1 for center)
     * @param y The window y position (or -1 for center)
     */
    protected void initializeWindowSettings(int width, int height, int x, int y) {
        if (x < 0 || y < 0) {
            // Center on screen if position not specified
            setLocationRelativeTo(null);
        } else {
            setLocation(x, y);
        }
        
        setSize(width, height);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
    }
    
    /**
     * Create and add a title label to the form
     * 
     * @param title The title text
     * @param x X position
     * @param y Y position
     * @param width Width
     * @return The created JLabel
     */
    protected JLabel createTitleLabel(String title, int x, int y, int width) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setBounds(x, y, width, 40);
        add(titleLabel);
        return titleLabel;
    }
    
    /**
     * Create and add a standard button
     * 
     * @param text Button text
     * @param x X position
     * @param y Y position
     * @param width Width
     * @param height Height
     * @param color Background color (or null for default)
     * @return The created JButton
     */
    protected JButton createButton(String text, int x, int y, int width, int height, Color color) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBackground(color != null ? color : BUTTON_BG);
        button.setForeground(BUTTON_FG);
        button.setFont(BUTTON_FONT);
        add(button);
        return button;
    }
    
    /**
     * Create and add a form label
     * 
     * @param text Label text
     * @param x X position
     * @param y Y position
     * @param width Width
     * @param height Height
     * @return The created JLabel
     */
    protected JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(LABEL_FONT);
        add(label);
        return label;
    }
    
    /**
     * Create and add a text field
     * 
     * @param x X position
     * @param y Y position
     * @param width Width
     * @param height Height
     * @return The created JTextField
     */
    protected JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        add(textField);
        return textField;
    }
    
    /**
     * Add a decorative image to the form
     * 
     * @param imagePath Path to the image from the resources folder
     * @param x X position
     * @param y Y position
     * @param width Width
     * @param height Height
     * @return The created JLabel containing the image, or null if error
     */
    protected JLabel addDecorativeImage(String imagePath, int x, int y, int width, int height) {
        try {
            File imageFile = ResourceUtil.getResourcePath("images/" + imagePath);
            if (imageFile != null) {
                ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
                Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setBounds(x, y, width, height);
                add(imageLabel);
                return imageLabel;
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Display an error message
     * 
     * @param message The error message
     * @param title The dialog title
     */
    protected void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Display a success message
     * 
     * @param message The success message
     */
    protected void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Display a confirmation dialog
     * 
     * @param message The confirmation message
     * @param title The dialog title
     * @return true if confirmed, false otherwise
     */
    protected boolean showConfirmationDialog(String message, String title) {
        int result = JOptionPane.showConfirmDialog(
            this, message, title, JOptionPane.YES_NO_OPTION
        );
        return result == JOptionPane.YES_OPTION;
    }
}