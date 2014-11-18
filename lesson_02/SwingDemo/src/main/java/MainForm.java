import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Основная форма
 */
public class MainForm {
    private JPanel panel1;
    private JButton sumButton;
    private JTextField aField;
    private JTextField bField;
    private JTextField resultField;

    public MainForm() {
        sumButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Нажали кнопку Сумма");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
