package com.calculator;

import javax.swing.*;
import java.awt.*;

public class DisplayPanel extends JPanel {

    private final JTextField displayField;

    public DisplayPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));

        displayField = new JTextField("0");
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setFont(new Font("SansSerif", Font.BOLD, 36));
        displayField.setBackground(new Color(30, 30, 30));
        displayField.setForeground(Color.WHITE);
        displayField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        displayField.setCaretColor(Color.WHITE);

        add(displayField, BorderLayout.CENTER);
    }

    public void setText(String text) {
        displayField.setText(text);
    }

    public String getText() {
        return displayField.getText();
    }

    public JTextField getField() {
        return displayField;
    }
}
