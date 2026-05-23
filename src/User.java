/**
 * Abstract User class demonstrating abstraction.
 * This class defines the common behavior for all users and forces subclasses 
 * to implement specific behavior through abstract methods.
 */
public abstract class User {
    protected String userId;
    protected String name;
    protected String password;

    public User(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getPassword() { return password; }

    /**
     * Abstract method for login - must be implemented by subclasses.
     */
    public abstract boolean login(String credentials) throws InvalidLoginException;

    /**
     * Abstract method for logout - must be implemented by subclasses.
     */
    public abstract void logout();

    /**
     * Abstract method for displaying user info - must be implemented by subclasses.
     * This demonstrates polymorphism - each subclass provides its own implementation.
     */
    public abstract void displayInfo();

    /**
     * Validates user credentials.
     */
    protected boolean validateCredentials(String inputPassword) {
        return password.equals(inputPassword);
    }
}