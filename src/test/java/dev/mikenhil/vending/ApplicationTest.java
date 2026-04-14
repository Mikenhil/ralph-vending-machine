package dev.mikenhil.vending;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationTest {

    private String runWith(String input) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        try {
            VendingMachine machine = new VendingMachine();
            Scanner scanner = new Scanner(new StringReader(input));
            Application app = new Application(machine, scanner);
            app.run();
        } finally {
            System.setOut(original);
        }
        return out.toString();
    }

    @Test
    void welcomeBannerIsPrinted() {
        String output = runWith("6\n");
        assertTrue(output.contains("Welcome to Ralph's Vending Machine"));
    }

    @Test
    void menuOptionsArePrinted() {
        String output = runWith("6\n");
        for (MenuOption option : MenuOption.values()) {
            assertTrue(output.contains(option.getDescription()),
                    "Expected menu to contain: " + option.getDescription());
        }
    }

    @Test
    void invalidNonNumericInputShowsError() {
        String output = runWith("abc\n6\n");
        assertTrue(output.contains("Invalid input"));
    }

    @Test
    void invalidNumberShowsError() {
        String output = runWith("99\n6\n");
        assertTrue(output.contains("Invalid option"));
    }

    @Test
    void exitOptionPrintsGoodbyeAndTerminates() {
        String output = runWith("6\n");
        assertTrue(output.contains("Goodbye"));
    }

    @Test
    void viewProductsPrintsAllProductNames() {
        String output = runWith("1\n6\n");
        for (Product product : Product.values()) {
            assertTrue(output.contains(product.getDisplayName()),
                    "Expected product table to contain: " + product.getDisplayName());
        }
    }

    @Test
    void viewProductsPrintsHeader() {
        String output = runWith("1\n6\n");
        assertTrue(output.contains("Product"));
        assertTrue(output.contains("Price"));
        assertTrue(output.contains("Status"));
    }

    @Test
    void viewProductsShowsInStockStatus() {
        String output = runWith("1\n6\n");
        assertTrue(output.contains("In Stock"));
    }

    @Test
    void viewProductsShowsPriceFormatted() {
        String output = runWith("1\n6\n");
        // Cola costs $1.50
        assertTrue(output.contains("$1.50"), "Expected Cola price $1.50 in output");
    }

    @Test
    void viewProductsNumbersStartAtOne() {
        String output = runWith("1\n6\n");
        assertTrue(output.contains("1"), "Expected product number 1 in output");
    }

    @Test
    void insertMoneyUpdatesBalance() {
        // Insert 150 cents, then check balance shows $1.50
        String output = runWith("2\n150\n3\n6\n");
        assertTrue(output.contains("$1.50"), "Expected balance of $1.50 after inserting 150 cents");
    }

    @Test
    void insertMoneyConfirmationShowsInsertedAmount() {
        String output = runWith("2\n100\n6\n");
        assertTrue(output.contains("Inserted $1.00"), "Expected inserted amount confirmation");
    }

    @Test
    void insertMoneyRejectsNonNumericInput() {
        String output = runWith("2\nabc\n6\n");
        assertTrue(output.contains("Invalid input"), "Expected invalid input message for non-numeric amount");
    }

    @Test
    void insertMoneyRejectsZero() {
        String output = runWith("2\n0\n6\n");
        assertTrue(output.contains("Amount must be positive"), "Expected positive-amount error for zero");
    }

    @Test
    void insertMoneyRejectsNegativeAmount() {
        String output = runWith("2\n-50\n6\n");
        assertTrue(output.contains("Amount must be positive"), "Expected positive-amount error for negative input");
    }

    @Test
    void checkBalanceShowsZeroInitially() {
        String output = runWith("3\n6\n");
        assertTrue(output.contains("$0.00"), "Expected zero balance initially");
    }

    @Test
    void checkBalanceReflectsInsertedMoney() {
        String output = runWith("2\n200\n3\n6\n");
        assertTrue(output.contains("$2.00"), "Expected balance of $2.00 after inserting 200 cents");
    }
}
