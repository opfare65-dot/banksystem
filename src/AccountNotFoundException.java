/**
 * Custom exception for account not found operations.
 * Thrown when a specified account number does not exist in the system.
 */
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
}