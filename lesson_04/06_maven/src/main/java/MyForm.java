import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

/**
 *
 */
public class MyForm {
    static Logger log = org.apache.logging.log4j.LogManager.getLogger(MyForm.class);

    private JPanel mainPanel;

    public static void main(String[] args) {
        log.info("Форма запускается");
        JFrame frame = new JFrame("MyForm");
        log.debug(frame.getName());
        frame.setContentPane(new MyForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
