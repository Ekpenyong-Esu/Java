package Electricity.dataAccessOutput;

import Electricity.model.Meter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Meter-related operations.
 * Handles CRUD operations for meter data.
 */
public class MeterDAO extends BaseDAO {

    /**
     * Add a new meter to the database
     * @param meter Meter object with details
     * @return true if added successfully, false otherwise
     */
    public boolean addMeter(Meter meter) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "INSERT INTO meter_info (meter_number, meter_location, meter_type, phase_code, bill_type, days) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meter.getMeterNumber());
            pstmt.setString(2, meter.getMeterLocation());
            pstmt.setString(3, meter.getMeterType());
            pstmt.setString(4, meter.getPhaseCode());
            pstmt.setString(5, meter.getBillType());
            pstmt.setInt(6, meter.getDays());

            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error adding meter: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return success;
    }

    /**
     * Get meter information by meter number
     * @param meterNumber Meter number
     * @return Meter object if found, null otherwise
     */
    public Meter getMeterByNumber(String meterNumber) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Meter meter = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM meter_info WHERE meter_number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meterNumber);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                meter = mapResultSetToMeter(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting meter by number: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return meter;
    }

    /**
     * Update meter information
     * @param meter Meter object with updated details
     * @return true if updated successfully, false otherwise
     */
    public boolean updateMeter(Meter meter) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "UPDATE meter_info SET meter_location = ?, meter_type = ?, " +
                    "phase_code = ?, bill_type = ?, days = ? WHERE meter_number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meter.getMeterLocation());
            pstmt.setString(2, meter.getMeterType());
            pstmt.setString(3, meter.getPhaseCode());
            pstmt.setString(4, meter.getBillType());
            pstmt.setInt(5, meter.getDays());
            pstmt.setString(6, meter.getMeterNumber());

            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error updating meter: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return success;
    }

    /**
     * Delete a meter record
     * @param meterNumber Meter number
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteMeter(String meterNumber) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "DELETE FROM meter_info WHERE meter_number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meterNumber);

            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error deleting meter: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return success;
    }

    /**
     * Get all meters
     * @return List of all meters
     */
    public List<Meter> getAllMeters() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Meter> meters = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT * FROM meter_info";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                meters.add(mapResultSetToMeter(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all meters: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return meters;
    }

    /**
     * Get meters by location
     * @param location Location to filter by
     * @return List of meters at the specified location
     */
    public List<Meter> getMetersByLocation(String location) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Meter> meters = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT * FROM meter_info WHERE meter_location = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, location);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                meters.add(mapResultSetToMeter(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting meters by location: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return meters;
    }

    /**
     * Get meters by type
     * @param type Type to filter by
     * @return List of meters of the specified type
     */
    public List<Meter> getMetersByType(String type) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Meter> meters = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT * FROM meter_info WHERE meter_type = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                meters.add(mapResultSetToMeter(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting meters by type: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return meters;
    }

    /**
     * Map ResultSet row to Meter object
     * @param rs ResultSet containing meter data
     * @return Meter object
     * @throws SQLException if mapping fails
     */
    private Meter mapResultSetToMeter(ResultSet rs) throws SQLException {
        Meter meter = new Meter();
        meter.setMeterNumber(rs.getString("meter_number"));
        meter.setMeterLocation(rs.getString("meter_location"));
        meter.setMeterType(rs.getString("meter_type"));
        meter.setPhaseCode(rs.getString("phase_code"));
        meter.setBillType(rs.getString("bill_type"));

        // Handle potential String to int conversion issues
        try {
            meter.setDays(Integer.parseInt(rs.getString("days")));
        } catch (NumberFormatException e) {
            meter.setDays(rs.getInt("days"));
        }

        return meter;
    }
}