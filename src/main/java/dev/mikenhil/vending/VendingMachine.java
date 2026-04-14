package dev.mikenhil.vending;

public class VendingMachine {
    private int balance;

    public VendingMachine() {
        this.balance = 0;
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
}
