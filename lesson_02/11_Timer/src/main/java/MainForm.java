import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Timer;

/**
 * Основная форма приложения
 */
public class MainForm {
    public static final int DELAY = 10;
    public static MainForm mainForm;
    public boolean timeGo = false;
    private JPanel mainPanel;
    private JTextField currentTime;
    private JButton сбросButton;
    private JButton стартButton;
    private JButton стопButton;
    /**
     * Дата и время начала
     */
    private Date startDate;

    public MainForm() {
        стартButton.addActionListener(new ActionListener() {
            /**
             * При нажатии на кнопку Старт
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:");
                timeGo = true;

                startDate = new Date();
                System.out.println("startDate: " + startDate.toString());

                Timer timer = new Timer();
                timer.schedule(new MyTimerTask(this), DELAY);
            }
        });
        сбросButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startDate = new Date();
                updateTime();
            }
        });
        стопButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeGo = false;
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Таймер");
        mainForm = new MainForm();
        frame.setContentPane(mainForm.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    public void updateTime() {
        Date now = new Date();
        // Сколько секунд прошло
        long timeDiff = now.getTime() - startDate.getTime();
        long inSeconds = timeDiff / 1000;
        //System.out.println("inSecond: " + inSeconds);
        long sec = inSeconds % 60;
        long min = inSeconds / 60;

        currentTime.setText(String.format("%02d:%02d.%02d", min, sec, (timeDiff % 1000) / 10));
    }
}
