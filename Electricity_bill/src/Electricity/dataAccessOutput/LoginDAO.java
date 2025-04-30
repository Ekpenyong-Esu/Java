package Electricity.dataAccessOutput;

import Electricity.model.Login;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Login-related operations.
 * Handles user authentication and user management.
 */
public class LoginDAO extends BaseDAO {

    /**
     * Authenticate user with given username, password, and user type
     * @param username Username
     * @param password Password
     * @param userType User type (Admin/Customer)
     * @return Login object if authenticated, null otherwise
     */
    public Login authenticate(String username, String password, String userType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Login login = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM login WHERE username = ? AND password = ? AND user = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, userType);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                login = mapResultSetToLogin(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return login;
    }

    /**
     * Get login details by meter number
     * @param meterNo Meter number
     * @return Login object if found, null otherwise
     */
    public Login getLoginByMeterNo(String meterNo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Login login = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM login WHERE meter_no = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meterNo);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                login = mapResultSetToLogin(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting login by meter number: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return login;
    }

    /**
     * Create a new user
     * @param login Login object with user details
     * @return true if created successfully, false otherwise
     */
    public boolean createUser(Login login) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "INSERT INTO login (meter_no, username, name, password, user) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login.getMeterNo());
            pstmt.setString(2, login.getUsername());
            pstmt.setString(3, login.getName());
            pstmt.setString(4, login.getPassword());
            pstmt.setString(5, login.getUserType());

            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return success;
    }

    /**
     * Update an existing user
     * @param login Login object with updated details
     * @return true if updated successfully, false otherwise
     */
    public boolean updateUser(Login login) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "UPDATE login SET username = ?, name = ?, password = ?, user = ? WHERE meter_no = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login.getUsername());
            pstmt.setString(2, login.getName());
            pstmt.setString(3, login.getPassword());
            pstmt.setString(4, login.getUserType());
            pstmt.setString(5, login.getMeterNo());

            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return success;
    }

    /**
     * Delete a user by meter number
     * @param meterNo Meter number
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteUser(String meterNo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "DELETE FROM login WHERE meter_no = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meterNo);

            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return success;
    }

    /**
     * Get all users
     * @return List of all Login objects
     */
    public List<Login> getAllUsers() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Login> users = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT * FROM login";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToLogin(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all users: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return users;
    }

    /**
     * Map result set to Login object
     * @param rs ResultSet containing login data
     * @return Login object
     * @throws SQLException if mapping fails
     */
    private Login mapResultSetToLogin(ResultSet rs) throws SQLException {
        Login login = new Login();
        login.setMeterNo(rs.getString("meter_no"));
        login.setUsername(rs.getString("username"));
        login.setName(rs.getString("name"));
        login.setPassword(rs.getString("password"));
        login.setUserType(rs.getString("user"));
        return login;
    }
}