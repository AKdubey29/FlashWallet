import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BankSystem {
    private static List<Account> accounts;
    public BankSystem(){
        this.accounts = new ArrayList<>();
    }
    public BankSystem(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Account> getAllAccounts() {
        return accounts;
    }

    public static Account getAccountById(String id) {
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



        public static boolean deposit(String accId, double amount) {
            Account acc = getAccountById(accId);
            if (acc != null) {
                acc.setBalance(acc.getBalance() + amount);
                saveAccounts();
                return true;
            }
            return false;
        }
    private static void saveAccounts() {
        try {
            FileManager.saveAccounts(accounts);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());

        }
    }


    public static boolean withdraw(String accId, double amount) throws IOException {
            Account acc = getAccountById(accId);
            if (acc != null && acc.getBalance() >= amount) {
                acc.setBalance(acc.getBalance() - amount);
                try {
                    FileManager.saveAccounts(accounts);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
            return false;
        }




    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}