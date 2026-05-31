/**
 * Simple bank account model.
 * Keeps account identity, credential, status, and balance.
 */
public class Account {
    // Constant for active account state.
    public static final String STATUS_ACTIVE = "ACTIVE";
    // Constant for inactive account state.
    public static final String STATUS_INACTIVE = "INACTIVE";

    // Unique account identifier.
    private String accountNumber;
    // Human-readable account owner name.
    private String accountHolderName;
    // Current wallet balance.
    private double balance;
    // PIN used by client login.
    private String pin;
    // Current account status flag.
    private String accountStatus;

    // Creates a new account object with ACTIVE default status.
    public Account(String accountNumber, String accountHolderName, double openingBalance, String pin) {
        // Save account number exactly as provided.
        this.accountNumber = accountNumber;
        // Save holder name exactly as provided.
        this.accountHolderName = accountHolderName;
        // Set initial balance to caller-provided opening balance.
        this.balance = openingBalance;
        // Set PIN to caller-provided value.
        this.pin = pin;
        // New accounts start as ACTIVE by default.
        this.accountStatus = STATUS_ACTIVE;
    }

    // Returns account number.
    public String getAccountNumber() {
        return accountNumber;
    }

    // Returns account holder name.
    public String getAccountHolderName() {
        return accountHolderName;
    }

    // Returns current numeric balance.
    public double getBalance() {
        return balance;
    }

    // Returns account PIN.
    public String getPin() {
        return pin;
    }

    // Returns account status string.
    public String getAccountStatus() {
        return accountStatus;
    }

    // Overwrites account balance with a new value.
    public void setBalance(double newBalance) {
        // Replace old balance value.
        this.balance = newBalance;
    }

    // Overwrites account status with caller-provided status text.
    public void setAccountStatus(String status) {
        // Replace old status value.
        this.accountStatus = status;
    }

    // Adds money to the account after validation.
    public void deposit(double amount) throws InvalidAmountException {
        // Reject zero or negative values.
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }
        // Increase balance by the deposit amount.
        balance += amount;
    }

    // Subtracts money from the account after validation.
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        // Reject zero or negative values.
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }
        // Reject withdrawal larger than current balance.
        if (amount > balance) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal.");
        }
        // Decrease balance by the withdrawal amount.
        balance -= amount;
    }

    // Checks whether entered PIN matches stored PIN.
    public boolean validatePin(String enteredPin) {
        // Return true only on exact string match.
        return pin.equals(enteredPin);
    }

    // Checks whether account status is ACTIVE.
    public boolean isActive() {
        // Return true only when status equals ACTIVE constant.
        return STATUS_ACTIVE.equals(accountStatus);
    }

    // Prints account details to standard output.
    public void displayAccountDetails() {
        // Print account number line.
        System.out.println("Account Number: " + accountNumber);
        // Print holder name line.
        System.out.println("Account Holder: " + accountHolderName);
        // Print balance line.
        System.out.println("Balance: $" + balance);
        // Print status line.
        System.out.println("Status: " + accountStatus);
    }

    // Serializes account into one CSV line.
    public String toFileString() {
        // Join all fields with commas in fixed order.
        return String.join(",", accountNumber, accountHolderName, String.valueOf(balance), pin, accountStatus);
    }

    // Parses one CSV line into an Account object.
    public static Account fromFileString(String line) {
        // Split the raw line by commas.
        String[] csvParts = line.split(",");
        // Validate minimum expected field count.
        if (csvParts.length >= 5) {
            // Build account from parsed values.
            Account loadedAccount = new Account(
                csvParts[0],
                csvParts[1],
                Double.parseDouble(csvParts[2]),
                csvParts[3]
            );
            // Apply parsed status value.
            loadedAccount.setAccountStatus(csvParts[4]);
            // Return parsed account instance.
            return loadedAccount;
        }
        // Return null when CSV format is invalid.
        return null;
    }
}
