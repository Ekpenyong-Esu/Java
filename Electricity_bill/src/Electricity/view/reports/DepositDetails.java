package Electricity.view.reports;

import Electricity.dataAccessOutput.BillDAO;
import Electricity.dataAccessOutput.CustomerDAO;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.text.MessageFormat;
import net.proteanit.sql.DbUtils;

/**
 * View class for displaying bill deposit details and payment history.
 * Uses MVC pattern with BillDAO for data operations.
 * Provides filtering by meter number and month, and printing capability.
 */
public class DepositDetails extends JFrame implements ActionListener {
    // Constants for UI
    private static final Color PRIMARY_COLOR = new Color(66, 139, 202);
    private static final Color WARNING_COLOR = new Color(240, 173, 78);
    private static final Color SUCCESS_COLOR = new Color(92, 184, 92);
    private static final Color TITLE_TEXT_COLOR = new Color(0, 102, 204);
    private static final int FORM_WIDTH = 700;
    private static final int FORM_HEIGHT = 750;
    private static final int TABLE_HEIGHT = 550;
    private static final Font TITLE_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 12);
    private static final Font TABLE_FONT = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 12);
    
    // UI components grouped by function
    // - Labels
    private JLabel titleLabel;
    private JLabel meterFilterLabel;
    private JLabel monthFilterLabel;
    
    // - Input fields
    private Choice meterNumberChoice;
    private Choice billMonthChoice;
    
    // - Buttons
    private JButton searchButton;
    private JButton printButton;
    private JButton clearButton;
    
    // - Table
    private JTable dataTable;
    
    // Table column headers
    private final String[] TABLE_COLUMNS = {
        "Meter Number", "Month", "Units", "Total Bill", "Status"
    };
    
    // Data access objects
    private BillDAO billDAO;
    private CustomerDAO customerDAO;

    /**
     * Constructor initializes the deposit details view
     */
    public DepositDetails() {
        super("Deposit Details");
        
        // Initialize data access objects
        initializeDataObjects();
        
        // Setup UI components
        initializeWindowSettings();
        initializeTitle();
        initializeFilterControls();
        initializeActionButtons();
        initializeDataTable();
        
        // Load initial data
        loadData();
    }
    
    /**
     * Initialize data access objects
     */
    private void initializeDataObjects() {
        billDAO = new BillDAO();
        customerDAO = new CustomerDAO();
    }
    
    /**
     * Initialize window settings
     */
    private void initializeWindowSettings() {
        setSize(FORM_WIDTH, FORM_HEIGHT);
        setLocation(600, 150);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }
    
    /**
     * Initialize title label
     */
    private void initializeTitle() {
        titleLabel = new JLabel("Bill Payment History");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TITLE_TEXT_COLOR);
        titleLabel.setBounds(250, 10, 200, 25);
        add(titleLabel);
    }
    
    /**
     * Initialize filter controls
     */
    private void initializeFilterControls() {
        // Meter Number filter
        meterFilterLabel = new JLabel("Filter by Meter Number");
        meterFilterLabel.setFont(LABEL_FONT);
        meterFilterLabel.setBounds(30, 50, 150, 20);
        add(meterFilterLabel);

        meterNumberChoice = new Choice();
        meterNumberChoice.add("All Meters"); // Default option
        meterNumberChoice.setBounds(180, 50, 150, 20);
        add(meterNumberChoice);

        // Month filter
        monthFilterLabel = new JLabel("Filter By Month");
        monthFilterLabel.setFont(LABEL_FONT);
        monthFilterLabel.setBounds(380, 50, 120, 20);
        add(monthFilterLabel);

        billMonthChoice = new Choice();
        billMonthChoice.add("All Months"); // Default option
        billMonthChoice.setBounds(500, 50, 150, 20);
        populateMonths();
        add(billMonthChoice);
    }
    
    /**
     * Initialize action buttons
     */
    private void initializeActionButtons() {
        // Search button
        searchButton = new JButton("Search");
        searchButton.setBounds(180, 90, 100, 30);
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(this);
        add(searchButton);

        // Clear filters button
        clearButton = new JButton("Clear Filters");
        clearButton.setBounds(300, 90, 120, 30);
        clearButton.setBackground(WARNING_COLOR);
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(this);
        add(clearButton);

        // Print button
        printButton = new JButton("Print");
        printButton.setBounds(440, 90, 100, 30);
        printButton.setBackground(SUCCESS_COLOR);
        printButton.setForeground(Color.WHITE);
        printButton.setFocusPainted(false);
        printButton.addActionListener(this);
        add(printButton);
    }
    
    /**
     * Initialize data table with scrolling
     */
    private void initializeDataTable() {
        dataTable = new JTable();
        configureTableAppearance();
        
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBounds(20, 140, 660, TABLE_HEIGHT);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Set table header properties
        JTableHeader header = dataTable.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        
        add(scrollPane);
    }
    
    /**
     * Configure table appearance settings
     */
    private void configureTableAppearance() {
        dataTable.setFont(TABLE_FONT);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.setShowGrid(true);
        dataTable.setGridColor(Color.LIGHT_GRAY);
        dataTable.setIntercellSpacing(new Dimension(10, 5));
    }

    /**
     * Populate the month dropdown with all months
     */
    private void populateMonths() {
        String[] months = {
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        };

        for (String month : months) {
            billMonthChoice.add(month);
        }
    }

    /**
     * Load initial data from database
     */
    private void loadData() {
        try {
            // Load all bills for initial display
            ResultSet billsData = billDAO.getAllBillsResultSet();
            if (billsData != null) {
                dataTable.setModel(DbUtils.resultSetToTableModel(billsData));
                formatTable();
            }

            // Populate meter numbers from customer data
            List<String> meterNumbers = customerDAO.getAllMeterNumbers();
            for (String meter : meterNumbers) {
                meterNumberChoice.add(meter);
            }
        } catch (Exception e) {
            showErrorMessage("Error loading data", e);
        }
    }
    
    /**
     * Format the table for better readability
     */
    private void formatTable() {
        // Set preferred column widths if needed
        if (dataTable.getColumnCount() >= 5) {
            dataTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Meter
            dataTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Month
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Units
            dataTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Total
            dataTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Status
        }
        
        // Set row height for better readability
        dataTable.setRowHeight(25);
        
        // Set auto resize mode
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    /**
     * Handle button click events
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == searchButton) {
            searchRecords();
        } else if (event.getSource() == printButton) {
            printRecords();
        } else if (event.getSource() == clearButton) {
            clearFilters();
        }
    }

    /**
     * Clear all applied filters and reload data
     */
    private void clearFilters() {
        meterNumberChoice.select("All Meters");
        billMonthChoice.select("All Months");
        
        // Reload all data
        try {
            ResultSet allData = billDAO.getAllBillsResultSet();
            if (allData != null) {
                dataTable.setModel(DbUtils.resultSetToTableModel(allData));
                formatTable();
            }
        } catch (Exception e) {
            showErrorMessage("Error clearing filters", e);
        }
    }

    /**
     * Search records based on selected filters
     */
    private void searchRecords() {
        String selectedMeter = meterNumberChoice.getSelectedItem();
        String selectedMonth = billMonthChoice.getSelectedItem();

        try {
            ResultSet filteredData = getFilteredData(selectedMeter, selectedMonth);
            
            // Update table with filtered data
            if (filteredData != null) {
                dataTable.setModel(DbUtils.resultSetToTableModel(filteredData));
                formatTable();
                
                // Show message if no records found
                if (dataTable.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, 
                        "No records found matching the criteria.",
                        "Search Results", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            showErrorMessage("Error searching records", e);
        }
    }
    
    /**
     * Get filtered data based on selections
     * 
     * @param meter Selected meter number or "All Meters"
     * @param month Selected month or "All Months"
     * @return ResultSet with filtered data
     * @throws Exception if database query fails
     */
    private ResultSet getFilteredData(String meter, String month) throws Exception {
        // Apply appropriate filters based on selections
        if (!meter.equals("All Meters") && !month.equals("All Months")) {
            // Filter by both meter number and month
            return billDAO.getBillsByMeterAndMonth(meter, month);
        } else if (!meter.equals("All Meters")) {
            // Filter by meter number only
            return billDAO.getBillsByMeterResultSet(meter);
        } else if (!month.equals("All Months")) {
            // Filter by month only
            return billDAO.getBillsByMonthResultSet(month);
        } else {
            // No filters applied, show all
            return billDAO.getAllBillsResultSet();
        }
    }

    /**
     * Print the current table contents
     */
    private void printRecords() {
        try {
            if (dataTable.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, 
                    "No records to print.",
                    "Print Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Show print preview dialog
            MessageFormat header = new MessageFormat("Bill Deposit Details - {0}");
            MessageFormat footer = new MessageFormat("Page {0}");
            
            boolean complete = dataTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
            
            if (complete) {
                JOptionPane.showMessageDialog(this,
                    "Printing completed successfully.",
                    "Print Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Printing was cancelled or failed.",
                    "Print Message", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            showErrorMessage("Error printing", e);
        }
    }
    
    /**
     * Show error message dialog and print stack trace
     * 
     * @param message Message to display
     * @param e Exception that occurred
     */
    private void showErrorMessage(String message, Exception e) {
        JOptionPane.showMessageDialog(this, 
            message + ": " + e.getMessage(),
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        try {
            // Set system look and feel for better appearance
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        new DepositDetails();
    }
}