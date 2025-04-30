package Electricity.controller;

import Electricity.dataAccessOutput.CustomerDAO;
import Electricity.model.Customer;
import java.util.List;

/**
 * Controller class for customer management operations.
 * Handles the business logic for customer-related functionality.
 */
public class CustomerController {

    private final CustomerDAO customerDAO;

    /**
     * Constructor initializing the CustomerDAO
     */
    public CustomerController() {
        customerDAO = new CustomerDAO();
    }

    /**
     * Add a new customer to the system
     * @param name Customer name
     * @param meter Meter number
     * @param address Customer address
     * @param city Customer city
     * @param state Customer state
     * @param email Customer email
     * @param phone Customer phone
     * @return true if customer added successfully, false otherwise
     */
    public boolean addCustomer(String name, String meter, String address, String city,
                               String state, String email, String phone) {
        // Input validation
        if (name == null || name.trim().isEmpty() ||
                meter == null || meter.trim().isEmpty() ||
                address == null || address.trim().isEmpty()) {
            return false;
        }

        // Create customer object
        Customer customer = new Customer(name, meter, address, city, state, email, phone);

        return customerDAO.addCustomer(customer);
    }

    /**
     * Update customer information
     * @param customer Customer object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateCustomer(Customer customer) {
        if (customer == null || customer.getMeter() == null || customer.getMeter().trim().isEmpty()) {
            return false;
        }

        return customerDAO.updateCustomer(customer);
    }

    /**
     * Update specific customer information fields
     * @param name Customer name
     * @param meter Meter number to identify customer
     * @param address Updated address
     * @param city Updated city
     * @param state Updated state
     * @param email Updated email
     * @param phone Updated phone
     * @return true if update successful, false otherwise
     */
    public boolean updateCustomerInfo(String name, String meter, String address, String city,
                                      String state, String email, String phone) {
        if (meter == null || meter.trim().isEmpty()) {
            return false;
        }

        // Get existing customer
        Customer customer = customerDAO.getCustomerByMeter(meter);
        if (customer == null) {
            return false;
        }

        // Update fields
        if (name != null && !name.trim().isEmpty()) {
            customer.setName(name);
        }
        if (address != null && !address.trim().isEmpty()) {
            customer.setAddress(address);
        }
        if (city != null && !city.trim().isEmpty()) {
            customer.setCity(city);
        }
        if (state != null && !state.trim().isEmpty()) {
            customer.setState(state);
        }
        if (email != null && !email.trim().isEmpty()) {
            customer.setEmail(email);
        }
        if (phone != null && !phone.trim().isEmpty()) {
            customer.setPhone(phone);
        }

        return customerDAO.updateCustomer(customer);
    }

    /**
     * Get customer information by meter number
     * @param meter Meter number
     * @return Customer object if found, null otherwise
     */
    public Customer getCustomerByMeter(String meter) {
        if (meter == null || meter.trim().isEmpty()) {
            return null;
        }

        return customerDAO.getCustomerByMeter(meter);
    }

    /**
     * Get all customers in the system
     * @return List of all customers
     */
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    /**
     * Search for customers by name (partial match)
     * @param name Name to search for
     * @return List of matching customers
     */
    public List<Customer> searchCustomersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllCustomers();
        }

        return customerDAO.searchCustomersByName(name);
    }

    /**
     * Delete a customer by meter number
     * @param meter Meter number
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteCustomer(String meter) {
        if (meter == null || meter.trim().isEmpty()) {
            return false;
        }

        return customerDAO.deleteCustomer(meter);
    }

    /**
     * Validate if customer exists by meter number
     * @param meter Meter number to check
     * @return true if customer exists, false otherwise
     */
    public boolean customerExists(String meter) {
        return getCustomerByMeter(meter) != null;
    }
}