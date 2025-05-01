import Electricity.view.common.Login;
import javax.swing.SwingUtilities;



// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    /**
     * Main entry point for the Electricity Billing System application.
     * Launches the login screen when the application starts.
     */
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure GUI creation happens on the EDT
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }       
}