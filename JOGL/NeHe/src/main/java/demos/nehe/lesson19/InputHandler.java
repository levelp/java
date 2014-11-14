package demos.nehe.lesson19;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay display) {
        this.renderer = renderer;
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), "Burst");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Toggle color cycling");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "Cycle color manually");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "Speed up");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "Slow down");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), "Increase pull upwards");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), "Increase pull downwards");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), "Increase pull right");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), "Increase pull left");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Increase X particle launch speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Decrease X particle launch speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Increase Y particle launch speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Decrease Y particle launch speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "Increase particle grow speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "Decrease particle grow speed");
    }

    public void keyPressed(KeyEvent e) {
        processKeyEvent(e, true);
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_T:
                renderer.createBurst();
                break;
            case KeyEvent.VK_ENTER:
                renderer.toggleRainbowEffect();
                break;
            case KeyEvent.VK_SPACE:
                renderer.cycleColor();
                break;
            default:
                processKeyEvent(e, false);
        }
    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ADD:
                renderer.speedUp(pressed);
                break;
            case KeyEvent.VK_SUBTRACT:
                renderer.slowDown(pressed);
                break;
            case KeyEvent.VK_NUMPAD8:
                renderer.increasePullUpwards(pressed);
                break;
            case KeyEvent.VK_NUMPAD2:
                renderer.increasePullDownwards(pressed);
                break;
            case KeyEvent.VK_NUMPAD4:
                renderer.increasePullLeft(pressed);
                break;
            case KeyEvent.VK_NUMPAD6:
                renderer.increasePullRight(pressed);
                break;
            case KeyEvent.VK_UP:
                renderer.increaseYSpeed(pressed);
                break;
            case KeyEvent.VK_DOWN:
                renderer.decreaseYSpeed(pressed);
                break;
            case KeyEvent.VK_LEFT:
                renderer.decreaseXSpeed(pressed);
                break;
            case KeyEvent.VK_RIGHT:
                renderer.increaseXSpeed(pressed);
                break;
            case KeyEvent.VK_PAGE_UP:
                renderer.increaseParticleGrowSpeed(pressed);
                break;
            case KeyEvent.VK_PAGE_DOWN:
                renderer.decreaseParticleGrowSpeed(pressed);
                break;
        }
    }
}
