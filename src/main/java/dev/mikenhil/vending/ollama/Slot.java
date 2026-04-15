// src/main/java/dev/mikenhil/vending/ollama/Slot.java
package dev.mikenhil.vending.ollama;

import java.util.Scanner;

public class Slot {
    private final Scanner scanner = new Scanner(System.in);

    private Product product;
    private int quantity;

    public Slot() {
        this.product = null;
        this.quantity = 0;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return this.product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public boolean isInStock() {
        if (this.quantity > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void decrementQuantity() {
        this.quantity -= 1;
    }
}
