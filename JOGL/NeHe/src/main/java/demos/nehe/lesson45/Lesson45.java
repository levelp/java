package demos.nehe.lesson45;

import demos.common.GLDisplay;

public class Lesson45 {
    public static void main(String[] args) {
        GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Lesson 45: Vertex buffer objects");
        Renderer renderer = new Renderer(neheGLDisplay);
        neheGLDisplay.addGLEventListener(renderer);
        neheGLDisplay.start();
    }
}
