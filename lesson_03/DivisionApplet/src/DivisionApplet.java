import java.applet.Applet;
import java.awt.*;

/**
 * Пример апплета
 */
public class DivisionApplet extends Applet {

    TextField textField1, textField2;
    String answerStr;

    public void init() {
        textField1 = new TextField(20);
        textField1.setText("10");
        add(textField1);
        textField2 = new TextField(20);
        textField2.setText("3");
        add(textField2);
        action(null, null);
        answerStr = "";
    }

    public boolean action(Event evt, Object arg) {
        String str1 = textField1.getText();
        String str2 = textField2.getText();
        double int1 = Double.parseDouble(str1);
        double int2 = Double.parseDouble(str2);
        double answer = int1 / int2;
        answerStr = str1 + "/" + str2 + "=" + String.valueOf(answer);
        repaint();
        return false;
    }

    public void paint(Graphics g) {
        Font font = new Font("Times New Roman", Font.PLAIN, 24);
        g.setFont(font);
        g.drawString(answerStr, 50, 100);
    }

}
