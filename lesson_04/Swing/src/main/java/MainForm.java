import javax.swing.*;

/**
 * Created by Денис on 29.03.14.
 */
public class MainForm {
    private JButton enter;
    private JTextField textField1;
    private JPasswordField passwordField1;
    public JPanel mainPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // Получаем из интерфейса логин
    //String login = textField1.getText();

    // Получаем из интерфейса пароль
    //char[] password = passwordField1.getPassword();
}
