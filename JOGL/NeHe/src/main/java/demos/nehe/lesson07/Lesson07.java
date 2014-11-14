package demos.nehe.lesson07;

import demos.common.GLDisplay;

/**
 * @author Kevin J. Duling
 */
public class Lesson07 {
    public static void main(String[] args) {
        GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Lesson 7: Texture Filters, Lighting & Keyboard Control");
        Renderer renderer = new Renderer();
        InputHandler inputHandler = new InputHandler(renderer, neheGLDisplay);
        neheGLDisplay.addGLEventListener(renderer);
        neheGLDisplay.addKeyListener(inputHandler);
        neheGLDisplay.start();
    }
}
