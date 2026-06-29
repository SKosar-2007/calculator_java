package com.calculator;

public class MemoryManager {

    private double memoryValue = 0;
    private boolean hasStoredValue = false;

    public void memoryClear() {
        memoryValue = 0;
        hasStoredValue = false;
    }

    public void memoryRecall() {
        // Caller reads via getMemoryValue()
    }

    public void memoryAdd(double value) {
        memoryValue += value;
        hasStoredValue = true;
    }

    public void memorySubtract(double value) {
        memoryValue -= value;
        hasStoredValue = true;
    }

    public void memoryStore(double value) {
        memoryValue = value;
        hasStoredValue = true;
    }

    public double getMemoryValue() {
        return memoryValue;
    }

    public boolean hasMemory() {
        return hasStoredValue;
    }
}
