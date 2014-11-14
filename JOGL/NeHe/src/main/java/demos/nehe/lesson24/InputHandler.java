package demos.nehe.lesson24;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Scroll up");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Scroll down");
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:                                 // Is Up Arrow Being Pressed?
                renderer.setScroll(renderer.getScroll() - 12);  // If So, Decrease 'scroll' Moving Screen Down
                break;
            case KeyEvent.VK_DOWN:                               // Is Down Arrow Being Pressed?
                renderer.setScroll(renderer.getScroll() + 12); // If So, Increase 'scroll' Moving Screen Up
                break;
        }
    }
}
