import java.io.*;
import java.util.*;

// Account class
class Account implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int accNo;
    private String name;
    private double balance;
    private List<String> transactions;

    public Account(int accNo, String name, double balance) {
        this.accNo = accNo;
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();
        transactions.add("Account created with balance: " + balance);
    }

    public int getAccNo() {
        return accNo;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add("Deposited: " + amount);
            System.out.println("✔ Deposit successful!");
        } else {
            System.out.println("✖ Invalid amount!");
        }
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("✖ Invalid amount!");
        } else if (amount > balance) {
            System.out.println("✖ Insufficient balance!");
        } else {
            balance -= amount;
            transactions.add("Withdrawn: " + amount);
            System.out.println("✔ Withdrawal successful!");
        }
    }

    public void showDetails() {
        System.out.println("\n--- Account Details ---");
        System.out.println("Account No: " + accNo);
        System.out.println("Name      : " + name);
        System.out.println("Balance   : " + balance);
    }

    public void showTransactions() {
        System.out.println("\n--- Transaction History ---");
        for (String t : transactions) {
            System.out.println(t);
        }
    }
}

// Main class
public class BankManagementSystem {

    static List<Account> accounts = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "accounts.dat";

    public static void main(String[] args) {
        loadData();

        while (true) {
            menu();
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> checkBalance();
                case 5 -> viewAll();
                case 6 -> viewTransactions();
                case 7 -> {
                    saveData();
                    System.out.println("💾 Data saved. Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // Menu
    static void menu() {
        System.out.println("\n===== BANK SYSTEM =====");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Check Balance");
        System.out.println("5. View All Accounts");
        System.out.println("6. View Transactions");
        System.out.println("7. Exit");
        System.out.print("Enter choice: ");
    }

    // Find account
    static Account find(int accNo) {
        for (Account a : accounts) {
            if (a.getAccNo() == accNo) return a;
        }
        return null;
    }

    static void createAccount() {
        System.out.print("Enter Account No: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (find(id) != null) {
            System.out.println("Account already exists!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Initial Balance: ");
        double bal = sc.nextDouble();

        accounts.add(new Account(id, name, bal));
        System.out.println("✔ Account Created!");
    }

    static void deposit() {
        System.out.print("Enter Account No: ");
        int id = sc.nextInt();

        Account acc = find(id);
        if (acc != null) {
            System.out.print("Enter amount: ");
            acc.deposit(sc.nextDouble());
        } else {
            System.out.println("Account not found!");
        }
    }

    static void withdraw() {
        System.out.print("Enter Account No: ");
        int id = sc.nextInt();

        Account acc = find(id);
        if (acc != null) {
            System.out.print("Enter amount: ");
            acc.withdraw(sc.nextDouble());
        } else {
            System.out.println("Account not found!");
        }
    }

    static void checkBalance() {
        System.out.print("Enter Account No: ");
        int id = sc.nextInt();

        Account acc = find(id);
        if (acc != null) {
            acc.showDetails();
        } else {
            System.out.println("Account not found!");
        }
    }

    static void viewAll() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (Account a : accounts) {
            a.showDetails();
        }
    }

    static void viewTransactions() {
        System.out.print("Enter Account No: ");
        int id = sc.nextInt();

        Account acc = find(id);
        if (acc != null) {
            acc.showTransactions();
        } else {
            System.out.println("Account not found!");
        }
    }

    // Save data
    static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving data!");
        }
    }

    // Load data
    static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (List<Account>) ois.readObject();
        } catch (Exception e) {
            accounts = new ArrayList<>();
        }
    }
}