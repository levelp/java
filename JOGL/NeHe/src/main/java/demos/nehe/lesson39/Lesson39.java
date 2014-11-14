package demos.nehe.lesson39;

import demos.common.GLDisplay;

/**
 * @author Pepijn Van Eeckhoudt
 */
public class Lesson39 {
    public static void main(String[] args) {
        GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Lesson 39: Physical simulation engine");
        Renderer renderer = new Renderer();
        InputHandler inputHandler = new InputHandler(renderer, neheGLDisplay);
        neheGLDisplay.addGLEventListener(renderer);
        neheGLDisplay.addKeyListener(inputHandler);
        neheGLDisplay.start();
    }
}
