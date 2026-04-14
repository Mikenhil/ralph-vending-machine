package dev.mikenhil.vending;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseResultTest {

    @Test
    void successHoldsProductAndChange() {
        var result = new PurchaseResult.Success(Product.COLA, 50);
        assertEquals(Product.COLA, result.product());
        assertEquals(50, result.change());
    }

    @Test
    void successWithZeroChange() {
        var result = new PurchaseResult.Success(Product.GUM, 0);
        assertEquals(Product.GUM, result.product());
        assertEquals(0, result.change());
    }

    @Test
    void insufficientFundsHoldsPriceAndBalance() {
        var result = new PurchaseResult.InsufficientFunds(150, 75);
        assertEquals(150, result.price());
        assertEquals(75, result.balance());
    }

    @Test
    void outOfStockHoldsProduct() {
        var result = new PurchaseResult.OutOfStock(Product.CHIPS);
        assertEquals(Product.CHIPS, result.product());
    }

    @Test
    void patternMatchingCoversAllVariants() {
        PurchaseResult[] results = {
            new PurchaseResult.Success(Product.WATER, 0),
            new PurchaseResult.InsufficientFunds(200, 100),
            new PurchaseResult.OutOfStock(Product.CANDY_BAR)
        };

        for (PurchaseResult result : results) {
            String label = switch (result) {
                case PurchaseResult.Success s -> "success";
                case PurchaseResult.InsufficientFunds f -> "insufficient";
                case PurchaseResult.OutOfStock o -> "out-of-stock";
            };
            assertNotNull(label);
        }
    }
}
