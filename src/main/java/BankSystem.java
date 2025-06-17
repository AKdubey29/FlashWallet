import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BankSystem {
    private List<Account> accounts;

    public BankSystem() {
        try {
            this.accounts = FileManager.loadAccounts();
        } catch (IOException e) {
            System.out.println(" Error loading accounts: " + e.getMessage());
            this.accounts = new ArrayList<>();
        }
    }

    public List<Account> getAllAccounts() {
        return accounts;
    }

    public Account getAccountById(String id) {
        for (Account acc : accounts) {
            if (acc.getId().equals(id)) {
                return acc;
            }
        }
        return null;
    }

    public boolean addAccount(Account account) {
        if (getAccountById(account.getId()) != null) {
            return false; // ID already exists
        }

        accounts.add(account);
        try {
            FileManager.saveAccounts(accounts);
            return true;
        } catch (IOException e) {
            System.out.println(" Failed to save account: " + e.getMessage());
            return false;
        }
    }

    public boolean transfer(String fromId, String toId, double amount) {
        Account from = getAccountById(fromId);
        Account to = getAccountById(toId);

        if (from == null || to == null) {
            System.out.println("One or both accounts not found.");
            return false;
        }

        if (from.getBalance() < amount) {
            System.out.println(" Insufficient balance.");
            return false;
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        try {
            FileManager.saveAccounts(accounts);
            return true;
        } catch (IOException e) {
            System.out.println(" Error saving transaction: " + e.getMessage());
            return false;
        }
    }

    public void printAccountDetails(String id) {
        Account acc = getAccountById(id);
        if (acc != null) {
            System.out.println("🔎 Account Details:");
            System.out.println("ID      : " + acc.getId());
            System.out.println("Name    : " + acc.getName());
            System.out.println("Balance : ₹" + acc.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }
}