package demos.nehe.lesson30;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Zoom in");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Zoom out");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Rotate scene left");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Rotate scene right");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), "Increase speed");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), "Decrease speed");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "Track ball");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "Toggle sound");
    }

    public void keyPressed(KeyEvent e) {
        processKeyEvent(e, true);
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F3:
                renderer.toggleSounds();
                break;
            case KeyEvent.VK_F2:
                renderer.toggleCameraAttachedToBall();
                break;
            default:
                processKeyEvent(e, false);
        }
    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                renderer.zoomOut(pressed);
                break;
            case KeyEvent.VK_UP:
                renderer.zoomIn(pressed);
                break;
            case KeyEvent.VK_LEFT:
                renderer.increaseCameraRotation(pressed);
                break;
            case KeyEvent.VK_RIGHT:
                renderer.decreaseCameraRotation(pressed);
                break;
            case KeyEvent.VK_NUMPAD8:
                renderer.increaseTimeStep(pressed);
                break;
            case KeyEvent.VK_NUMPAD2:
                renderer.decreaseTimeStep(pressed);
                break;
        }
    }
}
