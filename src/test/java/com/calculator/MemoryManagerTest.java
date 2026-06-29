package com.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryManagerTest {

    private MemoryManager memory;

    @BeforeEach
    void setUp() {
        memory = new MemoryManager();
    }

    @Test
    void initialStateEmpty() {
        assertFalse(memory.hasMemory());
        assertEquals(0, memory.getMemoryValue());
    }

    @Test
    void memoryStore() {
        memory.memoryStore(42);
        assertTrue(memory.hasMemory());
        assertEquals(42, memory.getMemoryValue());
    }

    @Test
    void memoryRecall() {
        memory.memoryStore(99);
        assertEquals(99, memory.getMemoryValue());
    }

    @Test
    void memoryClear() {
        memory.memoryStore(50);
        memory.memoryClear();
        assertFalse(memory.hasMemory());
        assertEquals(0, memory.getMemoryValue());
    }

    @Test
    void memoryAdd() {
        memory.memoryStore(10);
        memory.memoryAdd(5);
        assertEquals(15, memory.getMemoryValue());
    }

    @Test
    void memoryAddFromEmpty() {
        memory.memoryAdd(7);
        assertEquals(7, memory.getMemoryValue());
        assertTrue(memory.hasMemory());
    }

    @Test
    void memorySubtract() {
        memory.memoryStore(20);
        memory.memorySubtract(8);
        assertEquals(12, memory.getMemoryValue());
    }

    @Test
    void memorySubtractFromEmpty() {
        memory.memorySubtract(3);
        assertEquals(-3, memory.getMemoryValue());
        assertTrue(memory.hasMemory());
    }

    @Test
    void memoryStoreOverwrites() {
        memory.memoryStore(10);
        memory.memoryStore(20);
        assertEquals(20, memory.getMemoryValue());
    }

    @Test
    void memoryChainedOperations() {
        memory.memoryStore(100);
        memory.memoryAdd(50);
        memory.memorySubtract(25);
        assertEquals(125, memory.getMemoryValue());
    }

    @Test
    void memoryClearThenStore() {
        memory.memoryStore(10);
        memory.memoryClear();
        memory.memoryStore(20);
        assertTrue(memory.hasMemory());
        assertEquals(20, memory.getMemoryValue());
    }

    @Test
    void memoryStoreZero() {
        memory.memoryStore(0);
        assertTrue(memory.hasMemory());
        assertEquals(0, memory.getMemoryValue());
    }

    @Test
    void memoryStoreNegative() {
        memory.memoryStore(-15);
        assertTrue(memory.hasMemory());
        assertEquals(-15, memory.getMemoryValue());
    }

    @Test
    void memoryAddNegative() {
        memory.memoryStore(10);
        memory.memoryAdd(-3);
        assertEquals(7, memory.getMemoryValue());
    }

    @Test
    void memorySubtractNegative() {
        memory.memoryStore(10);
        memory.memorySubtract(-5);
        assertEquals(15, memory.getMemoryValue());
    }
}
