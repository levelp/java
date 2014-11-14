package demos.nehe.lesson25;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputHandler extends KeyAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay display) {
        this.renderer = renderer;
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "Morph to object 1");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "Morph to object 2");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "Morph to object 3");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0), "Morph to object 4");

        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Increase X speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Decrease X speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Increase Y speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Decrease Y speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "Increase Z speed");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "Decrease Z speed");

        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "Object farther");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "Object closer");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "Object up");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "Object down");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "Object left");
        display.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "Object right");
    }

    public void keyPressed(KeyEvent e) {
        processKeyEvent(e, true);
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_1:                 			// Is 1 Pressed, key Not Equal To 1 And Morph False?
                renderer.morphTo(1);					    // Sets key To 1 (To Prevent Pressing 1 2x In A Row)
                break;
            case KeyEvent.VK_2:	                    		// Is 2 Pressed, key Not Equal To 2 And Morph False?
                renderer.morphTo(2);					    // Sets key To 2 (To Prevent Pressing 2 2x In A Row)
                break;
            case KeyEvent.VK_3:			                    // Is 3 Pressed, key Not Equal To 3 And Morph False?
                renderer.morphTo(3);					    // Sets key To 3 (To Prevent Pressing 3 2x In A Row)
                break;
            case KeyEvent.VK_4:			                    // Is 4 Pressed, key Not Equal To 4 And Morph False?
                renderer.morphTo(4);					    // Sets key To 4 (To Prevent Pressing 4 2x In A Row)
                break;
            default:
                processKeyEvent(e, false);
        }
    }

    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_PAGE_UP:								// Is Page Up Being Pressed?
                renderer.increaseZspeed(pressed);								// Increase zspeed
                break;
            case KeyEvent.VK_PAGE_DOWN:								// Is Page Down Being Pressed?
                renderer.decreaseZspeed(pressed);								// Decrease zspeed
                break;
            case KeyEvent.VK_DOWN:								// Is Page Up Being Pressed?
                renderer.increaseXspeed(pressed);								// Increase xspeed
                break;
            case KeyEvent.VK_UP:									// Is Page Up Being Pressed?
                renderer.decreaseXspeed(pressed);								// Decrease xspeed
                break;
            case KeyEvent.VK_RIGHT:								// Is Page Up Being Pressed?
                renderer.increaseYspeed(pressed);								// Increase yspeed
                break;
            case KeyEvent.VK_LEFT:								// Is Page Up Being Pressed?
                renderer.decreaseYspeed(pressed);								// Decrease yspeed
                break;
            case KeyEvent.VK_Q:									// Is Q Key Being Pressed?
                renderer.moveObjectFarther(pressed);										// Move Object Away From Viewer
                break;
            case KeyEvent.VK_Z:									// Is Z Key Being Pressed?
                renderer.moveObjectCloser(pressed);										// Move Object Towards Viewer
                break;
            case KeyEvent.VK_W:									// Is W Key Being Pressed?
                renderer.moveObjectUp(pressed);										// Move Object Up
                break;
            case KeyEvent.VK_S:									// Is S Key Being Pressed?
                renderer.moveObjectDown(pressed);										// Move Object Down
                break;
            case KeyEvent.VK_D:									// Is D Key Being Pressed?
                renderer.moveObjectRight(pressed);										// Move Object Right
                break;
            case KeyEvent.VK_A:									// Is A Key Being Pressed?
                renderer.moveObjectLeft(pressed);										// Move Object Left
                break;
        }
    }
}
