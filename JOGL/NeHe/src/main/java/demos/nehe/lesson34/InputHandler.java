package demos.nehe.lesson34;

import demos.common.GLDisplay;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class InputHandler extends KeyAdapter implements MouseListener {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Zoom in");
        glDisplay.registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Zoom out");
        glDisplay.registerMouseEventForHelp(
                MouseEvent.MOUSE_CLICKED, MouseEvent.BUTTON1_DOWN_MASK,
                "Toggle display mode"
        );
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
                renderer.zoomIn(pressed);
                break;
            case KeyEvent.VK_DOWN:
                renderer.zoomOut(pressed);
                break;
        }
    }

    public void mouseClicked(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            RenderMode renderMode = renderer.getRenderMode();
            if (renderMode == RenderMode.QUADS)
                renderer.setRenderMode(RenderMode.LINES);
            else
                renderer.setRenderMode(RenderMode.QUADS);
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
