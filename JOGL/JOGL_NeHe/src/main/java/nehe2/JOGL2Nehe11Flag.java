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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static javax.media.opengl.GL.GL_BACK;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_FRONT;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.GL2GL3.GL_FILL;
import static javax.media.opengl.GL2GL3.GL_LINE;

/**
 * NeHe Lesson #11: Flag Effect (Waving Texture)
 */
public class JOGL2Nehe11Flag implements GLEventListener {  // Renderer
    private static String TITLE = "NeHe Lesson #11: Flag Effect (Waving Texture)";
    private static final int CANVAS_WIDTH = 640;  // width of the drawable
    private static final int CANVAS_HEIGHT = 480; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second

    private GLU glu;  // for the GL Utility

    // Divide the texture into 44x44 quads, with 45x45 points grid
    private int numPoints = 45;
    private float[][][] points = new float[numPoints][numPoints][3];
    int wiggleCount = 0; // Counter used to control how fast flag waves

    private static float rotateAnleX = 0; // rotational angle for x-axis in degree
    private static float rotateAnleY = 0; // rotational angle for y-axis in degree
    private static float rotateAngleZ = 0; // rotational angle for z-axis in degree
    private static float roateSpeedX = 0.05f; // rotational speed for x-axis
    private static float rotateSpeedY = 0.4f; // rotational speed for y-axis
    private static float rotateSpeedZ = 0.03f; // rotational speed for z-axis

    private Texture texture; // texture over the shape
    private String textureFileName = "images/tim.png";
    private String textureFileType = ".png";

    // Texture image flips vertically. Shall use TextureCoords class to retrieve the
    // top, bottom, left and right coordinates.
    private float textureTop;
    private float textureBottom;
//   private float textureLeft;
//   private float textureRight;

    /**
     * The entry main() method
     */
    public static void main(String[] args) {
        // Create the OpenGL rendering canvas
        GLCanvas canvas = new GLCanvas();  // heavy-weight GLCanvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        canvas.addGLEventListener(new JOGL2Nehe11Flag());

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
//      textureLeft = textureCoords.left();
//      textureRight = textureCoords.right();

        // Enable the texture
        texture.enable(gl);
        // gl.glEnable(GL_TEXTURE_2D);

        // we want back facing polygons to be filled completely and that we want front
        // facing polygons to be outlined only.
        gl.glPolygonMode(GL_BACK, GL_FILL); // Back Face Is Filled In
        gl.glPolygonMode(GL_FRONT, GL_LINE); // Front Face Is Drawn With Lines

        for (int x = 0; x < numPoints; x++) { // Loop Through The Y Plane
            for (int y = 0; y < numPoints; y++) {
                // Apply The Wave To Our Mesh
                // xmax is 45. Get 9 after dividing by 5. Subtract 4.5 to centralize.
                points[x][y][0] = (float) x / 5.0f - 4.5f;
                points[x][y][1] = (float) y / 5.0f - 4.5f;
                // Sine wave pattern
                points[x][y][2] = (float) (Math.sin(Math.toRadians(x / 5.0f * 40.0f)));
            }
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

        gl.glTranslatef(0.0f, 0.0f, -12.0f);
        gl.glRotatef(rotateAnleX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotateAnleY, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(rotateAngleZ, 0.0f, 0.0f, 1.0f);

        // need to flip the image
        float textureHeight = textureTop - textureBottom;

        float x1, y1, x2, y2; // used to break the flag into tiny quads

        gl.glBegin(GL_QUADS);
        for (int x = 0; x < numPoints - 1; x++) {
            for (int y = 0; y < numPoints - 1; y++) {
                x1 = (float) x / 44.0f;
                y1 = (float) y / 44.0f;
                x2 = (float) (x + 1) / 44.0f;
                y2 = (float) (y + 1) / 44.0f;

                // Texture need to flip vertically
                gl.glTexCoord2f(x1, y1 * textureHeight + textureBottom);
                gl.glVertex3f(points[x][y][0], points[x][y][1], points[x][y][2]);

                gl.glTexCoord2f(x1, y2 * textureHeight + textureBottom);
                gl.glVertex3f(points[x][y + 1][0], points[x][y + 1][1],
                        points[x][y + 1][2]);

                gl.glTexCoord2f(x2, y2 * textureHeight + textureBottom);
                gl.glVertex3f(points[x + 1][y + 1][0], points[x + 1][y + 1][1],
                        points[x + 1][y + 1][2]);

                gl.glTexCoord2f(x2, y1 * textureHeight + textureBottom);
                gl.glVertex3f(points[x + 1][y][0], points[x + 1][y][1],
                        points[x + 1][y][2]);
            }
        }
        gl.glEnd();

        if (wiggleCount == 2) { // Used To Slow Down The Wave (Every 2nd Frame Only)
            for (int y = 0; y < 45; y++) {
                float tempHold = points[0][y][2]; // Store current value One Left Side Of
                // Wave
                for (int x = 0; x < 44; x++) {
                    // Current Wave Value Equals Value To The Right
                    points[x][y][2] = points[x + 1][y][2];
                }
                points[44][y][2] = tempHold; // Last Value Becomes The Far Left Stored
                // Value
            }
            wiggleCount = 0; // Set Counter Back To Zero
        }
        wiggleCount++;

        // update the rotational position after each refresh
        rotateAnleX += roateSpeedX;
        rotateAnleY += rotateSpeedY;
        rotateAngleZ += rotateSpeedZ;
    }

    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
}
