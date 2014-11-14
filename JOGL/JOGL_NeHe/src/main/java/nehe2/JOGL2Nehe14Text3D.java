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
import java.text.DecimalFormat;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.GL2.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_COLOR_MATERIAL;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;

/**
 * NeHe Lesson #14: 3D Text Rendering using TextRenderer
 */
public class JOGL2Nehe14Text3D implements GLEventListener {  // Renderer
    private static String TITLE = "Nehe #14: Outline Fonts";  // window's title
    private static final int CANVAS_WIDTH = 640;  // width of the drawable
    private static final int CANVAS_HEIGHT = 480; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second

    private GLU glu;  // for the GL Utility

    private TextRenderer textRenderer;
    private String msg = "NeHe - ";
    private DecimalFormat formatter = new DecimalFormat("###0.00");

    //private float textPosX; // x-position of the 3D text
    //private float textPosY; // y-position of the 3D text
    //private float textScaling; // scaling factor for 3D text

    private static float rotateAngle = 0.0f;

    /**
     * The entry main() method
     */
    public static void main(String[] args) {
        // Create the OpenGL rendering canvas
        GLCanvas canvas = new GLCanvas();  // heavy-weight GLCanvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        canvas.addGLEventListener(new JOGL2Nehe14Text3D());

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

        gl.glEnable(GL_LIGHT0); // Enable default light (quick and dirty)
        gl.glEnable(GL_LIGHTING); // Enable lighting
        gl.glEnable(GL_COLOR_MATERIAL); // Enable coloring of material

        // Allocate textRenderer with the chosen font
        textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 12));

        // Calculate the position and scaling factor
        // Rectangle2D bounds = textRenderer.getBounds(msg + "00.00");
        // int textWidth = (int)bounds.getWidth();
        // int textHeight = (int)bounds.getHeight();
        // System.out.println("w = " + textWidth);
        // System.out.println("h = " + textHeight);
        // 104 x 14
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

        // ----- Rendering 3D text using TextRenderer class -----
        textRenderer.begin3DRendering();

        gl.glTranslatef(0.0f, 0.0f, -50.0f);
        gl.glRotatef(rotateAngle, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotateAngle * 1.5f, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(rotateAngle * 1.4f, 0.0f, 0.0f, 1.0f);

        // Pulsing Colors Based On Text Position
        textRenderer.setColor((float) (Math.cos(rotateAngle / 20.0f)),      // R
                (float) (Math.sin(rotateAngle / 25.0f)),                      // G
                1.0f - 0.5f * (float) (Math.cos(rotateAngle / 17.0f)), 0.5f); // B

        // String, x, y, z, and scaling - need to scale down
        // Not too sure how to compute the x, y and scaling - trial and error!
        textRenderer.draw3D(msg + formatter.format(rotateAngle / 50), -20.0f,
                0.0f, 0.0f, 0.4f);

        // Clean up rendering
        textRenderer.end3DRendering();

        // Update the rotate angle
        rotateAngle += 0.1f;
    }

    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
}
