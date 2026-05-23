import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * AdminDashboard provides administrative functions.
 * Allows admin to manage client accounts and view bank information.
 */
public class AdminDashboard extends JPanel {
    private BankManagementSystem app;
    
    private JTable accountsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel totalAccountsLabel;
    private JTextArea transactionLogArea;
    
    private JButton searchButton;
    private JButton viewAllButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton deactivateButton;
    private JButton transactionsButton;
    private JButton logoutButton;

    public AdminDashboard(BankManagementSystem app) {
        this.app = app;
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    /**
     * Initializes GUI components.
     */
    private void initializeComponents() {
        String[] columns = {"Account #", "Holder Name", "Balance", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        accountsTable = new JTable(tableModel);
        accountsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        searchField = new JTextField(15);
        totalAccountsLabel = new JLabel("Total Accounts: 0");
        
        transactionLogArea = new JTextArea(10, 30);
        transactionLogArea.setEditable(false);
        transactionLogArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        searchButton = new JButton("Search");
        viewAllButton = new JButton("View All");
        addButton = new JButton("Add Client");
        deleteButton = new JButton("Delete Account");
        deactivateButton = new JButton("Deactivate Account");
        transactionsButton = new JButton("View Transactions");
        logoutButton = new JButton("Logout");
    }

    /**
     * Sets up the panel layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Account #:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(viewAllButton);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(deactivateButton);
        buttonPanel.add(transactionsButton);
        
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(topPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);
        
        add(topContainer, BorderLayout.NORTH);
        add(new JScrollPane(accountsTable), BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalAccountsLabel, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(transactionLogArea), BorderLayout.CENTER);
        
        JPanel mainBottom = new JPanel(new BorderLayout());
        mainBottom.add(bottomPanel, BorderLayout.CENTER);
        mainBottom.add(logoutButton, BorderLayout.EAST);
        
        add(mainBottom, BorderLayout.SOUTH);
    }

    /**
     * Sets up event listeners.
     */
    private void setupListeners() {
        searchButton.addActionListener(e -> searchAccount());
        viewAllButton.addActionListener(e -> refreshData());
        addButton.addActionListener(e -> addNewClient());
        deleteButton.addActionListener(e -> deleteSelectedAccount());
        deactivateButton.addActionListener(e -> deactivateSelectedAccount());
        transactionsButton.addActionListener(e -> viewTransactions());
        logoutButton.addActionListener(e -> app.adminLogout());
    }

    /**
     * Refreshes the account table data.
     */
    public void refreshData() {
        tableModel.setRowCount(0);
        List<Account> accounts = app.getAllAccounts();
        
        for (Account acc : accounts) {
            Object[] row = {acc.getAccountNumber(), acc.getAccountHolderName(), 
                          String.format("$%.2f", acc.getBalance()), acc.getAccountStatus()};
            tableModel.addRow(row);
        }
        
        totalAccountsLabel.setText("Total Accounts: " + accounts.size());
    }

    /**
     * Searches for an account by account number.
     */
    private void searchAccount() {
        String accountNumber = searchField.getText().trim();
        if (accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an account number.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        tableModel.setRowCount(0);
        try {
            Account acc = app.getAccountByNumber(accountNumber);
            Object[] row = {acc.getAccountNumber(), acc.getAccountHolderName(), 
                          String.format("$%.2f", acc.getBalance()), acc.getAccountStatus()};
            tableModel.addRow(row);
        } catch (AccountNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Not Found", 
                                        JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Opens dialog to add a new client.
     */
    private void addNewClient() {
        JTextField accNumField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField pinField = new JTextField();
        JTextField balanceField = new JTextField("0");
        
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Account Number:"));
        panel.add(accNumField);
        panel.add(new JLabel("Holder Name:"));
        panel.add(nameField);
        panel.add(new JLabel("PIN:"));
        panel.add(pinField);
        panel.add(new JLabel("Initial Balance:"));
        panel.add(balanceField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Client", 
                                                   JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double balance = Double.parseDouble(balanceField.getText());
                if (app.addAccount(accNumField.getText().trim(), nameField.getText().trim(),
                                  pinField.getText().trim(), balance)) {
                    refreshData();
                    JOptionPane.showMessageDialog(this, "Account added successfully!", 
                                                  "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Account number already exists.", 
                                                  "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid balance amount.", 
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Deletes the selected account.
     */
    private void deleteSelectedAccount() {
        int selectedRow = accountsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account to delete.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String accountNumber = (String) tableModel.getValueAt(selectedRow, 0);
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete account " + accountNumber + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (app.deleteAccount(accountNumber)) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Account deleted successfully!", 
                                              "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Deactivates the selected account.
     */
    private void deactivateSelectedAccount() {
        int selectedRow = accountsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account to deactivate.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String accountNumber = (String) tableModel.getValueAt(selectedRow, 0);
        if (app.deactivateAccount(accountNumber)) {
            refreshData();
            JOptionPane.showMessageDialog(this, "Account deactivated successfully!", 
                                          "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Displays transaction history.
     */
    private void viewTransactions() {
        transactionLogArea.setText("");
        // This would show all transactions in a real implementation
        transactionLogArea.append("Transaction History (Feature to be implemented)\n");
    }
}