// src/main/java/dev/mikenhil/vending/ollama/Product.java
package dev.mikenhil.vending.ollama;

public enum Product {
    COLAS("Cola", 10),
    DIET_COLA("Diet Cola", 8),
    WATER("Water", 0),
    ORANGE_JUICE("Orange Juice", 12),
    CHIPS("Chips", 5),
    CANDY_BAR("Candy Bar", 7),
    GUM("Gum", 3);
    
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
