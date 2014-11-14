package demos.nehe.lesson47;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay display) {
        this.renderer = renderer;
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "Toggle shader");
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            // Toggle properties
            case KeyEvent.VK_SPACE:
                renderer.toggleShader();
                break;
        }
    }
}
