package com.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorEngineTest {

    private CalculatorEngine engine;

    @BeforeEach
    void setUp() {
        engine = new CalculatorEngine();
    }

    // --- Input Tests ---

    @Test
    void inputSingleDigit() {
        engine.inputDigit(5);
        assertEquals(5, engine.getCurrentValue());
    }

    @Test
    void inputMultipleDigits() {
        engine.inputDigit(1);
        engine.inputDigit(2);
        engine.inputDigit(3);
        assertEquals(123, engine.getCurrentValue());
    }

    @Test
    void inputAfterNewInputResets() {
        engine.inputDigit(5);
        engine.setOperator("+");
        engine.inputDigit(3);
        assertEquals(3, engine.getCurrentValue());
    }

    // --- Addition ---

    @Test
    void addition() {
        engine.inputDigit(5);
        engine.setOperator("+");
        engine.inputDigit(3);
        engine.equals();
        assertEquals(8, engine.getCurrentValue());
    }

    @Test
    void additionWithZero() {
        engine.inputDigit(5);
        engine.setOperator("+");
        engine.inputDigit(0);
        engine.equals();
        assertEquals(5, engine.getCurrentValue());
    }

    // --- Subtraction ---

    @Test
    void subtraction() {
        engine.inputDigit(9);
        engine.setOperator("-");
        engine.inputDigit(4);
        engine.equals();
        assertEquals(5, engine.getCurrentValue());
    }

    @Test
    void subtractionResultNegative() {
        engine.inputDigit(3);
        engine.setOperator("-");
        engine.inputDigit(7);
        engine.equals();
        assertEquals(-4, engine.getCurrentValue());
    }

    // --- Multiplication ---

    @Test
    void multiplication() {
        engine.inputDigit(6);
        engine.setOperator("*");
        engine.inputDigit(7);
        engine.equals();
        assertEquals(42, engine.getCurrentValue());
    }

    @Test
    void multiplicationByZero() {
        engine.inputDigit(5);
        engine.setOperator("*");
        engine.inputDigit(0);
        engine.equals();
        assertEquals(0, engine.getCurrentValue());
    }

    // --- Division ---

    @Test
    void division() {
        engine.inputDigit(1);
        engine.inputDigit(0);
        engine.setOperator("/");
        engine.inputDigit(2);
        engine.equals();
        assertEquals(5, engine.getCurrentValue());
    }

    @Test
    void divisionByZero() {
        engine.inputDigit(5);
        engine.setOperator("/");
        engine.inputDigit(0);
        engine.equals();
        assertTrue(Double.isInfinite(engine.getCurrentValue()));
    }

    // --- Chained Operations ---

    @Test
    void chainedAddition() {
        engine.inputDigit(2);
        engine.setOperator("+");
        engine.inputDigit(3);
        engine.setOperator("+");
        engine.inputDigit(4);
        engine.equals();
        assertEquals(9, engine.getCurrentValue());
    }

    @Test
    void chainedMixedOperations() {
        engine.inputDigit(5);
        engine.setOperator("+");
        engine.inputDigit(3);
        engine.setOperator("-");
        engine.inputDigit(2);
        engine.equals();
        assertEquals(6, engine.getCurrentValue());
    }

    // --- Clear ---

    @Test
    void clearResetsAll() {
        engine.inputDigit(9);
        engine.setOperator("+");
        engine.inputDigit(1);
        engine.clear();
        assertEquals(0, engine.getCurrentValue());
        assertTrue(engine.isNewInput());
    }

    // --- Backspace ---

    @Test
    void backspaceRemovesLastDigit() {
        engine.inputDigit(1);
        engine.inputDigit(2);
        engine.inputDigit(3);
        engine.backspace();
        assertEquals(12, engine.getCurrentValue());
    }

    @Test
    void backspaceOnSingleDigit() {
        engine.inputDigit(5);
        engine.backspace();
        assertEquals(0, engine.getCurrentValue());
        assertTrue(engine.isNewInput());
    }

    @Test
    void backspaceAfterNewInputDoesNothing() {
        engine.inputDigit(5);
        engine.setOperator("+");
        engine.backspace();
        assertEquals(5, engine.getPreviousValue());
    }

    // --- Negate ---

    @Test
    void negatePositive() {
        engine.inputDigit(5);
        engine.negate();
        assertEquals(-5, engine.getCurrentValue());
    }

    @Test
    void negateNegative() {
        engine.inputDigit(5);
        engine.negate();
        engine.negate();
        assertEquals(5, engine.getCurrentValue());
    }

    @Test
    void negateZero() {
        engine.negate();
        assertEquals(0.0, engine.getCurrentValue(), 0.0001);
    }

    // --- Percent ---

    @Test
    void percent() {
        engine.inputDigit(5);
        engine.inputDigit(0);
        engine.percent();
        assertEquals(0.5, engine.getCurrentValue(), 0.0001);
    }

    @Test
    void percentOfZero() {
        engine.percent();
        assertEquals(0, engine.getCurrentValue());
    }

    // --- State ---

    @Test
    void initialState() {
        assertEquals(0, engine.getCurrentValue());
        assertTrue(engine.isNewInput());
        assertEquals("", engine.getOperator());
    }

    @Test
    void setOperatorStoresPrevious() {
        engine.inputDigit(5);
        engine.setOperator("+");
        assertEquals(5, engine.getCurrentValue());
    }

    // --- Edge Cases ---

    @Test
    void largeNumberMultiplication() {
        engine.inputDigit(9);
        engine.inputDigit(9);
        engine.inputDigit(9);
        engine.inputDigit(9);
        engine.inputDigit(9);
        engine.inputDigit(9);
        engine.setOperator("*");
        engine.inputDigit(9);
        engine.inputDigit(9);
        engine.inputDigit(9);
        engine.inputDigit(9);
        engine.inputDigit(9);
        engine.inputDigit(9);
        engine.equals();
        assertTrue(engine.getCurrentValue() > 999000000000.0);
    }

    @Test
    void decimalInput() {
        engine.setCurrentValue(3.14);
        assertEquals(3.14, engine.getCurrentValue(), 0.0001);
    }

    @Test
    void inputDigitLimit() {
        for (int i = 0; i < 20; i++) {
            engine.inputDigit(1);
        }
        String num = String.valueOf((long) engine.getCurrentValue());
        assertTrue(num.length() <= 15);
    }
}
