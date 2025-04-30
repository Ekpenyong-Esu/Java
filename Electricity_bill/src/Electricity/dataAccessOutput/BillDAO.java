package Electricity.dataAccessOutput;

import Electricity.model.Bill;
import Electricity.model.Tax;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Bill-related operations.
 * Handles CRUD operations for bill data.
 */
public class BillDAO extends BaseDAO {

    /**
     * Save a new bill to the database
     * @param bill Bill object with details
     * @return true if saved successfully, false otherwise
     */
    public boolean saveBill(Bill bill) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "INSERT INTO bill (meter, month, units, total_bill, status) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bill.getMeter());
            pstmt.setString(2, bill.getMonth());
            pstmt.setInt(3, bill.getUnits());
            pstmt.setDouble(4, bill.getTotalBill());
            pstmt.setString(5, bill.getStatus());

            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error saving bill: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return success;
    }

    /**
     * Get bill by meter number and month
     * @param meter Meter number
     * @param month Month
     * @return Bill object if found, null otherwise
     */
    public Bill getBill(String meter, String month) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Bill bill = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM bill WHERE meter = ? AND month = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meter);
            pstmt.setString(2, month);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                bill = mapResultSetToBill(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting bill: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return bill;
    }

    /**
     * Update bill status (e.g., to mark as paid)
     * @param meter Meter number
     * @param month Month
     * @param status New status
     * @return true if updated successfully, false otherwise
     */
    public boolean updateBillStatus(String meter, String month, String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "UPDATE bill SET status = ? WHERE meter = ? AND month = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setString(2, meter);
            pstmt.setString(3, month);

            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error updating bill status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return success;
    }

    /**
     * Delete a bill record
     * @param meter Meter number
     * @param month Month
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteBill(String meter, String month) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "DELETE FROM bill WHERE meter = ? AND month = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meter);
            pstmt.setString(2, month);

            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error deleting bill: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return success;
    }

    /**
     * Get all bills for a specific meter
     * @param meter Meter number
     * @return List of bills for the meter
     */
    public List<Bill> getBillsByMeter(String meter) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Bill> bills = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT * FROM bill WHERE meter = ? ORDER BY month";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meter);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                bills.add(mapResultSetToBill(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting bills by meter: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return bills;
    }

    /**
     * Get ResultSet of bills for a specific meter
     * @param meter Meter number
     * @return ResultSet of bills for the meter
     */
    public ResultSet getBillsByMeterResultSet(String meter) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM bill WHERE meter = ? ORDER BY month";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meter);

            rs = pstmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println("Error getting bills by meter: " + e.getMessage());
            e.printStackTrace();
            closeResources(conn, pstmt, rs);
        }
        
        return null;
    }

    /**
     * Get all bills for a specific month
     * @param month Month to get bills for
     * @return List of bills for the month
     */
    public List<Bill> getBillsByMonth(String month) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Bill> bills = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT * FROM bill WHERE month = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, month);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                bills.add(mapResultSetToBill(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting bills by month: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return bills;
    }

    /**
     * Get ResultSet of bills for a specific month
     * @param month Month to get bills for
     * @return ResultSet of bills for the month
     */
    public ResultSet getBillsByMonthResultSet(String month) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM bill WHERE month = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, month);

            rs = pstmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println("Error getting bills by month: " + e.getMessage());
            e.printStackTrace();
            closeResources(conn, pstmt, rs);
        }
        
        return null;
    }

    /**
     * Get all bills
     * @return List of all bills
     */
    public List<Bill> getAllBills() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Bill> bills = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT * FROM bill";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                bills.add(mapResultSetToBill(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all bills: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return bills;
    }
    
    /**
     * Get ResultSet of all bills
     * @return ResultSet containing all bills
     */
    public ResultSet getAllBillsResultSet() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM bill";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println("Error getting all bills: " + e.getMessage());
            e.printStackTrace();
            closeResources(conn, pstmt, rs);
        }
        
        return null;
    }
    
    /**
     * Get bills for a specific meter and month
     * @param meter Meter number
     * @param month Month
     * @return ResultSet of matching bills
     */
    public ResultSet getBillsByMeterAndMonth(String meter, String month) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM bill WHERE meter = ? AND month = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meter);
            pstmt.setString(2, month);

            rs = pstmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println("Error getting bills by meter and month: " + e.getMessage());
            e.printStackTrace();
            closeResources(conn, pstmt, rs);
        }
        
        return null;
    }

    /**
     * Get current tax rates from database
     * @return Tax object with current rates
     */
    public Tax getTaxRates() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Tax tax = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM tax LIMIT 1";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                tax = new Tax(
                        Double.parseDouble(rs.getString("cost_per_unit")),
                        Double.parseDouble(rs.getString("meter_rent")),
                        Double.parseDouble(rs.getString("service_charge")),
                        Double.parseDouble(rs.getString("service_tax")),
                        Double.parseDouble(rs.getString("climate_change_levy")),
                        Double.parseDouble(rs.getString("fixed_tax"))
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting tax rates: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return tax;
    }

    /**
     * Map ResultSet row to Bill object
     * @param rs ResultSet containing bill data
     * @return Bill object
     * @throws SQLException if mapping fails
     */
    private Bill mapResultSetToBill(ResultSet rs) throws SQLException {
        Bill bill = new Bill();
        bill.setMeter(rs.getString("meter"));
        bill.setMonth(rs.getString("month"));

        // Convert string to int for units, handling potential SQL type issues
        try {
            bill.setUnits(Integer.parseInt(rs.getString("units")));
        } catch (NumberFormatException e) {
            bill.setUnits(rs.getInt("units"));
        }

        // Convert string to double for total_bill, handling potential SQL type issues
        try {
            bill.setTotalBill(Double.parseDouble(rs.getString("total_bill")));
        } catch (NumberFormatException e) {
            bill.setTotalBill(rs.getDouble("total_bill"));
        }
        
        bill.setStatus(rs.getString("status"));
        return bill;
    }
}