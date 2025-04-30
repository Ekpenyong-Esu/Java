package Electricity.model;

/**
 * Model class representing user login information in the Electricity Billing System.
 */
public class Login {
    private String meterNo;
    private String username;
    private String name;
    private String password;
    private String userType;  // "Admin" or "Customer"

    /**
     * Default constructor
     */
    public Login() {
    }

    /**
     * Parameterized constructor for creating a login with all details
     */
    public Login(String meterNo, String username, String name, String password, String userType) {
        this.meterNo = meterNo;
        this.username = username;
        this.name = name;
        this.password = password;
        this.userType = userType;
    }

    // Getters and Setters

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * Check if the user is an administrator
     * @return true if the user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(userType);
    }

    @Override
    public String toString() {
        return "Login{" +
                "meterNo='" + meterNo + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}