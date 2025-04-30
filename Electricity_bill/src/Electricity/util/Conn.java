package Electricity.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.sql.*;

public class Conn {
    public Connection connection; // Database connection object
    public Statement statement;   // Statement object for executing SQL queries

    public Conn() {
        try {
            // Load database properties from a configuration file
            Properties dbProps = new Properties();
            
            // Use the absolute path approach that works
            String projectRoot = System.getProperty("user.dir");
            File propertiesFile = new File(projectRoot + "/resources/config/db.properties");
            
            if (propertiesFile.exists()) {
                InputStream input = new FileInputStream(propertiesFile);
                System.out.println("Found properties file at: " + propertiesFile.getAbsolutePath());
                
                // Load properties from the file
                dbProps.load(input);
                input.close();

                // Load the database driver class
                Class.forName(dbProps.getProperty("db.driver"));

                // Establish a connection using properties from the file
                connection = DriverManager.getConnection(
                        dbProps.getProperty("db.url"),
                        dbProps.getProperty("db.username"),
                        dbProps.getProperty("db.password"));
                System.out.println("Connected using properties file configuration");
            } else {
                // Fallback to hardcoded values if the properties file is not found
                System.out.println("Warning: db.properties not found at: " + propertiesFile.getAbsolutePath());
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql:///ebs", "ebsuser", "password123");
                System.out.println("Connected using hardcoded default values");
            }

            // Create a statement object for executing SQL queries
            statement = connection.createStatement();
            System.out.println("Database connection and statement created successfully");
            
        } catch (Exception e) {
            // Log the full stack trace for debugging
            System.out.println("Connection Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
