import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * File persistence utility for admins, clients, and transactions.
 */
public class FileManager {
    // Path to client records file.
    private static final String CLIENTS_FILE = "clients.txt";
    // Path to admin records file.
    private static final String ADMINS_FILE = "admins.txt";
    // Path to transaction history file.
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    // Prevents instantiation because this is a static utility class.
    private FileManager() {
    }

    // Reads all client accounts from disk.
    public static List<Account> readClientsFromFile() {
        // Container for parsed accounts.
        List<Account> loadedAccounts = new ArrayList<>();
        // File object pointing to clients file.
        File clientsFile = new File(CLIENTS_FILE);

        try {
            // If file does not exist, create sample and retry loading.
            if (!clientsFile.exists()) {
                createSampleClientsFile();
                return readClientsFromFile();
            }

            // Open reader in try-with-resources so it auto-closes.
            try (BufferedReader reader = new BufferedReader(new FileReader(clientsFile))) {
                // Holds each line while iterating file content.
                String currentLine;
                // Read line by line until EOF.
                while ((currentLine = reader.readLine()) != null) {
                    // Skip blank lines.
                    if (!currentLine.trim().isEmpty()) {
                        // Convert CSV line into Account object.
                        Account parsedAccount = Account.fromFileString(currentLine);
                        // Add only successfully parsed account objects.
                        if (parsedAccount != null) {
                            loadedAccounts.add(parsedAccount);
                        }
                    }
                }
            }
        } catch (IOException exception) {
            // Log read failure to stderr.
            System.err.println("Error reading clients file: " + exception.getMessage());
        }

        // Return collected accounts (possibly empty).
        return loadedAccounts;
    }

    // Writes all accounts to clients file.
    public static void writeClientsToFile(List<Account> accountsToSave) throws IOException {
        // Open writer in overwrite mode.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CLIENTS_FILE))) {
            // Write each account as one CSV row.
            for (Account account : accountsToSave) {
                writer.write(account.toFileString());
                writer.newLine();
            }
        }
    }

    // Reads admin credentials map from disk.
    public static Map<String, String> readAdminsFromFile() {
        // Map where key=username and value=password.
        Map<String, String> adminCredentials = new HashMap<>();
        // File object for admins file.
        File adminsFile = new File(ADMINS_FILE);

        try {
            // If file does not exist, create sample and retry loading.
            if (!adminsFile.exists()) {
                createSampleAdminsFile();
                return readAdminsFromFile();
            }

            // Open reader with auto-close behavior.
            try (BufferedReader reader = new BufferedReader(new FileReader(adminsFile))) {
                // Temporary line holder.
                String currentLine;
                // Iterate through all lines.
                while ((currentLine = reader.readLine()) != null) {
                    // Skip blank rows.
                    if (!currentLine.trim().isEmpty()) {
                        // Split expected format username,role,password.
                        String[] csvParts = currentLine.split(",");
                        // Validate minimum expected columns.
                        if (csvParts.length >= 3) {
                            // Keep only username and password in runtime map.
                            adminCredentials.put(csvParts[0], csvParts[2]);
                        }
                    }
                }
            }
        } catch (IOException exception) {
            // Log read failure to stderr.
            System.err.println("Error reading admins file: " + exception.getMessage());
        }

        // Return loaded credential map.
        return adminCredentials;
    }

    // Writes admin credential map to disk.
    public static void writeAdminsToFile(Map<String, String> adminCredentials) throws IOException {
        // Open writer in overwrite mode.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMINS_FILE))) {
            // Write each credential row in username,Admin,password format.
            for (Map.Entry<String, String> credentialEntry : adminCredentials.entrySet()) {
                writer.write(credentialEntry.getKey() + ",Admin," + credentialEntry.getValue());
                writer.newLine();
            }
        }
    }

    // Reads all transaction records from disk.
    public static List<Transaction> readTransactionsFromFile() {
        // Container for parsed transactions.
        List<Transaction> loadedTransactions = new ArrayList<>();
        // File object for transaction file.
        File transactionsFile = new File(TRANSACTIONS_FILE);

        try {
            // Ensure file exists before reading.
            if (!transactionsFile.exists()) {
                transactionsFile.createNewFile();
                return loadedTransactions;
            }

            // Open reader with auto-close behavior.
            try (BufferedReader reader = new BufferedReader(new FileReader(transactionsFile))) {
                // Temporary line holder.
                String currentLine;
                // Iterate each file line.
                while ((currentLine = reader.readLine()) != null) {
                    // Skip blank lines.
                    if (!currentLine.trim().isEmpty()) {
                        // Parse line to transaction object.
                        Transaction parsedTransaction = Transaction.fromFileString(currentLine);
                        // Keep only valid parsed rows.
                        if (parsedTransaction != null) {
                            loadedTransactions.add(parsedTransaction);
                        }
                    }
                }
            }
        } catch (IOException exception) {
            // Log read failure to stderr.
            System.err.println("Error reading transactions file: " + exception.getMessage());
        }

        // Return loaded transaction list.
        return loadedTransactions;
    }

    // Writes complete transaction list to disk.
    public static void writeTransactionsToFile(List<Transaction> transactionsToSave) throws IOException {
        // Open writer in overwrite mode.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
            // Write each transaction as CSV row.
            for (Transaction transaction : transactionsToSave) {
                writer.write(transaction.toFileString());
                writer.newLine();
            }
        }
    }

    // Appends a single transaction row to existing file.
    public static void appendTransactionToFile(Transaction transactionToAppend) throws IOException {
        // Open writer in append mode.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            // Write appended transaction row.
            writer.write(transactionToAppend.toFileString());
            writer.newLine();
        }
    }

    // Creates starter clients file for first-run experience.
    private static void createSampleClientsFile() {
        // Create writer for clients file.
        try (FileWriter writer = new FileWriter(CLIENTS_FILE)) {
            // Write one starter client row.
            writer.write("ACC001,Jane Doe,2500.00,5678,ACTIVE\n");
        } catch (IOException exception) {
            // Log creation failure to stderr.
            System.err.println("Error creating sample clients file: " + exception.getMessage());
        }
    }

    // Creates starter admin file for first-run experience.
    private static void createSampleAdminsFile() {
        // Create writer for admins file.
        try (FileWriter writer = new FileWriter(ADMINS_FILE)) {
            // Write one starter admin row.
            writer.write("admin,Admin,admin123\n");
        } catch (IOException exception) {
            // Log creation failure to stderr.
            System.err.println("Error creating sample admins file: " + exception.getMessage());
        }
    }
}
