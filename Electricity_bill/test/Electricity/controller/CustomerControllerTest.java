package Electricity.controller;

import Electricity.dataAccessOutput.CustomerDAO;
import Electricity.model.Customer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @Mock
    private CustomerDAO customerDAO;

    private CustomerController customerController;
    private Customer testCustomer;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Create a controller with a mocked DAO
        customerController = new CustomerController();
        // Use reflection to replace the DAO with our mock
        try {
            java.lang.reflect.Field daoField = CustomerController.class.getDeclaredField("customerDAO");
            daoField.setAccessible(true);
            daoField.set(customerController, customerDAO);
        } catch (Exception e) {
            fail("Failed to inject mock DAO: " + e.getMessage());
        }
        
        // Setup test customer data
        testCustomer = new Customer(
            "John Doe",
            "M12345",
            "123 Electric Avenue",
            "Powertown",
            "Voltage State",
            "john.doe@example.com",
            "123-456-7890"
        );
    }

    @Test
    public void testAddCustomer() {
        // Configure mock to return true when addCustomer is called
        when(customerDAO.addCustomer(any(Customer.class))).thenReturn(true);
        
        // Call the controller method
        boolean result = customerController.addCustomer(
            testCustomer.getName(),
            testCustomer.getMeter(),
            testCustomer.getAddress(),
            testCustomer.getCity(),
            testCustomer.getState(),
            testCustomer.getEmail(),
            testCustomer.getPhone()
        );
        
        // Verify the result and DAO interaction
        assertTrue("Customer should be added successfully", result);
        verify(customerDAO).addCustomer(any(Customer.class));
    }
    
    @Test
    public void testAddCustomerWithInvalidData() {
        // Call with invalid data (null name)
        boolean result = customerController.addCustomer(
            null,
            testCustomer.getMeter(),
            testCustomer.getAddress(),
            testCustomer.getCity(),
            testCustomer.getState(),
            testCustomer.getEmail(),
            testCustomer.getPhone()
        );
        
        // Verify the result
        assertFalse("Adding customer with null name should fail", result);
        verify(customerDAO, never()).addCustomer(any(Customer.class));
    }
    
    @Test
    public void testGetCustomerByMeter() {
        // Configure mock to return a customer when getCustomerByMeter is called
        when(customerDAO.getCustomerByMeter(testCustomer.getMeter())).thenReturn(testCustomer);
        
        // Call the controller method
        Customer result = customerController.getCustomerByMeter(testCustomer.getMeter());
        
        // Verify the result and DAO interaction
        assertNotNull("Retrieved customer should not be null", result);
        assertEquals("Retrieved customer should match the expected one", testCustomer.getName(), result.getName());
        verify(customerDAO).getCustomerByMeter(testCustomer.getMeter());
    }
    
    @Test
    public void testGetCustomerByInvalidMeter() {
        // Configure mock to return null for a non-existent meter
        when(customerDAO.getCustomerByMeter("INVALID")).thenReturn(null);
        
        // Call the controller method
        Customer result = customerController.getCustomerByMeter("INVALID");
        
        // Verify the result and DAO interaction
        assertNull("Customer with invalid meter should not be found", result);
        verify(customerDAO).getCustomerByMeter("INVALID");
    }
    
    @Test
    public void testGetAllCustomers() {
        // Create a list of customers to return
        List<Customer> customers = new ArrayList<>();
        customers.add(testCustomer);
        customers.add(new Customer("Jane Smith", "M67890", "456 Power Street", "Circuit City", "Current State", "jane.smith@example.com", "987-654-3210"));
        
        // Configure mock to return the customer list
        when(customerDAO.getAllCustomers()).thenReturn(customers);
        
        // Call the controller method
        List<Customer> result = customerController.getAllCustomers();
        
        // Verify the result and DAO interaction
        assertNotNull("Customer list should not be null", result);
        assertEquals("Customer list size should match", 2, result.size());
        assertEquals("First customer name should match", "John Doe", result.get(0).getName());
        verify(customerDAO).getAllCustomers();
    }
    
    @Test
    public void testGetAllMeterNumbers() {
        // Create a list of meter numbers to return
        List<String> meterNumbers = new ArrayList<>();
        meterNumbers.add("M12345");
        meterNumbers.add("M67890");
        
        // Configure mock to return the meter number list
        when(customerDAO.getAllMeterNumbers()).thenReturn(meterNumbers);
        
        // Call the controller method
        List<String> result = customerController.getAllMeterNumbers();
        
        // Verify the result and DAO interaction
        assertNotNull("Meter number list should not be null", result);
        assertEquals("Meter number list size should match", 2, result.size());
        assertEquals("First meter number should match", "M12345", result.get(0));
        verify(customerDAO).getAllMeterNumbers();
    }
}