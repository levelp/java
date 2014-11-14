package demos.nehe.lesson10;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay display) {
        this.renderer = renderer;
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "Toggle blending");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "Switch texture filter");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Move forward");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Move backward");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Turn left");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Turn right");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "Look up");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "Look down");
    }

    public void keyPressed(KeyEvent e) {
        processKeyEvent(e, true);
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_B:
                renderer.toggleBlending();
                break;
            case KeyEvent.VK_F:
                renderer.switchFilter();
                break;
            default:
                processKeyEvent(e, false);
        }
    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                renderer.stepForward(pressed);
                break;
            case KeyEvent.VK_DOWN:
                renderer.stepBackward(pressed);
                break;
            case KeyEvent.VK_LEFT:
                renderer.turnLeft(pressed);
                break;
            case KeyEvent.VK_RIGHT:
                renderer.turnRight(pressed);
                break;
            case KeyEvent.VK_PAGE_UP:
                renderer.lookUp(pressed);
                break;
            case KeyEvent.VK_PAGE_DOWN:
                renderer.lookDown(pressed);
                break;
        }
    }
}
