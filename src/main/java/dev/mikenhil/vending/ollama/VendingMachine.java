package dev.mikenhil.vending.ollama;

import java.util.Scanner;

public class VendingMachine {
    private int balance;
    private List<Slot> slots;

    public VendingMachine() {
        this.balance = 0;
        this.slots = new ArrayList<>();
    }

    public void insertMoney(int amount) {
        this.balance += amount;
    }

    public int getBalance() {
        return this.balance;
    }

    public void collectChange(int amount) {
        this.balance -= amount;
    }

    public List<Slot> getSlots() {
        return new ArrayList<>(this.slots);
    }

    public PurchaseResult purchase(int slotIndex, int quantity) {
        Slot slot = slots.get(slotIndex);
        Product product = slot.getProduct();

        if (quantity > slot.getQuantity()) {
            return new PurchaseResult.OutOfStock(slotIndex, product);
        }

        if (this.balance < product.getPrice() * quantity) {
            return new PurchaseResult.InsufficientFunds(slotIndex, product)[8D[K
product);
        }

        this.slots.get(slotIndex).decrementQuantity(quantity);
        this.collectChange(product.getPrice() * quantity);

        return new PurchaseResult.Success(slotIndex, product, quantity);
    }
}
