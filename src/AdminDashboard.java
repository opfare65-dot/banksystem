import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

/**
 * Admin dashboard for account management.
 */
public class AdminDashboard extends JPanel {
    // Main app reference used for data operations and logout.
    private final BankManagementSystem bankingApp;

    // Table used to display account rows.
    private JTable accountTable;
    // Model backing the account table.
    private DefaultTableModel accountTableModel;
    // Input field for account-number search.
    private JTextField accountSearchField;
    // Label showing total account count.
    private JLabel totalAccountsLabel;
    // Read-only area for admin activity text.
    private JTextArea adminTransactionLogArea;

    // Button for account search.
    private JButton searchButton;
    // Button for loading all accounts.
    private JButton viewAllButton;
    // Button for adding a new client account.
    private JButton addClientButton;
    // Button for deleting selected account.
    private JButton deleteAccountButton;
    // Button for deactivating selected account.
    private JButton deactivateAccountButton;
    // Button for viewing transaction section.
    private JButton viewTransactionsButton;
    // Button for admin logout.
    private JButton logoutButton;

    // Creates dashboard and wires all components.
    public AdminDashboard(BankManagementSystem app) {
        // Save app reference.
        this.bankingApp = app;
        // Build Swing components.
        initializeComponents();
        // Arrange components on panel.
        buildLayout();
        // Attach event handlers.
        setupListeners();
    }

    // Initializes all controls used by admin dashboard.
    private void initializeComponents() {
        // Define table headers.
        String[] tableColumns = {"Account #", "Holder Name", "Balance", "Status"};
        // Create model with non-editable cells.
        accountTableModel = new DefaultTableModel(tableColumns, 0) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                // Prevent direct editing in table.
                return false;
            }
        };

        // Create account table from model.
        accountTable = new JTable(accountTableModel);
        // Allow single-row selection only.
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create search input field.
        accountSearchField = new JTextField(15);
        // Create total-accounts label.
        totalAccountsLabel = new JLabel("Total Accounts: 0");

        // Create multiline admin log area.
        adminTransactionLogArea = new JTextArea(10, 30);
        // Keep log area read-only.
        adminTransactionLogArea.setEditable(false);
        // Use monospaced font for clean logs.
        adminTransactionLogArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Create top action buttons.
        searchButton = new JButton("Search");
        viewAllButton = new JButton("View All");
        addClientButton = new JButton("Add Client");
        deleteAccountButton = new JButton("Delete Account");
        deactivateAccountButton = new JButton("Deactivate Account");
        viewTransactionsButton = new JButton("View Transactions");
        logoutButton = new JButton("Logout");
    }

    // Builds the visual layout.
    private void buildLayout() {
        // Use border layout as top-level manager.
        setLayout(new BorderLayout(10, 10));
        // Add outer padding.
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Build search row panel.
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Account #:"));
        searchPanel.add(accountSearchField);
        searchPanel.add(searchButton);
        searchPanel.add(viewAllButton);

        // Build admin action buttons row.
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionButtonsPanel.add(addClientButton);
        actionButtonsPanel.add(deleteAccountButton);
        actionButtonsPanel.add(deactivateAccountButton);
        actionButtonsPanel.add(viewTransactionsButton);

        // Combine search row and action row.
        JPanel topContainerPanel = new JPanel(new BorderLayout());
        topContainerPanel.add(searchPanel, BorderLayout.NORTH);
        topContainerPanel.add(actionButtonsPanel, BorderLayout.SOUTH);

        // Place top container in NORTH.
        add(topContainerPanel, BorderLayout.NORTH);
        // Place account table in CENTER.
        add(new JScrollPane(accountTable), BorderLayout.CENTER);

        // Build lower activity panel.
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.add(totalAccountsLabel, BorderLayout.NORTH);
        activityPanel.add(new JScrollPane(adminTransactionLogArea), BorderLayout.CENTER);

        // Build bottom container with activity + logout.
        JPanel bottomContainerPanel = new JPanel(new BorderLayout());
        bottomContainerPanel.add(activityPanel, BorderLayout.CENTER);
        bottomContainerPanel.add(logoutButton, BorderLayout.EAST);

        // Place bottom container in SOUTH.
        add(bottomContainerPanel, BorderLayout.SOUTH);
    }

    // Wires UI events to handler methods.
    private void setupListeners() {
        // Search action.
        searchButton.addActionListener(event -> searchAccount());
        // Load-all action.
        viewAllButton.addActionListener(event -> refreshData());
        // Add-client action.
        addClientButton.addActionListener(event -> addNewClient());
        // Delete action.
        deleteAccountButton.addActionListener(event -> deleteSelectedAccount());
        // Deactivate action.
        deactivateAccountButton.addActionListener(event -> deactivateSelectedAccount());
        // View-transactions action.
        viewTransactionsButton.addActionListener(event -> viewTransactions());
        // Logout action.
        logoutButton.addActionListener(event -> bankingApp.adminLogout());
    }

    // Reloads table rows from current account list.
    public void refreshData() {
        // Clear existing rows.
        accountTableModel.setRowCount(0);
        // Fetch all accounts.
        List<Account> allAccounts = bankingApp.getAllAccounts();

        // Add one row per account.
        for (Account account : allAccounts) {
            accountTableModel.addRow(buildAccountRow(account));
        }

        // Update total accounts label.
        totalAccountsLabel.setText("Total Accounts: " + allAccounts.size());
    }

    // Searches one account and shows only matching row.
    private void searchAccount() {
        // Read search input.
        String accountNumberInput = accountSearchField.getText().trim();
        // Validate required input.
        if (accountNumberInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an account number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Clear current table rows.
        accountTableModel.setRowCount(0);
        try {
            // Resolve account by account number.
            Account foundAccount = bankingApp.getAccountByNumber(accountNumberInput);
            // Add one matching row.
            accountTableModel.addRow(buildAccountRow(foundAccount));
        } catch (AccountNotFoundException exception) {
            // Show "not found" warning message.
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Opens dialog and creates a new client account.
    private void addNewClient() {
        // Input field for new account number.
        JTextField accountNumberField = new JTextField();
        // Input field for holder name.
        JTextField holderNameField = new JTextField();
        // Input field for PIN.
        JTextField pinField = new JTextField();
        // Input field for opening balance.
        JTextField initialBalanceField = new JTextField("0");

        // Build form panel with two columns.
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("Account Number:"));
        inputPanel.add(accountNumberField);
        inputPanel.add(new JLabel("Holder Name:"));
        inputPanel.add(holderNameField);
        inputPanel.add(new JLabel("PIN:"));
        inputPanel.add(pinField);
        inputPanel.add(new JLabel("Initial Balance:"));
        inputPanel.add(initialBalanceField);

        // Show confirm dialog with form.
        int dialogResult = JOptionPane.showConfirmDialog(this, inputPanel, "Add New Client", JOptionPane.OK_CANCEL_OPTION);
        // Stop when user cancels.
        if (dialogResult != JOptionPane.OK_OPTION) {
            return;
        }

        // Read account number input.
        String accountNumberInput = accountNumberField.getText().trim();
        // Read holder name input.
        String holderNameInput = holderNameField.getText().trim();
        // Read PIN input.
        String pinInput = pinField.getText().trim();

        // Validate required fields.
        if (accountNumberInput.isEmpty() || holderNameInput.isEmpty() || pinInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Parse opening balance input.
        Double initialBalance = parseMoneyInput(initialBalanceField.getText(), "balance");
        // Stop when parse fails.
        if (initialBalance == null) {
            return;
        }

        // Validate non-negative opening balance.
        if (initialBalance < 0) {
            JOptionPane.showMessageDialog(this, "Initial balance cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Attempt to add account in app controller.
        boolean accountAdded = bankingApp.addAccount(accountNumberInput, holderNameInput, pinInput, initialBalance);
        if (accountAdded) {
            // Refresh table when successful.
            refreshData();
            JOptionPane.showMessageDialog(this, "Account added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Show duplicate-account error.
            JOptionPane.showMessageDialog(this, "Account number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Deletes currently selected account row.
    private void deleteSelectedAccount() {
        // Read selected row index.
        int selectedRowIndex = accountTable.getSelectedRow();
        // Validate selection exists.
        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Resolve selected account number from table model.
        String selectedAccountNumber = (String) accountTableModel.getValueAt(selectedRowIndex, 0);
        // Ask for confirmation.
        int userChoice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete account " + selectedAccountNumber + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        // Proceed only when confirmed and deletion succeeds.
        if (userChoice == JOptionPane.YES_OPTION && bankingApp.deleteAccount(selectedAccountNumber)) {
            // Refresh table after deletion.
            refreshData();
            JOptionPane.showMessageDialog(this, "Account deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Deactivates currently selected account row.
    private void deactivateSelectedAccount() {
        // Read selected row index.
        int selectedRowIndex = accountTable.getSelectedRow();
        // Validate selection exists.
        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account to deactivate.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Resolve selected account number from table model.
        String selectedAccountNumber = (String) accountTableModel.getValueAt(selectedRowIndex, 0);
        // Call app controller deactivation flow.
        if (bankingApp.deactivateAccount(selectedAccountNumber)) {
            // Refresh table when successful.
            refreshData();
            JOptionPane.showMessageDialog(this, "Account deactivated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Shows placeholder admin transaction section text.
    private void viewTransactions() {
        // Clear current log text.
        adminTransactionLogArea.setText("");
        // Keep placeholder behavior to match original flow.
        adminTransactionLogArea.append("Transaction History (Feature to be implemented)\n");
    }

    // Converts account object to table row array.
    private Object[] buildAccountRow(Account account) {
        // Return row with number, holder, formatted balance, and status.
        return new Object[] {
            account.getAccountNumber(),
            account.getAccountHolderName(),
            String.format("$%.2f", account.getBalance()),
            account.getAccountStatus()
        };
    }

    // Parses money input text with field-specific error wording.
    private Double parseMoneyInput(String rawValue, String fieldName) {
        try {
            // Parse trimmed string into double value.
            return Double.parseDouble(rawValue.trim());
        } catch (NumberFormatException exception) {
            // Show invalid-number error message.
            JOptionPane.showMessageDialog(this, "Invalid " + fieldName + " amount.", "Error", JOptionPane.ERROR_MESSAGE);
            // Return null when parse fails.
            return null;
        }
    }
}
