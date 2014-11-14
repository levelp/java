package demos.nehe.lesson28;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay display) {
        this.renderer = renderer;
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Increase # subdivisions");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Decrease # subdivisions");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "Toggle control point display");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Rotate left");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Rotate right");
    }

    public void keyPressed(KeyEvent e) {
        // Set flags
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                renderer.increaseDivisions();
                break;
            case KeyEvent.VK_DOWN:
                renderer.decreaseDivisions();
                break;
            case KeyEvent.VK_SPACE:
                renderer.toggleControlPoints();
                break;
            default:
                processKeyEvent(e, true);
        }

    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            default:
                // Unset flags
                processKeyEvent(e, false);
        }
    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                renderer.rotateLeft(pressed);
                break;
            case KeyEvent.VK_RIGHT:
                renderer.rotateRight(pressed);
                break;
        }
    }
}
