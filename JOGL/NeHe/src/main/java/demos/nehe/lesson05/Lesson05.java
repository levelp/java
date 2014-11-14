package demos.nehe.lesson05;

import demos.common.GLDisplay;

/**
 * @author Kevin J. Duling
 */
public class Lesson05 {
    public static void main(String[] args) {
        GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Lesson 05: 3D Shapes");
        neheGLDisplay.addGLEventListener(new Renderer());
        neheGLDisplay.start();
    }
}
