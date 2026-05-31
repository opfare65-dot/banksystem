/**
 * Custom exception for invalid login attempts.
 * Thrown when username/password or account number/PIN combination is incorrect.
 */
public class InvalidLoginException extends Exception {
    // Builds a domain-specific exception for authentication failures.
    public InvalidLoginException(String message) {
        // Stores the authentication failure reason text.
        super(message);
    }
}
