/**
 * Account class represents a bank account.
 * Demonstrates encapsulation with private fields and public getters/setters.
 */
public class Account {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private String pin;
    private String accountStatus;

    public Account(String accountNumber, String accountHolderName, double balance, 
                   String pin) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.pin = pin;
        this.accountStatus = "ACTIVE";
    }

    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public double getBalance() { return balance; }
    public String getPin() { return pin; }
    public String getAccountStatus() { return accountStatus; }

    public void setBalance(double balance) { this.balance = balance; }
    public void setAccountStatus(String status) { this.accountStatus = status; }

    /**
     * Deposits money into the account.
     * @param amount Amount to deposit (must be positive)
     * @throws InvalidAmountException if amount is negative or zero
     */
    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }
        this.balance += amount;
    }

    /**
     * Withdraws money from the account.
     * @param amount Amount to withdraw (must be positive and not exceed balance)
     * @throws InvalidAmountException if amount is negative or zero
     * @throws InsufficientBalanceException if balance is insufficient
     */
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }
        if (amount > this.balance) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal.");
        }
        this.balance -= amount;
    }

    /**
     * Checks if the provided PIN matches the account PIN.
     */
    public boolean validatePin(String pin) {
        return this.pin.equals(pin);
    }

    /**
     * Checks if account is active.
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.accountStatus);
    }

    /**
     * Displays account details.
     */
    public void displayAccountDetails() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + accountHolderName);
        System.out.println("Balance: $" + balance);
        System.out.println("Status: " + accountStatus);
    }

    /**
     * Converts account data to CSV format for file storage.
     */
    public String toFileString() {
        return String.join(",", accountNumber, accountHolderName, 
                          String.valueOf(balance), pin, accountStatus);
    }

    /**
     * Creates an Account object from a CSV line.
     */
    public static Account fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 5) {
            Account acc = new Account(parts[0], parts[1], 
                                     Double.parseDouble(parts[2]), parts[3]);
            acc.setAccountStatus(parts[4]);
            return acc;
        }
        return null;
    }
}