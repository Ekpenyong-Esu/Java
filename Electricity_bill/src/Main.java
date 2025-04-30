import Electricity.util.Conn;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or

import java.sql.ResultSet;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        try {
            Conn conn = new Conn(); // Instantiate the Conn class
            if (conn.connection != null && !conn.connection.isClosed()) {
                System.out.println("Database connection established successfully.");
                // Try a simple query
                ResultSet rs = conn.statement.executeQuery("SELECT 1");
                if (rs.next()) {
                    System.out.println("Query test successful!");
                }

                // Close resources
                rs.close();
                conn.connection.close();
            } else {
                System.out.println("Failed to establish database connection.");
            }
        } catch (Exception e) {
            // Handle any exceptions during the connection test
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }
}