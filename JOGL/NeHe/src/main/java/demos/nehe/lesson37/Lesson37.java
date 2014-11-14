package demos.nehe.lesson37;

/*--.          .-"-.
/   o_O        / O o \
\_  (__\       \_ v _/
//   \\        //   \\
((     ))      ((     ))
¤¤¤¤¤¤¤¤¤¤¤¤¤¤--""---""--¤¤¤¤--""---""--¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤
¤                 |||            |||                             ¤
¤                  |              |                              ¤
¤                                                                ¤
¤ Programmer:Abdul Bezrati                                       ¤
¤ Program   :Nehe's 37th lesson port to JOGL                     ¤
¤ Comments  :None                                                ¤
¤    _______                                                     ¤
¤  /` _____ `\;,    abezrati@hotmail.com                         ¤
¤ (__(^===^)__)';,                                 ___           ¤
¤   /  :::  \   ,;                               /^   ^\         ¤
¤  |   :::   | ,;'                              ( Ö   Ö )        ¤
¤¤¤'._______.'`¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ --°oOo--(_)--oOo°--¤¤*/

import demos.common.GLDisplay;

/**
 * @author Abdul Bezrati
 */
public class Lesson37 {
    public static void main(String[] args) {
        GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Lesson 37: Cell shading");
        Renderer renderer = new Renderer();
        InputHandler inputHandler = new InputHandler(renderer, neheGLDisplay);
        neheGLDisplay.addGLEventListener(renderer);
        neheGLDisplay.addKeyListener(inputHandler);
        neheGLDisplay.start();
    }
}
