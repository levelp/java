package demos.nehe.lesson27;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Decrease X-axis rotation speed");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Increase X-axis rotation speed");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Decrease Y-axis rotation speed");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Increase Y-axis rotation speed");

        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "Move light right");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0), "Move light left");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "Move light up");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0), "Move light down");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_O, 0), "Move light forward");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0), "Move light backward");

        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "Move ball right");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "Move ball left");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "Move ball up");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "Move ball down");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "Move ball forward");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "Move ball backward");

        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), "Move cross right");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), "Move cross left");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), "Move cross up");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0), "Move cross down");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD9, 0), "Move cross forward");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, 0), "Move cross backward");
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
                // Adjust Light's Position
            case KeyEvent.VK_L:
                renderer.translateLightRight(pressed);      // 'L' Moves Light Right
                break;
            case KeyEvent.VK_J:
                renderer.translateLightLeft(pressed);      // 'J' Moves Light Left
                break;

            case KeyEvent.VK_I:
                renderer.translateLightUp(pressed);      // 'I' Moves Light Up
                break;
            case KeyEvent.VK_K:
                renderer.translateLightDown(pressed);       // 'K' Moves Light Down
                break;

            case KeyEvent.VK_O:
                renderer.translateLightForward(pressed);       // 'O' Moves Light Toward Viewer
                break;
            case KeyEvent.VK_U:
                renderer.translateLightBackward(pressed);      // 'U' Moves Light Away From Viewer
                break;

                // Adjust Object's Position
            case KeyEvent.VK_NUMPAD6:
                renderer.translateObjectRight(pressed);  // 'Numpad6' Move Object Right
                break;
            case KeyEvent.VK_NUMPAD4:
                renderer.translateObjectLeft(pressed);  // 'Numpad4' Move Object Left
                break;

            case KeyEvent.VK_NUMPAD8:
                renderer.translateObjectUp(pressed);  // 'Numpad8' Move Object Up
                break;
            case KeyEvent.VK_NUMPAD5:
                renderer.translateObjectDown(pressed);  // 'Numpad5' Move Object Down
                break;

            case KeyEvent.VK_NUMPAD9:
                renderer.translateObjectForward(pressed);  // 'Numpad9' Move Object Toward Viewer
                break;
            case KeyEvent.VK_NUMPAD7:
                renderer.translateObjectBackward(pressed);  // 'Numpad7' Move Object Away From Viewer
                break;

                // Adjust Ball's Position
            case KeyEvent.VK_D:
                renderer.translateSphereRight(pressed);  // 'D' Move Ball Right
                break;
            case KeyEvent.VK_A:
                renderer.translateSphereLeft(pressed);  // 'A' Move Ball Left
                break;

            case KeyEvent.VK_W:
                renderer.translateSphereUp(pressed);  // 'W' Move Ball Up
                break;
            case KeyEvent.VK_S:
                renderer.translateSphereDown(pressed);  // 'S' Move Ball Down
                break;

            case KeyEvent.VK_E:
                renderer.translateSphereForward(pressed);  // 'E' Move Ball Toward Viewer
                break;
            case KeyEvent.VK_Q:
                renderer.translateSphereBackward(pressed);  // 'Q' Move Ball Away From Viewer
                break;
        }
    }
}
