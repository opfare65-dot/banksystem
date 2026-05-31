import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Main application controller.
 * Holds GUI screens, loaded data, and active user session state.
 */
public class BankManagementSystem {
    // Card key for login screen.
    private static final String PANEL_LOGIN = "LOGIN";
    // Card key for admin dashboard screen.
    private static final String PANEL_ADMIN_DASHBOARD = "ADMIN_DASHBOARD";
    // Card key for client dashboard screen.
    private static final String PANEL_CLIENT_DASHBOARD = "CLIENT_DASHBOARD";
    // Card key for about-us screen.
    private static final String PANEL_ABOUT_US = "ABOUT_US";

    // Transaction type constant for deposits.
    private static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    // Transaction type constant for withdrawals.
    private static final String TRANSACTION_TYPE_WITHDRAWAL = "WITHDRAWAL";
    // Transaction type constant for transfers.
    private static final String TRANSACTION_TYPE_TRANSFER = "TRANSFER";

    // Top-level JFrame for the whole app.
    private JFrame applicationFrame;
    // CardLayout used to switch between screens.
    private CardLayout screenCardLayout;
    // Panel that hosts all card screens.
    private JPanel screenContainerPanel;

    // Login screen component.
    private LoginPanel loginScreen;
    // Admin dashboard component.
    private AdminDashboard adminDashboardScreen;
    // Client dashboard component.
    private ClientDashboard clientDashboardScreen;
    // About-us screen component.
    private AboutUsPanel aboutUsScreen;

    // Map of admin username to admin password.
    private Map<String, String> adminCredentialMap;
    // In-memory list of all client accounts.
    private List<Account> accountList;
    // In-memory list of all transactions.
    private List<Transaction> transactionHistoryList;

    // Currently logged-in admin object.
    private Admin loggedInAdmin;
    // Currently logged-in client object.
    private Client loggedInClient;

    // Creates app controller and initializes data/UI.
    public BankManagementSystem() {
        // Load accounts, admins, and transactions from files.
        loadInitialData();
        // Build application frame and screens.
        buildApplicationWindow();
    }

    // Loads persisted data into memory.
    private void loadInitialData() {
        // Load admin credentials map from file.
        adminCredentialMap = FileManager.readAdminsFromFile();
        // Load accounts list from file.
        accountList = FileManager.readClientsFromFile();
        // Load transaction history list from file.
        transactionHistoryList = FileManager.readTransactionsFromFile();
    }

    // Builds the app frame and all card panels.
    private void buildApplicationWindow() {
        // Create main app frame.
        applicationFrame = new JFrame("KAAFI BANK Management System");
        // Use manual close behavior so we can show confirmation.
        applicationFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // Set default frame size.
        applicationFrame.setSize(800, 600);
        // Center frame on screen.
        applicationFrame.setLocationRelativeTo(null);

        // Create card layout manager.
        screenCardLayout = new CardLayout();
        // Create panel that hosts all cards.
        screenContainerPanel = new JPanel(screenCardLayout);

        // Create login panel and pass app reference.
        loginScreen = new LoginPanel(this);
        // Create admin dashboard and pass app reference.
        adminDashboardScreen = new AdminDashboard(this);
        // Create client dashboard and pass app reference.
        clientDashboardScreen = new ClientDashboard(this);
        // Create about-us panel and pass app reference.
        aboutUsScreen = new AboutUsPanel(this);

        // Register login card.
        screenContainerPanel.add(loginScreen, PANEL_LOGIN);
        // Register admin dashboard card.
        screenContainerPanel.add(adminDashboardScreen, PANEL_ADMIN_DASHBOARD);
        // Register client dashboard card.
        screenContainerPanel.add(clientDashboardScreen, PANEL_CLIENT_DASHBOARD);
        // Register about-us card.
        screenContainerPanel.add(aboutUsScreen, PANEL_ABOUT_US);

        // Add card container to frame.
        applicationFrame.add(screenContainerPanel);

        // Intercept window-close clicks to confirm before exit.
        applicationFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                // Reuse central exit confirmation flow.
                exitApplication();
            }
        });
    }

    // Shows a screen by card key.
    public void showPanel(String panelName) {
        // Ask CardLayout to display requested card.
        screenCardLayout.show(screenContainerPanel, panelName);
    }

    // Authenticates admin credentials and opens admin dashboard.
    public boolean adminLogin(String username, String password) {
        // Validate username exists and password matches stored value.
        if (adminCredentialMap.containsKey(username) && adminCredentialMap.get(username).equals(password)) {
            // Create logged-in admin session object.
            loggedInAdmin = new Admin(username, "Admin User", password);
            // Refresh dashboard table before showing it.
            adminDashboardScreen.refreshData();
            // Navigate to admin dashboard.
            showPanel(PANEL_ADMIN_DASHBOARD);
            return true;
        }
        // Return false when credentials do not match.
        return false;
    }

    // Authenticates client credentials and opens client dashboard.
    public boolean clientLogin(String accountNumber, String pin) {
        // Loop through all accounts.
        for (Account account : accountList) {
            // Check matching account number.
            if (account.getAccountNumber().equals(accountNumber)) {
                // Check active status and PIN match.
                if (account.isActive() && account.validatePin(pin)) {
                    // Create logged-in client session object.
                    loggedInClient = new Client(accountNumber, account.getAccountHolderName(), pin, account);
                    // Refresh dashboard summary before showing it.
                    clientDashboardScreen.refreshData();
                    // Navigate to client dashboard.
                    showPanel(PANEL_CLIENT_DASHBOARD);
                    return true;
                }
                // Stop early when account found but PIN/status is invalid.
                return false;
            }
        }
        // Return false when no matching account number is found.
        return false;
    }

    // Clears client session and returns to login screen.
    public void clientLogout() {
        // Remove logged-in client reference.
        loggedInClient = null;
        // Navigate back to login card.
        showPanel(PANEL_LOGIN);
    }

    // Clears admin session and returns to login screen.
    public void adminLogout() {
        // Remove logged-in admin reference.
        loggedInAdmin = null;
        // Navigate back to login card.
        showPanel(PANEL_LOGIN);
    }

    // Finds account object by account number.
    public Account getAccountByNumber(String accountNumber) throws AccountNotFoundException {
        // Scan every account in list.
        for (Account account : accountList) {
            // Return account immediately on match.
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        // Throw typed exception when account is missing.
        throw new AccountNotFoundException("Account " + accountNumber + " not found.");
    }

    // Handles deposit operation.
    public boolean deposit(String accountNumber, double amount) {
        try {
            // Resolve destination account.
            Account destinationAccount = getAccountByNumber(accountNumber);
            // Apply deposit business logic on account.
            destinationAccount.deposit(amount);
            // Record transaction in memory and file.
            saveTransaction(new Transaction(generateTransactionId(), accountNumber, TRANSACTION_TYPE_DEPOSIT, amount));
            // Persist updated account balances.
            saveAccountsToFile();
            return true;
        } catch (Exception exception) {
            // Show user-friendly error message dialog.
            JOptionPane.showMessageDialog(applicationFrame, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Handles withdrawal operation.
    public boolean withdraw(String accountNumber, double amount) {
        try {
            // Resolve source account.
            Account sourceAccount = getAccountByNumber(accountNumber);
            // Apply withdrawal business logic on account.
            sourceAccount.withdraw(amount);
            // Record transaction in memory and file.
            saveTransaction(new Transaction(generateTransactionId(), accountNumber, TRANSACTION_TYPE_WITHDRAWAL, amount));
            // Persist updated account balances.
            saveAccountsToFile();
            return true;
        } catch (Exception exception) {
            // Show user-friendly error message dialog.
            JOptionPane.showMessageDialog(applicationFrame, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Handles transfer operation.
    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        try {
            // Prevent self-transfer because it is usually accidental/no-op.
            if (fromAccountNumber.equals(toAccountNumber)) {
                throw new InvalidAmountException("Cannot transfer to the same account.");
            }

            // Resolve source account.
            Account sourceAccount = getAccountByNumber(fromAccountNumber);
            // Resolve destination account.
            Account targetAccount = getAccountByNumber(toAccountNumber);

            // First debit source account.
            sourceAccount.withdraw(amount);
            // Then credit target account.
            targetAccount.deposit(amount);

            // Record transfer transaction in memory and file.
            saveTransaction(
                new Transaction(generateTransactionId(), fromAccountNumber, TRANSACTION_TYPE_TRANSFER, amount, toAccountNumber)
            );
            // Persist both affected account balances.
            saveAccountsToFile();
            return true;
        } catch (Exception exception) {
            // Show user-friendly error message dialog.
            JOptionPane.showMessageDialog(applicationFrame, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Adds transaction to in-memory list and appends to file.
    private void saveTransaction(Transaction transactionRecord) {
        // Add transaction to runtime history list.
        transactionHistoryList.add(transactionRecord);
        try {
            // Append transaction row to transaction file.
            FileManager.appendTransactionToFile(transactionRecord);
        } catch (Exception exception) {
            // Print file-write error in console.
            System.err.println("Error saving transaction: " + exception.getMessage());
        }
    }

    // Writes current account list to clients file.
    private void saveAccountsToFile() {
        try {
            // Persist all accounts.
            FileManager.writeClientsToFile(accountList);
        } catch (Exception exception) {
            // Print file-write error in console.
            System.err.println("Error saving accounts: " + exception.getMessage());
        }
    }

    // Returns transactions where account is sender or receiver.
    public List<Transaction> getAccountTransactions(String accountNumber) {
        // Prepare output list.
        List<Transaction> matchingTransactions = new ArrayList<>();
        // Check each transaction record.
        for (Transaction transaction : transactionHistoryList) {
            // Include when account is primary account or transfer target.
            if (transaction.getAccountNumber().equals(accountNumber) || transaction.getTargetAccount().equals(accountNumber)) {
                matchingTransactions.add(transaction);
            }
        }
        // Return filtered transaction list.
        return matchingTransactions;
    }

    // Returns total account count.
    public int getTotalAccounts() {
        // Return list size.
        return accountList.size();
    }

    // Returns defensive copy of all accounts.
    public List<Account> getAllAccounts() {
        // Return copied list to avoid exposing internal list reference.
        return new ArrayList<>(accountList);
    }

    // Returns currently logged-in client's account object.
    public Account getCurrentClientAccount() {
        // Return account when client session exists, otherwise null.
        return loggedInClient != null ? loggedInClient.getAccount() : null;
    }

    // Deletes one account by account number.
    public boolean deleteAccount(String accountNumber) {
        // Iterate with index because we remove on match.
        for (int accountIndex = 0; accountIndex < accountList.size(); accountIndex++) {
            // Compare account number at current index.
            if (accountList.get(accountIndex).getAccountNumber().equals(accountNumber)) {
                // Remove matching account.
                accountList.remove(accountIndex);
                // Persist updated account list.
                saveAccountsToFile();
                return true;
            }
        }
        // Return false when no account was removed.
        return false;
    }

    // Sets one account to INACTIVE status.
    public boolean deactivateAccount(String accountNumber) {
        // Scan all accounts.
        for (Account account : accountList) {
            // Find matching account.
            if (account.getAccountNumber().equals(accountNumber)) {
                // Set account status to INACTIVE.
                account.setAccountStatus(Account.STATUS_INACTIVE);
                // Persist updated account status.
                saveAccountsToFile();
                return true;
            }
        }
        // Return false when account is not found.
        return false;
    }

    // Adds a new account when number is unique.
    public boolean addAccount(String accountNumber, String holderName, String pin, double openingBalance) {
        // Scan existing accounts to enforce unique account number.
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return false;
            }
        }

        // Create new account object.
        Account createdAccount = new Account(accountNumber, holderName, openingBalance, pin);
        // Add account to runtime list.
        accountList.add(createdAccount);
        // Persist updated account list.
        saveAccountsToFile();
        return true;
    }

    // Shows exit confirmation and closes app on YES.
    public void exitApplication() {
        // Ask user for exit confirmation.
        int userChoice = JOptionPane.showConfirmDialog(
            applicationFrame,
            "Are you sure you want to exit?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION
        );
        // Exit process only on YES selection.
        if (userChoice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // Makes frame visible.
    public void start() {
        // Display top-level frame.
        applicationFrame.setVisible(true);
    }

    // Generates next transaction ID based on list size.
    private String generateTransactionId() {
        // Example format: TXN1, TXN2, ...
        return "TXN" + (transactionHistoryList.size() + 1);
    }

    // Application entry point.
    public static void main(String[] args) {
        // Start Swing app on EDT thread.
        SwingUtilities.invokeLater(() -> new BankManagementSystem().start());
    }
}
