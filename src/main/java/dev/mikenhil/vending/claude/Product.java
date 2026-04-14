package dev.mikenhil.vending.claude;

public enum Product {
    COLA("Cola", 150),
    DIET_COLA("Diet Cola", 150),
    WATER("Water", 100),
    ORANGE_JUICE("Orange Juice", 200),
    CHIPS("Chips", 175),
    CANDY_BAR("Candy Bar", 125),
    GUM("Gum", 75);

    private final String displayName;
    private final int price;

    Product(String displayName, int price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPrice() {
        return price;
    }
}
