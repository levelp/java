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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

import static java.awt.event.KeyEvent.*;
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
import static javax.media.opengl.GL.GL_REPEAT;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_WRAP_S;
import static javax.media.opengl.GL.GL_TEXTURE_WRAP_T;
import static javax.media.opengl.GL.GL_TRIANGLES;
import static javax.media.opengl.GL2.*;

/**
 * NeHe Lesson #10: Loading And Moving Through A 3D World
 * <p/>
 * 'b': toggle blending on/off
 * 't': switch to the next texture filters (nearest, linear, mipmap)
 * Page-up/Page-down: player looks up/down, scene rotates in negative x-axis
 * up-arrow/down-arrow: player move in/out, posX and posZ become smaller
 * left-arrow/right-arrow: player turns left/right (scene rotates right/left)
 */
public class JOGL2Nehe10World3D implements GLEventListener, KeyListener {
    private static String TITLE = "NeHe Lesson #10: Loading And Moving Through A 3D World";
    private static final int CANVAS_WIDTH = 640;  // width of the drawable
    private static final int CANVAS_HEIGHT = 480; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second

    private GLU glu;  // for the GL Utility

    // The world
    Sector sector;

    private boolean blendingEnabled; // Blending ON/OFF

    // x and z position of the player, y is 0
    private float posX = 0;
    private float posZ = 0;
    private float headingY = 0; // heading of player, about y-axis
    private float lookUpAngle = 0.0f;

    private float moveIncrement = 0.05f;
    private float turnIncrement = 1.5f; // each turn in degree
    private float lookUpIncrement = 1.0f;

    private float walkBias = 0;
    private float walkBiasAngle = 0;

    private Texture[] textures = new Texture[3];
    private int currTextureFilter = 0; // Which Filter To Use
    private String textureFilename = "images/mud.png";

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
        JOGL2Nehe10World3D renderer = new JOGL2Nehe10World3D();
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

    private void setupWorld() throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(getClass().getClassLoader().getResourceAsStream("models/world.txt")));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.trim().length() == 0 || line.trim().startsWith("//"))
                    continue;
                if (line.startsWith("NUMPOLLIES")) {
                    int numTriangles;

                    numTriangles = Integer.parseInt(line.substring(line
                            .indexOf("NUMPOLLIES")
                            + "NUMPOLLIES".length() + 1));
                    sector = new Sector(numTriangles);
                    break;
                }
            }

            for (int i = 0; i < sector.triangles.length; i++) {
                for (int vert = 0; vert < 3; vert++) {
                    while ((line = in.readLine()) != null) {
                        if (line.trim().length() == 0 || line.trim().startsWith("//"))
                            continue;
                        break;
                    }
                    if (line != null) {
                        System.out.println("line = " + line);
                        Scanner scanner = new Scanner(line).useLocale(Locale.ENGLISH);
                        sector.triangles[i].vertices[vert].x = scanner.nextFloat();
                        sector.triangles[i].vertices[vert].y = scanner.nextFloat();
                        sector.triangles[i].vertices[vert].z = scanner.nextFloat();
                        sector.triangles[i].vertices[vert].u = scanner.nextFloat();
                        sector.triangles[i].vertices[vert].v = scanner.nextFloat();
                        // System.out.println(sector.triangles[i].vertices[vert]);
                    }
                }
            }
        } finally {
            if (in != null)
                in.close();
        }
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

        // Read the world
        try {
            setupWorld();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // Load the texture image
        try {
            // Use URL so that can read from JAR and disk file.
            BufferedImage image = ImageIO.read(getResource());

            // Create a OpenGL Texture object
            textures[0] = AWTTextureIO.newTexture(GLProfile.getDefault(), image, false);
            // Nearest filter is least compute-intensive
            // Use nearer filter if image is larger than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            // Use nearer filter if image is smaller than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

            // For texture coordinates more than 1, set to wrap mode to GL_REPEAT for
            // both S and T axes (default setting is GL_CLAMP)
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            textures[1] = AWTTextureIO.newTexture(GLProfile.getDefault(), image, false);
            // Linear filter is more compute-intensive
            // Use linear filter if image is larger than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            // Use linear filter if image is smaller than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            textures[2] = AWTTextureIO.newTexture(GLProfile.getDefault(), image, true); // mipmap is true
            // Use mipmap filter is the image is smaller than the texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
                    GL_LINEAR_MIPMAP_NEAREST);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
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
//      textureLeft = textureCoords.left();
//      textureRight = textureCoords.right();

        // Enable the texture
        gl.glEnable(GL_TEXTURE_2D);

        // Blending control
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f); // Brightness with alpha
        // Blending function For translucency based On source alpha value
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
    }

    private URL getResource() {
        return this.getClass().getClassLoader().getResource(textureFilename);
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

        // Blending control
        if (blendingEnabled) {
            gl.glEnable(GL_BLEND); // Turn Blending On
            gl.glDisable(GL_DEPTH_TEST); // Turn Depth Testing Off
        } else {
            gl.glDisable(GL_BLEND); // Turn Blending Off
            gl.glEnable(GL_DEPTH_TEST); // Turn Depth Testing On
        }

        // Rotate up and down to look up and down
        gl.glRotatef(lookUpAngle, 1.0f, 0, 0);

        // Player at headingY. Rotate the scene by -headingY instead (add 360 to get a
        // positive angle)
        gl.glRotatef(360.0f - headingY, 0, 1.0f, 0);

        // Player is at (posX, 0, posZ). Translate the scene to (-posX, 0, -posZ)
        // instead.
        gl.glTranslatef(-posX, -walkBias - 0.25f, -posZ);

        // Select a texture based on filter
        textures[currTextureFilter].bind(gl);

        // Process each triangle
        for (int i = 0; i < sector.triangles.length; i++) {
            gl.glBegin(GL_TRIANGLES);
            gl.glNormal3f(0.0f, 0.0f, 1.0f); // Normal pointing out of screen

            // need to flip the image
            float textureHeight = textureTop - textureBottom;
            float u, v;

            u = sector.triangles[i].vertices[0].u;
            v = sector.triangles[i].vertices[0].v * textureHeight - textureBottom;
            gl.glTexCoord2f(u, v);
            gl.glVertex3f(sector.triangles[i].vertices[0].x,
                    sector.triangles[i].vertices[0].y, sector.triangles[i].vertices[0].z);

            u = sector.triangles[i].vertices[1].u;
            v = sector.triangles[i].vertices[1].v * textureHeight - textureBottom;
            gl.glTexCoord2f(u, v);
            gl.glVertex3f(sector.triangles[i].vertices[1].x,
                    sector.triangles[i].vertices[1].y, sector.triangles[i].vertices[1].z);

            u = sector.triangles[i].vertices[2].u;
            v = sector.triangles[i].vertices[2].v * textureHeight - textureBottom;
            gl.glTexCoord2f(u, v);
            gl.glVertex3f(sector.triangles[i].vertices[2].x,
                    sector.triangles[i].vertices[2].y, sector.triangles[i].vertices[2].z);

            gl.glEnd();
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
            case VK_LEFT:  // player turns left (scene rotates right)
                headingY += turnIncrement;
                break;
            case VK_RIGHT: // player turns right (scene rotates left)
                headingY -= turnIncrement;
                break;
            case VK_UP:
                // Player move in, posX and posZ become smaller
                posX -= (float) Math.sin(Math.toRadians(headingY)) * moveIncrement;
                posZ -= (float) Math.cos(Math.toRadians(headingY)) * moveIncrement;

                walkBiasAngle = (walkBiasAngle >= 359.0f) ? 0.0f : walkBiasAngle + 10.0f;
                // What is this walkbias? It's a word I invented :-) It's basically an
                // offset that occurs when a person walks around (head bobbing up and
                // down like a buoy. It simply adjusts the camera's Y position with a
                // sine wave. I had to put this in, as simply moving forwards and
                // backwards didn't look to great.

                // Causes the player to bounce in sine-wave pattern rather than
                // straight-line
                walkBias = (float) Math.sin(Math.toRadians(walkBiasAngle)) / 20.0f;
                break;
            case VK_DOWN:
                // Player move out, posX and posZ become bigger
                posX += (float) Math.sin(Math.toRadians(headingY)) * moveIncrement;
                posZ += (float) Math.cos(Math.toRadians(headingY)) * moveIncrement;
                walkBiasAngle = (walkBiasAngle <= 1.0f) ? 359.0f : walkBiasAngle - 10.0f;
                walkBias = (float) Math.sin(Math.toRadians(walkBiasAngle)) / 20.0f;
                break;
            case KeyEvent.VK_PAGE_UP:
                // player looks up, scene rotates in negative x-axis
                lookUpAngle -= lookUpIncrement;
                break;
            case KeyEvent.VK_PAGE_DOWN:
                // player looks down, scene rotates in positive x-axis
                lookUpAngle += lookUpIncrement;
                break;
            case VK_T: // switch texture filter nearer -> linear -> mipmap
                currTextureFilter = (currTextureFilter + 1) % textures.length;
                break;
            case VK_B: // toggle blending mode
                blendingEnabled = !blendingEnabled;
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

    // A sector comprises many triangles (inner class)
    class Sector {
        Triangle[] triangles;

        // Constructor
        public Sector(int numTriangles) {
            triangles = new Triangle[numTriangles];
            for (int i = 0; i < numTriangles; i++) {
                triangles[i] = new Triangle();
            }
        }
    }

    // A triangle has 3 vertices (inner class)
    class Triangle {
        Vertex[] vertices = new Vertex[3];

        public Triangle() {
            vertices[0] = new Vertex();
            vertices[1] = new Vertex();
            vertices[2] = new Vertex();
        }
    }

    // A vertex has xyz (location) and uv (for texture) (inner class)
    class Vertex {
        float x, y, z; // 3D x,y,z location
        float u, v; // 2D texture coordinates

        public String toString() {
            return "(" + x + "," + y + "," + z + ")" + "(" + u + "," + v + ")";
        }
    }
}
