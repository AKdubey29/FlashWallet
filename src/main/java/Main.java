import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.*;

public class Main {
    static List<Account> loadedAccounts;

    static {
        try {
            loadedAccounts = FileManager.loadAccounts();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final BankSystem bankSystem = new BankSystem(loadedAccounts);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("Welcome to Flash Wallet");

        boolean running = true;
        while (running) {
            System.out.println("\n======= MENU =======");
            System.out.println("1. Add Account");
            System.out.println("2. View Account");
            System.out.println("3. Send Money (Offline)");
            System.out.println("4. Sync Transactions" +"\n"+
                    "Network has come and you are Online");
            System.out.println("5. Deposit funds");
            System.out.println("6. Withdraw funds");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addAccount();
                    running = false;
                    System.out.println(" Exiting Flash Wallet...");
                    break;
                case "2":
                    viewAccount();
                    running = false;
                    System.out.println(" Exiting Flash Wallet...");
                    break;
                case "3":
                    sendMoneyOffline();

                    try{
                        Thread.sleep(5000);
                    }
                    catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    running = false;
                    System.out.println(" Exiting Flash Wallet...");
                    break;
                case "4":
                    Thread.sleep(300);
                    System.out.println("Starting sync...");
                   Thread syncThread=new Thread(new SyncService());
                   syncThread.start();
                   syncThread.join();

                    try {
                        sleep(100);
                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    running = false;
                    System.out.println(" Exiting Flash Wallet...");
                case "5":
                    System.out.print("Enter Account ID to deposit: ");
                    String depId = scanner.next();
                    System.out.print("Enter amount to deposit: ");
                    double depAmt = scanner.nextDouble();
                    if (BankSystem.deposit(depId, depAmt)) {
                        System.out.println("Deposit successful.");
                    } else {
                        System.out.println("Deposit failed. Check account ID.");
                    }
                    running = false;
                    System.out.println(" Exiting Flash Wallet...");
                    break;

                case "6":
                    System.out.print("Enter Account ID to withdraw: ");
                    String withId = scanner.next();
                    System.out.print("Enter amount to withdraw: ");
                    double withAmt = scanner.nextDouble();
                    if (BankSystem.withdraw(withId, withAmt)) {
                        System.out.println("Withdrawal successful.");
                    } else {
                        System.out.println("Withdrawal failed. Check balance or account ID.");
                    }
                    running = false;
                    System.out.println(" Exiting Flash Wallet...");
                    break;
                case "7":
                    running = false;
                    System.out.println(" Exiting Flash Wallet...");
                    break;
                default:
                    System.out.println("Invalid choice. Try 1-7.");
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
            FileManager. clearOfflineTransactions();

        } catch (Exception e) {
            System.out.println("Failed to save offline transaction: " + e.getMessage());
        }
    }


}