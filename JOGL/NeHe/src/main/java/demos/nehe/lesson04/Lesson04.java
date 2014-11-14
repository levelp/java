package demos.nehe.lesson04;

import demos.common.GLDisplay;

/**
 * @author Kevin J. Duling
 */
public class Lesson04 {
    public static void main(String[] args) {
        GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Lesson 04: Rotation");
        neheGLDisplay.addGLEventListener(new Renderer());
        neheGLDisplay.start();
    }
}
