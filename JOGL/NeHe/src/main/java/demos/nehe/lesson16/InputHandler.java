package demos.nehe.lesson16;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay display) {
        this.renderer = renderer;
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "Switch texture filter");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0), "Switch fog mode");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "Toggle lighting");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Increase X rotation speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Decrease X rotation speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Decrease Y rotation speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Increase Y rotation speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "Zoom in");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "Zoom out");
    }

    public void keyPressed(KeyEvent e) {
        processKeyEvent(e, true);
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F:
                renderer.switchFilter();
                break;
            case KeyEvent.VK_G:
                renderer.switchFogMode();
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
            case KeyEvent.VK_UP:
                renderer.rotateFasterX(pressed);
                break;
            case KeyEvent.VK_DOWN:
                renderer.rotateSlowerX(pressed);
                break;
            case KeyEvent.VK_LEFT:
                renderer.rotateSlowerY(pressed);
                break;
            case KeyEvent.VK_RIGHT:
                renderer.rotateFasterY(pressed);
                break;
            case KeyEvent.VK_PAGE_UP:
                renderer.zoomIn(pressed);
                break;
            case KeyEvent.VK_PAGE_DOWN:
                renderer.zoomOut(pressed);
                break;
        }
    }
}
