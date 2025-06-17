import java.util.Scanner;

public class Main {
    private static final BankSystem bankSystem = new BankSystem();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to Flash Wallet");

        boolean running = true;
        while (running) {
            System.out.println("\n======= MENU =======");
            System.out.println("1. Add Account");
            System.out.println("2. View Account");
            System.out.println("3. Send Money (Offline)");
            System.out.println("4. Sync Transactions");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addAccount();
                    break;
                case "2":
                    viewAccount();
                    break;
                case "3":
                    sendMoneyOffline();
                    break;
                case "4":
                    System.out.println("Starting sync...");

                    new Thread(new SyncService()).start();
                    Thread.sleep(5000);
                    break;
                case "5":
                    running = false;
                    System.out.println(" Exiting Flash Wallet...");
                    break;
                default:
                    System.out.println("Invalid choice. Try 1-5.");
            }
        }

        scanner.close();
    }

    private static void addAccount() {
        System.out.print("Enter Account ID: ");
        String id = scanner.nextLine().trim();

        if (bankSystem.getAccountById(id) != null) {
            System.out.println(" Account already exists.");
            return;
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Initial Balance: ");
        double balance;
        try {
            balance = Double.parseDouble(scanner.nextLine());
            if (balance < 0) {
                System.out.println("Balance can't be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid balance.");
            return;
        }

        Account acc = new Account(id, name, balance);
        boolean success = bankSystem.addAccount(acc);
        System.out.println(success ? "Account created!" : " Failed to create account.");
    }

    private static void viewAccount() {
        System.out.print("Enter Account ID: ");
        String id = scanner.nextLine().trim();
        bankSystem.printAccountDetails(id);
    }

    private static void sendMoneyOffline() {
        System.out.print("From Account ID: ");
        String fromId = scanner.nextLine().trim();
        System.out.print("To Account ID: ");
        String toId = scanner.nextLine().trim();
        System.out.print("Amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
            if (amount <= 0) {
                System.out.println("Invalid amount.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(" Enter a valid number.");
            return;
        }

        Transaction txn = new Transaction(fromId, toId, amount);
        try {
            FileManager.addOfflineTransaction(txn);
            System.out.println(" Offline transaction saved.");
        } catch (Exception e) {
            System.out.println("Failed to save offline transaction: " + e.getMessage());
        }
    }
}