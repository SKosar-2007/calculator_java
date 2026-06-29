package com.calculator;

public class CalculatorEngine {

    private double currentValue = 0;
    private double previousValue = 0;
    private String operator = "";
    private boolean newInput = true;

    public void inputDigit(int digit) {
        if (newInput) {
            currentValue = digit;
            newInput = false;
        } else {
            currentValue = currentValue * 10 + digit;
        }
    }

    public void inputDecimal() {
        if (newInput) {
            currentValue = 0;
            newInput = false;
        }
        // Decimal logic handled by display panel appending "."
    }

    public void setOperator(String op) {
        if (!newInput) {
            calculate();
        }
        previousValue = currentValue;
        operator = op;
        newInput = true;
    }

    public void calculate() {
        if (operator.isEmpty()) return;

        switch (operator) {
            case "+":
                currentValue = previousValue + currentValue;
                break;
            case "-":
                currentValue = previousValue - currentValue;
                break;
            case "*":
                currentValue = previousValue * currentValue;
                break;
            case "/":
                if (currentValue == 0) {
                    currentValue = Double.POSITIVE_INFINITY;
                } else {
                    currentValue = previousValue / currentValue;
                }
                break;
        }
        operator = "";
        newInput = true;
    }

    public void equals() {
        calculate();
    }

    public void clear() {
        currentValue = 0;
        previousValue = 0;
        operator = "";
        newInput = true;
    }

    public void backspace() {
        if (newInput) return;
        currentValue = (long) currentValue / 10;
    }

    public void negate() {
        currentValue = -currentValue;
    }

    public void percent() {
        currentValue = currentValue / 100.0;
        newInput = true;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public String getOperator() {
        return operator;
    }

    public boolean isNewInput() {
        return newInput;
    }

    public void setCurrentValue(double value) {
        currentValue = value;
    }
}
