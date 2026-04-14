package dev.mikenhil.vending;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MenuOptionTest {

    @Test
    void allOptionsHaveUniqueNumbers() {
        long distinctNumbers = java.util.Arrays.stream(MenuOption.values())
                .mapToInt(MenuOption::getNumber)
                .distinct()
                .count();
        assertEquals(MenuOption.values().length, distinctNumbers);
    }

    @Test
    void fromNumberReturnsCorrectOption() {
        assertEquals(Optional.of(MenuOption.VIEW_PRODUCTS), MenuOption.fromNumber(1));
        assertEquals(Optional.of(MenuOption.INSERT_MONEY), MenuOption.fromNumber(2));
        assertEquals(Optional.of(MenuOption.CHECK_BALANCE), MenuOption.fromNumber(3));
        assertEquals(Optional.of(MenuOption.PURCHASE), MenuOption.fromNumber(4));
        assertEquals(Optional.of(MenuOption.GET_CHANGE), MenuOption.fromNumber(5));
        assertEquals(Optional.of(MenuOption.EXIT), MenuOption.fromNumber(6));
    }

    @Test
    void fromNumberReturnsEmptyForUnknownNumber() {
        assertEquals(Optional.empty(), MenuOption.fromNumber(0));
        assertEquals(Optional.empty(), MenuOption.fromNumber(99));
        assertEquals(Optional.empty(), MenuOption.fromNumber(-1));
    }

    @Test
    void eachOptionHasNonBlankDescription() {
        for (MenuOption option : MenuOption.values()) {
            assertFalse(option.getDescription().isBlank(),
                    option + " should have a non-blank description");
        }
    }
}
