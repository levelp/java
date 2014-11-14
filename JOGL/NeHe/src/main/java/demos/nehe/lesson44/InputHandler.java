package demos.nehe.lesson44;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay display) {
        this.renderer = renderer;
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "Show info");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "Hide info");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "Pitch camera up");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "Pitch camera down");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "Yaw camera right");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "Yaw camera left");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "Move camera forward");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "Move camera backward");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "Stop camera movement");
    }

    public void keyPressed(KeyEvent e) {
        // Set flags
        processKeyEvent(e, true);
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            // Toggle properties
            case KeyEvent.VK_1:
                renderer.setInfoDisplayed(true);
                break;
            case KeyEvent.VK_2:
                renderer.setInfoDisplayed(false);
                break;
            case KeyEvent.VK_Q:
                renderer.moveCameraForward();
                break;
            case KeyEvent.VK_E:
                renderer.moveCameraBackward();
                break;
            case KeyEvent.VK_X:
                renderer.stopCamera();
                break;
            default:
                // Unset flags
                processKeyEvent(e, false);
        }
    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                renderer.pitchCameraUp(pressed);
                break;
            case KeyEvent.VK_S:
                renderer.pitchCameraDown(pressed);
                break;
            case KeyEvent.VK_A:
                renderer.yawCameraLeft(pressed);
                break;
            case KeyEvent.VK_D:
                renderer.yawCameraRight(pressed);
                break;
        }
    }
}
