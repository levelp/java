package demos.nehe.lesson13;

import demos.common.GLDisplay;

/**
 * @author Jeff Kirby
 */
public class Lesson13 {
    public static void main(String[] args) {
        GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Lesson 13: Bitmap fonts");
        Renderer renderer = new Renderer();
        neheGLDisplay.addGLEventListener(renderer);
        neheGLDisplay.start();
    }
}
