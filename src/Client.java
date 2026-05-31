/**
 * Concrete client user implementation.
 */
public class Client extends User {
    // Direct reference to the account this client session is allowed to use.
    private Account linkedAccount;

    // Creates a client session and links it to an account object.
    public Client(String userId, String name, String password, Account account) {
        // Fill shared fields defined in User.
        super(userId, name, password);
        // Store linked account reference for account-specific operations.
        this.linkedAccount = account;
    }

    /**
     * Validates client PIN with account status checks.
     *
     * @param enteredPin PIN entered by the client
     * @return true when account is active and PIN matches
     * @throws InvalidLoginException when PIN is empty or account is inactive
     */
    @Override
    public boolean login(String enteredPin) throws InvalidLoginException {
        // Reject empty PIN input.
        if (enteredPin == null || enteredPin.isEmpty()) {
            throw new InvalidLoginException("PIN cannot be empty.");
        }
        // Reject login if admin has deactivated the account.
        if (!linkedAccount.isActive()) {
            throw new InvalidLoginException("Account is not active.");
        }
        // Return true only when entered PIN matches account PIN.
        return linkedAccount.validatePin(enteredPin);
    }

    // Writes a simple logout log line.
    @Override
    public void logout() {
        System.out.println("Client " + name + " logged out successfully.");
    }

    // Prints client details and linked account info.
    @Override
    public void displayInfo() {
        System.out.println("=== Client Information ===");
        System.out.println("Client ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Role: Bank Client");
        // Print account details only when linked account reference exists.
        if (linkedAccount != null) {
            linkedAccount.displayAccountDetails();
        }
    }

    // Returns linked account reference.
    public Account getAccount() {
        return linkedAccount;
    }
}
