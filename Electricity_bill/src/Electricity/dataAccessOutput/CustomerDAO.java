package Electricity.dataAccessOutput;

import Electricity.model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Customer-related operations.
 * Handles CRUD operations for customer data.
 */
public class CustomerDAO extends BaseDAO {

    /**
     * Add a new customer to the database
     * @param customer Customer object with details
     * @return true if added successfully, false otherwise
     */
    public boolean addCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "INSERT INTO customer (name, meter, address, city, state, email, phone) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getMeter());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getCity());
            pstmt.setString(5, customer.getState());
            pstmt.setString(6, customer.getEmail());
            pstmt.setString(7, customer.getPhone());

            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return success;
    }

    /**
     * Update customer information
     * @param customer Customer object with updated details
     * @return true if updated successfully, false otherwise
     */
    public boolean updateCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "UPDATE customer SET name = ?, address = ?, city = ?, " +
                    "state = ?, email = ?, phone = ? WHERE meter = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getAddress());
            pstmt.setString(3, customer.getCity());
            pstmt.setString(4, customer.getState());
            pstmt.setString(5, customer.getEmail());
            pstmt.setString(6, customer.getPhone());
            pstmt.setString(7, customer.getMeter());

            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return success;
    }

    /**
     * Get customer information by meter number
     * @param meter Meter number
     * @return Customer object if found, null otherwise
     */
    public Customer getCustomerByMeter(String meter) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Customer customer = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM customer WHERE meter = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meter);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                customer = mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting customer by meter: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return customer;
    }

    /**
     * Delete a customer record
     * @param meter Meter number to identify the customer
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteCustomer(String meter) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = getConnection();
            String sql = "DELETE FROM customer WHERE meter = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, meter);

            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return success;
    }

    /**
     * Get all customers from the database
     * @return List of all customers
     */
    public List<Customer> getAllCustomers() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Customer> customers = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT * FROM customer";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all customers: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return customers;
    }
    
    /**
     * Get all meter numbers from the database
     * @return List of all meter numbers
     */
    public List<String> getAllMeterNumbers() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> meterNumbers = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT meter FROM customer";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                meterNumbers.add(rs.getString("meter"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all meter numbers: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return meterNumbers;
    }

    /**
     * Search customers by name (partial match)
     * @param name Name to search for
     * @return List of matching customers
     */
    public List<Customer> searchCustomersByName(String name) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Customer> customers = new ArrayList<>();

        try {
            conn = getConnection();
            String sql = "SELECT * FROM customer WHERE name LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error searching customers by name: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return customers;
    }

    /**
     * Map ResultSet row to Customer object
     * @param rs ResultSet containing customer data
     * @return Customer object
     * @throws SQLException if mapping fails
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setName(rs.getString("name"));
        customer.setMeter(rs.getString("meter"));
        customer.setAddress(rs.getString("address"));
        customer.setCity(rs.getString("city"));
        customer.setState(rs.getString("state"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        return customer;
    }
}