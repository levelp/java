package demos.nehe.lesson06;

import demos.common.GLDisplay;

/**
 * @author Kevin J. Duling
 */
public class Lesson06 {
    public static void main(String[] args) {
        GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Lesson 06: Texture mapping");
        neheGLDisplay.addGLEventListener(new Renderer());
        neheGLDisplay.start();
    }
}
