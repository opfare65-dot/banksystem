import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

/**
 * Login entry screen for admin and client users.
 */
public class LoginPanel extends JPanel {
    // Dropdown option text for admin login mode.
    private static final String USER_TYPE_ADMIN = "Admin Login";
    // Dropdown option text for client login mode.
    private static final String USER_TYPE_CLIENT = "Client Login";

    // Reference to app controller for login actions and screen switching.
    private final BankManagementSystem bankingApp;

    // Dropdown used to choose admin/client login type.
    private JComboBox<String> userTypeDropdown;
    // Text field for username (admin) or account number (client).
    private JTextField usernameOrAccountField;
    // Secret field for password (admin) or PIN (client).
    private JPasswordField passwordOrPinField;
    // Button that triggers login action.
    private JButton loginButton;
    // Button that opens About screen.
    private JButton aboutUsButton;
    // Button that exits application.
    private JButton exitButton;
    // Label that shows current login status message.
    private JLabel loginStatusLabel;

    // Creates login screen with parent app controller reference.
    public LoginPanel(BankManagementSystem app) {
        // Save parent app reference.
        this.bankingApp = app;
        // Instantiate all Swing components.
        initializeComponents();
        // Arrange components on screen.
        buildLayout();
        // Attach user interaction listeners.
        setupListeners();
    }

    // Creates all UI components used by this screen.
    private void initializeComponents() {
        // Build dropdown with two login modes.
        userTypeDropdown = new JComboBox<>(new String[] {USER_TYPE_ADMIN, USER_TYPE_CLIENT});
        // Create text field for identity input.
        usernameOrAccountField = new JTextField(15);
        // Create password/PIN field.
        passwordOrPinField = new JPasswordField(15);
        // Create login button.
        loginButton = new JButton("Login");
        // Create about-us button.
        aboutUsButton = new JButton("About Us");
        // Create exit button.
        exitButton = new JButton("Exit");
        // Initialize status label with blank text.
        loginStatusLabel = new JLabel(" ");
    }

    // Builds visual layout using GridBag constraints.
    private void buildLayout() {
        // Set layout manager.
        setLayout(new GridBagLayout());
        // Apply panel background color.
        setBackground(new Color(240, 248, 255));

        // Reusable constraints object for component placement.
        GridBagConstraints constraints = new GridBagConstraints();
        // Add spacing around each cell.
        constraints.insets = new Insets(10, 10, 10, 10);
        // Center components in their grid cells.
        constraints.anchor = GridBagConstraints.CENTER;

        // Create title label text.
        JLabel titleLabel = new JLabel("KAAFI BANK MANAGEMENT SYSTEM");
        // Use large bold font for title.
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        // Apply dark-blue title color.
        titleLabel.setForeground(new Color(25, 25, 112));
        // Let title span two columns.
        constraints.gridwidth = 2;
        // Place title on row 0.
        constraints.gridy = 0;
        // Add title to panel.
        add(titleLabel, constraints);

        // Reset to single-column width.
        constraints.gridwidth = 1;
        // Move to row 1.
        constraints.gridy = 1;
        // Add "User Type" label.
        add(new JLabel("User Type:"), constraints);
        // Move to second column.
        constraints.gridx = 1;
        // Add dropdown control.
        add(userTypeDropdown, constraints);

        // Move back to first column.
        constraints.gridx = 0;
        // Move to row 2.
        constraints.gridy = 2;
        // Add identity field label.
        add(new JLabel("Username/Account #:"), constraints);
        // Move to second column.
        constraints.gridx = 1;
        // Add identity text field.
        add(usernameOrAccountField, constraints);

        // Move back to first column.
        constraints.gridx = 0;
        // Move to row 3.
        constraints.gridy = 3;
        // Add secret field label.
        add(new JLabel("Password/PIN:"), constraints);
        // Move to second column.
        constraints.gridx = 1;
        // Add secret field.
        add(passwordOrPinField, constraints);

        // Move to first column.
        constraints.gridx = 0;
        // Move to row 4.
        constraints.gridy = 4;
        // Add login button.
        add(loginButton, constraints);
        // Move to second column.
        constraints.gridx = 1;
        // Add about-us button.
        add(aboutUsButton, constraints);

        // Move to first column.
        constraints.gridx = 0;
        // Move to row 5.
        constraints.gridy = 5;
        // Let exit button span both columns.
        constraints.gridwidth = 2;
        // Add exit button.
        add(exitButton, constraints);

        // Move to row 6.
        constraints.gridy = 6;
        // Add status label.
        add(loginStatusLabel, constraints);
    }

    // Connects user interactions to app actions.
    private void setupListeners() {
        // Handle login button click.
        loginButton.addActionListener(event -> performLogin());
        // Navigate to About screen.
        aboutUsButton.addActionListener(event -> bankingApp.showPanel("ABOUT_US"));
        // Request app shutdown flow.
        exitButton.addActionListener(event -> bankingApp.exitApplication());
        // Trigger login when Enter is pressed inside password field.
        passwordOrPinField.addActionListener(event -> performLogin());
    }

    // Reads form values and performs selected login flow.
    private void performLogin() {
        // Read trimmed identity input text.
        String userInput = usernameOrAccountField.getText().trim();
        // Read secret characters from password field.
        char[] credentialChars = passwordOrPinField.getPassword();
        // Convert chars to String for existing API usage.
        String enteredSecret = new String(credentialChars);

        try {
            // Validate required fields before attempting login.
            if (userInput.isEmpty() || enteredSecret.isEmpty()) {
                loginStatusLabel.setText("Please enter all fields.");
                return;
            }

            // Get currently selected login type.
            String selectedUserType = (String) userTypeDropdown.getSelectedItem();
            // Route to admin login flow when admin mode is selected.
            if (USER_TYPE_ADMIN.equals(selectedUserType)) {
                handleAdminLogin(userInput, enteredSecret);
            } else {
                // Otherwise run client login flow.
                handleClientLogin(userInput, enteredSecret);
            }
        } finally {
            // Clear secret chars from memory buffer as a best-effort cleanup.
            Arrays.fill(credentialChars, '\0');
        }
    }

    // Runs admin authentication and updates status label.
    private void handleAdminLogin(String username, String password) {
        // Try admin login through app controller.
        if (bankingApp.adminLogin(username, password)) {
            // Show success message.
            loginStatusLabel.setText("Login successful!");
            // Clear input fields after success.
            clearFields();
        } else {
            // Show failure message.
            loginStatusLabel.setText("Invalid admin credentials.");
        }
    }

    // Runs client authentication and updates status label.
    private void handleClientLogin(String accountNumber, String pin) {
        // Try client login through app controller.
        if (bankingApp.clientLogin(accountNumber, pin)) {
            // Show success message.
            loginStatusLabel.setText("Login successful!");
            // Clear input fields after success.
            clearFields();
        } else {
            // Show failure message.
            loginStatusLabel.setText("Invalid account number or PIN.");
        }
    }

    // Clears identity and secret fields.
    private void clearFields() {
        // Clear username/account field text.
        usernameOrAccountField.setText("");
        // Clear password/PIN field text.
        passwordOrPinField.setText("");
    }

    // Clears status label text.
    public void clearStatus() {
        // Replace status with blank placeholder.
        loginStatusLabel.setText(" ");
    }
}
