# Product Requirements

## Vending Machine Console Application - Java 25

## Tasks
- [x] Create Product enum with displayName and price fields (Cola, Diet Cola, Water, Orange Juice, Chips, Candy Bar, Gum)
- [x] Create Slot class with Product reference, quantity tracking, isInStock() and decrementQuantity() methods
- [x] Create PurchaseResult sealed interface with Success, InsufficientFunds, and OutOfStock record variants
- [x] Create MenuOption enum with number and description fields and fromNumber() static lookup
- [x] Create VendingMachine class with balance management (insertMoney, getBalance, collectChange)
- [x] Add inventory loading and getSlots() with defensive copy to VendingMachine
- [] Add purchase() method to VendingMachine returning PurchaseResult with stock and balance checks
- [] Create Application class with main(), Scanner setup, welcome banner, and main menu loop
- [] Add viewProducts handler with formatted table showing product number, name, price, and stock status
- [] Add insertMoney and checkBalance handlers with input validation
- [] Add purchase handler with pattern matching switch on PurchaseResult sealed interface
- [] Add getChange handler and exit flow with Scanner cleanup
- [] Verify full application flow end-to-end (compile, run, test all menu options and edge cases)