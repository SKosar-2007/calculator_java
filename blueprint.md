# Calculator App Blueprint — Java Swing

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17+ (or Java 8+) |
| GUI | Swing (`JFrame`, `JPanel`, `JButton`, `JTextField`, `KeyStroke`) |
| Build | Maven or Gradle |
| Testing | JUnit 5 |
| Version Control | Git |

---

## Project Structure

```
Calculator_java/
├── pom.xml (or build.gradle)
├── src/
│   ├── main/java/com/calculator/
│   │   ├── App.java              # Entry point (main method)
│   │   ├── CalculatorFrame.java  # JFrame setup, layout, key bindings
│   │   ├── CalculatorEngine.java # Arithmetic logic (no UI code)
│   │   ├── MemoryManager.java    # MC / MR / M+ / M- / MS logic
│   │   └── DisplayPanel.java     # JTextField for input/output display
│   └── test/java/com/calculator/
│       ├── CalculatorEngineTest.java
│       └── MemoryManagerTest.java
└── README.md
```

---

## Implementation Steps

### Phase 1 — Project Setup
1. Initialize Maven/Gradle project with correct group/artifact IDs.
2. Add JUnit 5 dependency for testing.
3. Create package `com.calculator`.

### Phase 2 — Calculator Engine (logic first, no UI)
4. `CalculatorEngine.java` — state fields: `currentValue`, `previousValue`, `operator`, `newInput` flag.
5. Methods: `add()`, `subtract()`, `multiply()`, `divide()`, `equals()`, `clear()`, `backspace()`, `decimal()`, `negate()`, `percent()`.
6. Handle divide-by-zero gracefully (return `Infinity` or show error).

### Phase 3 — Memory Manager
7. `MemoryManager.java` — single `double memoryValue` field.
8. Methods: `memoryClear()`, `memoryRecall()`, `memoryAdd(double)`, `memorySubtract(double)`, `memoryStore(double)`, `hasMemory()` (boolean).

### Phase 4 — GUI Layout
9. `CalculatorFrame.java` — extend `JFrame`, set title, size, default close operation.
10. Use `GridLayout` or `GridBagLayout` for the button grid.
11. Create a `DisplayPanel` with a right-aligned, non-editable `JTextField` (or editable for direct input).
12. Buttons layout (4×5 grid):
```
[MC][MR][M+][M-][MS]
[C ][± ][% ][÷ ]
[7 ][8 ][9 ][× ]
[4 ][5 ][6 ][- ]
[1 ][2 ][3 ][+ ]
[0      ][. ][= ]
```

### Phase 5 — Event Handling
13. Add `ActionListener` to each button — route to `CalculatorEngine` or `MemoryManager`.
14. Update display after every operation.
15. **Keyboard input**: register `KeyStroke` bindings on the frame's `rootPane`:
    - `0-9`, `.` → number input
    - `+`, `-`, `*`, `/` → operators
    - `Enter` / `=` → equals
    - `Backspace` → delete last char
    - `Escape` → clear
    - `Delete` → memory clear

### Phase 6 — Polish
16. Format display: remove trailing `.0` for integers, limit decimal places.
17. Highlight active operator button visually.
18. Handle edge cases: leading zeros, double decimal points, overflow.

### Phase 7 — Testing
19. Unit tests for `CalculatorEngine`: all operators, divide-by-zero, chained operations, percent, negate.
20. Unit tests for `MemoryManager`: store, recall, add, subtract, clear.

### Phase 8 — Build & Run
21. `mvn compile exec:java` or build JAR with `maven-shade-plugin`.
22. Verify cross-platform: macOS, Windows, Linux.

---

## Key Design Decisions

- **Separation of concerns**: Engine/Memory have zero Swing imports — easy to test and swap UI.
- **State machine approach** in `CalculatorEngine`: track whether next input is a new number or continuation.
- **Key bindings over `KeyListener`**: Swing `InputMap`/`ActionMap` works reliably even when focus is on buttons.
