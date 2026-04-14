package dev.mikenhil.vending;

import java.util.Scanner;

public class Application {

    private final VendingMachine machine;
    private final Scanner scanner;

    public Application(VendingMachine machine, Scanner scanner) {
        this.machine = machine;
        this.scanner = scanner;
    }

    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine();
        Scanner scanner = new Scanner(System.in);
        Application app = new Application(machine, scanner);
        app.run();
        scanner.close();
    }

    public void run() {
        printWelcomeBanner();
        boolean running = true;
        while (running) {
            printMenu();
            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            var option = MenuOption.fromNumber(choice);
            if (option.isEmpty()) {
                System.out.println("Invalid option. Please choose a number from the menu.");
                continue;
            }
            switch (option.get()) {
                case VIEW_PRODUCTS -> handleViewProducts();
                case INSERT_MONEY -> handleInsertMoney();
                case CHECK_BALANCE -> handleCheckBalance();
                case PURCHASE -> handlePurchase();
                case GET_CHANGE -> handleGetChange();
                case EXIT -> {
                    running = false;
                    handleExit();
                }
            }
        }
    }

    private void printWelcomeBanner() {
        System.out.println("============================================");
        System.out.println("       Welcome to Ralph's Vending Machine   ");
        System.out.println("============================================");
    }

    private void printMenu() {
        System.out.println();
        for (MenuOption option : MenuOption.values()) {
            System.out.printf("  %d. %s%n", option.getNumber(), option.getDescription());
        }
        System.out.print("Enter choice: ");
    }

    private void handleViewProducts() {
        System.out.println();
        System.out.printf("  %-4s %-14s %-8s %s%n", "#", "Product", "Price", "Status");
        System.out.println("  " + "-".repeat(38));
        for (Slot slot : machine.getSlots()) {
            Product product = slot.getProduct();
            String status = slot.isInStock() ? "In Stock" : "Out of Stock";
            System.out.printf("  %-4d %-14s $%-7.2f %s%n",
                    product.ordinal() + 1,
                    product.getDisplayName(),
                    product.getPrice() / 100.0,
                    status);
        }
    }

    private void handleInsertMoney() {
        System.out.print("Enter amount in cents (e.g. 25 for $0.25): ");
        String input = scanner.nextLine().trim();
        int amount;
        try {
            amount = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a whole number of cents.");
            return;
        }
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }
        machine.insertMoney(amount);
        System.out.printf("Inserted $%.2f. Current balance: $%.2f%n",
                amount / 100.0, machine.getBalance() / 100.0);
    }

    private void handleCheckBalance() {
        System.out.printf("Current balance: $%.2f%n", machine.getBalance() / 100.0);
    }

    private void handlePurchase() {
        Product[] products = Product.values();
        System.out.printf("Enter product number (1-%d): ", products.length);
        String input = scanner.nextLine().trim();
        int number;
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a product number.");
            return;
        }
        if (number < 1 || number > products.length) {
            System.out.printf("Invalid product number. Please choose between 1 and %d.%n", products.length);
            return;
        }
        Product product = products[number - 1];
        PurchaseResult result = machine.purchase(product);
        switch (result) {
            case PurchaseResult.Success s ->
                System.out.printf("Dispensing %s. Remaining balance: $%.2f%n",
                        s.product().getDisplayName(), s.change() / 100.0);
            case PurchaseResult.InsufficientFunds f ->
                System.out.printf("Insufficient funds. Price: $%.2f, Balance: $%.2f%n",
                        f.price() / 100.0, f.balance() / 100.0);
            case PurchaseResult.OutOfStock o ->
                System.out.printf("%s is out of stock.%n", o.product().getDisplayName());
        }
    }

    private void handleGetChange() {
        int change = machine.collectChange();
        if (change > 0) {
            System.out.printf("Returning $%.2f. Have a great day!%n", change / 100.0);
        } else {
            System.out.println("No change to return.");
        }
    }

    private void handleExit() {
        System.out.println("Thank you for using Ralph's Vending Machine. Goodbye!");
    }
}
