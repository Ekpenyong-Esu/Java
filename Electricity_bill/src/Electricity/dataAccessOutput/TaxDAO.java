package Electricity.dataAccessOutput;

import Electricity.model.Tax;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Tax-related database operations.
 * Handles CRUD operations for tax data.
 */
public class TaxDAO extends BaseDAO {

    /**
     * Saves tax information to the database
     * @param tax the tax object to save
     * @return true if save was successful, false otherwise
     */
    public boolean saveTax(Tax tax) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        boolean success = false;
        
        try {
            conn = getConnection();
            String sql = "INSERT INTO tax (cost_per_unit, meter_rent, service_charge, service_tax, climate_change_levy, fixed_tax) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setDouble(1, tax.getCostPerUnit());
            pstmt.setDouble(2, tax.getMeterRent());
            pstmt.setDouble(3, tax.getServiceCharge());
            pstmt.setDouble(4, tax.getServiceTax());
            pstmt.setDouble(5, tax.getClimateChangeLevy());
            pstmt.setDouble(6, tax.getFixedTax());
            
            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
            
            // Get the auto-generated key and set it in the tax object
            if (success) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    tax.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving tax: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, generatedKeys);
        }
        
        return success;
    }

    /**
     * Updates existing tax information in the database
     * @param tax the tax object with updated values
     * @return true if update was successful, false otherwise
     */
    public boolean updateTax(Tax tax) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = getConnection();
            String sql = "UPDATE tax SET cost_per_unit = ?, meter_rent = ?, service_charge = ?, " +
                    "service_tax = ?, climate_change_levy = ?, fixed_tax = ? WHERE id = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, tax.getCostPerUnit());
            pstmt.setDouble(2, tax.getMeterRent());
            pstmt.setDouble(3, tax.getServiceCharge());
            pstmt.setDouble(4, tax.getServiceTax());
            pstmt.setDouble(5, tax.getClimateChangeLevy());
            pstmt.setDouble(6, tax.getFixedTax());
            pstmt.setInt(7, tax.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error updating tax: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        
        return success;
    }

    /**
     * Retrieves the current tax information from the database
     * @return Tax object with current rates or default Tax if none found
     */
    public Tax getCurrentTax() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Tax tax = new Tax();
        
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tax ORDER BY id DESC LIMIT 1";
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                tax = mapResultSetToTax(rs);
            } else {
                // Return default tax values if no records found
                tax = new Tax(9.0, 50.0, 20.0, 5.0, 1.0, 18.0);
            }
        } catch (SQLException e) {
            System.out.println("Error getting current tax: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return tax;
    }

    /**
     * Gets a tax record by ID
     * @param id ID of the tax record
     * @return Tax object if found, null otherwise
     */
    public Tax getTaxById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Tax tax = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tax WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                tax = mapResultSetToTax(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting tax by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return tax;
    }

    /**
     * Deletes tax record from the database
     * @param id id of the tax record to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteTax(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = getConnection();
            String sql = "DELETE FROM tax WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error deleting tax: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        
        return success;
    }

    /**
     * Gets historical tax rates from the database
     * @return List of all tax records
     */
    public List<Tax> getAllTaxes() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Tax> taxes = new ArrayList<>();
        
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tax ORDER BY id DESC";
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                taxes.add(mapResultSetToTax(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all taxes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return taxes;
    }
    
    /**
     * Maps a ResultSet to a Tax object
     * @param rs ResultSet containing tax data
     * @return mapped Tax object
     * @throws SQLException if mapping fails
     */
    private Tax mapResultSetToTax(ResultSet rs) throws SQLException {
        Tax tax = new Tax();
        
        // Set the ID first
        tax.setId(rs.getInt("id"));
        
        // Handle potential String to double conversion issues
        try {
            tax.setCostPerUnit(Double.parseDouble(rs.getString("cost_per_unit")));
            tax.setMeterRent(Double.parseDouble(rs.getString("meter_rent")));
            tax.setServiceCharge(Double.parseDouble(rs.getString("service_charge")));
            tax.setServiceTax(Double.parseDouble(rs.getString("service_tax")));
            tax.setClimateChangeLevy(Double.parseDouble(rs.getString("climate_change_levy")));
            tax.setFixedTax(Double.parseDouble(rs.getString("fixed_tax")));
        } catch (NumberFormatException e) {
            // Fallback to direct getDouble if getString fails
            tax.setCostPerUnit(rs.getDouble("cost_per_unit"));
            tax.setMeterRent(rs.getDouble("meter_rent"));
            tax.setServiceCharge(rs.getDouble("service_charge"));
            tax.setServiceTax(rs.getDouble("service_tax"));
            tax.setClimateChangeLevy(rs.getDouble("climate_change_levy"));
            tax.setFixedTax(rs.getDouble("fixed_tax"));
        }
        
        return tax;
    }
}