package demos.nehe.lesson15;

import demos.common.GLDisplay;

/**
 * @author Jeff Kirby
 */
public class Lesson15 {
    public static void main(String[] args) {
        GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Lesson 15: Texture mapped outline fonts");
        Renderer renderer = new Renderer();
        neheGLDisplay.addGLEventListener(renderer);
        neheGLDisplay.start();
    }
}
