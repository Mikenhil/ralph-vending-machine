package dev.mikenhil.vending.claude;

public class Slot {
    private final Product product;
    private int quantity;

    public Slot(Product product, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isInStock() {
        return quantity > 0;
    }

    public void decrementQuantity() {
        if (!isInStock()) {
            throw new IllegalStateException("Cannot decrement quantity: slot is out of stock");
        }
        quantity--;
    }
}
