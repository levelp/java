package com.demo;

import javax.swing.*;

/**
 * Основная форма игры
 */
public class MainWindow {
    private JButton button1;
    private JPanel mainPanel;
    private JTextField textField1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Игра Крестики-Нолики");
        frame.setContentPane(new MainWindow().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
