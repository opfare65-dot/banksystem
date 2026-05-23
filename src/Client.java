/**
 * Client class extends User.
 * Demonstrates inheritance from the abstract User class.
 * Provides client-specific functionality for bank account operations.
 */
public class Client extends User {
    private Account account;

    public Client(String userId, String name, String password, Account account) {
        super(userId, name, password);
        this.account = account;
    }

    /**
     * Client login validation using account number and PIN.
     * @param pin The PIN to validate against the account
     * @return true if login successful
     * @throws InvalidLoginException if credentials are invalid
     */
    @Override
    public boolean login(String pin) throws InvalidLoginException {
        if (pin == null || pin.isEmpty()) {
            throw new InvalidLoginException("PIN cannot be empty.");
        }
        if (!account.isActive()) {
            throw new InvalidLoginException("Account is not active.");
        }
        return account.validatePin(pin);
    }

    /**
     * Client logout operation.
     */
    @Override
    public void logout() {
        System.out.println("Client " + name + " logged out successfully.");
    }

    /**
     * Displays client information.
     * Polymorphism: This overrides the parent User class method.
     */
    @Override
    public void displayInfo() {
        System.out.println("=== Client Information ===");
        System.out.println("Client ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Role: Bank Client");
        if (account != null) {
            account.displayAccountDetails();
        }
    }

    public Account getAccount() {
        return account;
    }
}