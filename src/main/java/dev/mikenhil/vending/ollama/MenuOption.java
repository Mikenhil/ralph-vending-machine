// src/main/java/dev/mikenhil/vending/ollama/MenuOption.java
package dev.mikenhil.vending.ollama;

public enum MenuOption {
    INSERT_MONEY(1, "Insert money"),
    CHECK_BALANCE(2, "Check balance"),
    COLLECT_CHANGE(3, "Collect change"),
    VIEW_PRODUCTS(4, "View products"),
    PURCHASE(5, "Purchase"),
    EXIT(6, "Exit");

    private final int number;
    private final String description;

    MenuOption(int number, String description) {
        this.number = number;
        this.description = description;
    }

    public int getNumber() {
        return this.number;
    }

    public String getDescription() {
        return this.description;
    }
}
