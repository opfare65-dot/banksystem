/**
 * Admin class extends User.
 * Demonstrates inheritance from the abstract User class.
 * Provides admin-specific functionality for managing bank clients.
 */
public class Admin extends User {
    private static int adminCount = 0;

    public Admin(String userId, String name, String password) {
        super(userId, name, password);
        adminCount++;
    }

    /**
     * Admin login validation.
     * @param password The password to validate
     * @return true if login successful
     * @throws InvalidLoginException if credentials are invalid
     */
    @Override
    public boolean login(String password) throws InvalidLoginException {
        if (password == null || password.isEmpty()) {
            throw new InvalidLoginException("Password cannot be empty.");
        }
        return validateCredentials(password);
    }

    /**
     * Admin logout operation.
     */
    @Override
    public void logout() {
        System.out.println("Admin " + name + " logged out successfully.");
    }

    /**
     * Displays admin information.
     * Polymorphism: This overrides the parent User class method.
     */
    @Override
    public void displayInfo() {
        System.out.println("=== Admin Information ===");
        System.out.println("Admin ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Role: Administrator");
    }

    public static int getAdminCount() {
        return adminCount;
    }
}