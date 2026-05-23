import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Main application class for Bank Management System.
 * Demonstrates OOP principles through a complete Java Swing application.
 */
public class BankManagementSystem {
    private JFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private LoginPanel loginPanel;
    private AdminDashboard adminDashboard;
    private ClientDashboard clientDashboard;
    private AboutUsPanel aboutUsPanel;
    
    private Map<String, String> adminCredentials;
    private List<Account> accounts;
    private List<Transaction> transactions;
    
    private Admin currentAdmin;
    private Client currentClient;

    public BankManagementSystem() {
        initializeData();
        initializeGUI();
    }

    /**
     * Initializes data from files.
     */
    private void initializeData() {
        adminCredentials = FileManager.readAdminsFromFile();
        accounts = FileManager.readClientsFromFile();
        transactions = FileManager.readTransactionsFromFile();
    }

    /**
     * Initializes the main GUI components.
     */
    private void initializeGUI() {
        mainFrame = new JFrame("KAAFI BANK Management System");
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        loginPanel = new LoginPanel(this);
        adminDashboard = new AdminDashboard(this);
        clientDashboard = new ClientDashboard(this);
        aboutUsPanel = new AboutUsPanel(this);
        
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(adminDashboard, "ADMIN_DASHBOARD");
        mainPanel.add(clientDashboard, "CLIENT_DASHBOARD");
        mainPanel.add(aboutUsPanel, "ABOUT_US");
        
        mainFrame.add(mainPanel);
        
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }

    /**
     * Shows the specified panel by name.
     */
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    /**
     * Handles admin login.
     */
    public boolean adminLogin(String username, String password) {
        if (adminCredentials.containsKey(username) && 
            adminCredentials.get(username).equals(password)) {
            currentAdmin = new Admin(username, "Admin User", password);
            adminDashboard.refreshData();
            showPanel("ADMIN_DASHBOARD");
            return true;
        }
        return false;
    }

    /**
     * Handles client login.
     */
    public boolean clientLogin(String accountNumber, String pin) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                if (acc.isActive() && acc.validatePin(pin)) {
                    currentClient = new Client(accountNumber, acc.getAccountHolderName(), 
                                                pin, acc);
                    clientDashboard.refreshData();
                    showPanel("CLIENT_DASHBOARD");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Handles client logout.
     */
    public void clientLogout() {
        currentClient = null;
        showPanel("LOGIN");
    }

    /**
     * Handles admin logout.
     */
    public void adminLogout() {
        currentAdmin = null;
        showPanel("LOGIN");
    }

    /**
     * Gets account by account number.
     */
    public Account getAccountByNumber(String accountNumber) throws AccountNotFoundException {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        throw new AccountNotFoundException("Account " + accountNumber + " not found.");
    }

    /**
     * Handles deposit operation.
     */
    public boolean deposit(String accountNumber, double amount) {
        try {
            Account acc = getAccountByNumber(accountNumber);
            acc.deposit(amount);
            saveTransaction(new Transaction("TXN" + (transactions.size() + 1), 
                          accountNumber, "DEPOSIT", amount));
            saveAccountsToFile();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", 
                                        JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Handles withdrawal operation.
     */
    public boolean withdraw(String accountNumber, double amount) {
        try {
            Account acc = getAccountByNumber(accountNumber);
            acc.withdraw(amount);
            saveTransaction(new Transaction("TXN" + (transactions.size() + 1), 
                          accountNumber, "WITHDRAWAL", amount));
            saveAccountsToFile();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", 
                                        JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Handles transfer operation.
     */
    public boolean transfer(String fromAccount, String toAccount, double amount) {
        try {
            Account source = getAccountByNumber(fromAccount);
            Account target = getAccountByNumber(toAccount);
            
            if (source == null || target == null) {
                throw new AccountNotFoundException("One or both accounts not found.");
            }
            
            source.withdraw(amount);
            target.deposit(amount);
            
            saveTransaction(new Transaction("TXN" + (transactions.size() + 1), 
                          fromAccount, "TRANSFER", amount, toAccount));
            saveAccountsToFile();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", 
                                        JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Saves transaction to file.
     */
    private void saveTransaction(Transaction trans) {
        transactions.add(trans);
        try {
            FileManager.appendTransactionToFile(trans);
        } catch (Exception e) {
            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }

    /**
     * Saves accounts to file.
     */
    private void saveAccountsToFile() {
        try {
            FileManager.writeClientsToFile(accounts);
        } catch (Exception e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }

    /**
     * Gets transactions for an account.
     */
    public List<Transaction> getAccountTransactions(String accountNumber) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAccountNumber().equals(accountNumber) || 
                t.getTargetAccount().equals(accountNumber)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Gets total account count.
     */
    public int getTotalAccounts() {
        return accounts.size();
    }

    /**
     * Gets account list for admin view.
     */
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    /**
     * Gets the account for the currently logged-in client.
     */
    public Account getCurrentClientAccount() {
        if (currentClient != null) {
            return currentClient.getAccount();
        }
        return null;
    }

    /**
     * Deletes a client account.
     */
    public boolean deleteAccount(String accountNumber) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNumber().equals(accountNumber)) {
                accounts.remove(i);
                saveAccountsToFile();
                return true;
            }
        }
        return false;
    }

    /**
     * Deactivates a client account.
     */
    public boolean deactivateAccount(String accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                acc.setAccountStatus("INACTIVE");
                saveAccountsToFile();
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new client account.
     */
    public boolean addAccount(String accountNumber, String name, String pin, double balance) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return false; // Account already exists
            }
        }
        Account newAccount = new Account(accountNumber, name, balance, pin);
        accounts.add(newAccount);
        saveAccountsToFile();
        return true;
    }

    /**
     * Exits the application with confirmation.
     */
    public void exitApplication() {
        int result = JOptionPane.showConfirmDialog(mainFrame, 
            "Are you sure you want to exit?", "Exit Confirmation", 
            JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Starts the application.
     */
    public void start() {
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BankManagementSystem().start();
        });
    }
}