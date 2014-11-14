package demos.nehe.lesson21;

import demos.common.GLDisplay;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay display) {
        this.renderer = renderer;
//        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_, 0), "");
    }

    public void keyPressed(KeyEvent e) {
        // Set flags
        processKeyEvent(e, true);
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            // Toggle properties
            case KeyEvent.VK_SPACE:
                renderer.resetGame();
                break;
            default:
                // Unset flags
                processKeyEvent(e, false);
        }
    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                renderer.moveUp(pressed);
                break;
            case KeyEvent.VK_DOWN:
                renderer.moveDown(pressed);
                break;
            case KeyEvent.VK_LEFT:
                renderer.moveLeft(pressed);
                break;
            case KeyEvent.VK_RIGHT:
                renderer.moveRight(pressed);
                break;
        }
    }
}
