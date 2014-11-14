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
import javax.media.opengl.glu.GLUquadric;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static java.awt.event.KeyEvent.*;
import static javax.media.opengl.GL.GL_ALWAYS;
import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_EQUAL;
import static javax.media.opengl.GL.GL_KEEP;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_ONE;
import static javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static javax.media.opengl.GL.GL_REPLACE;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL.GL_STENCIL_TEST;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.GL2ES1.GL_CLIP_PLANE0;
import static javax.media.opengl.GL2ES1.GL_TEXTURE_GEN_MODE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;

/**
 * NeHe Lesson #26: Clipping and Reflection using the stencil Buffer
 */
public class JOGL2Nehe26Reflection implements GLEventListener, KeyListener {
    private static String TITLE = "Nehe #26: Clipping & Reflections Using The Stencil Buffer";
    private static final int CANVAS_WIDTH = 640;  // width of the drawable
    private static final int CANVAS_HEIGHT = 480; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second

    private GLU glu;  // for the GL Utility

    private float rotateAngleX = 0.0f;   // Rotation angle about x-axis in degree
    private float rotateAngleY = 0.0f;   // Rotation angle about y-axis in degree
    private float rotateSpeedX = 0.0f;   // Rotation speed about x-axis
    private float rotateSpeedY = 0.0f;   // Rotation speed about y-axis
    private float z = -7.0f; // Depth into the screen (z-axis)
    private float height = 2.0f; // Height of ball from floor

    // Quadric for drawing spheres
    private GLUquadric quadric;

    // 3 textures
    private Texture[] textures = new Texture[3];
    private String[] textureFileNames = {
            "images/envwall.jpg", "images/ball.jpg", "images/envroll.jpg"};
    private String textureFileType = ".jpg";

    // Texture image flips vertically. Shall use TextureCoords class to retrieve the
    // top, bottom, left and right coordinates.
    private float[] textureTops = new float[3];
    private float[] textureBottoms = new float[3];
    private float[] textureLefts = new float[3];
    private float[] textureRights = new float[3];

    private float[] lightAmbientValue = {0.7f, 0.7f, 0.7f, 1.0f}; // Ambient light value in RGBA
    private float[] lightDiffuseValue = {1.0f, 1.0f, 1.0f, 1.0f}; // Diffuse light value in RGBA
    private float[] lightDiffusePosition = {4.0f, 4.0f, 6.0f, 1.0f}; // Position of the light XYZ

    /**
     * The entry main() method
     */
    public static void main(String[] args) {
        // Create the OpenGL rendering canvas
        GLCanvas canvas = new GLCanvas();  // heavy-weight GLCanvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        JOGL2Nehe26Reflection renderer = new JOGL2Nehe26Reflection();
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

        // Clear The Stencil Buffer To 0
        gl.glClearStencil(0);

        // Load all the texture images
        try {
            for (int i = 0; i < textures.length; i++) {
                String textureFileName = textureFileNames[i];
                System.out.println("textureFileName = " + textureFileName);
                textures[i] = TextureIO.newTexture(this.getClass().getClassLoader().getResource(
                        textureFileName), false, textureFileType);
                // Use linear filter for texture if image is larger than the original texture
                gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                // Use linear filter for texture if image is smaller than the original texture
                gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

                TextureCoords textureCoords = textures[i].getImageTexCoords();
                textureTops[i] = textureCoords.top();
                textureBottoms[i] = textureCoords.bottom();
                textureLefts[i] = textureCoords.left();
                textureRights[i] = textureCoords.right();
            }
        } catch (GLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up lighting
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbientValue, 0); // Set the ambient lighting for LIGHT0
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuseValue, 0); // Set the diffuse lighting for LIGHT0
        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightDiffusePosition, 0); // Set the position for LIGHT0
        gl.glEnable(GL_LIGHT0);   // Enable LIGHT0
        gl.glEnable(GL_LIGHTING); // Enable Lighting

        // Set up the Quadric for drawing sphere
        quadric = glu.gluNewQuadric();             // Create a new quadric
        glu.gluQuadricNormals(quadric, GL_SMOOTH); // Generate smooth normals
        glu.gluQuadricTexture(quadric, true);      // Enable texture
        // set up sphere mapping for both the s- and t-axes
        gl.glTexGeni(GL_S, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
        gl.glTexGeni(GL_T, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
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

        // Clip plane equations for clipping the reflected image
        double eqr[] = {0.0f, -1.0f, 0.0f, 0.0f};

        // Translate downward to see the floor, and z
        gl.glTranslatef(0.0f, -0.6f, z);

        // Set the color mask RGBA to false, i.e., no color gets thru
        gl.glColorMask(false, false, false, false);

        // Setting up the stencil buffer and stencil testing
        gl.glEnable(GL_STENCIL_TEST);      // Enable stencil buffer for "marking" the floor
        gl.glStencilFunc(GL_ALWAYS, 1, 1); // Always passes, 1 Bit Plane, 1 As Mask
        gl.glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE); // Set the stencil buffer to 1 where we draw any polygon
        // Keep if test fails, keep if test passes but buffer test fails
        // replace if test passes
        gl.glDisable(GL_DEPTH_TEST); // Disable depth testing

        drawFloor(gl);

        gl.glEnable(GL_DEPTH_TEST);  // Enable depth testing
        gl.glColorMask(true, true, true, true); // Set color mask to let RGBA thru
        gl.glStencilFunc(GL_EQUAL, 1, 1);       // We draw only where the stencil is 1
        // (i.e. where the floor was drawn)
        gl.glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP); // Don't change the stencil buffer

        gl.glEnable(GL_CLIP_PLANE0); // Enable clip plane for removing artifacts
        // (when the object crosses the floor)
        gl.glClipPlane(GL_CLIP_PLANE0, eqr, 0); // Equation for reflected objects
        gl.glPushMatrix();                      // Push the matrix onto the stack
        gl.glScalef(1.0f, -1.0f, 1.0f);         // Mirror y-axis

        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightDiffusePosition, 0); // Set up LIGHT0
        gl.glTranslatef(0.0f, height, 0.0f); // Position the ball
        gl.glRotatef(rotateAngleX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotateAngleY, 0.0f, 1.0f, 0.0f);
        drawBall(gl);                  // Draw the sphere (reflection)
        gl.glPopMatrix();              // Pop the matrix off the stack
        gl.glDisable(GL_CLIP_PLANE0);  // Disable clip plane for drawing the floor
        gl.glDisable(GL_STENCIL_TEST); // We don't need the stencil buffer any more

        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightDiffusePosition, 0); // Set up LIGHT0 position
        gl.glEnable(GL_BLEND);     // Enable blending (otherwise the reflected object won't show)
        gl.glDisable(GL_LIGHTING); // Since we use blending, we disable Lighting
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.8f); // Set color to white with 80% alpha
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // Blending based on source alpha and 1 minus dest alpha
        drawFloor(gl); // Draw the floor

        gl.glEnable(GL_LIGHTING);  // Enable lighting
        gl.glDisable(GL_BLEND);    // Disable blending
        gl.glTranslatef(0.0f, height, 0.0f); // Position the ball at proper height
        gl.glRotatef(rotateAngleX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotateAngleY, 0.0f, 1.0f, 0.0f);
        drawBall(gl); // Draw the ball

        // Update rotational angle
        rotateAngleX += rotateSpeedX;
        rotateAngleY += rotateSpeedY;
        gl.glFlush();
    }

    // Render the beach ball by drawing two fully overlapped spheres
    private void drawBall(GL2 gl) {
        // draw the first sphere with texture "ball"
        gl.glColor3f(1.0f, 1.0f, 1.0f); // Set color to white
        textures[1].enable(gl);
        textures[1].bind(gl);
        glu.gluSphere(quadric, 0.35f, 32, 16); // Draw first sphere
        textures[1].disable(gl);

        // Draw the second sphere using texture "EnvRoll", set the alpha value to 40%
        // and enable blending based on the source alpha value. Enables sphere mapping.
        // The final result is a reflection that almost looks like bright points
        // of light mapped to the beach ball.
        // Because we enable sphere mapping, the texture is always facing the viewer,
        // even as the ball spins.
        // We blend so that the new texture doesn't cancel out the old texture
        // (a form of multi-texturing).
        textures[2].enable(gl);
        textures[2].bind(gl);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.4f);  // Set color to white with 40% alpha
        gl.glEnable(GL_BLEND);                 // Enable blending
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);  // Set blending mode to mix based on source alpha
        gl.glEnable(GL_TEXTURE_GEN_S);   // Enable sphere mapping on s-axis
        gl.glEnable(GL_TEXTURE_GEN_T);   // Enable sphere mapping on t-axis
        glu.gluSphere(quadric, 0.35f, 32, 16); // Draw another sphere using new texture
        // Textures will mix creating a multi-texture effect (Reflection)
        gl.glDisable(GL_TEXTURE_GEN_S);  // Disable sphere mapping
        gl.glDisable(GL_TEXTURE_GEN_T);  // Disable sphere mapping
        gl.glDisable(GL_BLEND);
        textures[2].disable(gl);
    }

    // Render the floor
    private void drawFloor(GL2 gl) {
        textures[0].enable(gl);  // "evnwall"
        textures[0].bind(gl);
        gl.glBegin(GL_QUADS);
        gl.glNormal3f(0.0f, 1.0f, 0.0f);   // Normal pointing up
        gl.glTexCoord2f(textureLefts[0], textureBottoms[0]);  // Bottom-left of texture
        gl.glVertex3f(-2.0f, 0.0f, 2.0f);  // Bottom-Left corner of the floor
        gl.glTexCoord2f(textureLefts[0], textureTops[0]);     // Top-left of texture
        gl.glVertex3f(-2.0f, 0.0f, -2.0f); // Top-left corner of the floor
        gl.glTexCoord2f(textureRights[0], textureTops[0]);    // Top-right of texture
        gl.glVertex3f(2.0f, 0.0f, -2.0f);  // Top-right corner of the floor
        gl.glTexCoord2f(textureRights[0], textureBottoms[0]); // Bottom-right of texture
        gl.glVertex3f(2.0f, 0.0f, 2.0f);  // Bottom-right corner of the floor
        gl.glEnd();
    }

    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_RIGHT:  // increase rotational speed y
                rotateSpeedY += 0.08f;
                break;
            case VK_LEFT:   // decrease rotational speed y
                rotateSpeedY -= 0.08f;
                break;
            case VK_DOWN:   // increase rotational speed x
                rotateSpeedX += 0.08f;
                break;
            case VK_UP:     // decrease rotational speed x
                rotateSpeedX -= 0.08f;
                break;
            case VK_A:      // zoom in
                z += 0.05f;
                break;
            case VK_Z:      // zoom out
                z -= 0.05f;
                break;
            case VK_PAGE_UP:    // move ball up
                height += 0.03f;
                break;
            case VK_PAGE_DOWN:  // move ball down
                height -= 0.03f;
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
