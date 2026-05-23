/**
 * Custom exception for insufficient balance operations.
 * Thrown when account has insufficient funds for withdrawal or transfer.
 */
public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}