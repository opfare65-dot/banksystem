/**
 * Base type for all users in the system.
 * Every user has an ID, a display name, and a secret credential.
 */
public abstract class User {
    // Unique identifier for the user (admin username or client account number).
    protected String userId;
    // Friendly display name shown in logs or debug output.
    protected String name;
    // Secret credential kept for simple in-memory validation.
    protected String password;

    // Creates the common user state shared by all subclasses.
    public User(String userId, String name, String password) {
        // Store caller-provided user identifier.
        this.userId = userId;
        // Store caller-provided display name.
        this.name = name;
        // Store caller-provided password/PIN string.
        this.password = password;
    }

    // Returns the user identifier.
    public String getUserId() {
        return userId;
    }

    // Returns the display name.
    public String getName() {
        return name;
    }

    // Returns the currently stored credential.
    public String getPassword() {
        return password;
    }

    /**
     * Subclass-specific login implementation.
     *
     * @param enteredCredential raw credential entered by the user
     * @return true when login check succeeds
     * @throws InvalidLoginException when credential validation fails
     */
    public abstract boolean login(String enteredCredential) throws InvalidLoginException;

    // Subclass-specific logout behavior.
    public abstract void logout();

    // Subclass-specific user info output.
    public abstract void displayInfo();

    // Shared helper that checks plain-text credential equality.
    protected boolean validateCredentials(String inputPassword) {
        // Return true only when strings are exactly equal.
        return password.equals(inputPassword);
    }
}
