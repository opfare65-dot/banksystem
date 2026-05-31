import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents one transaction record in the system.
 */
public class Transaction {
    // Shared date format used for saved transaction timestamps.
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Unique transaction ID such as TXN1.
    private String transactionId;
    // Primary account number associated with the transaction.
    private String accountNumber;
    // Business type (DEPOSIT, WITHDRAWAL, TRANSFER).
    private String transactionType;
    // Transaction amount value.
    private double amount;
    // Stored transaction date/time text.
    private String date;
    // Target account used for transfers (empty for other types).
    private String targetAccount;

    // Public constructor for normal runtime transaction creation.
    public Transaction(String transactionId, String accountNumber, String transactionType, double amount, String targetAccount) {
        // Delegate to full constructor and auto-generate current timestamp.
        this(transactionId, accountNumber, transactionType, amount, LocalDateTime.now().format(DATE_FORMAT), targetAccount);
    }

    // Convenience constructor for non-transfer transactions.
    public Transaction(String transactionId, String accountNumber, String transactionType, double amount) {
        // Use empty target account when not needed.
        this(transactionId, accountNumber, transactionType, amount, "");
    }

    // Internal constructor used for both runtime and file-loading flows.
    private Transaction(
        String transactionId,
        String accountNumber,
        String transactionType,
        double amount,
        String transactionDate,
        String targetAccount
    ) {
        // Save transaction ID.
        this.transactionId = transactionId;
        // Save source/main account number.
        this.accountNumber = accountNumber;
        // Save transaction type string.
        this.transactionType = transactionType;
        // Save transaction amount.
        this.amount = amount;
        // Save date text.
        this.date = transactionDate;
        // Save target account number if provided.
        this.targetAccount = targetAccount;
    }

    // Returns transaction ID.
    public String getTransactionId() {
        return transactionId;
    }

    // Returns account number.
    public String getAccountNumber() {
        return accountNumber;
    }

    // Returns transaction type.
    public String getTransactionType() {
        return transactionType;
    }

    // Returns transaction amount.
    public double getAmount() {
        return amount;
    }

    // Returns date text.
    public String getDate() {
        return date;
    }

    // Returns target account number text.
    public String getTargetAccount() {
        return targetAccount;
    }

    // Serializes transaction into one CSV line.
    public String toFileString() {
        // Join all persisted fields in fixed order.
        return String.join(",", transactionId, accountNumber, transactionType, String.valueOf(amount), date, targetAccount);
    }

    // Parses one CSV line into a Transaction object.
    public static Transaction fromFileString(String line) {
        // Split input by comma.
        String[] csvParts = line.split(",");
        // Validate minimum required field count.
        if (csvParts.length >= 5) {
            // Read optional target account when sixth field exists.
            String parsedTargetAccount = csvParts.length >= 6 ? csvParts[5] : "";
            // Rebuild transaction using parsed values.
            return new Transaction(
                csvParts[0],
                csvParts[1],
                csvParts[2],
                Double.parseDouble(csvParts[3]),
                csvParts[4],
                parsedTargetAccount
            );
        }
        // Return null when data format is invalid.
        return null;
    }

    // Prints transaction details to standard output.
    public void displayTransaction() {
        // Print ID line.
        System.out.println("Transaction ID: " + transactionId);
        // Print account line.
        System.out.println("Account Number: " + accountNumber);
        // Print type line.
        System.out.println("Type: " + transactionType);
        // Print amount line.
        System.out.println("Amount: $" + amount);
        // Print date line.
        System.out.println("Date: " + date);
        // Print target only for transfer-like records.
        if (!targetAccount.isEmpty()) {
            System.out.println("Target Account: " + targetAccount);
        }
        // Print footer divider.
        System.out.println("-------------------------");
    }
}
