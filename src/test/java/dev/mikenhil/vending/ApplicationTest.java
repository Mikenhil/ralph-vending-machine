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
}
