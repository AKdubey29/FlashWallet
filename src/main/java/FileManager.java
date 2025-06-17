import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String ACCOUNTS_FILE = "data/accounts.txt";
    private static final String TRANSACTIONS_FILE = "data/offline_txns.txt";

    // Load all accounts from the file
    public static List<Account> loadAccounts() throws IOException {
        List<Account> accounts = new ArrayList<>();
        File file = new File(ACCOUNTS_FILE);

        if (!file.exists()) {
            file.getParentFile().mkdirs(); // create "data" folder if not exists
            file.createNewFile();
            return accounts; // empty
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String id = parts[0].trim();
                String name = parts[1].trim();
                double balance = Double.parseDouble(parts[2].trim());
                accounts.add(new Account(id, name, balance));
            }
        }

        reader.close();
        return accounts;
    }

    // Save all accounts to the file
    public static void saveAccounts(List<Account> accounts) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNTS_FILE));
        for (Account acc : accounts) {
            writer.write(acc.getId() + "," + acc.getName() + "," + acc.getBalance());
            writer.newLine();
        }
        writer.close();
    }

    // Add a transaction to offline queue
    public static void addOfflineTransaction(Transaction txn) throws IOException {
        File file = new File(TRANSACTIONS_FILE);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(txn.getFromId() + "," + txn.getToId() + "," + txn.getAmount());
        writer.newLine();
        writer.close();
    }

    // Load and clear offline transactions
    public static List<Transaction> loadOfflineTransactions() throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(TRANSACTIONS_FILE);

        if (!file.exists()) {
            return transactions;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String from = parts[0].trim();
                String to = parts[1].trim();
                double amount = Double.parseDouble(parts[2].trim());
                transactions.add(new Transaction(from, to, amount));
            }
        }

        reader.close();

        // Clear the file after loading
        new PrintWriter(TRANSACTIONS_FILE).close();

        return transactions;
    }
}