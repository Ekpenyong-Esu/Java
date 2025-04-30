package Electricity.view.customer;

import Electricity.controller.BillController;
import Electricity.model.Bill;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * BillDetails view component for displaying bill history.
 * Used by customers to view their billing history.
 * Uses MVC architecture with BillController for data retrieval.
 */
public class BillDetails extends BaseCustomerView {
    // Constants for UI
    private static final int FORM_WIDTH = 700;
    private static final int FORM_HEIGHT = 650;
    private static final int ROW_HEIGHT = 30;
    private static final Color TABLE_HEADER_BG = new Color(66, 139, 202);
    private static final Color TABLE_HEADER_FG = Color.WHITE;
    private static final Font TABLE_HEADER_FONT = new Font("SansSerif", Font.BOLD, 12);
    private static final Font TABLE_FONT = new Font("SansSerif", Font.PLAIN, 12);
    
    // UI components grouped by function
    // - Table
    private JTable billTable;
    private JScrollPane scrollPane;
    
    // Table column configuration
    private final String[] columnNames = {"Meter Number", "Month", "Units", "Total Bill", "Status"};
    private final int[] columnWidths = {100, 100, 80, 100, 100};
    
    // Controller
    private BillController billController;

    /**
     * Constructor to initialize the view
     * @param meterNumber The meter number of the customer
     */
    public BillDetails(String meterNumber) {
        super("Bill Details", meterNumber);

        // Initialize controller
        initializeController();
        
        // Setup UI components
        initializeWindowSettings(FORM_WIDTH, FORM_HEIGHT, 600, 150);
        initializeTable();
        
        // Load data
        loadBillData();
    }
    
    /**
     * Initialize controller
     */
    private void initializeController() {
        billController = new BillController();
    }
    
    /**
     * Initialize table and scrollpane
     */
    private void initializeTable() {
        // Create empty table with column names
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        billTable = new JTable(model);
        
        // Configure table appearance
        configureTableAppearance();
        
        // Add table to scrollpane
        scrollPane = new JScrollPane(billTable);
        scrollPane.setBounds(0, 0, FORM_WIDTH, FORM_HEIGHT);
        add(scrollPane);
    }
    
    /**
     * Configure table appearance settings
     */
    private void configureTableAppearance() {
        billTable.setFont(TABLE_FONT);
        billTable.setRowHeight(ROW_HEIGHT);
        billTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        billTable.setShowGrid(true);
        billTable.setGridColor(Color.LIGHT_GRAY);
        billTable.setIntercellSpacing(new Dimension(10, 5));
        
        // Set column widths
        for (int i = 0; i < Math.min(columnWidths.length, billTable.getColumnCount()); i++) {
            billTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        
        // Configure table header
        JTableHeader header = billTable.getTableHeader();
        header.setFont(TABLE_HEADER_FONT);
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TABLE_HEADER_FG);
    }

    /**
     * Load bill data for the specified meter number using the controller
     */
    private void loadBillData() {
        try {
            // Get display data from controller
            List<BillController.BillDisplayData> displayData = billController.getBillDisplayData(meterNumber);
            
            // Update table with data
            updateTableWithData(displayData);
        } catch (Exception e) {
            handleDataLoadError(e);
        }
    }
    
    /**
     * Update table with bill display data
     * 
     * @param displayData List of bill display data objects
     */
    private void updateTableWithData(List<BillController.BillDisplayData> displayData) {
        // Get the table model
        DefaultTableModel model = (DefaultTableModel) billTable.getModel();
        
        // Clear existing rows
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        
        // Add data rows
        for (BillController.BillDisplayData data : displayData) {
            Object[] row = {
                data.meterNumber,
                data.month,
                data.units,
                data.totalBill,
                data.status
            };
            model.addRow(row);
        }
        
        // Show message if no records
        if (displayData.isEmpty()) {
            showNoRecordsMessage();
        }
    }
    
    /**
     * Show message when no records are found
     */
    private void showNoRecordsMessage() {
        showErrorMessage("No bill records found for meter " + meterNumber, "No Records");
    }
    
    /**
     * Handle error when loading data
     * 
     * @param e Exception that occurred
     */
    private void handleDataLoadError(Exception e) {
        showErrorMessage("Error loading bill data: " + e.getMessage(), "Data Load Error");
        e.printStackTrace();
    }

    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        new BillDetails("").setVisible(true);
    }
}