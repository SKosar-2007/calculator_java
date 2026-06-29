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
    void memorySubtract() {
        memory.memoryStore(20);
        memory.memorySubtract(8);
        assertEquals(12, memory.getMemoryValue());
    }

    @Test
    void memoryStoreOverwrites() {
        memory.memoryStore(10);
        memory.memoryStore(20);
        assertEquals(20, memory.getMemoryValue());
    }
}
