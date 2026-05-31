/**
 * Custom exception for account not found operations.
 * Thrown when a specified account number does not exist in the system.
 */
public class AccountNotFoundException extends Exception {
    // Creates a typed exception so callers can specifically catch "missing account" failures.
    public AccountNotFoundException(String message) {
        // Passes the human-readable message to the base Exception class.
        super(message);
    }
}
