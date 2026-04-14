package dev.mikenhil.vending.claude;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlotTest {

    @Test
    void inStockWhenQuantityIsPositive() {
        Slot slot = new Slot(Product.COLA, 5);
        assertTrue(slot.isInStock());
    }

    @Test
    void outOfStockWhenQuantityIsZero() {
        Slot slot = new Slot(Product.COLA, 0);
        assertFalse(slot.isInStock());
    }

    @Test
    void decrementReducesQuantityByOne() {
        Slot slot = new Slot(Product.WATER, 3);
        slot.decrementQuantity();
        assertEquals(2, slot.getQuantity());
    }

    @Test
    void decrementToZeroMakesOutOfStock() {
        Slot slot = new Slot(Product.GUM, 1);
        slot.decrementQuantity();
        assertFalse(slot.isInStock());
        assertEquals(0, slot.getQuantity());
    }

    @Test
    void decrementWhenOutOfStockThrows() {
        Slot slot = new Slot(Product.CHIPS, 0);
        assertThrows(IllegalStateException.class, slot::decrementQuantity);
    }

    @Test
    void negativeInitialQuantityThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Slot(Product.COLA, -1));
    }

    @Test
    void getProductReturnsCorrectProduct() {
        Slot slot = new Slot(Product.CANDY_BAR, 2);
        assertEquals(Product.CANDY_BAR, slot.getProduct());
    }
}
