package com.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CalculatorFrame extends JFrame {

    private final CalculatorEngine engine;
    private final MemoryManager memory;
    private final DisplayPanel display;
    private String displayString = "0";
    private boolean hasDecimal = false;
    private JButton activeOpButton = null;
    private JLabel memoryIndicator;

    private static final Color BG_COLOR = new Color(30, 30, 30);
    private static final Color NUM_COLOR = new Color(70, 70, 70);
    private static final Color NUM_HOVER = new Color(90, 90, 90);
    private static final Color OP_COLOR = new Color(255, 149, 0);
    private static final Color OP_HOVER = new Color(255, 170, 50);
    private static final Color OP_ACTIVE = new Color(200, 110, 0);
    private static final Color MEM_COLOR = new Color(40, 40, 40);
    private static final Color MEM_HOVER = new Color(55, 55, 55);
    private static final Color TEXT_WHITE = Color.WHITE;

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

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_COLOR);
        topPanel.add(display, BorderLayout.CENTER);

        memoryIndicator = new JLabel(" ");
        memoryIndicator.setFont(new Font("SansSerif", Font.PLAIN, 10));
        memoryIndicator.setForeground(new Color(150, 150, 150));
        memoryIndicator.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        topPanel.add(memoryIndicator, BorderLayout.NORTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(createButtonPanel(), BorderLayout.CENTER);

        add(mainPanel);
        pack();
        setSize(320, 520);
        setLocationRelativeTo(null);

        setupKeyboardBindings();
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 4, 4, 4));
        panel.setBackground(BG_COLOR);

        // Row 1: Memory
        panel.add(createMemButton("MC", e -> memoryClear()));
        panel.add(createMemButton("MR", e -> memoryRecall()));
        panel.add(createMemButton("M+", e -> memoryAdd()));
        panel.add(createMemButton("M-", e -> memorySubtract()));

        // Row 2: Clear row
        panel.add(createMemButton("MS", e -> memoryStore()));
        panel.add(createColorButton("C", OP_COLOR, OP_HOVER, e -> clearAll()));
        panel.add(createColorButton("±", OP_COLOR, OP_HOVER, e -> negate()));
        panel.add(createColorButton("%", OP_COLOR, OP_HOVER, e -> percent()));

        // Row 3
        panel.add(createNumButton("7", e -> inputDigit(7)));
        panel.add(createNumButton("8", e -> inputDigit(8)));
        panel.add(createNumButton("9", e -> inputDigit(9)));
        panel.add(createOpButton("÷", e -> inputOperator("/")));

        // Row 4
        panel.add(createNumButton("4", e -> inputDigit(4)));
        panel.add(createNumButton("5", e -> inputDigit(5)));
        panel.add(createNumButton("6", e -> inputDigit(6)));
        panel.add(createOpButton("×", e -> inputOperator("*")));

        // Row 5
        panel.add(createNumButton("1", e -> inputDigit(1)));
        panel.add(createNumButton("2", e -> inputDigit(2)));
        panel.add(createNumButton("3", e -> inputDigit(3)));
        panel.add(createOpButton("-", e -> inputOperator("-")));

        // Row 6: 0 spans 2 columns
        panel.add(createNumButton("0", e -> inputDigit(0)));
        panel.add(createNumButton(".", e -> inputDecimal()));
        panel.add(createOpButton("=", e -> inputEquals()));
        panel.add(createOpButton("+", e -> inputOperator("+")));

        return panel;
    }

    // --- Button Factories ---

    private JButton createNumButton(String text, java.util.function.Consumer<ActionEvent> action) {
        return createStyledButton(text, NUM_COLOR, NUM_HOVER, action);
    }

    private JButton createOpButton(String text, java.util.function.Consumer<ActionEvent> action) {
        JButton btn = createStyledButton(text, OP_COLOR, OP_HOVER, action);
        btn.setFont(new Font("SansSerif", Font.BOLD, 22));
        return btn;
    }

    private JButton createMemButton(String text, java.util.function.Consumer<ActionEvent> action) {
        return createStyledButton(text, MEM_COLOR, MEM_HOVER, action);
    }

    private JButton createColorButton(String text, Color bg, Color hover, java.util.function.Consumer<ActionEvent> action) {
        return createStyledButton(text, bg, hover, action);
    }

    private JButton createStyledButton(String text, Color bg, Color hover, java.util.function.Consumer<ActionEvent> action) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(bg);
        button.setForeground(TEXT_WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> action.accept(e));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != activeOpButton) {
                    button.setBackground(hover);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != activeOpButton) {
                    button.setBackground(bg);
                }
            }
        });

        return button;
    }

    // --- Calculator Actions ---

    private void inputDigit(int digit) {
        clearActiveOpButton();
        engine.inputDigit(digit);
        if (displayString.equals("0") || displayString.equals("-0")) {
            displayString = String.valueOf(digit);
        } else if (displayString.equals("Cannot divide by zero")) {
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
            if (engine.isNewInput() || displayString.equals("Cannot divide by zero")) {
                displayString = "0.";
                engine.setCurrentValue(0);
            } else {
                displayString += ".";
            }
            hasDecimal = true;
            engine.inputDecimal();
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
        hasDecimal = displayString.contains(".");
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
        hasDecimal = displayString.contains(".");
        updateDisplay();
    }

    // --- Memory Actions ---

    private void memoryClear() {
        memory.memoryClear();
        updateMemoryIndicator();
    }

    private void memoryRecall() {
        if (memory.hasMemory()) {
            engine.clear();
            engine.setCurrentValue(memory.getMemoryValue());
            displayString = formatValue(memory.getMemoryValue());
            hasDecimal = displayString.contains(".");
            updateDisplay();
        }
    }

    private void memoryAdd() {
        memory.memoryAdd(engine.getCurrentValue());
        updateMemoryIndicator();
    }

    private void memorySubtract() {
        memory.memorySubtract(engine.getCurrentValue());
        updateMemoryIndicator();
    }

    private void memoryStore() {
        memory.memoryStore(engine.getCurrentValue());
        updateMemoryIndicator();
    }

    private void updateMemoryIndicator() {
        memoryIndicator.setText(memory.hasMemory() ? "M" : " ");
    }

    // --- Display ---

    private void updateDisplay() {
        display.setText(displayString);
    }

    private String formatValue(double value) {
        if (Double.isInfinite(value)) {
            return "Cannot divide by zero";
        }
        if (Double.isNaN(value)) {
            return "Error";
        }

        if (value == (long) value && Math.abs(value) < 1e15) {
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
        JPanel mainPanel = (JPanel) getContentPane().getComponent(0);
        JPanel topPanel = (JPanel) mainPanel.getComponent(0);
        JPanel buttonPanel = (JPanel) mainPanel.getComponent(1);
        for (Component c : buttonPanel.getComponents()) {
            if (c instanceof JButton btn && btn.getText().equals(symbol)) {
                btn.setBackground(OP_ACTIVE);
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

        bindKey(inputMap, actionMap, KeyEvent.VK_ADD, "numpad_plus", () -> inputOperator("+"));
        bindKey(inputMap, actionMap, KeyEvent.VK_SUBTRACT, "numpad_minus", () -> inputOperator("-"));
        bindKey(inputMap, actionMap, KeyEvent.VK_MULTIPLY, "numpad_mul", () -> inputOperator("*"));
        bindKey(inputMap, actionMap, KeyEvent.VK_DIVIDE, "numpad_div", () -> inputOperator("/"));
        bindKey(inputMap, actionMap, KeyEvent.VK_PLUS, "plus", () -> inputOperator("+"));
        bindKey(inputMap, actionMap, KeyEvent.VK_EQUALS, "equals", () -> inputEquals());
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
        if (displayString.equals("Cannot divide by zero") || displayString.equals("Error")) {
            clearAll();
            return;
        }
        engine.backspace();
        displayString = formatValue(engine.getCurrentValue());
        hasDecimal = displayString.contains(".");
        updateDisplay();
    }
}
