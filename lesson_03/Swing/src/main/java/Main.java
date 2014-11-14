import javax.swing.*;

/**
 * Запуск приложения
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Моё приложение");
        MainForm mainForm = new MainForm();
        frame.setContentPane(mainForm.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
