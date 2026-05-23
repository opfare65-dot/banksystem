/**
 * Custom exception for invalid login attempts.
 * Thrown when username/password or account number/PIN combination is incorrect.
 */
public class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super(message);
    }
}