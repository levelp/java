package nehe2;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL2.*;

/**
 * NeHe Lesson #13: 2D Text Rendering using TextRenderer class
 */
public class JOGL2Nehe13Text2D implements GLEventListener {  // Renderer
    private static String TITLE = "Nehe #13: Bitmap Fonts";  // window's title
    private static final int CANVAS_WIDTH = 640;  // width of the drawable
    private static final int CANVAS_HEIGHT = 480; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second

    private GLU glu;  // for the GL Utility

    private TextRenderer textRenderer;
    private String msg = "Active OpenGL Text With NeHe - ";
    private DecimalFormat formatter = new DecimalFormat("###0.00");
    private int textPosX; // x-position of the text
    private int textPosY; // y-position of the text

    private float counter1 = 0; // 1st Counter Used To Move Text & For Coloring
    private float counter2 = 0; // 2nd Counter Used To Move Text & For Coloring

    /**
     * The entry main() method
     */
    public static void main(String[] args) {
        // Create the OpenGL rendering canvas
        GLCanvas canvas = new GLCanvas();  // heavy-weight GLCanvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        canvas.addGLEventListener(new JOGL2Nehe13Text2D());

        // Create a animator that drives canvas' display() at the specified FPS.
        final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

        // Create the top-level container frame
        final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame
        frame.getContentPane().add(canvas);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Use a dedicate thread to run the stop() to ensure that the
                // animator stops before program exits.
                new Thread() {
                    @Override
                    public void run() {
                        animator.stop(); // stop the animator loop
                        System.exit(0);
                    }
                }.start();
            }
        });
        frame.setTitle(TITLE);
        frame.pack();
        frame.setVisible(true);
        animator.start(); // start the animation loop
    }

    // ------ Implement methods declared in GLEventListener ------

    /**
     * Called back immediately after the OpenGL context is initialized. Can be used
     * to perform one-time initialization. Run only once.
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
        glu = new GLU();                         // get GL Utilities
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        // Allocate textRenderer with the chosen font
        textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 14));

        Rectangle2D bounds = textRenderer.getBounds(msg + "0000.00");
        int textWidth = (int) bounds.getWidth();
        int textHeight = (int) bounds.getHeight();
        // System.out.println("w = " + textWidth);
        // System.out.println("h = " + textHeight);
        // 528 x 26

        // Centralize text on the canvas
        textPosX = (drawable.getSurfaceWidth() - textWidth) / 2;
        textPosY = (drawable.getSurfaceHeight() - textHeight) / 2 + textHeight;
    }

    /**
     * Call-back handler for window re-size event. Also called when the drawable is
     * first set to visible.
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        if (height == 0) height = 1;   // prevent divide by zero
        float aspect = (float) width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }

    /**
     * Called back by the animator to perform rendering.
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
        gl.glLoadIdentity();  // reset the model-view matrix

        // ----- Rendering 2D text using TextRenderer class -----

        // Prepare to draw text
        textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());

        // Pulsing colors based on text position, set color in RGBA
        textRenderer.setColor((float) (Math.cos(counter1)),            // R
                (float) (Math.sin(counter2)),                          // G
                1.0f - 0.5f * (float) (Math.cos(counter1 + counter2)), // B
                0.5f);                                                 // Alpha

        // 2D text using int (x, y) coordinates in OpenGL coordinates system,
        // i.e., (0,0) at the bottom-left corner, instead of Java Graphics coordinates.
        // x is set to between (+/-)10. y is set to be (+/-)80.
        textRenderer.draw(msg + formatter.format(counter1), // string
                (int) ((textPosX + 10 * Math.cos(counter1))),  // x
                (int) (textPosY + 80 * Math.sin(counter2)));   // y

        textRenderer.endRendering();  // finish rendering

        // Update the counters to move the text around the screen
        counter1 += 0.102f; // increase The First Counter
        counter2 += 0.010f; // increase The Second Counter
    }

    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
}
