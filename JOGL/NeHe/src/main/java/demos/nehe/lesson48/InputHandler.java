package demos.nehe.lesson48;

import demos.common.GLDisplay;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class InputHandler extends MouseInputAdapter {
    private Renderer renderer;

    public InputHandler(Renderer renderer, GLDisplay glDisplay) {
        this.renderer = renderer;
        glDisplay.registerMouseEventForHelp(
                MouseEvent.MOUSE_CLICKED, MouseEvent.BUTTON1_DOWN_MASK,
                "Toggle display mode"
        );
    }

    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            renderer.reset();
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
            renderer.startDrag(mouseEvent.getPoint());
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
            renderer.drag(mouseEvent.getPoint());
        }
    }
}
