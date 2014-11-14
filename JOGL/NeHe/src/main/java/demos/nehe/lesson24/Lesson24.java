package demos.nehe.lesson24;

import demos.common.GLDisplay;

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
¤ Program   :Nehe's 24th lesson port to JOGL                     ¤
¤ Comments  :None                                                ¤
¤    _______                                                     ¤
¤  /` _____ `\;,    abezrati@hotmail.com                         ¤
¤ (__(^===^)__)';,                                 ___           ¤
¤   /  :::  \   ,;                               /^   ^\         ¤
¤  |   :::   | ,;'                              ( Ö   Ö )        ¤
¤¤¤'._______.'`¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ --°oOo--(_)--oOo°--¤¤*/

/**
 * @author Abdul Bezrati
 */
public class Lesson24 {
    public static void main(String[] args) {
        GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Lesson 24: Tokens, Scissor testing, TGA");
        Renderer renderer = new Renderer();
        InputHandler inputHandler = new InputHandler(renderer, neheGLDisplay);
        neheGLDisplay.addGLEventListener(renderer);
        neheGLDisplay.addKeyListener(inputHandler);
        neheGLDisplay.start();
    }
}
