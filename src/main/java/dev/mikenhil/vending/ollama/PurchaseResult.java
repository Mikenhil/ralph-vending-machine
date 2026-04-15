// src/main/java/dev/mikenhil/vending/ollama/PurchaseResult.java
package dev.mikenhil.vending.ollama;

public sealed interface PurchaseResult permits Success, InsufficientFunds, [K
OutOfStock {
    record Success(String message) { }
    record InsufficientFunds(String message) { }
    record OutOfStock(String message) { }
}
