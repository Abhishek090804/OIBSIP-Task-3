import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

class User {
    private String userId;
    private String userPin;
    private double balance;
    private List<Transaction> transactionHistory;

    public User(String userId, String userPin) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPin() {
        return userPin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(new Transaction("Deposit", amount));
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add(new Transaction("Withdrawal", -amount));
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public void transfer(User recipient, double amount) {
        if (amount <= balance) {
            balance -= amount;
            recipient.deposit(amount);
            transactionHistory.add(new Transaction("Transfer to " + recipient.getUserId(), -amount));
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}

class UserManager {
    private Map<String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public void registerUser(String userId, String userPin) {
        User newUser = new User(userId, userPin);
        users.put(userId, newUser);
    }

    public User getUserById(String userId) {
        return users.get(userId);
    }
}

public class ATMInterface {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("----------------------------------------");
            System.out.println("\n Welcome to the  YOUR BANK ATM!");
             System.out.println("\n----------------------------------------");
             System.out.print("Select an option: ");
             System.out.println("");
            System.out.println("1. Register");
            System.out.println("2. Log In");
            System.out.println("3. Quit");


            int choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    System.out.print("Enter User ID: ");
                    String newUserId = scanner.nextLine();
                    String newUserPin = generatePin();
                    userManager.registerUser(newUserId, newUserPin);
                    System.out.println("Registration successful.");
                    System.out.println("Your PIN is: " + newUserPin);
                    break;

                case 2:
                    System.out.print("Enter User ID: ");
                    String userId = scanner.nextLine();

                    User user = userManager.getUserById(userId);
                    if (user == null) {
                        System.out.println("User not found.");
                        continue;
                    }

                    System.out.print("Enter PIN: ");
                    String userPin = scanner.nextLine();

                    if (!user.getUserPin().equals(userPin)) {
                        System.out.println("Incorrect PIN.");
                        continue;
                    }
                    System.out.println("Login successful.");
                    performATMOperations(user, userManager, scanner);
                    break;

                case 3:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    public static String generatePin() {
        Random random = new Random();
        StringBuilder pin = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            pin.append(random.nextInt(10)); 
        }

        return pin.toString();
    }

    public static void printTransactionHistory(User user) {
        System.out.println("\nTransaction History:");
        for (Transaction transaction : user.getTransactionHistory()) {
            String type = transaction.getType();
            double amount = transaction.getAmount();
            System.out.println(type + ": $" + amount);
        }
    }

    public static void performATMOperations(User user, UserManager userManager, Scanner scanner) {
        while (true) {
            System.out.print("----------------------------------");
            System.out.println("");
            System.out.println("\nATM Menu:");
            System.out.println("");
            System.out.print("----------------------------------");
            System.out.print("");
            System.out.print("\n Select an option: ");
            System.out.print("");
            System.out.println("\n 1. Transaction History");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Check Balance");
            System.out.println("6. Quit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                printTransactionHistory(user);
                break;

                case 2:
                    System.out.print("Enter deposit amount: $");
                    double depositAmount = scanner.nextDouble();
                    user.deposit(depositAmount);
                    System.out.println("Deposit successful.");
                    break;

                case 3:
                    System.out.print("Enter withdrawal amount: $");
                    double withdrawalAmount = scanner.nextDouble();
                    user.withdraw(withdrawalAmount);
                    break;

                case 4:
                    System.out.print("Enter recipient's User ID: ");
                    String recipientId = scanner.next();
                    scanner.nextLine(); 
                    User recipient = userManager.getUserById(recipientId);

                    if (recipient == null) {
                        System.out.println("Recipient not found.");
                        continue;
                    }

                    System.out.print("Enter transfer amount: $");
                    double transferAmount = scanner.nextDouble();
                    user.transfer(recipient, transferAmount);
                    System.out.println("Transfer successful.");
                    break;

                case 5:
                    System.out.println("Balance: $" + user.getBalance());
                    break;
                case 6:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
    
}
