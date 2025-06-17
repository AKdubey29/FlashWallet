import java.io.IOException;
import java.util.List;

public class SyncService implements Runnable {

    @Override
    public void run() {
        System.out.println("Syncing offline transactions...");

        BankSystem bankSystem = new BankSystem();
        try {
            List<Account> accounts = FileManager.loadAccounts();
            bankSystem.setAccounts(accounts);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        List<Transaction> offlineTxns;

        try {
            offlineTxns = FileManager.loadOfflineTransactions();
        } catch (IOException e) {
            System.out.println(" Error reading offline transactions: " + e.getMessage());
            return;
        }

        if (offlineTxns.isEmpty()) {
            System.out.println(" No offline transactions to sync.");
            return;
        }

        int successCount = 0;
        int failCount = 0;

        for (Transaction txn : offlineTxns) {
            Account from = bankSystem.getAccountById(txn.getFromId());
            Account to = bankSystem.getAccountById(txn.getToId());

            if (from == null || to == null) {
                System.out.println(" Invalid account(s) in transaction: " + txn);
                failCount++;
                continue;
            }

            if (from.getBalance() >= txn.getAmount()) {
                from.setBalance(from.getBalance() - txn.getAmount());
                to.setBalance(to.getBalance() + txn.getAmount());
                successCount++;
                try {
                    FileManager.saveAccounts(bankSystem.getAllAccounts());
                } catch (IOException e) {
                    System.out.println(" Failed to save updated accounts: " + e.getMessage());
                    return;
                }
            } else {
                System.out.println(" Insufficient funds for transaction: " + txn);
                failCount++;
            }

        }



        System.out.println(" Sync Complete: " + successCount + " successful, " + failCount + " failed.");
    }
}