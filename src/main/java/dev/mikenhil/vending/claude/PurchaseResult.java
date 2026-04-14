package dev.mikenhil.vending.claude;

public sealed interface PurchaseResult {
    record Success(Product product, int change) implements PurchaseResult {}
    record InsufficientFunds(int price, int balance) implements PurchaseResult {}
    record OutOfStock(Product product) implements PurchaseResult {}
}
