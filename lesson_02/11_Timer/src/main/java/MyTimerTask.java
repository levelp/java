import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Обновление таймера
 */
public class MyTimerTask extends TimerTask {
    private final ActionListener actionListener;

    public MyTimerTask(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public void run() {
        if (MainForm.mainForm.timeGo) {
            Timer timer = new Timer();
            timer.schedule(new MyTimerTask(actionListener), MainForm.DELAY);
        }
        MainForm.mainForm.updateTime();
    }
}
