package dev.mikenhil.vending;

import java.util.ArrayList;
import java.util.List;

public class VendingMachine {
    private static final int DEFAULT_QUANTITY = 5;

    private int balance;
    private final List<Slot> slots;

    public VendingMachine() {
        this.balance = 0;
        this.slots = new ArrayList<>();
        for (Product product : Product.values()) {
            slots.add(new Slot(product, DEFAULT_QUANTITY));
        }
    }

    public List<Slot> getSlots() {
        return new ArrayList<>(slots);
    }

    public void insertMoney(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
    }

    public int getBalance() {
        return balance;
    }

    public int collectChange() {
        int change = balance;
        balance = 0;
        return change;
    }

    public PurchaseResult purchase(Product product) {
        Slot slot = slots.stream()
                .filter(s -> s.getProduct() == product)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + product));

        if (!slot.isInStock()) {
            return new PurchaseResult.OutOfStock(product);
        }
        if (balance < product.getPrice()) {
            return new PurchaseResult.InsufficientFunds(product.getPrice(), balance);
        }

        slot.decrementQuantity();
        balance -= product.getPrice();
        return new PurchaseResult.Success(product, balance);
    }
}
