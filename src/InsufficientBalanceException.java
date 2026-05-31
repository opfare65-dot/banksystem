/**
 * Custom exception for insufficient balance operations.
 * Thrown when account has insufficient funds for withdrawal or transfer.
 */
public class InsufficientBalanceException extends Exception {
    // Builds a domain-specific exception for "not enough money" scenarios.
    public InsufficientBalanceException(String message) {
        // Stores the descriptive error message in the parent Exception object.
        super(message);
    }
}
