package Electricity.model;

/**
 * Model class representing a customer in the Electricity Billing System.
 * Contains customer personal information and meter details.
 */
public class Customer {
    private String name;
    private String meter;
    private String address;
    private String city;
    private String state;
    private String email;
    private String phone;

    /**
     * Default constructor
     */
    public Customer() {
    }

    /**
     * Parameterized constructor to create a customer with all details
     */
    public Customer(String name, String meter, String address, String city,
                    String state, String email, String phone) {
        this.name = name;
        this.meter = meter;
        this.address = address;
        this.city = city;
        this.state = state;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", meter='" + meter + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}