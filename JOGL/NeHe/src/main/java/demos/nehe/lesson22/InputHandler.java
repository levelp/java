package demos.nehe.lesson22;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "Switch texture filter");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "Toggle multitexturing");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "Toggle bumpmapping");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "Toggle embossed");
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
            case KeyEvent.VK_M:
                renderer.toggleMultitexture();
                break;
            case KeyEvent.VK_B:
                renderer.toggleBumps();
                break;
            case KeyEvent.VK_E:
                renderer.toggleEmboss();
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
