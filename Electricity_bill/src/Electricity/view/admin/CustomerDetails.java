package Electricity.view.admin;

import Electricity.controller.CustomerController;
import Electricity.model.Customer;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.text.MessageFormat;

/**
 * CustomerDetails view component for displaying customer information.
 * Used by administrators to view all customer records.
 * Uses MVC architecture with CustomerController for data operations.
 */
public class CustomerDetails extends JFrame implements ActionListener {
    // Constants for UI
    private static final int FORM_WIDTH = 1200;
    private static final int FORM_HEIGHT = 650;
    private static final int ROW_HEIGHT = 30;
    private static final int ADDRESS_COLUMN_WIDTH = 200;
    private static final Color BUTTON_BG = new Color(92, 184, 92);
    private static final Color BUTTON_FG = Color.WHITE;
    private static final Color TABLE_HEADER_BG = new Color(66, 139, 202);
    private static final Color TABLE_HEADER_FG = Color.WHITE;
    private static final Font TABLE_HEADER_FONT = new Font("SansSerif", Font.BOLD, 12);
    private static final Font TABLE_FONT = new Font("SansSerif", Font.PLAIN, 12);
    
    // UI components grouped by function
    // - Table
    private JTable customerTable;
    private JScrollPane scrollPane;
    
    // - Buttons
    private JButton printButton;
    private JButton searchButton;
    private JButton refreshButton;
    private JTextField searchField;
    
    // Table column configuration
    private final String[] tableColumnNames = {
        "Customer Name", "Meter Number", "Address", "City", "State", "Email", "Phone"
    };
    
    // Controller
    private CustomerController customerController;

    /**
     * Constructor to initialize the view
     */
    public CustomerDetails() {
        super("Customer Details");

        // Initialize controller
        initializeController();
        
        // Setup UI components
        initializeWindowSettings();
        initializeSearchPanel();
        initializeTable();
        
        // Load data
        loadCustomerData();
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
        setSize(FORM_WIDTH, FORM_HEIGHT);
        setLocation(400, 150);
        setLayout(new BorderLayout());
    }
    
    /**
     * Initialize search panel with controls
     */
    private void initializeSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Search field
        searchField = new JTextField(20);
        searchPanel.add(new JLabel("Search by Name or Meter:"));
        searchPanel.add(searchField);
        
        // Search button
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(searchButton);
        
        // Refresh button
        refreshButton = new JButton("Show All");
        refreshButton.addActionListener(this);
        searchPanel.add(refreshButton);
        
        // Print button
        printButton = new JButton("Print");
        printButton.setBackground(BUTTON_BG);
        printButton.setForeground(BUTTON_FG);
        printButton.addActionListener(this);
        searchPanel.add(printButton);
        
        // Add to frame
        add(searchPanel, BorderLayout.NORTH);
    }
    
    /**
     * Initialize table with scrollpane
     */
    private void initializeTable() {
        // Create empty table with column names
        DefaultTableModel model = new DefaultTableModel(tableColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        customerTable = new JTable(model);
        
        // Configure table appearance
        configureTableAppearance();
        
        // Add table to scrollpane
        scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Configure table appearance settings
     */
    private void configureTableAppearance() {
        customerTable.setFont(TABLE_FONT);
        customerTable.setRowHeight(ROW_HEIGHT);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setShowGrid(true);
        customerTable.setGridColor(Color.LIGHT_GRAY);
        customerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Set column widths for better readability
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(ADDRESS_COLUMN_WIDTH); // Address column wider
        
        // Configure table header
        JTableHeader header = customerTable.getTableHeader();
        header.setFont(TABLE_HEADER_FONT);
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TABLE_HEADER_FG);
    }

    /**
     * Load customer data from database via controller
     */
    private void loadCustomerData() {
        try {
            // Get all customers from controller
            List<Customer> customers = customerController.getAllCustomers();
            
            // Update table with customer data
            updateTableWithCustomers(customers);
            
            // Show message if no records
            if (customers.isEmpty()) {
                showNoRecordsMessage();
            }
        } catch (Exception e) {
            handleDataLoadError(e);
        }
    }
    
    /**
     * Update table with customer data
     * 
     * @param customers List of customer objects
     */
    private void updateTableWithCustomers(List<Customer> customers) {
        // Get the table model and clear existing rows
        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        model.setRowCount(0);
        
        // Fill data from customer objects
        for (Customer customer : customers) {
            Object[] row = {
                customer.getName(),
                customer.getMeter(),
                customer.getAddress(),
                customer.getCity(),
                customer.getState(),
                customer.getEmail(),
                customer.getPhone()
            };
            model.addRow(row);
        }
    }
    
    /**
     * Show message when no records are found
     */
    private void showNoRecordsMessage() {
        JOptionPane.showMessageDialog(this, 
            "No customer records found",
            "No Records", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Handle error when loading data
     * 
     * @param e Exception that occurred
     */
    private void handleDataLoadError(Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error loading customer data: " + e.getMessage(),
            "Data Load Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    
    /**
     * Search customers by name or meter number
     * 
     * @param searchTerm Term to search for
     */
    private void searchCustomers(String searchTerm) {
        try {
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search term");
                return;
            }
            
            List<Customer> results = customerController.searchCustomersByName(searchTerm);
            updateTableWithCustomers(results);
            
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No customers found matching '" + searchTerm + "'",
                    "Search Results", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error searching: " + e.getMessage(),
                "Search Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Handle button actions
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == printButton) {
            printCustomerTable();
        } else if (ae.getSource() == searchButton) {
            searchCustomers(searchField.getText().trim());
        } else if (ae.getSource() == refreshButton) {
            searchField.setText("");
            refresh();
        }
    }
    
    /**
     * Print the customer table
     */
    private void printCustomerTable() {
        try {
            if (customerTable.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No data to print");
                return;
            }
            
            // Show print dialog with headers
            MessageFormat header = new MessageFormat("Customer Details - {0}");
            MessageFormat footer = new MessageFormat("Page {0}");
            
            boolean complete = customerTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
            
            if (complete) {
                JOptionPane.showMessageDialog(this, 
                    "Printing completed successfully",
                    "Print Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error printing: " + e.getMessage(),
                "Print Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Refresh data in the table
     */
    public void refresh() {
        loadCustomerData();
    }

    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        new CustomerDetails().setVisible(true);
    }
}