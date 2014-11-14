package demos.nehe.lesson12;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Decrease X-axis rotation");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Increase X-axis rotation");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Decrease Y-axis rotation");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Increase Y-axis rotation");
    }

    public void keyPressed(KeyEvent e) {
        processKeyEvent(e, true);
    }

    public void keyReleased(KeyEvent e) {
        processKeyEvent(e, false);
    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                renderer.decreaseXrot(pressed);
                break;
            case KeyEvent.VK_DOWN:
                renderer.increaseXrot(pressed);
                break;
            case KeyEvent.VK_LEFT:
                renderer.decreaseYrot(pressed);
                break;
            case KeyEvent.VK_RIGHT:
                renderer.increaseYrot(pressed);
                break;
        }
    }
}
