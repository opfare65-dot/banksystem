/**
 * Custom exception for invalid amount operations.
 * Thrown when deposit, withdrawal, or transfer amount is invalid (negative or zero).
 */
public class InvalidAmountException extends Exception {
    // Builds a domain-specific exception for invalid numeric amounts.
    public InvalidAmountException(String message) {
        // Keeps the caller-provided validation message.
        super(message);
    }
}
