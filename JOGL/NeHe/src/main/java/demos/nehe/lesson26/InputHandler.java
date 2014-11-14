package demos.nehe.lesson26;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "Move ball up");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "Move ball down");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "Zoom in");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "Zoom out");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Decrease X-axis rotation speed");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Increase X-axis rotation speed");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Decrease Y-axis rotation speed");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Increase Y-axis rotation speed");
    }

    public void keyPressed(KeyEvent e) {
        processKeyEvent(e, true);
    }

    public void keyReleased(KeyEvent e) {
        processKeyEvent(e, false);
    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                renderer.zoomIn(pressed);
                break;
            case KeyEvent.VK_Z:
                renderer.zoomOut(pressed);
                break;
            case KeyEvent.VK_PAGE_UP:
                renderer.increaseHeight(pressed);
                break;
            case KeyEvent.VK_PAGE_DOWN:
                renderer.decreaseHeight(pressed);
                break;
            case KeyEvent.VK_UP:
                renderer.decreaseXspeed(pressed);
                break;
            case KeyEvent.VK_DOWN:
                renderer.increaseXspeed(pressed);
                break;
            case KeyEvent.VK_RIGHT:
                renderer.increaseYspeed(pressed);
                break;
            case KeyEvent.VK_LEFT:
                renderer.decreaseYspeed(pressed);
                break;
        }
    }
}
