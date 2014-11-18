import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Основная форма
 */
public class MainForm {
    private static JFrame frame;

    private JPanel panel1;
    private JButton sumButton;
    private JTextField aField;
    private JTextField bField;
    private JTextField resultField;

    public MainForm() {
        resultField.setVisible(false);
        sumButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Нажали кнопку Сумма");
                // Получаю значения из интерфейса
                double a = Double.parseDouble(aField.getText());
                double b = Double.parseDouble(bField.getText());
                // Сумма чисел
                double sum = a + b;
                // Отправляю результат в интерфейс
                resultField.setText(Double.toString(sum));

                resultField.setVisible(true);

                frame.pack();
            }
        });
    }

    public static void main(String[] args) {
        frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
