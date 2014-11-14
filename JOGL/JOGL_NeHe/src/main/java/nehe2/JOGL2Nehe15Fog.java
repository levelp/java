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

import static java.awt.event.KeyEvent.*;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_DONT_CARE;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_LINEAR_MIPMAP_NEAREST;
import static javax.media.opengl.GL.GL_NEAREST;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL2.GL_MODELVIEW;
import static javax.media.opengl.GL2.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.GL2.GL_PROJECTION;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.GL2.GL_SMOOTH;
import static javax.media.opengl.GL2ES1.GL_EXP;
import static javax.media.opengl.GL2ES1.GL_EXP2;
import static javax.media.opengl.GL2ES1.GL_FOG;
import static javax.media.opengl.GL2ES1.GL_FOG_COLOR;
import static javax.media.opengl.GL2ES1.GL_FOG_DENSITY;
import static javax.media.opengl.GL2ES1.GL_FOG_END;
import static javax.media.opengl.GL2ES1.GL_FOG_HINT;
import static javax.media.opengl.GL2ES1.GL_FOG_MODE;
import static javax.media.opengl.GL2ES1.GL_FOG_START;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT1;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;

/**
 * NeHe Lesson #16: Cool Looking Fog
 * <p/>
 * 'g': switch to the next fog mode
 * 'l': toggle light on/off
 * 'f': switch to the next texture filters (nearest, linear, mipmap)
 * Page-up/Page-down: zoom in/out decrease/increase z
 * up-arrow/down-arrow: decrease/increase x rotational speed
 * left-arrow/right-arrow: decrease/increase y rotational speed
 */
public class JOGL2Nehe15Fog implements GLEventListener, KeyListener {
    private static String TITLE = "Nehe #16: Cool Looking Fog";  // window's title
    private static final int CANVAS_WIDTH = 640;  // width of the drawable
    private static final int CANVAS_HEIGHT = 480; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second

    private GLU glu;  // for the GL Utility

    private static float rotateAngleX = 0; // rotational angle for x-axis in degree
    private static float rotateAngleY = 0; // rotational angle for y-axis in degree
    private static float z = -5.0f; // z-location
    private static float rotateSpeedX = 0.7f; // rotational speed for x-axis
    private static float rotateSpeedY = 0.9f; // rotational speed for y-axis

    private static float zIncrement = 0.5f; // for zoom in/out
    private static float rotateSpeedXIncrement = 0.5f; // adjusting x rotational speed
    private static float rotateSpeedYIncrement = 0.5f; // adjusting y rotational speed

    // Textures with three different filters - Nearest, Linear & MIPMAP
    private Texture[] textures = new Texture[3];
    private static int currTextureFilter = 0; // currently used filter
    private String textureFileName = "images/crate.png";

    int[] fogModes = {GL_EXP, GL_EXP2, GL_LINEAR}; // storage for 3 types of fogs
    int currFogFilter = 0;                           // which fog to use
    float[] fogColor = {0.5f, 0.5f, 0.5f, 1.0f};   // fog color

    // Texture image flips vertically. Shall use TextureCoords class to retrieve the
    // top, bottom, left and right coordinates.
    private float textureTop;
    private float textureBottom;
    private float textureLeft;
    private float textureRight;

    // Lighting
    private static boolean lightOn = true;
    // Ambient light does not come from a particular direction. Need some ambient
    // light to light up the scene. Ambient's value in RGBA
    private static float[] LightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};

    // Diffuse light comes from a particular location. Diffuse's value in RGBA
    private static float[] LightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    // Diffuse light location xyz (in front of the screen).
    private static float LightDiffusePosition[] = {0.0f, 0.0f, 2.0f, 1.0f};

    /**
     * The entry main() method
     */
    public static void main(String[] args) {
        // Create the OpenGL rendering canvas
        GLCanvas canvas = new GLCanvas();  // heavy-weight GLCanvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        JOGL2Nehe15Fog renderer = new JOGL2Nehe15Fog();
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
//      gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // clear to the color of the fog

        // Set up fog mode
        gl.glFogfv(GL_FOG_COLOR, fogColor, 0); // set fog color
        gl.glFogf(GL_FOG_DENSITY, 0.35f);      // how dense will the fog be
        gl.glHint(GL_FOG_HINT, GL_DONT_CARE);  // fog hint value
        gl.glFogf(GL_FOG_START, 1.0f); // fog start depth
        gl.glFogf(GL_FOG_END, 5.0f);   // fog end depth
        gl.glEnable(GL_FOG);           // enables GL_FOG

        // Load the texture image
        try {
            // Use URL so that can read from JAR and disk file.
            BufferedImage image = ImageIO.read(this.getClass().getResource(textureFileName));

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
        } catch (GLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the top and bottom coordinates of the textures. Image flips vertically.
        TextureCoords textureCoords;
        textureCoords = textures[0].getImageTexCoords();
        textureTop = textureCoords.top();
        textureBottom = textureCoords.bottom();
        textureLeft = textureCoords.left();
        textureRight = textureCoords.right();

        // Enable the texture
        gl.glEnable(GL_TEXTURE_2D);
        // texture.enable(gl);

        // Set up the lighting for light named GL_LIGHT1
        gl.glLightfv(GL_LIGHT1, GL_AMBIENT, LightAmbient, 0);
        gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, LightDiffuse, 0);
        gl.glLightfv(GL_LIGHT1, GL_POSITION, LightDiffusePosition, 0);
        gl.glEnable(GL_LIGHT1);
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

        // set up fog mode
        gl.glFogi(GL_FOG_MODE, fogModes[currFogFilter]); // Fog Mode

        // Check whether light shall be turn on (toggle via the 'L' key)
        // This LIGHTING is different form LIGHT1 (??)
        if (lightOn) {
            gl.glEnable(GL_LIGHTING);
        } else {
            gl.glDisable(GL_LIGHTING);
        }

        // ------ Render a Cube with texture ------

        gl.glTranslatef(0.0f, 0.0f, z); // translate into the screen
        gl.glRotatef(rotateAngleX, 1.0f, 0.0f, 0.0f); // rotate about the x-axis
        gl.glRotatef(rotateAngleY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis

        // Bind the texture with the currently chosen filter to the current OpenGL
        // graphics context.
        textures[currTextureFilter].bind(gl);

        gl.glBegin(GL_QUADS); // of the color cube
        // Define groups of 4 vertices in CCW order

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

        // Update the rotational position after each refresh, based on rotational
        // speed.
        rotateAngleX += rotateSpeedX;
        rotateAngleY += rotateSpeedY;
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
            case VK_G: // switch to the next fog mode
                currFogFilter = (currFogFilter + 1) % fogModes.length;
                break;
            case VK_L: // toggle light on/off
                lightOn = !lightOn;
                break;
            case VK_F: // switch to the next filter (NEAREST, LINEAR, MIPMAP)
                currTextureFilter = (currTextureFilter + 1) % textures.length;
                break;
            case VK_PAGE_UP:   // zoom-out
                z -= zIncrement;
                break;
            case VK_PAGE_DOWN: // zoom-in
                z += zIncrement;
                break;
            case VK_UP:   // decrease rotational speed in x
                rotateSpeedX -= rotateSpeedXIncrement;
                break;
            case VK_DOWN: // increase rotational speed in x
                rotateSpeedX += rotateSpeedXIncrement;
                break;
            case VK_LEFT:  // decrease rotational speed in y
                rotateSpeedY -= rotateSpeedYIncrement;
                break;
            case VK_RIGHT: // increase rotational speed in y
                rotateSpeedY += rotateSpeedYIncrement;
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
