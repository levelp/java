package nehe2;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Random;

import static java.awt.event.KeyEvent.VK_T;
import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_ONE;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL.GL_TRIANGLE_STRIP;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.GL2ES1.GL_POINT_SMOOTH_HINT;

/**
 * NeHe Lesson #19a: Fireworks
 */
public class JOGL2Nehe19Fireworks implements GLEventListener, KeyListener {
    private static String TITLE = "NeHe Lesson #19a: Fireworks";  // window's title
    private static final int CANVAS_WIDTH = 640;  // width of the drawable
    private static final int CANVAS_HEIGHT = 480; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second

    private GLU glu;  // for the GL Utility

    private static final int MAX_PARTICLES = 1000; // max number of particles
    private Particle[] particles = new Particle[MAX_PARTICLES];

    private static boolean enabledBurst = false;

    // Pull forces in each direction
    private static float gravityY = -0.0008f; // gravity

    // Global speed for all the particles
//   private static float speedXGlobal = 0.0f;
    private static float speedYGlobal = 0.1f;
    private static float z = -40.0f;
    private static float y = 5.0f;

    // Texture applied over the shape
    private Texture texture;
    private String textureFileName = "images/star.bmp";
    private String textureFileType = ".bmp";

    // Texture image flips vertically. Shall use TextureCoords class to retrieve the
    // top, bottom, left and right coordinates.
    private float textureTop, textureBottom, textureLeft, textureRight;

    /**
     * The entry main() method
     */
    public static void main(String[] args) {
        // Create the OpenGL rendering canvas
        GLCanvas canvas = new GLCanvas();  // heavy-weight GLCanvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        JOGL2Nehe19Fireworks renderer = new JOGL2Nehe19Fireworks();
        canvas.addGLEventListener(renderer);

        canvas.addKeyListener(renderer);
        canvas.setFocusable(true);  // To receive key event
        canvas.requestFocus();

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
//      gl.glEnable(GL_DEPTH_TEST); // enables depth testing
//      gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        gl.glEnable(GL_BLEND); // Enable Blending
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE); // Type Of Blending To Perform
        gl.glHint(GL_POINT_SMOOTH_HINT, GL_NICEST); // Really Nice Point Smoothing

        // Load the texture image
        try {
            // Create a OpenGL Texture object from (URL, mipmap, file suffix)
            // Use URL so that can read from JAR and disk file.
            texture = TextureIO.newTexture(
                    this.getClass().getResource(textureFileName), false, textureFileType);
        } catch (GLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Use linear filter for texture if image is larger than the original texture
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // Use linear filter for texture if image is smaller than the original texture
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        // Texture image flips vertically. Shall use TextureCoords class to retrieve
        // the top, bottom, left and right coordinates, instead of using 0.0f and 1.0f.
        TextureCoords textureCoords = texture.getImageTexCoords();
        textureTop = textureCoords.top();
        textureBottom = textureCoords.bottom();
        textureLeft = textureCoords.left();
        textureRight = textureCoords.right();

        // Initialize the particles
        for (int i = 0; i < MAX_PARTICLES; i++) {
            particles[i] = new Particle();
        }
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

        // Render the particles
        for (int i = 0; i < MAX_PARTICLES; i++) {
            if (particles[i].active) {
                // Draw the particle using our RGB values, fade the particle based on it's life
                gl.glColor4f(particles[i].r, particles[i].g, particles[i].b, particles[i].life);

                texture.enable(gl);
                texture.bind(gl);

                gl.glBegin(GL_TRIANGLE_STRIP); // build quad from a triangle strip

                float px = particles[i].x;
                float py = particles[i].y + y;
                float pz = particles[i].z + z;

                gl.glTexCoord2d(textureRight, textureTop);
                gl.glVertex3f(px + 0.5f, py + 0.5f, pz); // Top Right
                gl.glTexCoord2d(textureLeft, textureTop);
                gl.glVertex3f(px - 0.5f, py + 0.5f, pz); // Top Left
                gl.glTexCoord2d(textureRight, textureBottom);
                gl.glVertex3f(px + 0.5f, py - 0.5f, pz); // Bottom Right
                gl.glTexCoord2d(textureLeft, textureBottom);
                gl.glVertex3f(px - 0.5f, py - 0.5f, pz); // Bottom Left
                gl.glEnd();

                // Move the particle
                particles[i].x += particles[i].speedX;
                particles[i].y += particles[i].speedY;
                particles[i].z += particles[i].speedZ;

                // Apply the gravity force on y-axis
                particles[i].speedY += gravityY;

                if (enabledBurst) {
                    particles[i].burst();
                }
            }
        }
        if (enabledBurst) enabledBurst = false;
    }

    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    // Particle (inner class)
    class Particle {
        boolean active; // always active in this program
        float life;     // life time
        float fade;     // fading speed, which reduces the life time
        float r, g, b;  // color
        float x, y, z;  // position
        float speedX, speedY, speedZ; // speed in the direction

        private final float[][] colors = {    // rainbow of 12 colors
                {1.0f, 0.5f, 0.5f}, {1.0f, 0.75f, 0.5f}, {1.0f, 1.0f, 0.5f},
                {0.75f, 1.0f, 0.5f}, {0.5f, 1.0f, 0.5f}, {0.5f, 1.0f, 0.75f},
                {0.5f, 1.0f, 1.0f}, {0.5f, 0.75f, 1.0f}, {0.5f, 0.5f, 1.0f},
                {0.75f, 0.5f, 1.0f}, {1.0f, 0.5f, 1.0f}, {1.0f, 0.5f, 0.75f}};

        private Random rand = new Random();

        // Constructor
        public Particle() {
            active = true;
            burst();
        }

        public void burst() {
            life = 1.0f;

            // Set a random fade speed value between 0.003 and 0.103
            fade = rand.nextInt(100) / 1000.0f + 0.003f;

            // Set the initial position
            x = y = z = 0.0f;

            // Generate a random speed and direction in polar coordinate, then resolve
            // them into x and y.
            float maxSpeed = 0.1f;
            float speed = 0.02f + (rand.nextFloat() - 0.5f) * maxSpeed;
            float angle = (float) Math.toRadians(rand.nextInt(360));

            speedX = speed * (float) Math.cos(angle);
            speedY = speed * (float) Math.sin(angle) + speedYGlobal;
            speedZ = (rand.nextFloat() - 0.5f) * maxSpeed;

            int colorIndex = (int) (((speed - 0.02f) + maxSpeed) / (maxSpeed * 2) * colors.length) % colors.length;
            // Pick a random color
            r = colors[colorIndex][0];
            g = colors[colorIndex][1];
            b = colors[colorIndex][2];
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_T:
                if (!enabledBurst) enabledBurst = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
