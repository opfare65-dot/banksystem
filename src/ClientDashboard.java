import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

/**
 * Client dashboard for daily account operations.
 */
public class ClientDashboard extends JPanel {
    // Main app reference used for account operations and navigation.
    private final BankManagementSystem bankingApp;

    // Displays logged-in account number.
    private JLabel accountNumberLabel;
    // Displays account holder name.
    private JLabel accountHolderLabel;
    // Displays current account balance.
    private JLabel balanceLabel;
    // Displays account status text.
    private JLabel accountStatusLabel;

    // Input for transaction amount.
    private JTextField amountInputField;
    // Input for target account when transferring funds.
    private JTextField targetAccountInputField;
    // Area showing activity and transaction history lines.
    private JTextArea activityLogArea;

    // Button for deposit action.
    private JButton depositButton;
    // Button for withdrawal action.
    private JButton withdrawButton;
    // Button for transfer action.
    private JButton transferButton;
    // Button for loading transaction history.
    private JButton viewTransactionsButton;
    // Button for client logout.
    private JButton logoutButton;

    // Creates dashboard and wires everything.
    public ClientDashboard(BankManagementSystem app) {
        // Save app reference.
        this.bankingApp = app;
        // Build Swing components.
        initializeComponents();
        // Arrange components on panel.
        buildLayout();
        // Attach event listeners.
        setupListeners();
    }

    // Creates all Swing controls used by the dashboard.
    private void initializeComponents() {
        // Build account number label.
        accountNumberLabel = new JLabel("Account #: ");
        // Build account holder label.
        accountHolderLabel = new JLabel("Holder: ");
        // Build balance label with default value.
        balanceLabel = new JLabel("Balance: $0.00");
        // Build status label.
        accountStatusLabel = new JLabel("Status: ");

        // Build amount input field.
        amountInputField = new JTextField(10);
        // Build target account input field.
        targetAccountInputField = new JTextField(15);

        // Build multiline activity area.
        activityLogArea = new JTextArea(15, 40);
        // Keep activity area read-only.
        activityLogArea.setEditable(false);
        // Use monospaced font for clearer logs.
        activityLogArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Create operation buttons.
        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        transferButton = new JButton("Transfer");
        viewTransactionsButton = new JButton("View Transactions");
        logoutButton = new JButton("Logout");
    }

    // Builds visual layout sections.
    private void buildLayout() {
        // Use border layout for panel structure.
        setLayout(new BorderLayout(10, 10));
        // Add outer padding around the dashboard.
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create top account summary panel.
        JPanel accountInfoPanel = new JPanel(new GridLayout(4, 1));
        // Add titled border to account section.
        accountInfoPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));
        // Add account fields to info panel.
        accountInfoPanel.add(accountNumberLabel);
        accountInfoPanel.add(accountHolderLabel);
        accountInfoPanel.add(balanceLabel);
        accountInfoPanel.add(accountStatusLabel);

        // Create center operations panel.
        JPanel operationsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Add titled border to operations section.
        operationsPanel.setBorder(BorderFactory.createTitledBorder("Bank Operations"));
        // Add amount label and input.
        operationsPanel.add(new JLabel("Amount: $"));
        operationsPanel.add(amountInputField);
        // Add deposit and withdraw buttons.
        operationsPanel.add(depositButton);
        operationsPanel.add(withdrawButton);
        // Add transfer target field.
        operationsPanel.add(new JLabel("To Account:"));
        operationsPanel.add(targetAccountInputField);
        operationsPanel.add(transferButton);

        // Create bottom action button row.
        JPanel bottomButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Add transaction-history and logout buttons.
        bottomButtonsPanel.add(viewTransactionsButton);
        bottomButtonsPanel.add(logoutButton);

        // Place top panel.
        add(accountInfoPanel, BorderLayout.NORTH);
        // Place center panel.
        add(operationsPanel, BorderLayout.CENTER);
        // Place bottom panel.
        add(bottomButtonsPanel, BorderLayout.SOUTH);
        // Place activity log on right side inside scroll pane.
        add(new JScrollPane(activityLogArea), BorderLayout.EAST);
    }

    // Wires button actions to handler methods.
    private void setupListeners() {
        // Deposit handler.
        depositButton.addActionListener(event -> performDeposit());
        // Withdraw handler.
        withdrawButton.addActionListener(event -> performWithdrawal());
        // Transfer handler.
        transferButton.addActionListener(event -> performTransfer());
        // View-transactions handler.
        viewTransactionsButton.addActionListener(event -> viewTransactions());
        // Logout handler.
        logoutButton.addActionListener(event -> bankingApp.clientLogout());
    }

    // Refreshes summary labels using current account data.
    public void refreshData() {
        // Resolve current session account.
        Account currentAccount = getCurrentSessionAccount();
        // Update labels only when account exists.
        if (currentAccount != null) {
            accountNumberLabel.setText("Account #: " + currentAccount.getAccountNumber());
            accountHolderLabel.setText("Holder: " + currentAccount.getAccountHolderName());
            balanceLabel.setText(String.format("Balance: $%.2f", currentAccount.getBalance()));
            accountStatusLabel.setText("Status: " + currentAccount.getAccountStatus());
        }
    }

    // Returns currently logged-in account or shows session error.
    private Account getCurrentSessionAccount() {
        // Ask app controller for current client account.
        Account currentAccount = bankingApp.getCurrentClientAccount();
        // Warn user if no active session is present.
        if (currentAccount == null) {
            JOptionPane.showMessageDialog(this, "No active client session found.", "Session Error", JOptionPane.ERROR_MESSAGE);
        }
        // Return account reference (or null).
        return currentAccount;
    }

    // Handles deposit action.
    private void performDeposit() {
        // Resolve current account.
        Account currentAccount = getCurrentSessionAccount();
        // Stop when no account found.
        if (currentAccount == null) {
            return;
        }

        // Parse amount input text.
        Double parsedAmount = parseMoneyInput(amountInputField.getText());
        // Stop when amount is invalid.
        if (parsedAmount == null) {
            return;
        }

        // Attempt deposit through controller.
        if (bankingApp.deposit(currentAccount.getAccountNumber(), parsedAmount)) {
            // Refresh account summary after successful deposit.
            refreshData();
            // Append operation log line.
            activityLogArea.append("Deposited $" + parsedAmount + "\n");
            // Clear amount field for next input.
            amountInputField.setText("");
        }
    }

    // Handles withdrawal action.
    private void performWithdrawal() {
        // Resolve current account.
        Account currentAccount = getCurrentSessionAccount();
        // Stop when no account found.
        if (currentAccount == null) {
            return;
        }

        // Parse amount input text.
        Double parsedAmount = parseMoneyInput(amountInputField.getText());
        // Stop when amount is invalid.
        if (parsedAmount == null) {
            return;
        }

        // Attempt withdrawal through controller.
        if (bankingApp.withdraw(currentAccount.getAccountNumber(), parsedAmount)) {
            // Refresh account summary after successful withdrawal.
            refreshData();
            // Append operation log line.
            activityLogArea.append("Withdrew $" + parsedAmount + "\n");
            // Clear amount field for next input.
            amountInputField.setText("");
        }
    }

    // Handles transfer action.
    private void performTransfer() {
        // Resolve current account.
        Account currentAccount = getCurrentSessionAccount();
        // Stop when no account found.
        if (currentAccount == null) {
            return;
        }

        // Read target account number from input field.
        String destinationAccountNumber = targetAccountInputField.getText().trim();
        // Validate required target account input.
        if (destinationAccountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter target account number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Parse amount input text.
        Double parsedAmount = parseMoneyInput(amountInputField.getText());
        // Stop when amount is invalid.
        if (parsedAmount == null) {
            return;
        }

        // Attempt transfer through controller.
        if (bankingApp.transfer(currentAccount.getAccountNumber(), destinationAccountNumber, parsedAmount)) {
            // Refresh account summary after successful transfer.
            refreshData();
            // Append operation log line.
            activityLogArea.append("Transferred $" + parsedAmount + " to " + destinationAccountNumber + "\n");
            // Clear amount field after action.
            amountInputField.setText("");
            // Clear target field after action.
            targetAccountInputField.setText("");
        }
    }

    // Loads and displays account transaction history.
    private void viewTransactions() {
        // Resolve current account.
        Account currentAccount = getCurrentSessionAccount();
        // Stop when no account found.
        if (currentAccount == null) {
            return;
        }

        // Clear old log text.
        activityLogArea.setText("");
        // Fetch account transaction list from controller.
        List<Transaction> accountTransactions = bankingApp.getAccountTransactions(currentAccount.getAccountNumber());
        // Show no-data message when list is empty.
        if (accountTransactions.isEmpty()) {
            activityLogArea.setText("No transactions found.");
            return;
        }

        // Print each transaction on one log line.
        for (Transaction transaction : accountTransactions) {
            activityLogArea.append(
                transaction.getTransactionType() + ": $" + transaction.getAmount() + " on " + transaction.getDate() + "\n"
            );
        }
    }

    // Parses text to double amount or shows error.
    private Double parseMoneyInput(String rawInput) {
        try {
            // Convert trimmed text into numeric double.
            return Double.parseDouble(rawInput.trim());
        } catch (NumberFormatException exception) {
            // Show invalid-amount message.
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            // Return null on parse failure.
            return null;
        }
    }
}
