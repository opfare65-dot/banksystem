/**
 * Concrete admin user implementation.
 */
public class Admin extends User {
    // Runtime counter of how many admin objects were created.
    private static int adminCount = 0;

    // Builds one admin session object.
    public Admin(String userId, String name, String password) {
        // Initialize common user fields in the parent class.
        super(userId, name, password);
        // Increment total created admin objects.
        adminCount++;
    }

    /**
     * Validates admin password.
     *
     * @param enteredPassword password text entered by the admin
     * @return true when password matches stored credential
     * @throws InvalidLoginException when password is empty
     */
    @Override
    public boolean login(String enteredPassword) throws InvalidLoginException {
        // Reject null or empty password input.
        if (enteredPassword == null || enteredPassword.isEmpty()) {
            throw new InvalidLoginException("Password cannot be empty.");
        }
        // Delegate final comparison to shared helper.
        return validateCredentials(enteredPassword);
    }

    // Writes a simple logout log line.
    @Override
    public void logout() {
        System.out.println("Admin " + name + " logged out successfully.");
    }

    // Prints admin details to stdout.
    @Override
    public void displayInfo() {
        System.out.println("=== Admin Information ===");
        System.out.println("Admin ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Role: Administrator");
    }

    // Returns how many admin objects were created in this app run.
    public static int getAdminCount() {
        return adminCount;
    }
}
