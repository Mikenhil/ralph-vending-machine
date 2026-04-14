package dev.mikenhil.vending.claude;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void allProductsHaveDisplayName() {
        for (Product p : Product.values()) {
            assertNotNull(p.getDisplayName());
            assertFalse(p.getDisplayName().isBlank());
        }
    }

    @Test
    void allProductsHavePositivePrice() {
        for (Product p : Product.values()) {
            assertTrue(p.getPrice() > 0, p.name() + " should have a positive price");
        }
    }

    @Test
    void productDisplayNames() {
        assertEquals("Cola", Product.COLA.getDisplayName());
        assertEquals("Diet Cola", Product.DIET_COLA.getDisplayName());
        assertEquals("Water", Product.WATER.getDisplayName());
        assertEquals("Orange Juice", Product.ORANGE_JUICE.getDisplayName());
        assertEquals("Chips", Product.CHIPS.getDisplayName());
        assertEquals("Candy Bar", Product.CANDY_BAR.getDisplayName());
        assertEquals("Gum", Product.GUM.getDisplayName());
    }

    @Test
    void productPrices() {
        assertEquals(150, Product.COLA.getPrice());
        assertEquals(150, Product.DIET_COLA.getPrice());
        assertEquals(100, Product.WATER.getPrice());
        assertEquals(200, Product.ORANGE_JUICE.getPrice());
        assertEquals(175, Product.CHIPS.getPrice());
        assertEquals(125, Product.CANDY_BAR.getPrice());
        assertEquals(75, Product.GUM.getPrice());
    }

    @Test
    void sevenProductsDefined() {
        assertEquals(7, Product.values().length);
    }
}
