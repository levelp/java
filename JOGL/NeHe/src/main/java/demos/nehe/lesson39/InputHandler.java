package demos.nehe.lesson39;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "Normal speed");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "Slow motion");
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F2:                         // Is the F2 Being Pressed?
                renderer.setSlowMotionRatio(1.0f);                       // Set slowMotionRatio To 1.0f (Normal Motion)
                break;

            case KeyEvent.VK_F3:                             // Is The F3 Being Pressed?
                renderer.setSlowMotionRatio(10.0f);                       // Set slowMotionRatio To 10.0f (Very Slow Motion)
                break;
        }
    }
}
