package Electricity.view.admin;

import Electricity.controller.TaxController;
import Electricity.model.Tax;
import Electricity.util.Conn;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Admin view for managing tax rates in the electricity billing system
 */
public class TaxConfiguration extends JFrame implements ActionListener {
    
    private JLabel titleLabel, costPerUnitLabel, meterRentLabel, serviceChargeLabel, 
                   serviceTaxLabel, levyLabel, fixedTaxLabel, statusLabel;
    private JTextField costPerUnitField, meterRentField, serviceChargeField, 
                       serviceTaxField, levyField, fixedTaxField;
    private JButton saveButton, updateButton, cancelButton, clearButton, deleteButton;
    private JTable taxTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JPanel formPanel, buttonPanel, tablePanel, mainPanel;
    
    private TaxController taxController;
    private Tax selectedTax = null;

    /**
     * Constructor to initialize the Tax Configuration view
     */
    public TaxConfiguration() {
        super("Tax Configuration");
        this.taxController = new TaxController();
        
        // Create table structure if not exists
        createTaxTableIfNotExists();
        
        // Initialize UI components
        initComponents();
        
        // Load existing tax data
        loadTaxData();
        
        setVisible(true);
    }
    
    /**
     * Creates the tax table in the database if it doesn't exist
     */
    private void createTaxTableIfNotExists() {
        try {
            Connection conn = new Conn().connection;
            Statement stmt = conn.createStatement();
            
            String sql = "CREATE TABLE IF NOT EXISTS tax (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "cost_per_unit DOUBLE NOT NULL, " +
                    "meter_rent DOUBLE NOT NULL, " +
                    "service_charge DOUBLE NOT NULL, " +
                    "service_tax DOUBLE NOT NULL, " +
                    "climate_change_levy DOUBLE NOT NULL, " +
                    "fixed_tax DOUBLE NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error creating tax table: " + e.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes all UI components
     */
    private void initComponents() {
        // Set basic frame properties
        setSize(800, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Initialize labels
        titleLabel = new JLabel("Tax Configuration");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        costPerUnitLabel = new JLabel("Cost Per Unit (₹):");
        meterRentLabel = new JLabel("Meter Rent (₹):");
        serviceChargeLabel = new JLabel("Service Charge (₹):");
        serviceTaxLabel = new JLabel("Service Tax (%):");
        levyLabel = new JLabel("Climate Change Levy (₹):");
        fixedTaxLabel = new JLabel("Fixed Tax (₹):");
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        
        // Initialize text fields
        costPerUnitField = new JTextField(10);
        meterRentField = new JTextField(10);
        serviceChargeField = new JTextField(10);
        serviceTaxField = new JTextField(10);
        levyField = new JTextField(10);
        fixedTaxField = new JTextField(10);
        
        // Initialize buttons
        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        cancelButton = new JButton("Cancel");
        clearButton = new JButton("Clear");
        deleteButton = new JButton("Delete");
        
        // Add action listeners to buttons
        saveButton.addActionListener(this);
        updateButton.addActionListener(this);
        cancelButton.addActionListener(this);
        clearButton.addActionListener(this);
        deleteButton.addActionListener(this);
        
        // Initially disable update and delete buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        // Initialize table
        String[] columnNames = {"ID", "Cost/Unit", "Meter Rent", "Service Charge", 
                               "Service Tax %", "Climate Levy", "Fixed Tax"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        taxTable = new JTable(tableModel);
        scrollPane = new JScrollPane(taxTable);
        
        // Add table selection listener
        taxTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && taxTable.getSelectedRow() != -1) {
                int row = taxTable.getSelectedRow();
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                selectedTax = taxController.getTaxById(id);

                if (selectedTax != null) {
                    costPerUnitField.setText(String.valueOf(selectedTax.getCostPerUnit()));
                    meterRentField.setText(String.valueOf(selectedTax.getMeterRent()));
                    serviceChargeField.setText(String.valueOf(selectedTax.getServiceCharge()));
                    serviceTaxField.setText(String.valueOf(selectedTax.getServiceTax()));
                    levyField.setText(String.valueOf(selectedTax.getClimateChangeLevy()));
                    fixedTaxField.setText(String.valueOf(selectedTax.getFixedTax()));

                    // Enable update and delete buttons, disable save button
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true); // Highlight delete button
                    saveButton.setEnabled(false);
                }
            }
        });
        
        // Set up form panel with labels and fields in grid layout
        formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(costPerUnitLabel);
        formPanel.add(costPerUnitField);
        formPanel.add(meterRentLabel);
        formPanel.add(meterRentField);
        formPanel.add(serviceChargeLabel);
        formPanel.add(serviceChargeField);
        formPanel.add(serviceTaxLabel);
        formPanel.add(serviceTaxField);
        formPanel.add(levyLabel);
        formPanel.add(levyField);
        formPanel.add(fixedTaxLabel);
        formPanel.add(fixedTaxField);
        
        // Set up button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(saveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);
        
        // Set up table panel
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Tax Rate History"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create main panel and add components
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Put everything together in a split layout
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                mainPanel,
                tablePanel);
        splitPane.setDividerLocation(400);
        
        // Add status label at bottom
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);
        
        // Add all components to frame
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(splitPane, BorderLayout.CENTER);
        contentPane.add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Loads existing tax data into the table
     */
    private void loadTaxData() {
        // Clear existing table data
        tableModel.setRowCount(0);
        
        // Get tax history from controller
        List<Tax> taxHistory = taxController.getTaxHistory();
        
        // Add each tax entry to the table
        for (Tax tax : taxHistory) {
            Object[] rowData = {
                tax.getId(),
                tax.getCostPerUnit(),
                tax.getMeterRent(),
                tax.getServiceCharge(),
                tax.getServiceTax(),
                tax.getClimateChangeLevy(),
                tax.getFixedTax()
            };
            tableModel.addRow(rowData);
        }
        
        // If there are no tax entries, set default values
        if (taxHistory.isEmpty()) {
            setDefaultTaxValues();
        }
    }
    
    /**
     * Sets default tax values in the form
     */
    private void setDefaultTaxValues() {
        costPerUnitField.setText("9.0");
        meterRentField.setText("50.0");
        serviceChargeField.setText("20.0");
        serviceTaxField.setText("5.0");
        levyField.setText("1.0");
        fixedTaxField.setText("18.0");
    }
    
    /**
     * Clears all form fields and resets the selection
     */
    private void clearForm() {
        costPerUnitField.setText("");
        meterRentField.setText("");
        serviceChargeField.setText("");
        serviceTaxField.setText("");
        levyField.setText("");
        fixedTaxField.setText("");
        
        selectedTax = null;
        taxTable.clearSelection();
        
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        saveButton.setEnabled(true);
        
        statusLabel.setText("");
    }
    
    /**
     * Validates all input fields
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateInputs() {
        try {
            double costPerUnit = Double.parseDouble(costPerUnitField.getText().trim());
            double meterRent = Double.parseDouble(meterRentField.getText().trim());
            double serviceCharge = Double.parseDouble(serviceChargeField.getText().trim());
            double serviceTax = Double.parseDouble(serviceTaxField.getText().trim());
            double levy = Double.parseDouble(levyField.getText().trim());
            double fixedTax = Double.parseDouble(fixedTaxField.getText().trim());
            
            if (costPerUnit < 0 || meterRent < 0 || serviceCharge < 0 || 
                serviceTax < 0 || levy < 0 || fixedTax < 0) {
                statusLabel.setText("Error: All values must be non-negative");
                return false;
            }
            
            return true;
        } catch (NumberFormatException e) {
            statusLabel.setText("Error: All fields must contain valid numbers");
            return false;
        }
    }
    
    /**
     * Handles button click events
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            if (validateInputs()) {
                double costPerUnit = Double.parseDouble(costPerUnitField.getText().trim());
                double meterRent = Double.parseDouble(meterRentField.getText().trim());
                double serviceCharge = Double.parseDouble(serviceChargeField.getText().trim());
                double serviceTax = Double.parseDouble(serviceTaxField.getText().trim());
                double levy = Double.parseDouble(levyField.getText().trim());
                double fixedTax = Double.parseDouble(fixedTaxField.getText().trim());
                
                boolean success = taxController.createTax(
                        costPerUnit, meterRent, serviceCharge, serviceTax, levy, fixedTax);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Tax rates saved successfully", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadTaxData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save tax rates", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == updateButton) {
            if (selectedTax != null && validateInputs()) {
                selectedTax.setCostPerUnit(Double.parseDouble(costPerUnitField.getText().trim()));
                selectedTax.setMeterRent(Double.parseDouble(meterRentField.getText().trim()));
                selectedTax.setServiceCharge(Double.parseDouble(serviceChargeField.getText().trim()));
                selectedTax.setServiceTax(Double.parseDouble(serviceTaxField.getText().trim()));
                selectedTax.setClimateChangeLevy(Double.parseDouble(levyField.getText().trim()));
                selectedTax.setFixedTax(Double.parseDouble(fixedTaxField.getText().trim()));
                
                boolean success = taxController.updateTaxRates(selectedTax);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Tax rates updated successfully", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadTaxData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update tax rates", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == clearButton) {
            clearForm();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }

    /**
     * Main method for testing the Tax Configuration view
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaxConfiguration());
    }
}