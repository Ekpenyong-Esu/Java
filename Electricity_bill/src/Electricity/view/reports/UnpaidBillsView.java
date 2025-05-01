package Electricity.view.reports;

import Electricity.controller.BillController;
import Electricity.controller.CustomerController;
import Electricity.model.Bill;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.ResultSet;

/**
 * View class for displaying unpaid bills.
 * Uses MVC pattern with BillController for data operations.
 * Provides filtering by meter number and printing capability.
 */
public class UnpaidBillsView extends JFrame implements ActionListener {
    // Constants for UI
    private static final Color PRIMARY_COLOR = new Color(66, 139, 202);
    private static final Color WARNING_COLOR = new Color(240, 173, 78);
    private static final Color SUCCESS_COLOR = new Color(92, 184, 92);
    private static final Color TITLE_TEXT_COLOR = new Color(204, 0, 0); // Red for emphasis
    private static final int FORM_WIDTH = 700;
    private static final int FORM_HEIGHT = 650;
    private static final int TABLE_HEIGHT = 450;
    private static final Font TITLE_FONT = new Font("Tahoma", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 12);
    private static final Font TABLE_FONT = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 12);
    
    // UI components grouped by function
    // - Labels
    private JLabel titleLabel;
    private JLabel meterFilterLabel;
    private JLabel totalLabel;
    
    // - Input fields
    private Choice meterNumberChoice;
    
    // - Buttons
    private JButton searchButton;
    private JButton printButton;
    private JButton clearButton;
    private JButton paySelectedButton;
    
    // - Table
    private JTable dataTable;
    private DefaultTableModel tableModel;
    
    // Controllers
    private BillController billController;
    private CustomerController customerController;
    
    // Tracking total unpaid amount
    private double totalUnpaidAmount = 0.0;

    /**
     * Constructor initializes the unpaid bills view
     */
    public UnpaidBillsView() {
        super("Unpaid Bills");
        
        // Initialize controllers
        initializeControllers();
        
        // Setup UI components
        initializeWindowSettings();
        initializeTitle();
        initializeFilterControls();
        initializeActionButtons();
        initializeDataTable();
        initializeTotalPanel();
        
        // Load initial data
        loadUnpaidBills();
    }
    
    /**
     * Initialize controllers
     */
    private void initializeControllers() {
        billController = new BillController();
        customerController = new CustomerController();
    }
    
    /**
     * Initialize window settings
     */
    private void initializeWindowSettings() {
        setSize(FORM_WIDTH, FORM_HEIGHT);
        setLocation(600, 150);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Initialize title label
     */
    private void initializeTitle() {
        titleLabel = new JLabel("Unpaid Bills Report");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TITLE_TEXT_COLOR);
        titleLabel.setBounds(250, 10, 300, 30);
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
        populateMeterNumbers();
        add(meterNumberChoice);
    }
    
    /**
     * Initialize action buttons
     */
    private void initializeActionButtons() {
        // Search button
        searchButton = new JButton("Search");
        searchButton.setBounds(350, 50, 100, 30);
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(this);
        add(searchButton);

        // Clear filters button
        clearButton = new JButton("Show All");
        clearButton.setBounds(470, 50, 100, 30);
        clearButton.setBackground(WARNING_COLOR);
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(this);
        add(clearButton);

        // Print button
        printButton = new JButton("Print");
        printButton.setBounds(590, 50, 80, 30);
        printButton.setBackground(SUCCESS_COLOR);
        printButton.setForeground(Color.WHITE);
        printButton.setFocusPainted(false);
        printButton.addActionListener(this);
        add(printButton);
        
        // Pay Selected Bill button
        paySelectedButton = new JButton("Pay Selected Bill");
        paySelectedButton.setBounds(250, 550, 150, 30);
        paySelectedButton.setBackground(new Color(0, 153, 0));
        paySelectedButton.setForeground(Color.WHITE);
        paySelectedButton.setFocusPainted(false);
        paySelectedButton.addActionListener(this);
        add(paySelectedButton);
    }
    
    /**
     * Initialize data table with scrolling
     */
    private void initializeDataTable() {
        // Create table with custom model
        String[] columns = {"Meter Number", "Customer Name", "Month", "Units", "Amount (Rs)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        dataTable = new JTable(tableModel);
        configureTableAppearance();
        
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBounds(20, 100, 660, TABLE_HEIGHT);
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
     * Initialize total panel to display sum of unpaid bills
     */
    private void initializeTotalPanel() {
        totalLabel = new JLabel("Total Unpaid Amount: Rs 0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setForeground(Color.RED);
        totalLabel.setBounds(400, 550, 300, 30);
        add(totalLabel);
    }
    
    /**
     * Configure table appearance settings
     */
    private void configureTableAppearance() {
        dataTable.setFont(TABLE_FONT);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.setShowGrid(true);
        dataTable.setGridColor(Color.LIGHT_GRAY);
        dataTable.setRowHeight(25);
        dataTable.setIntercellSpacing(new Dimension(10, 5));
        
        // Set column widths
        TableColumnModel columnModel = dataTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100); // Meter
        columnModel.getColumn(1).setPreferredWidth(150); // Name
        columnModel.getColumn(2).setPreferredWidth(80);  // Month
        columnModel.getColumn(3).setPreferredWidth(60);  // Units
        columnModel.getColumn(4).setPreferredWidth(100); // Amount
        columnModel.getColumn(5).setPreferredWidth(80);  // Status
        
        // Set automatic row sorting
        dataTable.setAutoCreateRowSorter(true);
    }

    /**
     * Populate meter number dropdown from database
     */
    private void populateMeterNumbers() {
        List<String> meterNumbers = customerController.getAllMeterNumbers();
        for (String meter : meterNumbers) {
            meterNumberChoice.add(meter);
        }
    }

    /**
     * Load all unpaid bills from database
     */
    private void loadUnpaidBills() {
        // Clear existing table data
        tableModel.setRowCount(0);
        totalUnpaidAmount = 0.0;
        
        // Get unpaid bills display data from controller
        List<BillController.BillDisplayData> unpaidBills = billController.getUnpaidBillsDisplayData();
        
        // Populate table with unpaid bills
        for (BillController.BillDisplayData bill : unpaidBills) {
            String customerName = getCustomerName(bill.meterNumber);
            
            Object[] rowData = {
                bill.meterNumber,
                customerName,
                bill.month,
                bill.units,
                bill.totalBill,
                bill.status
            };
            tableModel.addRow(rowData);
            
            // Add to total unpaid amount
            try {
                totalUnpaidAmount += Double.parseDouble(bill.totalBill.replace(",", ""));
            } catch (NumberFormatException e) {
                System.out.println("Error parsing bill amount: " + e.getMessage());
            }
        }
        
        // Update total label
        updateTotalLabel();
        
        // Show message if no unpaid bills found
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No unpaid bills found.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Filter unpaid bills by meter number
     */
    private void filterByMeterNumber(String meterNumber) {
        // Clear existing table data
        tableModel.setRowCount(0);
        totalUnpaidAmount = 0.0;
        
        // Get all unpaid bills
        List<BillController.BillDisplayData> allUnpaidBills = billController.getUnpaidBillsDisplayData();
        
        // Filter and add matching bills to table
        for (BillController.BillDisplayData bill : allUnpaidBills) {
            // Skip if meter doesn't match (unless "All Meters" is selected)
            if (!meterNumber.equals("All Meters") && !bill.meterNumber.equals(meterNumber)) {
                continue;
            }
            
            String customerName = getCustomerName(bill.meterNumber);
            
            Object[] rowData = {
                bill.meterNumber,
                customerName,
                bill.month,
                bill.units,
                bill.totalBill,
                bill.status
            };
            tableModel.addRow(rowData);
            
            // Add to total unpaid amount
            try {
                totalUnpaidAmount += Double.parseDouble(bill.totalBill.replace(",", ""));
            } catch (NumberFormatException e) {
                System.out.println("Error parsing bill amount: " + e.getMessage());
            }
        }
        
        // Update total label
        updateTotalLabel();
    }
    
    /**
     * Get customer name from meter number
     */
    private String getCustomerName(String meterNumber) {
        try {
            return customerController.getCustomerByMeter(meterNumber).getName();
        } catch (Exception e) {
            return "Unknown";
        }
    }
    
    /**
     * Update total unpaid amount label
     */
    private void updateTotalLabel() {
        totalLabel.setText(String.format("Total Unpaid Amount: Rs %.2f", totalUnpaidAmount));
    }

    /**
     * Handle payment for selected bill
     */
    private void paySelectedBill() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a bill to pay.",
                "Selection Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get selected bill details
        String meterNumber = dataTable.getValueAt(selectedRow, 0).toString();
        String month = dataTable.getValueAt(selectedRow, 2).toString();
        String amountStr = dataTable.getValueAt(selectedRow, 4).toString();
        
        // Confirmation dialog
        int confirmation = JOptionPane.showConfirmDialog(this,
            "Confirm payment of Rs " + amountStr + " for Meter " + meterNumber + " (" + month + ")?",
            "Payment Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                // Process payment through controller
                double amount = Double.parseDouble(amountStr.replace(",", ""));
                boolean success = billController.processPayment(meterNumber, month, amount);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Payment successfully processed.",
                        "Payment Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh data
                    loadUnpaidBills();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Payment processing failed. Please try again.",
                        "Payment Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error processing payment: " + e.getMessage(),
                    "Payment Error",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
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
            MessageFormat header = new MessageFormat("Unpaid Bills Report - {0}");
            MessageFormat footer = new MessageFormat("Page {0} - Total Unpaid: Rs " + 
                String.format("%.2f", totalUnpaidAmount));
            
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
            JOptionPane.showMessageDialog(this,
                "Error printing: " + e.getMessage(),
                "Print Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Handle button click events
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == searchButton) {
            String selectedMeter = meterNumberChoice.getSelectedItem();
            filterByMeterNumber(selectedMeter);
        } else if (event.getSource() == printButton) {
            printRecords();
        } else if (event.getSource() == clearButton) {
            meterNumberChoice.select("All Meters");
            loadUnpaidBills();
        } else if (event.getSource() == paySelectedButton) {
            paySelectedBill();
        }
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
        
        SwingUtilities.invokeLater(() -> {
            new UnpaidBillsView().setVisible(true);
        });
    }
}