package nehe2;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import static java.awt.event.KeyEvent.*;
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
import static javax.media.opengl.GL2.*;

/**
 * NeHe Lesson #9: Moving Bitmaps In 3D Space
 * <p/>
 * 't': toggle twinkle on/off
 * 'f': switch to the next texture filters (nearest, linear, mipmap)
 * Page-up/Page-down: zoom in/out decrease/increase z
 * up-arrow/down-arrow: decrease/increase the tile (rotation bout x-axis)
 */
public class JOGL2Nehe09Stars implements GLEventListener, KeyListener {
    private static String TITLE = "NeHe Lesson #9: Moving Bitmaps In 3D Space";
    private static final int CANVAS_WIDTH = 640;  // width of the drawable
    private static final int CANVAS_HEIGHT = 480; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second

    private GLU glu;  // for the GL Utility

    // Twinkling stars
    private static boolean twinkleOn = true; // twinkle on/off

    public static final int numStars = 50;   // number of twinkling stars
    private Star[] stars = new Star[numStars]; // array of Star objects

    private static float z = -15.0f;   // viewing distance away from stars
    private static float tilt = 90.0f; // tilting angle of the view
    private static float starSpinAngle = 0.0f; // spin stars on the z-axis
    private static float zIncrement = 1.0f;    // increment per key pressed
    private static float tileIncrement = 1.0f;

    // Texture applied over the shape
    private Texture texture; // texture object
    private String textureFileName = "images/star.bmp";

    // Texture image flips vertically. Shall use TextureCoords class to retrieve the
    // top, bottom, left and right coordinates.
    private float textureCoordTop, textureCoordBottom, textureCoordLeft, textureCoordRight;

    /**
     * The entry main() method
     */
    public static void main(String[] args) {
        // Create the OpenGL rendering canvas
        GLCanvas canvas = new GLCanvas();  // heavy-weight GLCanvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        JOGL2Nehe09Stars renderer = new JOGL2Nehe09Stars();
        canvas.addGLEventListener(renderer);

        // For Handling KeyEvents
        canvas.addKeyListener(renderer);
        canvas.setFocusable(true);
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
        // Do not enable depth test
//      gl.glEnable(GL_DEPTH_TEST); // enables depth testing
//      gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        // Load the texture image
        try {
            // Use URL so that can read from JAR and disk file.
            BufferedImage image = ImageIO.read(this.getClass().getResource(textureFileName));

            // Create a OpenGL Texture object
            texture = AWTTextureIO.newTexture(GLProfile.getDefault(), image, false);
            // Use linear filter if image is larger than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            // Use linear filter if image is smaller than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        } catch (GLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Texture image flips vertically. Shall use TextureCoords class to retrieve
        // the top, bottom, left and right coordinates, instead of using 0.0f and 1.0f.
        TextureCoords textureCoords = texture.getImageTexCoords();
        textureCoordTop = textureCoords.top();
        textureCoordBottom = textureCoords.bottom();
        textureCoordLeft = textureCoords.left();
        textureCoordRight = textureCoords.right();

        // Enable the texture
        texture.enable(gl);
        texture.bind(gl);
        // gl.glEnable(GL_TEXTURE_2D);

        // Enable blending
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        // Allocate the stars
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
            // Linearly distributed according to the star number
            stars[i].distance = ((float) i / numStars) * 5.0f;
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

        for (int i = 0; i < stars.length; i++) {
            // Reset the view (x, y, z axes back to normal)
            gl.glLoadIdentity();
            gl.glTranslatef(0.0f, 0.0f, z);

            // The stars are texture quad square drawn on x-y plane and
            // distributed on on x-z plane around the y-axis

            // Initial 90 degree tile in the x-axis, y-axis pointing out of screen
            gl.glRotatef(tilt, 1.0f, 0.0f, 0.0f);
            // Rotate about y-axis (pointing out of screen), initial angle is 0
            gl.glRotatef(stars[i].angle, 0.0f, 1.0f, 0.0f);
            // Translate about the x-axis (pointing right) to its current distance
            gl.glTranslatef(stars[i].distance, 0.0f, 0.0f);

            // The stars have initial angle of 0, and initial distance linearly
            // distributed between 0 and 5.0f

            // Rotate the axes back, so that z-axis is again facing us, to ensure that
            // the quad (with texture) on x-y plane is facing us
            gl.glRotatef(-stars[i].angle, 0.0f, 1.0f, 0.0f);
            gl.glRotatef(-tilt, 1.0f, 0.0f, 0.0f);

            // Take note that without the two rotations and undo, there is only one
            // translation along the x-axis
            // Matrix operation is non-commutative. That is, AB != BA.
            // Hence, ABCB'A' != C

            // Draw the star, which spins on the z axis (pointing out of the screen)
            gl.glRotatef(starSpinAngle, 0.0f, 0.0f, 1.0f);
            // Set the star's color using bytes (why bytes? not float or int?)
            gl.glColor4ub(stars[i].r, stars[i].g, stars[i].b, (byte) 255);
            gl.glBegin(GL_QUADS);
            // draw a square on x-y plane
            gl.glTexCoord2f(textureCoordLeft, textureCoordBottom);
            gl.glVertex3f(-1.0f, -1.0f, 0.0f);
            gl.glTexCoord2f(textureCoordRight, textureCoordBottom);
            gl.glVertex3f(1.0f, -1.0f, 0.0f);
            gl.glTexCoord2f(textureCoordRight, textureCoordTop);
            gl.glVertex3f(1.0f, 1.0f, 0.0f);
            gl.glTexCoord2f(textureCoordLeft, textureCoordTop);
            gl.glVertex3f(-1.0f, 1.0f, 0.0f);
            gl.glEnd();

            // If twinkling, overlay with another drawing of an arbitrary color
            if (twinkleOn) {
                // Assign a color using bytes
                gl.glColor4ub(stars[(numStars - i) - 1].r, stars[(numStars - i) - 1].g,
                        stars[(numStars - i) - 1].b, (byte) 255);
                gl.glBegin(GL_QUADS);
                // draw a square on x-y plane
                gl.glTexCoord2f(textureCoordLeft, textureCoordBottom);
                gl.glVertex3f(-1.0f, -1.0f, 0.0f);
                gl.glTexCoord2f(textureCoordRight, textureCoordBottom);
                gl.glVertex3f(1.0f, -1.0f, 0.0f);
                gl.glTexCoord2f(textureCoordRight, textureCoordTop);
                gl.glVertex3f(1.0f, 1.0f, 0.0f);
                gl.glTexCoord2f(textureCoordLeft, textureCoordTop);
                gl.glVertex3f(-1.0f, 1.0f, 0.0f);
                gl.glEnd();
            }

            // Update for the next refresh
            // The star spins about the z-axis (pointing out of the screen), and spiral
            // inwards and collapse towards the center, by increasing the angle on x-y
            // plane and reducing the distance.

            starSpinAngle += 0.01f; // used to spin the stars about the z-axis
            // spiral pattern
            stars[i].angle += (float) i / numStars; // changes the angle of a star
            // collapsing the star to the center
            stars[i].distance -= 0.01f; // changes the distance of a star
            // re-bone at the edge
            if (stars[i].distance < 0.0f) { // Is the star collapsed to the center?
                stars[i].distance += 5.0f; // move to the outer ring
                stars[i].setRandomRGB(); // choose a random color for the star
            }
        }
    }

    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    // ----- Implement methods declared in KeyListener -----

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_T:  // toggle twinkle
                twinkleOn = !twinkleOn;
                break;
            case VK_UP:   // decrease the tile (rotation bout x-axis)
                tilt -= tileIncrement;
                break;
            case VK_DOWN: // increase the tile (rotation bout x-axis)
                tilt += tileIncrement;
                break;
            case VK_PAGE_UP:   // zoom out (further z)
                z -= zIncrement;
                break;
            case VK_PAGE_DOWN: // zoom in (nearer z)
                z += zIncrement;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {

        }
    }

    // Star class (inner class)
    class Star {
        // public access for simplicity
        public byte r, g, b;   // RGB values for the star
        public float distance; // distance from the center
        public float angle;    // current angle about the center

        private Random rand = new Random();

        // Constructor
        public Star() {
            angle = 0.0f;
            r = (byte) rand.nextInt(256);
            g = (byte) rand.nextInt(256);
            b = (byte) rand.nextInt(256);
        }

        // Set the RGB color of this star to some random values
        public void setRandomRGB() {
            r = (byte) rand.nextInt(256);
            g = (byte) rand.nextInt(256);
            b = (byte) rand.nextInt(256);
        }
    }
}
