package Electricity.view.common;

import Electricity.view.admin.CalculateBill;
import Electricity.view.admin.CustomerDetails;
import Electricity.view.admin.NewCustomer;
import Electricity.view.customer.BillDetails;
import Electricity.view.customer.PayBill;
import Electricity.view.customer.UpdateInformation;
import Electricity.view.customer.ViewInformation;
import Electricity.view.reports.DepositDetails;
import Electricity.view.reports.GenerateBill;
import Electricity.util.ResourceUtil;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

/**
 * Project view component that serves as the main dashboard of the application.
 * Different menus are presented based on user type (Admin/Customer).
 */
public class Project extends JFrame implements ActionListener {
    // Constants for UI
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1030;
    private static final int BACKGROUND_WIDTH = 1900;
    private static final int BACKGROUND_HEIGHT = 950;
    private static final int ICON_SIZE = 20;
    private static final Font MENU_FONT = new Font("monospaced", Font.PLAIN, 12);
    
    // User data
    private String meter;
    private String userType;
    
    // Menu components
    private JMenuBar menuBar;
    private JMenu masterMenu;
    private JMenu infoMenu;
    private JMenu userMenu;
    private JMenu reportMenu;
    private JMenu utilityMenu;
    private JMenu exitMenu;

    /**
     * Constructor to initialize the dashboard
     * @param meter Meter number of logged in user
     * @param person User type (Admin/Customer)
     */
    public Project(String meter, String person) {
        super("Electricity Billing System");
        this.meter = meter;
        this.userType = person;

        // Set up the UI
        initializeWindowSettings();
        setupBackgroundImage();
        
        // Create menus
        menuBar = new JMenuBar();
        createMenus();
        
        // Add appropriate menus based on user type
        addMenusBasedOnUserType();
        
        // Set the menu bar
        setJMenuBar(menuBar);
        
        // Final window settings
        setFont(new Font("Senserif", Font.BOLD, 16));
        setLayout(new FlowLayout());
        setVisible(false);
    }
    
    /**
     * Initialize window settings
     */
    private void initializeWindowSettings() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    }
    
    /**
     * Setup background image
     */
    private void setupBackgroundImage() {
        try {
            File backgroundFile = ResourceUtil.getResourcePath("images/elect1.jpg");
            if (backgroundFile != null) {
                ImageIcon backgroundIcon = new ImageIcon(backgroundFile.getAbsolutePath());
                Image scaledImage = backgroundIcon.getImage().getScaledInstance(
                        BACKGROUND_WIDTH, BACKGROUND_HEIGHT, Image.SCALE_DEFAULT);
                JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));
                add(backgroundLabel);
            }
        } catch (Exception e) {
            System.out.println("Error loading background image: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create all menus
     */
    private void createMenus() {
        createMasterMenu();
        createInfoMenu();
        createUserMenu();
        createReportMenu();
        createUtilityMenu();
        createExitMenu();
    }
    
    /**
     * Create the Master menu (Admin Only)
     */
    private void createMasterMenu() {
        masterMenu = new JMenu("Master");
        masterMenu.setForeground(Color.BLUE);
        
        // New Customer menu item
        JMenuItem newCustomerItem = createMenuItem(
                "New Customer", "images/icon1.png", 
                'D', KeyEvent.VK_D, Color.WHITE);
        
        // Customer Details menu item
        JMenuItem customerDetailsItem = createMenuItem(
                "Customer Details", "images/icon2.png", 
                'M', KeyEvent.VK_M, Color.WHITE);
        
        // Deposit Details menu item
        JMenuItem depositDetailsItem = createMenuItem(
                "Deposit Details", "images/icon3.png", 
                'N', KeyEvent.VK_N, Color.WHITE);
        
        // Calculate Bill menu item
        JMenuItem calculateBillItem = createMenuItem(
                "Calculate Bill", "images/icon5.png", 
                'B', KeyEvent.VK_B, Color.WHITE);
        
        // Add items to Master menu
        masterMenu.add(newCustomerItem);
        masterMenu.add(customerDetailsItem);
        masterMenu.add(depositDetailsItem);
        masterMenu.add(calculateBillItem);
    }
    
    /**
     * Create the Information menu (Customer Only)
     */
    private void createInfoMenu() {
        infoMenu = new JMenu("Information");
        infoMenu.setForeground(Color.RED);
        
        // Update Information menu item
        JMenuItem updateInfoItem = createMenuItem(
                "Update Information", "images/icon4.png", 
                'P', KeyEvent.VK_P, Color.WHITE);
        
        // View Information menu item
        JMenuItem viewInfoItem = createMenuItem(
                "View Information", "images/icon6.png", 
                'L', KeyEvent.VK_L, Color.WHITE);
        
        // Add items to Information menu
        infoMenu.add(updateInfoItem);
        infoMenu.add(viewInfoItem);
    }
    
    /**
     * Create the User menu (Customer Only)
     */
    private void createUserMenu() {
        userMenu = new JMenu("User");
        userMenu.setForeground(Color.RED);
        
        // Pay Bill menu item
        JMenuItem payBillItem = createMenuItem(
                "Pay Bill", "images/icon4.png", 
                'P', KeyEvent.VK_P, Color.WHITE);
        
        // Bill Details menu item
        JMenuItem billDetailsItem = createMenuItem(
                "Bill Details", "images/icon6.png", 
                'L', KeyEvent.VK_L, Color.WHITE);
        
        // Add items to User menu
        userMenu.add(payBillItem);
        userMenu.add(billDetailsItem);
    }
    
    /**
     * Create the Report menu (Customer Only)
     */
    private void createReportMenu() {
        reportMenu = new JMenu("Report");
        reportMenu.setForeground(Color.BLUE);
        
        // Generate Bill menu item
        JMenuItem generateBillItem = createMenuItem(
                "Generate Bill", "images/icon7.png", 
                'R', KeyEvent.VK_R, Color.WHITE);
        
        // Add item to Report menu
        reportMenu.add(generateBillItem);
    }
    
    /**
     * Create the Utility menu (Both Admin and Customer)
     */
    private void createUtilityMenu() {
        utilityMenu = new JMenu("Utility");
        utilityMenu.setForeground(Color.RED);
        
        // Notepad menu item
        JMenuItem notepadItem = createMenuItem(
                "Notepad", "images/icon12.png", 
                'C', KeyEvent.VK_C, Color.WHITE);
        
        // Calculator menu item
        JMenuItem calculatorItem = createMenuItem(
                "Calculator", "images/icon9.png", 
                'X', KeyEvent.VK_X, Color.WHITE);
        
        // Web Browser menu item
        JMenuItem browserItem = createMenuItem(
                "Web Browser", "images/icon10.png", 
                'W', KeyEvent.VK_W, Color.WHITE);
        
        // Add items to Utility menu
        utilityMenu.add(notepadItem);
        utilityMenu.add(calculatorItem);
        utilityMenu.add(browserItem);
    }
    
    /**
     * Create the Exit menu (Both Admin and Customer)
     */
    private void createExitMenu() {
        exitMenu = new JMenu("Logout");
        exitMenu.setForeground(Color.BLUE);
        
        // Logout menu item
        JMenuItem logoutItem = createMenuItem(
                "Logout", "images/icon11.png", 
                'Z', KeyEvent.VK_Z, Color.WHITE);
        
        // Add item to Exit menu
        exitMenu.add(logoutItem);
    }
    
    /**
     * Helper method to create menu items with consistent style
     * 
     * @param text Text for the menu item
     * @param iconPath Path to the icon resource
     * @param mnemonic Mnemonic key
     * @param acceleratorKey Accelerator key code
     * @param bgColor Background color
     * @return Configured JMenuItem
     */
    private JMenuItem createMenuItem(String text, String iconPath, 
            char mnemonic, int acceleratorKey, Color bgColor) {
        
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setFont(MENU_FONT);
        
        // Set icon if available
        try {
            File iconFile = ResourceUtil.getResourcePath(iconPath);
            if (iconFile != null) {
                ImageIcon icon = new ImageIcon(iconFile.getAbsolutePath());
                Image scaledImage = icon.getImage().getScaledInstance(
                        ICON_SIZE, ICON_SIZE, Image.SCALE_DEFAULT);
                menuItem.setIcon(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            System.out.println("Error loading " + iconPath + ": " + e.getMessage());
        }
        
        // Set keyboard shortcuts
        menuItem.setMnemonic(mnemonic);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(acceleratorKey, ActionEvent.CTRL_MASK));
        menuItem.setBackground(bgColor);
        
        // Add action listener
        menuItem.addActionListener(this);
        
        return menuItem;
    }
    
    /**
     * Add menus to the menu bar based on user type
     */
    private void addMenusBasedOnUserType() {
        if (userType.equals("Admin")) {
            menuBar.add(masterMenu);
        } else {
            menuBar.add(infoMenu);
            menuBar.add(userMenu);
            menuBar.add(reportMenu);
        }
        
        // These menus are available to both user types
        menuBar.add(utilityMenu);
        menuBar.add(exitMenu);
    }

    /**
     * Handle menu item actions
     */
    public void actionPerformed(ActionEvent ae) {
        String menuAction = ae.getActionCommand();
        
        // Use separate methods to handle different menu categories
        handleAdminActions(menuAction);
        handleCustomerActions(menuAction);
        handleUtilityActions(menuAction);
        handleLogoutAction(menuAction);
    }
    
    /**
     * Handle admin-specific menu actions
     * 
     * @param action The menu action command
     */
    private void handleAdminActions(String action) {
        switch (action) {
            case "Customer Details":
                new CustomerDetails().setVisible(true);
                break;
            case "New Customer":
                new NewCustomer().setVisible(true);
                break;
            case "Calculate Bill":
                new CalculateBill().setVisible(true);
                break;
            case "Deposit Details":
                new DepositDetails().setVisible(true);
                break;
        }
    }
    
    /**
     * Handle customer-specific menu actions
     * 
     * @param action The menu action command
     */
    private void handleCustomerActions(String action) {
        switch (action) {
            case "Pay Bill":
                new PayBill(meter).setVisible(true);
                break;
            case "View Information":
                new ViewInformation(meter).setVisible(true);
                break;
            case "Update Information":
                new UpdateInformation(meter).setVisible(true);
                break;
            case "Bill Details":
                new BillDetails(meter).setVisible(true);
                break;
            case "Generate Bill":
                new GenerateBill(meter).setVisible(true);
                break;
        }
    }
    
    /**
     * Handle utility menu actions
     * 
     * @param action The menu action command
     */
    private void handleUtilityActions(String action) {
        switch (action) {
            case "Notepad":
                launchExternalApplication("notepad.exe", "Notepad not found on your system");
                break;
            case "Calculator":
                launchExternalApplication("calc.exe", "Calculator not found on your system");
                break;
            case "Web Browser":
                launchWebBrowser();
                break;
        }
    }
    
    /**
     * Handle logout action
     * 
     * @param action The menu action command
     */
    private void handleLogoutAction(String action) {
        if (action.equals("Logout")) {
            this.setVisible(false);
            new Login().setVisible(true);
        }
    }
    
    /**
     * Launch an external application
     * 
     * @param command Command to execute
     * @param errorMessage Error message to display if command fails
     */
    private void launchExternalApplication(String command, String errorMessage) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, errorMessage);
        }
    }
    
    /**
     * Launch web browser with fallback from Edge to Chrome
     */
    private void launchWebBrowser() {
        try {
            Runtime.getRuntime().exec("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe");
        } catch (Exception e) {
            try {
                // Try Chrome if Edge is not available
                Runtime.getRuntime().exec("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Web browser not found on your system");
            }
        }
    }

    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        new Project("", "").setVisible(true);
    }
}