package nehe1;

import com.jogamp.opengl.util.FPSAnimator;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * JOGL 2.0 Applet Template (with GLCanvas and Swing JApplet)
 * This is the top-level "Container", which allocates and add GLCanvas ("Component")
 * and animator.
 */
@SuppressWarnings("serial")
public class JOGL2Setup_Applet extends JApplet {
    // Define constants for top-level container
    private static final int FPS = 60; // animator's target frames per second

    FPSAnimator animator;

    @Override
    public void init() {
        // Run the GUI codes in the event-dispatching thread for thread safety
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    // Create the OpenGL rendering canvas
                    GLCanvas canvas = new JOGL2Setup_GLCanvas();
                    getContentPane().add(canvas);

                    // Create a animator that drives canvas' display() at the specified FPS.
                    animator = new FPSAnimator(canvas, FPS, true);
                    animator.start(); // start the animation loop
                }
            });
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        if (animator.isStarted()) animator.stop();
    }

    @Override
    public void start() {
        animator.start();
    }

    @Override
    public void stop() {
        if (animator.isStarted()) animator.stop();
    }
}
