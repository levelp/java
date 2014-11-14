package nehe2;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
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
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_LINEAR_MIPMAP_NEAREST;
import static javax.media.opengl.GL.GL_NEAREST;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.GL2.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT1;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;

/**
 * NeHe Lesson #18: Quadrics
 * <p/>
 * space: switch into the next object
 * 'l': toggle light on/off
 * 'f': switch to the next texture filters (nearest, linear, mipmap)
 * Page-up/Page-down: zoom in/out decrease/increase z
 * up-arrow/down-arrow: decrease/increase x rotational speed
 * left-arrow/right-arrow: decrease/increase y rotational speed
 */
public class JOGL2Nehe18Quadrics implements GLEventListener, KeyListener {
    private static String TITLE = "NeHe Lesson #18: Quadrics";  // window's title
    private static final int CANVAS_WIDTH = 640;  // width of the drawable
    private static final int CANVAS_HEIGHT = 480; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second

    private GLU glu;  // for the GL Utility

    private int part1;   // start of disc ( NEW )
    private int part2;   // end of disc   ( NEW )
    private int p1 = 0;  // increase 1    ( NEW )
    private int p2 = 1;  // increase 2    ( NEW )

    private GLUquadric quadric;
    int numObjects = 6;
    int object = 0;     // which object to draw ( NEW )

    private static float rotateAngleX = 0.0f; // rotational angle for x-axis in degree
    private static float rotateAngleY = 0.0f; // rotational angle for y-axis in degree
    private static float z = -5.0f;           // z-location
    private static float rotateSpeedX = 0.0f; // rotational speed for x-axis
    private static float rotateSpeedY = 0.0f; // rotational speed for y-axis

    private static float zIncrement = 0.5f;   // for zoom in/out
    private static float rotateSpeedXIncrement = 0.5f; // adjusting x rotational speed
    private static float rotateSpeedYIncrement = 0.5f; // adjusting y rotational speed

    // Textures with three different filters - Nearest, Linear & MIPMAP
    private Texture[] textures = new Texture[3];
    private static int currTextureFilter = 0; // currently used filter
    private String textureFileName = "images/wall.bmp";

    // Texture image flips vertically. Shall use TextureCoords class to retrieve the
    // top, bottom, left and right coordinates.
    private float textureTop;
    private float textureBottom;
    private float textureLeft;
    private float textureRight;

    // Lighting
    private static boolean lightOn = false;

    /**
     * The entry main() method
     */
    public static void main(String[] args) {
        // Create the OpenGL rendering canvas
        GLCanvas canvas = new GLCanvas();  // heavy-weight GLCanvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        JOGL2Nehe18Quadrics renderer = new JOGL2Nehe18Quadrics();
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
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

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
            // gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, textures[0].getWidth(), textures[0]
            //     .getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, textures[0].getPixels());

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

        // Set up the lighting for light named GL_LIGHT1

        // Ambient light does not come from a particular direction. Need some ambient
        // light to light up the scene. Ambient's value in RGBA
        float[] lightAmbientValue = {0.5f, 0.5f, 0.5f, 1.0f};
        // Diffuse light comes from a particular location. Diffuse's value in RGBA
        float[] lightDiffuseValue = {1.0f, 1.0f, 1.0f, 1.0f};
        // Diffuse light location xyz (in front of the screen).
        float[] lightDiffusePosition = {0.0f, 0.0f, 2.0f, 1.0f};

        gl.glLightfv(GL_LIGHT1, GL_AMBIENT, lightAmbientValue, 0);
        gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, lightDiffuseValue, 0);
        gl.glLightfv(GL_LIGHT1, GL_POSITION, lightDiffusePosition, 0);
        gl.glEnable(GL_LIGHT1);

        // Set up the Quadrics
        quadric = glu.gluNewQuadric(); // create a pointer to the Quadric object ( NEW )
        glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH); // create smooth normals ( NEW )
        glu.gluQuadricTexture(quadric, true); // create texture coords ( NEW )
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

        // Check whether light shall be turn on (toggle via the 'L' key)
        // This LIGHTING is different form LIGHT1 (??)
        if (lightOn) {
            gl.glEnable(GL_LIGHTING);
        } else {
            gl.glDisable(GL_LIGHTING);
        }

        // Enables this texture's target (e.g., GL_TEXTURE_2D) in the current GL
        // context's state.
        textures[currTextureFilter].enable(gl);
        // Bind the texture with the currently chosen filter to the current OpenGL graphics context.
        textures[currTextureFilter].bind(gl);

        gl.glTranslatef(0.0f, 0.0f, z);               // translate into the screen
        gl.glRotatef(rotateAngleX, 1.0f, 0.0f, 0.0f); // rotate about the x-axis
        gl.glRotatef(rotateAngleY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis

        switch (object) { // check object To find out what to draw
            case 0: // cube
                drawCube(gl);
                break;
            case 1: // cylinder
                gl.glTranslatef(0.0f, 0.0f, -1.5f);  // center the cylinder
                // The first parameter (1.0f) is the radius of the cylinder at base (bottom).
                // The second parameter (1.0f) is the radius of the cylinder at the top.
                // The third parameter ( 3.0f) is the height of the cylinder (how long it is).
                // The fourth parameter (32) is how many subdivisions there are "around" the Z axis,
                // and finally, the fifth parameter (32) is the amount of subdivisions "along" the Z axis.
                // The more subdivisions there are the more detailed the object is.
                glu.gluCylinder(quadric, 1.0f, 1.0f, 3.0f, 32, 32); // draw cylinder
                break;
            case 2: // CD shaped disc
                // The first parameter (0.5f) is the inner radius of the disk.
                // This value can be zero, meaning there will be no hole in the middle.
                // The larger the inner radius is, the bigger the hole in the middle of the disc will be.
                // The second parameter (1.5f) is the outer radius.
                // This value should be larger than the inner radius.
                // If you make this value a little bit larger than the inner radius
                // you will end up with a thing ring.
                // If you make this value a lot larger than the inner radius you will
                // end up with a thick ring.
                // The third parameter (32) is the number of slices that make up the disc.
                // Think of slices like the slices in a piazza.
                // The more slices you have, the smoother the outer edge of the disc will be.
                // Finally the fourth parameter (32) is the number of rings that make up the disc.
                glu.gluDisk(quadric, 0.5f, 1.5f, 32, 32);  // draw a disc (CD shape)
                break;
            case 3: // Sphere
                // The first parameter is the radius of the sphere.
                // In case you're not familiar with radius/diameter, etc,
                // the radius is the distance from the center of the object to the outside
                // of the object. In this case our radius is 1.3f.
                // Next we have our subdivision "around" the Z axis (32),
                // and our subdivision "along" the Z axis (32).
                glu.gluSphere(quadric, 1.3f, 32, 32); // draw a sphere
                break;
            case 4: // Cone
                gl.glTranslatef(0.0f, 0.0f, -1.5f);   // center the cone
                // To make a cone it makes sense that all we'd have to do is
                // make the radius at one end zero. This will create a point at one end.
                // A cone with a bottom radius of 0.5 and a height of 2.0
                glu.gluCylinder(quadric, 1.0f, 0.0f, 3.0f, 32, 32);
                break;
            case 5: // gluPartialDisc
                // The object we create using this command will look exactly like the
                // disc we created above, but with the command gluPartialDisk
                // there are two new parameters.
                // The fifth parameter (part1) is the start angle we want to start
                // drawing the disc at.
                // The sixth parameter is the sweep angle.
                // The sweep angle is the distance we travel from the current angle.
                // We'll increase the sweep angle, which causes the disc to be slowly
                // drawn to the screen in a clockwise direction.
                // Once our sweep hits 360 degrees we start to increase the start angle.
                part1 += p1;  // increase Start Angle
                part2 += p2;  // increase Sweep Angle

                if (part1 > 359) { // 360 Degrees
                    p1 = 0;    // stop increasing start angle
                    part1 = 0; // set start angle to zero
                    p2 = 1;    // start increasing sweep angle
                    part2 = 0; // start sweep angle at zero
                }
                if (part2 > 359) { // 360 Degrees
                    p1 = 1;    // start increasing start angle
                    p2 = 0;    // stop increasing sweep angle
                }
                // A disk like the one before
                glu.gluPartialDisk(quadric, 0.5f, 1.5f, 32, 32, part1, part2 - part1);
                break;
        }

        // Disables this texture's target (e.g., GL_TEXTURE_2D) in the current GL context's state.
        textures[currTextureFilter].disable(gl);

        // Update the rotational position after each refresh, based on rotational speed.
        rotateAngleX += rotateSpeedX;
        rotateAngleY += rotateSpeedY;
    }

    private void drawCube(GL2 gl) {
        // ------ Render a Cube with texture ------

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
            case VK_SPACE: // switch into the next object
                object = (object + 1) % numObjects;
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
