package com.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class CalculatorFrame extends JFrame {

    private final CalculatorEngine engine;
    private final MemoryManager memory;
    private final DisplayPanel display;
    private String displayString = "0";
    private boolean hasDecimal = false;
    private JButton activeOpButton = null;

    private static final Color BG_COLOR = new Color(30, 30, 30);
    private static final Color BTN_COLOR = new Color(50, 50, 50);
    private static final Color NUM_COLOR = new Color(70, 70, 70);
    private static final Color OP_COLOR = new Color(255, 149, 0);
    private static final Color MEM_COLOR = new Color(40, 40, 40);
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color TEXT_ORANGE = Color.WHITE;

    public CalculatorFrame() {
        engine = new CalculatorEngine();
        memory = new MemoryManager();
        display = new DisplayPanel();

        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(BG_COLOR);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 5));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        mainPanel.add(display, BorderLayout.NORTH);
        mainPanel.add(createButtonPanel(), BorderLayout.CENTER);

        add(mainPanel);
        pack();
        setSize(320, 500);
        setLocationRelativeTo(null);

        setupKeyboardBindings();
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 4, 4, 4));
        panel.setBackground(BG_COLOR);

        // Row 1: Memory
        panel.add(createButton("MC", MEM_COLOR, TEXT_WHITE, e -> memoryClear()));
        panel.add(createButton("MR", MEM_COLOR, TEXT_WHITE, e -> memoryRecall()));
        panel.add(createButton("M+", MEM_COLOR, TEXT_WHITE, e -> memoryAdd()));
        panel.add(createButton("M-", MEM_COLOR, TEXT_WHITE, e -> memorySubtract()));

        // Row 2: Clear row
        panel.add(createButton("MS", MEM_COLOR, TEXT_WHITE, e -> memoryStore()));
        panel.add(createButton("C", OP_COLOR, TEXT_WHITE, e -> clearAll()));
        panel.add(createButton("±", OP_COLOR, TEXT_WHITE, e -> negate()));
        panel.add(createButton("%", OP_COLOR, TEXT_WHITE, e -> percent()));

        // Row 3
        panel.add(createButton("7", NUM_COLOR, TEXT_WHITE, e -> inputDigit(7)));
        panel.add(createButton("8", NUM_COLOR, TEXT_WHITE, e -> inputDigit(8)));
        panel.add(createButton("9", NUM_COLOR, TEXT_WHITE, e -> inputDigit(9)));
        panel.add(createButton("÷", OP_COLOR, TEXT_WHITE, e -> inputOperator("/")));

        // Row 4
        panel.add(createButton("4", NUM_COLOR, TEXT_WHITE, e -> inputDigit(4)));
        panel.add(createButton("5", NUM_COLOR, TEXT_WHITE, e -> inputDigit(5)));
        panel.add(createButton("6", NUM_COLOR, TEXT_WHITE, e -> inputDigit(6)));
        panel.add(createButton("×", OP_COLOR, TEXT_WHITE, e -> inputOperator("*")));

        // Row 5
        panel.add(createButton("1", NUM_COLOR, TEXT_WHITE, e -> inputDigit(1)));
        panel.add(createButton("2", NUM_COLOR, TEXT_WHITE, e -> inputDigit(2)));
        panel.add(createButton("3", NUM_COLOR, TEXT_WHITE, e -> inputDigit(3)));
        panel.add(createButton("-", OP_COLOR, TEXT_WHITE, e -> inputOperator("-")));

        // Row 6
        panel.add(createButton("0", NUM_COLOR, TEXT_WHITE, e -> inputDigit(0)));
        panel.add(createButton(".", NUM_COLOR, TEXT_WHITE, e -> inputDecimal()));
        panel.add(createButton("=", OP_COLOR, TEXT_WHITE, e -> inputEquals()));
        panel.add(createButton("+", OP_COLOR, TEXT_WHITE, e -> inputOperator("+")));

        return panel;
    }

    private JButton createButton(String text, Color bg, Color fg, java.util.function.Consumer<ActionEvent> action) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> action.accept(e));
        return button;
    }

    // --- Calculator Actions ---

    private void inputDigit(int digit) {
        clearActiveOpButton();
        engine.inputDigit(digit);
        if (displayString.equals("0") || displayString.equals("-0")) {
            displayString = String.valueOf(digit);
        } else {
            displayString += digit;
        }
        hasDecimal = false;
        updateDisplay();
    }

    private void inputDecimal() {
        clearActiveOpButton();
        if (!hasDecimal) {
            if (engine.isNewInput()) {
                displayString = "0.";
                engine.setCurrentValue(0);
            } else {
                displayString += ".";
            }
            hasDecimal = true;
            updateDisplay();
        }
    }

    private void inputOperator(String op) {
        engine.setOperator(op);
        displayString = formatValue(engine.getCurrentValue());
        hasDecimal = false;
        highlightOpButton(op);
        updateDisplay();
    }

    private void inputEquals() {
        clearActiveOpButton();
        engine.equals();
        displayString = formatValue(engine.getCurrentValue());
        hasDecimal = false;
        updateDisplay();
    }

    private void clearAll() {
        clearActiveOpButton();
        engine.clear();
        displayString = "0";
        hasDecimal = false;
        updateDisplay();
    }

    private void negate() {
        clearActiveOpButton();
        engine.negate();
        displayString = formatValue(engine.getCurrentValue());
        updateDisplay();
    }

    private void percent() {
        clearActiveOpButton();
        engine.percent();
        displayString = formatValue(engine.getCurrentValue());
        hasDecimal = false;
        updateDisplay();
    }

    // --- Memory Actions ---

    private void memoryClear() {
        memory.memoryClear();
        updateDisplay();
    }

    private void memoryRecall() {
        if (memory.hasMemory()) {
            engine.setCurrentValue(memory.getMemoryValue());
            engine.clear(); // reset so we start fresh with recalled value
            engine.setCurrentValue(memory.getMemoryValue());
            displayString = formatValue(memory.getMemoryValue());
            hasDecimal = displayString.contains(".");
            updateDisplay();
        }
    }

    private void memoryAdd() {
        memory.memoryAdd(engine.getCurrentValue());
    }

    private void memorySubtract() {
        memory.memorySubtract(engine.getCurrentValue());
    }

    private void memoryStore() {
        memory.memoryStore(engine.getCurrentValue());
    }

    // --- Display ---

    private void updateDisplay() {
        display.setText(displayString);
    }

    private String formatValue(double value) {
        if (Double.isInfinite(value)) {
            return "Cannot divide by zero";
        }
        if (value == (long) value && !Double.isInfinite(value)) {
            return String.valueOf((long) value);
        }
        String formatted = String.valueOf(value);
        if (formatted.length() > 16) {
            formatted = String.format("%.10g", value);
        }
        return formatted;
    }

    private void highlightOpButton(String op) {
        clearActiveOpButton();
        String symbol = switch (op) {
            case "+" -> "+";
            case "-" -> "-";
            case "*" -> "×";
            case "/" -> "÷";
            default -> "";
        };
        Component[] components = ((JPanel) getContentPane().getComponent(0)).getComponents();
        // Walk mainPanel -> buttonPanel
        JPanel mainPanel = (JPanel) getContentPane().getComponent(0);
        JPanel buttonPanel = (JPanel) mainPanel.getComponent(1);
        for (Component c : buttonPanel.getComponents()) {
            if (c instanceof JButton btn && btn.getText().equals(symbol)) {
                btn.setBackground(OP_COLOR.darker());
                activeOpButton = btn;
                break;
            }
        }
    }

    private void clearActiveOpButton() {
        if (activeOpButton != null) {
            activeOpButton.setBackground(OP_COLOR);
            activeOpButton = null;
        }
    }

    // --- Keyboard Bindings ---

    private void setupKeyboardBindings() {
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        for (int i = 0; i <= 9; i++) {
            int digit = i;
            String key = "digit" + i;
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, 0), key);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0 + i, 0), key);
            actionMap.put(key, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    inputDigit(digit);
                }
            });
        }

        bindKey(inputMap, actionMap, KeyEvent.VK_ADD, "+", () -> inputOperator("+"));
        bindKey(inputMap, actionMap, KeyEvent.VK_SUBTRACT, "-", () -> inputOperator("-"));
        bindKey(inputMap, actionMap, KeyEvent.VK_MULTIPLY, "*", () -> inputOperator("*"));
        bindKey(inputMap, actionMap, KeyEvent.VK_DIVIDE, "/", () -> inputOperator("/"));
        bindKey(inputMap, actionMap, KeyEvent.VK_PLUS, "+_shift", () -> inputOperator("+"));
        bindKey(inputMap, actionMap, KeyEvent.VK_EQUALS, "equals_shift", () -> inputEquals());
        bindKey(inputMap, actionMap, KeyEvent.VK_ENTER, "enter", () -> inputEquals());
        bindKey(inputMap, actionMap, KeyEvent.VK_BACK_SPACE, "backspace", () -> backspace());
        bindKey(inputMap, actionMap, KeyEvent.VK_ESCAPE, "escape", () -> clearAll());
        bindKey(inputMap, actionMap, KeyEvent.VK_DELETE, "delete", () -> memoryClear());
        bindKey(inputMap, actionMap, KeyEvent.VK_PERIOD, "period", () -> inputDecimal());
        bindKey(inputMap, actionMap, KeyEvent.VK_DECIMAL, "numpad_period", () -> inputDecimal());
    }

    private void bindKey(InputMap inputMap, ActionMap actionMap, int vk, String name, Runnable action) {
        inputMap.put(KeyStroke.getKeyStroke(vk, 0), name);
        actionMap.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    private void backspace() {
        clearActiveOpButton();
        if (!displayString.equals("0") && displayString.length() > 1) {
            displayString = displayString.substring(0, displayString.length() - 1);
            if (displayString.equals("-")) displayString = "0";
        } else {
            displayString = "0";
        }
        engine.setCurrentValue(Double.parseDouble(displayString));
        updateDisplay();
    }
}
