package com.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebCalculatorApp {

    private static boolean guiMode = false;

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--gui")) {
            guiMode = true;
            javax.swing.SwingUtilities.invokeLater(() -> {
                CalculatorFrame frame = new CalculatorFrame();
                frame.setVisible(true);
            });
        } else {
            SpringApplication.run(WebCalculatorApp.class, args);
        }
    }
}
