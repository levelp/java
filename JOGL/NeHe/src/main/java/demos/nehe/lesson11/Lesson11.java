package demos.nehe.lesson11;

import demos.common.GLDisplay;

/**
 * @author Pepijn Van Eeckhoudt
 */
public class Lesson11 {
    public static void main(String[] args) {
        final GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Lesson 11: Flag effect");
        Renderer renderer = new Renderer();
        neheGLDisplay.addGLEventListener(renderer);
        neheGLDisplay.start();
    }
}
