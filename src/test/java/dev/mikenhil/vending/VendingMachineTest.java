package dev.mikenhil.vending;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VendingMachineTest {

    private VendingMachine machine;

    @BeforeEach
    void setUp() {
        machine = new VendingMachine();
    }

    @Test
    void initialBalanceIsZero() {
        assertEquals(0, machine.getBalance());
    }

    @Test
    void insertMoneyIncreasesBalance() {
        machine.insertMoney(100);
        assertEquals(100, machine.getBalance());
    }

    @Test
    void insertMoneyAccumulatesBalance() {
        machine.insertMoney(100);
        machine.insertMoney(50);
        assertEquals(150, machine.getBalance());
    }

    @Test
    void insertMoneyRejectsZero() {
        assertThrows(IllegalArgumentException.class, () -> machine.insertMoney(0));
    }

    @Test
    void insertMoneyRejectsNegative() {
        assertThrows(IllegalArgumentException.class, () -> machine.insertMoney(-25));
    }

    @Test
    void collectChangeReturnsBalance() {
        machine.insertMoney(200);
        assertEquals(200, machine.collectChange());
    }

    @Test
    void collectChangeResetsBalanceToZero() {
        machine.insertMoney(200);
        machine.collectChange();
        assertEquals(0, machine.getBalance());
    }

    @Test
    void collectChangeWhenBalanceIsZeroReturnsZero() {
        assertEquals(0, machine.collectChange());
    }

    @Test
    void getSlotsReturnsOneSlotPerProduct() {
        List<Slot> slots = machine.getSlots();
        assertEquals(Product.values().length, slots.size());
    }

    @Test
    void getSlotsContainsAllProducts() {
        List<Slot> slots = machine.getSlots();
        for (Product product : Product.values()) {
            assertTrue(slots.stream().anyMatch(s -> s.getProduct() == product));
        }
    }

    @Test
    void getSlotsAreInStock() {
        machine.getSlots().forEach(slot -> assertTrue(slot.isInStock()));
    }

    @Test
    void getSlotsReturnsDefensiveCopy() {
        List<Slot> first = machine.getSlots();
        List<Slot> second = machine.getSlots();
        assertNotSame(first, second);
    }

    @Test
    void mutatingSlotsListDoesNotAffectMachine() {
        List<Slot> slots = machine.getSlots();
        slots.clear();
        assertEquals(Product.values().length, machine.getSlots().size());
    }

    @Test
    void purchaseSucceedsWhenBalanceSufficient() {
        machine.insertMoney(200);
        PurchaseResult result = machine.purchase(Product.COLA);
        assertInstanceOf(PurchaseResult.Success.class, result);
        PurchaseResult.Success success = (PurchaseResult.Success) result;
        assertEquals(Product.COLA, success.product());
        assertEquals(50, success.change());
    }

    @Test
    void purchaseDeductsProductPriceFromBalance() {
        machine.insertMoney(200);
        machine.purchase(Product.COLA);
        assertEquals(50, machine.getBalance());
    }

    @Test
    void purchaseDecrementsSlotQuantity() {
        machine.insertMoney(200);
        machine.purchase(Product.COLA);
        Slot colaSlot = machine.getSlots().stream()
                .filter(s -> s.getProduct() == Product.COLA)
                .findFirst().orElseThrow();
        assertEquals(4, colaSlot.getQuantity());
    }

    @Test
    void purchaseReturnsInsufficientFundsWhenBalanceLow() {
        machine.insertMoney(50);
        PurchaseResult result = machine.purchase(Product.COLA);
        assertInstanceOf(PurchaseResult.InsufficientFunds.class, result);
        PurchaseResult.InsufficientFunds insufficient = (PurchaseResult.InsufficientFunds) result;
        assertEquals(Product.COLA.getPrice(), insufficient.price());
        assertEquals(50, insufficient.balance());
    }

    @Test
    void purchaseReturnsOutOfStockWhenSlotEmpty() {
        machine.insertMoney(1000);
        for (int i = 0; i < 5; i++) {
            machine.purchase(Product.GUM);
        }
        PurchaseResult result = machine.purchase(Product.GUM);
        assertInstanceOf(PurchaseResult.OutOfStock.class, result);
        PurchaseResult.OutOfStock outOfStock = (PurchaseResult.OutOfStock) result;
        assertEquals(Product.GUM, outOfStock.product());
    }

    @Test
    void purchaseDoesNotAlterBalanceWhenInsufficientFunds() {
        machine.insertMoney(50);
        machine.purchase(Product.COLA);
        assertEquals(50, machine.getBalance());
    }

    @Test
    void purchaseDoesNotAlterBalanceWhenOutOfStock() {
        machine.insertMoney(1000);
        for (int i = 0; i < 5; i++) {
            machine.purchase(Product.GUM);
        }
        int balanceBefore = machine.getBalance();
        machine.purchase(Product.GUM);
        assertEquals(balanceBefore, machine.getBalance());
    }
}
