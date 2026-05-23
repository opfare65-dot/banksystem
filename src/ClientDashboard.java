import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * ClientDashboard provides client banking functions.
 * Allows clients to view account details, deposit, withdraw, transfer, and view transactions.
 */
public class ClientDashboard extends JPanel {
    private BankManagementSystem app;
    
    private JLabel accountNumberLabel;
    private JLabel holderNameLabel;
    private JLabel balanceLabel;
    private JLabel statusLabel;
    
    private JTextField amountField;
    private JTextField targetAccountField;
    
    private JTextArea transactionArea;
    
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton transferButton;
    private JButton transactionButton;
    private JButton logoutButton;

    public ClientDashboard(BankManagementSystem app) {
        this.app = app;
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    /**
     * Initializes GUI components.
     */
    private void initializeComponents() {
        accountNumberLabel = new JLabel("Account #: ");
        holderNameLabel = new JLabel("Holder: ");
        balanceLabel = new JLabel("Balance: $0.00");
        statusLabel = new JLabel("Status: ");
        
        amountField = new JTextField(10);
        targetAccountField = new JTextField(15);
        
        transactionArea = new JTextArea(15, 40);
        transactionArea.setEditable(false);
        transactionArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        transferButton = new JButton("Transfer");
        transactionButton = new JButton("View Transactions");
        logoutButton = new JButton("Logout");
    }

    /**
     * Sets up the panel layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));
        infoPanel.add(accountNumberLabel);
        infoPanel.add(holderNameLabel);
        infoPanel.add(balanceLabel);
        infoPanel.add(statusLabel);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Bank Operations"));
        actionPanel.add(new JLabel("Amount: $"));
        actionPanel.add(amountField);
        actionPanel.add(depositButton);
        actionPanel.add(withdrawButton);
        actionPanel.add(new JLabel("To Account:"));
        actionPanel.add(targetAccountField);
        actionPanel.add(transferButton);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(transactionButton);
        buttonPanel.add(logoutButton);
        
        add(infoPanel, BorderLayout.NORTH);
        add(actionPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(new JScrollPane(transactionArea), BorderLayout.EAST);
    }

    /**
     * Sets up event listeners.
     */
    private void setupListeners() {
        depositButton.addActionListener(e -> performDeposit());
        withdrawButton.addActionListener(e -> performWithdrawal());
        transferButton.addActionListener(e -> performTransfer());
        transactionButton.addActionListener(e -> viewTransactions());
        logoutButton.addActionListener(e -> app.clientLogout());
    }

    /**
     * Refreshes the dashboard with current account data.
     */
    public void refreshData() {
        Account acc = getCurrentAccount();
        if (acc != null) {
            accountNumberLabel.setText("Account #: " + acc.getAccountNumber());
            holderNameLabel.setText("Holder: " + acc.getAccountHolderName());
            balanceLabel.setText(String.format("Balance: $%.2f", acc.getBalance()));
            statusLabel.setText("Status: " + acc.getAccountStatus());
        }
    }

    /**
     * Gets the current logged-in client's account.
     */
    private Account getCurrentAccount() {
        // Return the logged-in client's account
        Account acc = app.getCurrentClientAccount();
        if (acc == null && app.getAllAccounts().size() > 0) {
            return app.getAllAccounts().get(0); // Fallback for demo
        }
        return acc;
    }

    /**
     * Performs deposit operation.
     */
    private void performDeposit() {
        Account acc = getCurrentAccount();
        if (acc == null) return;
        
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (app.deposit(acc.getAccountNumber(), amount)) {
                refreshData();
                transactionArea.append("Deposited $" + amount + "\n");
                amountField.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", 
                                        JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Performs withdrawal operation.
     */
    private void performWithdrawal() {
        Account acc = getCurrentAccount();
        if (acc == null) return;
        
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (app.withdraw(acc.getAccountNumber(), amount)) {
                refreshData();
                transactionArea.append("Withdrew $" + amount + "\n");
                amountField.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", 
                                        JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Performs transfer operation.
     */
    private void performTransfer() {
        Account acc = getCurrentAccount();
        if (acc == null) return;
        
        String targetAccount = targetAccountField.getText().trim();
        if (targetAccount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter target account number.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (app.transfer(acc.getAccountNumber(), targetAccount, amount)) {
                refreshData();
                transactionArea.append("Transferred $" + amount + " to " + targetAccount + "\n");
                amountField.setText("");
                targetAccountField.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", 
                                        JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Views transaction history.
     */
    private void viewTransactions() {
        Account acc = getCurrentAccount();
        if (acc == null) return;
        
        transactionArea.setText("");
        List<Transaction> transactions = app.getAccountTransactions(acc.getAccountNumber());
        for (Transaction t : transactions) {
            transactionArea.append(t.getTransactionType() + ": $" + t.getAmount() + 
                                  " on " + t.getDate() + "\n");
        }
        if (transactions.isEmpty()) {
            transactionArea.setText("No transactions found.");
        }
    }
}