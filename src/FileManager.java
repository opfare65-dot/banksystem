import java.io.*;
import java.util.*;

/**
 * FileManager class handles all file read/write operations.
 * Responsible for persisting and loading data from text files.
 */
public class FileManager {
    private static final String CLIENTS_FILE = "clients.txt";
    private static final String ADMINS_FILE = "admins.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    /**
     * Reads client accounts from file.
     * @return List of Account objects
     */
    public static List<Account> readClientsFromFile() {
        List<Account> accounts = new ArrayList<>();
        try {
            File file = new File(CLIENTS_FILE);
            if (!file.exists()) {
                // Create sample data file if it doesn't exist
                createSampleClientsFile();
                return readClientsFromFile();
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Account acc = Account.fromFileString(line);
                    if (acc != null) {
                        accounts.add(acc);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading clients file: " + e.getMessage());
        }
        return accounts;
    }

    /**
     * Writes client accounts to file.
     * @param accounts List of accounts to save
     * @throws IOException if file write fails
     */
    public static void writeClientsToFile(List<Account> accounts) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(CLIENTS_FILE));
        for (Account acc : accounts) {
            writer.write(acc.toFileString());
            writer.newLine();
        }
        writer.close();
    }

    /**
     * Reads admin credentials from file.
     * @return Map of admin username to password
     */
    public static Map<String, String> readAdminsFromFile() {
        Map<String, String> admins = new HashMap<>();
        try {
            File file = new File(ADMINS_FILE);
            if (!file.exists()) {
                createSampleAdminsFile();
                return readAdminsFromFile();
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        admins.put(parts[0], parts[2]); // username -> password
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading admins file: " + e.getMessage());
        }
        return admins;
    }

    /**
     * Writes admin credentials to file.
     * @param admins Map of admin username to password
     * @throws IOException if file write fails
     */
    public static void writeAdminsToFile(Map<String, String> admins) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(ADMINS_FILE));
        for (Map.Entry<String, String> entry : admins.entrySet()) {
            writer.write(entry.getKey() + ",Admin," + entry.getValue());
            writer.newLine();
        }
        writer.close();
    }

    /**
     * Reads transactions from file.
     * @return List of Transaction objects
     */
    public static List<Transaction> readTransactionsFromFile() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            File file = new File(TRANSACTIONS_FILE);
            if (!file.exists()) {
                // Create empty file
                file.createNewFile();
                return transactions;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Transaction trans = Transaction.fromFileString(line);
                    if (trans != null) {
                        transactions.add(trans);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading transactions file: " + e.getMessage());
        }
        return transactions;
    }

    /**
     * Writes transactions to file.
     * @param transactions List of transactions to save
     * @throws IOException if file write fails
     */
    public static void writeTransactionsToFile(List<Transaction> transactions) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE));
        for (Transaction trans : transactions) {
            writer.write(trans.toFileString());
            writer.newLine();
        }
        writer.close();
    }

    /**
     * Appends a single transaction to the file.
     * @param transaction Transaction to append
     * @throws IOException if file write fails
     */
    public static void appendTransactionToFile(Transaction transaction) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true));
        writer.write(transaction.toFileString());
        writer.newLine();
        writer.close();
    }

    /**
     * Creates sample clients file with initial data.
     */
    private static void createSampleClientsFile() {
        try {
            FileWriter writer = new FileWriter(CLIENTS_FILE);
            writer.write("ACC001,John Smith,1500.00,1234,ACTIVE\n");
            writer.write("ACC002,Jane Doe,2500.00,5678,ACTIVE\n");
            writer.write("ACC003,Bob Wilson,750.50,9999,ACTIVE\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error creating sample clients file: " + e.getMessage());
        }
    }

    /**
     * Creates sample admins file with initial data.
     */
    private static void createSampleAdminsFile() {
        try {
            FileWriter writer = new FileWriter(ADMINS_FILE);
            writer.write("admin,Admin,admin123\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error creating sample admins file: " + e.getMessage());
        }
    }
}