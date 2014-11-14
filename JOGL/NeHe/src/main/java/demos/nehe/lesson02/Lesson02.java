package demos.nehe.lesson02;

import demos.common.GLDisplay;

/**
 * @author Kevin J. Duling
 */
public class Lesson02 {
    public static void main(String[] args) {
        GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Lesson 02: Your First Polygon");
        neheGLDisplay.addGLEventListener(new Renderer());
        neheGLDisplay.start();
    }
}
