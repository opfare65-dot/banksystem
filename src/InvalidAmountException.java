/**
 * Custom exception for invalid amount operations.
 * Thrown when deposit, withdrawal, or transfer amount is invalid (negative or zero).
 */
public class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}