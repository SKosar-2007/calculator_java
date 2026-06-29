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
    void addition() {
        engine.inputDigit(5);
        engine.setOperator("+");
        engine.inputDigit(3);
        engine.equals();
        assertEquals(8, engine.getCurrentValue());
    }

    @Test
    void subtraction() {
        engine.inputDigit(9);
        engine.setOperator("-");
        engine.inputDigit(4);
        engine.equals();
        assertEquals(5, engine.getCurrentValue());
    }

    @Test
    void multiplication() {
        engine.inputDigit(6);
        engine.setOperator("*");
        engine.inputDigit(7);
        engine.equals();
        assertEquals(42, engine.getCurrentValue());
    }

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

    @Test
    void chainedOperations() {
        engine.inputDigit(2);
        engine.setOperator("+");
        engine.inputDigit(3);
        engine.setOperator("+");
        engine.inputDigit(4);
        engine.equals();
        assertEquals(9, engine.getCurrentValue());
    }

    @Test
    void clearResetsAll() {
        engine.inputDigit(9);
        engine.setOperator("+");
        engine.inputDigit(1);
        engine.clear();
        assertEquals(0, engine.getCurrentValue());
        assertTrue(engine.isNewInput());
    }

    @Test
    void backspaceRemovesLastDigit() {
        engine.inputDigit(1);
        engine.inputDigit(2);
        engine.inputDigit(3);
        engine.backspace();
        assertEquals(12, engine.getCurrentValue());
    }

    @Test
    void negatePositive() {
        engine.inputDigit(5);
        engine.negate();
        assertEquals(-5, engine.getCurrentValue());
    }

    @Test
    void percent() {
        engine.setCurrentValue(50);
        engine.percent();
        assertEquals(0.5, engine.getCurrentValue(), 0.0001);
    }

    @Test
    void initialState() {
        assertEquals(0, engine.getCurrentValue());
        assertTrue(engine.isNewInput());
        assertEquals("", engine.getOperator());
    }

    @Test
    void decimalInput() {
        engine.setCurrentValue(3.14);
        assertEquals(3.14, engine.getCurrentValue(), 0.0001);
    }
}
