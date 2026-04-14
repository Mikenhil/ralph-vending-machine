package dev.mikenhil.vending;

import java.util.Optional;

public enum MenuOption {
    VIEW_PRODUCTS(1, "View Products"),
    INSERT_MONEY(2, "Insert Money"),
    CHECK_BALANCE(3, "Check Balance"),
    PURCHASE(4, "Purchase Item"),
    GET_CHANGE(5, "Get Change / Return Coins"),
    EXIT(6, "Exit");

    private final int number;
    private final String description;

    MenuOption(int number, String description) {
        this.number = number;
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<MenuOption> fromNumber(int number) {
        for (MenuOption option : values()) {
            if (option.number == number) {
                return Optional.of(option);
            }
        }
        return Optional.empty();
    }
}
