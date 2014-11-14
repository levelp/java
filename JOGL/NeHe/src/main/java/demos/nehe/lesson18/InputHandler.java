package demos.nehe.lesson18;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "Switch texture fiulter");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "Switch object");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "Toggle lighting");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "Zoom in");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "Zoom out");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Decrease X-axis rotation speed");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Increase X-axis rotation speed");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Decrease Y-axis rotation speed");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Increase Y-axis rotation speed");
    }

    public void keyPressed(KeyEvent e) {
        processKeyEvent(e, true);
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F:
                renderer.switchFilter();
                break;
            case KeyEvent.VK_SPACE:
                renderer.switchObject();
                break;
            case KeyEvent.VK_L:
                renderer.toggleLighting();
                break;
            default:
                processKeyEvent(e, false);
        }
    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_PAGE_UP:
                renderer.zoomIn(pressed);
                break;
            case KeyEvent.VK_PAGE_DOWN:
                renderer.zoomOut(pressed);
                break;
            case KeyEvent.VK_UP:
                renderer.increaseXspeed(pressed);
                break;
            case KeyEvent.VK_DOWN:
                renderer.decreaseXspeed(pressed);
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
