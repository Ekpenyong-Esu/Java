package Electricity.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    private Customer customer;
    private static final String TEST_NAME = "John Doe";
    private static final String TEST_METER = "M12345";
    private static final String TEST_ADDRESS = "123 Electric Avenue";
    private static final String TEST_CITY = "Powertown";
    private static final String TEST_STATE = "Voltage State";
    private static final String TEST_EMAIL = "john.doe@example.com";
    private static final String TEST_PHONE = "123-456-7890";

    @BeforeEach // Changed from @Before to @BeforeEach for JUnit 5
    public void setUp() {
        customer = new Customer(TEST_NAME, TEST_METER, TEST_ADDRESS, TEST_CITY, TEST_STATE, TEST_EMAIL, TEST_PHONE);
    }

    @Test
    public void testCustomerConstructor() {
        assertNotNull(customer, "Customer object should not be null");
        assertEquals(TEST_NAME, customer.getName(), "Name should match constructor parameter");
        assertEquals(TEST_METER, customer.getMeter(), "Meter should match constructor parameter");
        assertEquals(TEST_ADDRESS, customer.getAddress(), "Address should match constructor parameter");
        assertEquals(TEST_CITY, customer.getCity(), "City should match constructor parameter");
        assertEquals(TEST_STATE, customer.getState(), "State should match constructor parameter");
        assertEquals(TEST_EMAIL, customer.getEmail(), "Email should match constructor parameter");
        assertEquals(TEST_PHONE, customer.getPhone(), "Phone should match constructor parameter");
    }

    @Test
    public void testSettersAndGetters() {
        // Test setters
        String newName = "Jane Smith";
        customer.setName(newName);
        assertEquals(newName, customer.getName(), "Name should be updated");
        
        String newMeter = "M67890";
        customer.setMeter(newMeter);
        assertEquals(newMeter, customer.getMeter(), "Meter should be updated");
        
        String newAddress = "456 Power Street";
        customer.setAddress(newAddress);
        assertEquals(newAddress, customer.getAddress(), "Address should be updated");
        
        String newCity = "Circuit City";
        customer.setCity(newCity);
        assertEquals(newCity, customer.getCity(), "City should be updated");
        
        String newState = "Current State";
        customer.setState(newState);
        assertEquals(newState, customer.getState(), "State should be updated");
        
        String newEmail = "jane.smith@example.com";
        customer.setEmail(newEmail);
        assertEquals(newEmail, customer.getEmail(), "Email should be updated");
        
        String newPhone = "987-654-3210";
        customer.setPhone(newPhone);
        assertEquals(newPhone, customer.getPhone(), "Phone should be updated");
    }
    
    @Test
    public void testDefaultConstructor() {
        Customer defaultCustomer = new Customer();
        assertNotNull(defaultCustomer, "Default customer should not be null");
        // Values should be null or default as set by the default constructor
    }
}