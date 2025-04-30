package Electricity.view.admin;

import Electricity.util.Conn;
import Electricity.util.ResourceUtil;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

/**
 * MeterInfo view component for setting up meter properties.
 * Used by administrators after adding a new customer.
 * Allows configuration of meter location, type, phase code, and bill type.
 */
public class MeterInfo extends JFrame implements ActionListener {
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
    // - Labels for field names
    private JLabel meterNumberLabel;
    private JLabel meterLocationLabel;
    private JLabel meterTypeLabel;
    private JLabel billTypeLabel;
    private JLabel phaseCodeLabel;
    private JLabel daysLabel;
    private JLabel noteLabel;
    private JLabel imageLabel;
    
    // - Display value labels
    private JLabel meterNumberValueLabel;
    private JLabel daysValueLabel;
    private JLabel noteValueLabel;
    
    // - Input fields
    private Choice meterLocationChoice;
    private Choice meterTypeChoice;
    private Choice phaseCodeChoice;
    private Choice billTypeChoice;
    
    // - Buttons
    private JButton submitButton;
    private JButton cancelButton;
    
    // - Panel
    private JPanel mainPanel;

    /**
     * Constructor to initialize the view
     * @param meterNumber The meter number to set up
     */
    public MeterInfo(String meterNumber) {
        super("Meter Information");
        
        // Setup UI components
        initializeWindowSettings();
        initializePanel();
        initializeTitle();
        initializeFormFields(meterNumber);
        initializeButtons();
        
        // Add decorative image
        JLabel decorativeImageLabel = createDecorativeImage();
        
        // Set layout
        setLayout(new BorderLayout());
        add(mainPanel, "Center");
        if (decorativeImageLabel != null) {
            add(decorativeImageLabel, "West");
        }
    }
    
    /**
     * Initialize window settings
     */
    private void initializeWindowSettings() {
        setLocation(600, 200);
        setSize(FORM_WIDTH, FORM_HEIGHT);
        getContentPane().setBackground(Color.WHITE);
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
        JLabel titleLabel = new JLabel("Meter Information");
        titleLabel.setBounds(180, 10, 200, 26);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
        mainPanel.add(titleLabel);
    }
    
    /**
     * Initialize all form input fields
     * @param meterNumber The meter number to set
     */
    private void initializeFormFields(String meterNumber) {
        // Meter Number (read-only)
        meterNumberLabel = new JLabel("Meter Number");
        meterNumberLabel.setBounds(LABEL_X, 80, 100, 20);
        meterNumberValueLabel = new JLabel(meterNumber);
        meterNumberValueLabel.setBounds(FIELD_X, 80, FIELD_WIDTH, 20);
        mainPanel.add(meterNumberLabel);
        mainPanel.add(meterNumberValueLabel);
        
        // Meter Location
        meterLocationLabel = new JLabel("Meter Location");
        meterLocationLabel.setBounds(LABEL_X, 120, 100, 20);
        meterLocationChoice = new Choice();
        populateMeterLocationOptions();
        meterLocationChoice.setBounds(FIELD_X, 120, FIELD_WIDTH, 20);
        mainPanel.add(meterLocationLabel);
        mainPanel.add(meterLocationChoice);
        
        // Meter Type
        meterTypeLabel = new JLabel("Meter Type");
        meterTypeLabel.setBounds(LABEL_X, 160, 100, 20);
        meterTypeChoice = new Choice();
        populateMeterTypeOptions();
        meterTypeChoice.setBounds(FIELD_X, 160, FIELD_WIDTH, 20);
        mainPanel.add(meterTypeLabel);
        mainPanel.add(meterTypeChoice);
        
        // Phase Code
        phaseCodeLabel = new JLabel("Phase Code");
        phaseCodeLabel.setBounds(LABEL_X, 200, 100, 20);
        phaseCodeChoice = new Choice();
        populatePhaseCodeOptions();
        phaseCodeChoice.setBounds(FIELD_X, 200, FIELD_WIDTH, 20);
        mainPanel.add(phaseCodeLabel);
        mainPanel.add(phaseCodeChoice);
        
        // Bill Type
        billTypeLabel = new JLabel("Bill Type");
        billTypeLabel.setBounds(LABEL_X, 240, 100, 20);
        billTypeChoice = new Choice();
        populateBillTypeOptions();
        billTypeChoice.setBounds(FIELD_X, 240, FIELD_WIDTH, 20);
        mainPanel.add(billTypeLabel);
        mainPanel.add(billTypeChoice);
        
        // Days (read-only)
        daysLabel = new JLabel("Days");
        daysLabel.setBounds(LABEL_X, 280, 100, 20);
        daysValueLabel = new JLabel("30 Days");
        daysValueLabel.setBounds(FIELD_X, 280, FIELD_WIDTH, 20);
        mainPanel.add(daysLabel);
        mainPanel.add(daysValueLabel);
        
        // Note (read-only)
        noteLabel = new JLabel("Note");
        noteLabel.setBounds(LABEL_X, 320, 100, 20);
        noteValueLabel = new JLabel("By Default Bill is calculated for 30 days only");
        noteValueLabel.setBounds(FIELD_X, 320, 300, 20);
        mainPanel.add(noteLabel);
        mainPanel.add(noteValueLabel);
    }
    
    /**
     * Populate meter location dropdown options
     */
    private void populateMeterLocationOptions() {
        meterLocationChoice.add("Outside");
        meterLocationChoice.add("Inside");
    }
    
    /**
     * Populate meter type dropdown options
     */
    private void populateMeterTypeOptions() {
        meterTypeChoice.add("Electric Meter");
        meterTypeChoice.add("Solar Meter");
        meterTypeChoice.add("Smart Meter");
    }
    
    /**
     * Populate phase code dropdown options
     */
    private void populatePhaseCodeOptions() {
        String[] phaseCodes = {"011", "022", "033", "044", "055", "066", "077", "088", "099"};
        for (String code : phaseCodes) {
            phaseCodeChoice.add(code);
        }
    }
    
    /**
     * Populate bill type dropdown options
     */
    private void populateBillTypeOptions() {
        billTypeChoice.add("Normal");
        billTypeChoice.add("Industrial");
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
                ImageIcon meterIcon = new ImageIcon(imageFile.getAbsolutePath());
                Image scaledImage = meterIcon.getImage().getScaledInstance(150, 300, Image.SCALE_DEFAULT);
                return new JLabel(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
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
        String meterNumber = meterNumberValueLabel.getText();
        String meterLocation = meterLocationChoice.getSelectedItem();
        String meterType = meterTypeChoice.getSelectedItem();
        String phaseCode = phaseCodeChoice.getSelectedItem();
        String billType = billTypeChoice.getSelectedItem();
        String days = "30";

        try {
            // Create database connection and execute insert query
            Conn connection = new Conn();
            String query = "INSERT INTO meter_info VALUES('" 
                + meterNumber + "','" 
                + meterLocation + "','" 
                + meterType + "','" 
                + phaseCode + "','" 
                + billType + "','" 
                + days + "')";
                
            connection.statement.executeUpdate(query);
            JOptionPane.showMessageDialog(this, "Meter Info Added Successfully");
            this.setVisible(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving meter information: " + ex.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        new MeterInfo("").setVisible(true);
    }
}
