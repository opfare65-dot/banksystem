import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginPanel provides the main login screen.
 * Allows users to choose between Admin login, Client login, About Us, and Exit.
 */
public class LoginPanel extends JPanel {
    private BankManagementSystem app;
    
    private JComboBox<String> userTypeCombo;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton aboutButton;
    private JButton exitButton;
    
    private JLabel statusLabel;

    public LoginPanel(BankManagementSystem app) {
        this.app = app;
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    /**
     * Initializes GUI components.
     */
    private void initializeComponents() {
        userTypeCombo = new JComboBox<>(new String[]{"Admin Login", "Client Login"});
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        aboutButton = new JButton("About Us");
        exitButton = new JButton("Exit");
        statusLabel = new JLabel(" ");
    }

    /**
     * Sets up the panel layout.
     */
    private void setupLayout() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel titleLabel = new JLabel("KAAFI BANK MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("User Type:"), gbc);
        gbc.gridx = 1;
        add(userTypeCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Username/Account #:"), gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Password/PIN:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(loginButton, gbc);
        gbc.gridx = 1;
        add(aboutButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(exitButton, gbc);
        
        gbc.gridy = 6;
        add(statusLabel, gbc);
    }

    /**
     * Sets up event listeners.
     */
    private void setupListeners() {
        loginButton.addActionListener(e -> performLogin());
        
        aboutButton.addActionListener(e -> app.showPanel("ABOUT_US"));
        
        exitButton.addActionListener(e -> app.exitApplication());
        
        passwordField.addActionListener(e -> performLogin());
    }

    /**
     * Validates input and performs login.
     */
    private void performLogin() {
        String input = usernameField.getText().trim();
        char[] pwd = passwordField.getPassword();
        String password = new String(pwd);
        
        if (input.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter all fields.");
            return;
        }
        
        if (userTypeCombo.getSelectedIndex() == 0) {
            // Admin login
            if (app.adminLogin(input, password)) {
                statusLabel.setText("Login successful!");
                clearFields();
            } else {
                statusLabel.setText("Invalid admin credentials.");
            }
        } else {
            // Client login
            if (app.clientLogin(input, password)) {
                statusLabel.setText("Login successful!");
                clearFields();
            } else {
                statusLabel.setText("Invalid account number or PIN.");
            }
        }
    }

    /**
     * Clears input fields.
     */
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    /**
     * Clears status message.
     */
    public void clearStatus() {
        statusLabel.setText(" ");
    }
}