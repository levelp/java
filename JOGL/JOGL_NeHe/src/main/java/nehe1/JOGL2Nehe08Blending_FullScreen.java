package nehe1;

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
import java.awt.image.BufferedImage;
import java.io.IOException;

import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_LINEAR_MIPMAP_NEAREST;
import static javax.media.opengl.GL.GL_NEAREST;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_ONE;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL2.*;

/**
 * NeHe Lesson #8 (JOGL 2 Port): Blending (Full Screen Mode)
 *
 * @author Hock-Chuan Chua
 * @version May 2012
 *          <p/>
 *          'b': toggle blending on/off
 *          'l': toggle light on/off
 *          'f': switch to the next texture filters (nearest, linear, mipmap)
 *          Page-up/Page-down: zoom in/out decrease/increase z
 *          up-arrow/down-arrow: decrease/increase x rotational speed
 *          left-arrow/right-arrow: decrease/increase y rotational speed
 *          ESC: quit
 */
@SuppressWarnings("serial")
public class JOGL2Nehe08Blending_FullScreen extends GLCanvas
        implements GLEventListener, KeyListener {
    // Define constants for top-level container
    private static final int FPS = 60; // animator's target frames per second

    /**
     * The entry main() method to setup the top-level container and animator
     */
    public static void main(String[] args) {
        // Run the GUI codes in the event-dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create the OpenGL rendering canvas
                GLCanvas canvas = new JOGL2Nehe08Blending_FullScreen();

                // Create a animator that drives canvas' display() at the specified FPS.
                FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

                // Create the top-level container frame
                JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame
                frame.getContentPane().add(canvas);
                frame.setUndecorated(true);     // no decoration such as title bar
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);  // full screen mode
                frame.setVisible(true);
                animator.start(); // start the animation loop
            }
        });
    }

    // Setup OpenGL Graphics Renderer

    private GLU glu;  // for the GL Utility
    private static float angleX = 0.0f; // rotational angle for x-axis in degree
    private static float angleY = 0.0f; // rotational angle for y-axis in degree
    private static float z = -5.0f; // z-location
    private static float rotateSpeedX = 0.0f; // rotational speed for x-axis
    private static float rotateSpeedY = 0.0f; // rotational speed for y-axis

    private static float zIncrement = 0.02f; // for zoom in/out
    private static float rotateSpeedXIncrement = 0.01f; // adjusting x rotational speed
    private static float rotateSpeedYIncrement = 0.01f; // adjusting y rotational speed

    // Textures with three different filters - Nearest, Linear & MIPMAP_NEAREST
    private Texture[] textures = new Texture[3];
    private static int currTextureFilter = 0; // currently used filter
    private String textureFileName = "images/glass.png";

    // Texture image flips vertically. Shall use TextureCoords class to retrieve the
    // top, bottom, left and right coordinates.
    private float textureTop, textureBottom, textureLeft, textureRight;

    // Lighting
    private static boolean isLightOn;

    // Blending
    private static boolean blendingEnabled; // blending on/off

    /**
     * Constructor to setup the GUI for this Component
     */
    public JOGL2Nehe08Blending_FullScreen() {
        this.addGLEventListener(this);   // for handling GLEvents
        this.addKeyListener(this); // for Handling KeyEvents
        this.setFocusable(true);
        this.requestFocus();
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

        // Load textures from image
        try {
            // Use URL so that can read from JAR and disk file. Filename relative to project root
            BufferedImage image =
                    ImageIO.read(getClass().getClassLoader().getResource(textureFileName));

            // Create a OpenGL Texture object
            textures[0] = AWTTextureIO.newTexture(GLProfile.getDefault(), image, false);
            // Nearest filter is least compute-intensive
            // Use nearer filter if image is larger than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            // Use nearer filter if image is smaller than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

            textures[1] = AWTTextureIO.newTexture(GLProfile.getDefault(), image, false);
            // Linear filter is more compute-intensive
            // Use linear filter if image is larger than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            // Use linear filter if image is smaller than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

            textures[2] = AWTTextureIO.newTexture(GLProfile.getDefault(), image, true); // mipmap is true
            // Use mipmap filter is the image is smaller than the texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
                    GL_LINEAR_MIPMAP_NEAREST);

            // Get the top and bottom coordinates of the textures. Image flips vertically.
            TextureCoords textureCoords;
            textureCoords = textures[0].getImageTexCoords();
            textureTop = textureCoords.top();
            textureBottom = textureCoords.bottom();
            textureLeft = textureCoords.left();
            textureRight = textureCoords.right();
        } catch (GLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up the lighting for Light-1
        // Ambient light does not come from a particular direction. Need some ambient
        // light to light up the scene. Ambient's value in RGBA
        float[] lightAmbientValue = {0.5f, 0.5f, 0.5f, 1.0f};
        // Diffuse light comes from a particular location. Diffuse's value in RGBA
        float[] lightDiffuseValue = {1.0f, 1.0f, 1.0f, 1.0f};
        // Diffuse light location xyz (in front of the screen).
        float lightDiffusePosition[] = {0.0f, 0.0f, 2.0f, 1.0f};

        gl.glLightfv(GL_LIGHT1, GL_AMBIENT, lightAmbientValue, 0);
        gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, lightDiffuseValue, 0);
        gl.glLightfv(GL_LIGHT1, GL_POSITION, lightDiffusePosition, 0);
        gl.glEnable(GL_LIGHT1); // Enable Light-1
        gl.glDisable(GL_LIGHTING); // But disable lighting
        isLightOn = false;

        // Blending control
        // Full Brightness with specific alpha (1 for opaque, 0 for transparent)
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
        // Used blending function based On source alpha value
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        gl.glEnable(GL_BLEND);
        gl.glDisable(GL_DEPTH_TEST);
        blendingEnabled = true;

        // Changing the color4f's alpha value has no effect.
        // Vertex colors have no effect if lighting is enabled, instead material
        // colors could be used, as follows:
        //gl.glEnable(GL_COLOR_MATERIAL);
        //gl.glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
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

        // ------ Render a Cube with texture ------
        gl.glLoadIdentity();                    // reset model-view matrix
        gl.glTranslatef(0.0f, 0.0f, z);         // translate into the screen
        gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f); // rotate about the x-axis
        gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis

        // Enables this texture's target (e.g., GL_TEXTURE_2D) in the current GL
        // context's state.
        textures[currTextureFilter].enable(gl);
        // Bind the texture with the currently chosen filter to the current OpenGL
        // graphics context.
        textures[currTextureFilter].bind(gl);

        // Lighting
        if (isLightOn) {
            gl.glEnable(GL_LIGHTING);
        } else {
            gl.glDisable(GL_LIGHTING);
        }

        // Blending control
        if (blendingEnabled) {
            gl.glEnable(GL_BLEND);       // Turn blending on
            gl.glDisable(GL_DEPTH_TEST); // Turn depth testing off
        } else {
            gl.glDisable(GL_BLEND);      // Turn blending off
            gl.glEnable(GL_DEPTH_TEST);  // Turn depth testing on
        }

        gl.glBegin(GL_QUADS); // of the color cube

        // Front Face
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(textureLeft, textureBottom);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f); // bottom-left of the texture and quad
        gl.glTexCoord2f(textureRight, textureBottom);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);  // bottom-right of the texture and quad
        gl.glTexCoord2f(textureRight, textureTop);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);   // top-right of the texture and quad
        gl.glTexCoord2f(textureLeft, textureTop);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);  // top-left of the texture and quad

        // Back Face
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(textureRight, textureBottom);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(textureRight, textureTop);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(textureLeft, textureTop);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(textureLeft, textureBottom);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);

        // Top Face
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        gl.glTexCoord2f(textureLeft, textureTop);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(textureLeft, textureBottom);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(textureRight, textureBottom);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(textureRight, textureTop);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);

        // Bottom Face
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glTexCoord2f(textureRight, textureTop);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(textureLeft, textureTop);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(textureLeft, textureBottom);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(textureRight, textureBottom);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);

        // Right face
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(textureRight, textureBottom);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(textureRight, textureTop);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(textureLeft, textureTop);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(textureLeft, textureBottom);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);

        // Left Face
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(textureLeft, textureBottom);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(textureRight, textureBottom);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(textureRight, textureTop);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(textureLeft, textureTop);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);

        gl.glEnd();

        // Update the rotational position after each refresh.
        angleX += rotateSpeedX;
        angleY += rotateSpeedY;
    }

    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    // ------ Implement methods declared in KeyListener ------

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_B: // toggle blending on/off
                blendingEnabled = !blendingEnabled;
                break;
            case KeyEvent.VK_L: // toggle light on/off
                isLightOn = !isLightOn;
                break;
            case KeyEvent.VK_F: // switch to the next filter (NEAREST, LINEAR, MIPMAP)
                currTextureFilter = (currTextureFilter + 1) % textures.length;
                break;
            case KeyEvent.VK_PAGE_UP:   // zoom-out
                z -= zIncrement;
                break;
            case KeyEvent.VK_PAGE_DOWN: // zoom-in
                z += zIncrement;
                break;
            case KeyEvent.VK_UP:   // decrease rotational speed in x
                rotateSpeedX -= rotateSpeedXIncrement;
                break;
            case KeyEvent.VK_DOWN: // increase rotational speed in x
                rotateSpeedX += rotateSpeedXIncrement;
                break;
            case KeyEvent.VK_LEFT:  // decrease rotational speed in y
                rotateSpeedY -= rotateSpeedYIncrement;
                break;
            case KeyEvent.VK_RIGHT: // increase rotational speed in y
                rotateSpeedY += rotateSpeedYIncrement;
                break;
            case KeyEvent.VK_ESCAPE: // quit
                // Use a dedicate thread to run the stop() to ensure that the
                // animator stops before program exits.
                new Thread() {
                    @Override
                    public void run() {
                        GLAnimatorControl animator = getAnimator();
                        if (animator.isStarted()) animator.stop();
                        System.exit(0);
                    }
                }.start();
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
