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
        // TODO: implement in next task
    }

    private void handleInsertMoney() {
        // TODO: implement in next task
    }

    private void handleCheckBalance() {
        // TODO: implement in next task
    }

    private void handlePurchase() {
        // TODO: implement in next task
    }

    private void handleGetChange() {
        // TODO: implement in next task
    }

    private void handleExit() {
        System.out.println("Thank you for using Ralph's Vending Machine. Goodbye!");
    }
}
