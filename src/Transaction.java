import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transaction class represents a bank transaction record.
 * Encapsulates transaction details and provides methods to save and display transactions.
 */
public class Transaction {
    private String transactionId;
    private String accountNumber;
    private String transactionType;
    private double amount;
    private String date;
    private String targetAccount; // For transfers

    public Transaction(String transactionId, String accountNumber, String transactionType, 
                       double amount, String targetAccount) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.targetAccount = targetAccount;
    }

    public Transaction(String transactionId, String accountNumber, String transactionType, 
                       double amount) {
        this(transactionId, accountNumber, transactionType, amount, "");
    }

    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public String getTransactionType() { return transactionType; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getTargetAccount() { return targetAccount; }

    /**
     * Formats transaction data as a CSV line for file storage.
     */
    public String toFileString() {
        return String.join(",", transactionId, accountNumber, transactionType, 
                          String.valueOf(amount), date, targetAccount);
    }

    /**
     * Creates a Transaction object from a CSV line.
     */
    public static Transaction fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 5) {
            return new Transaction(parts[0], parts[1], parts[2], 
                                 Double.parseDouble(parts[3]), parts[4]);
        }
        return null;
    }

    /**
     * Displays transaction details in a formatted manner.
     * Polymorphism: This method can be overridden by subclasses if needed.
     */
    public void displayTransaction() {
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Type: " + transactionType);
        System.out.println("Amount: $" + amount);
        System.out.println("Date: " + date);
        if (!targetAccount.isEmpty()) {
            System.out.println("Target Account: " + targetAccount);
        }
        System.out.println("-------------------------");
    }
}