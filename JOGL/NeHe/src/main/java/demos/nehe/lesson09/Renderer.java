package demos.nehe.lesson09;

import demos.common.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.io.IOException;

class Renderer implements GLEventListener {
    private static final int NUMBER_OF_STARS = 50;

    // User Defined Variables
    private boolean twinkle;                      // Twinkling Stars

    private Star stars[] = new Star[NUMBER_OF_STARS];      // Need To Keep Track Of 'num' Stars

    private float zoom = -15.0f;              // Distance Away From Stars
    private boolean zoomIn;
    private boolean zoomOut;

    private float tilt = 90.0f;              // Tilt The View
    private boolean increaseTilt;
    private boolean decreaseTilt;

    private float spin;                         // Spin Stars

    private int textures[] = new int[1];       // Storage For One textures

    private GLU glu = new GLU();

    public void zoomIn(boolean zoom) {
        zoomIn = zoom;
    }

    public void zoomOut(boolean zoom) {
        zoomOut = zoom;
    }

    public void increaseTilt(boolean increase) {
        increaseTilt = increase;
    }

    public void decreaseTilt(boolean decrease) {
        decreaseTilt = decrease;
    }

    public void toggleTwinkle() {
        twinkle = !twinkle;
    }

    private void LoadGLTextures(GL gl) {                                    // Load Bitmaps And Convert To Textures
        TextureReader.Texture texture = null;// Create Storage Space For The Texture
        try {
            texture = TextureReader.readTexture("demos/data/images/Star.bmp");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        gl.glGenTextures(1, textures, 0);                           // Create One Texture

        // Create Linear Filtered Texture
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glEnable(GL.GL_TEXTURE_2D);                              // Enable Texture Mapping
        gl.glShadeModel(GL.GL_SMOOTH);                              // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);                    // Black Background
        gl.glClearDepth(1.0f);                                      // Depth Buffer Setup
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);  // Really Nice Perspective Calculations
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);                  // Set The Blending Function For Translucency
        gl.glEnable(GL.GL_BLEND);

        for (int loop = 0; loop < stars.length; loop++) {
            stars[loop] = new Star();
            stars[loop].angle = 0.0f;
            stars[loop].dist = ((float) loop / stars.length) * 5.0f;
            stars[loop].r = (int) (Math.random() * 1000) % 256;
            stars[loop].g = (int) (Math.random() * 1000) % 256;
            stars[loop].b = (int) (Math.random() * 1000) % 256;
        }

        LoadGLTextures(gl);
    }

    private void update() {
        if (decreaseTilt)
            tilt -= 0.5f;
        if (increaseTilt)
            tilt += 0.5f;
        if (zoomOut)
            zoom -= 0.2f;
        if (zoomIn)
            zoom += 0.2f;
    }

    public void display(GLAutoDrawable drawable) {
        update();
        GL gl = drawable.getGL();

        // Clear Color Buffer, Depth Buffer
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);     // Select Our Texture

        for (int loop = 0; loop < stars.length; loop++) {                      // Loop Through All The Stars
            gl.glLoadIdentity();                              // Reset The View Before We Draw Each Star
            gl.glTranslatef(0.0f, 0.0f, zoom);                  // Zoom Into The Screen (Using The Value In 'zoom')
            gl.glRotatef(tilt, 1.0f, 0.0f, 0.0f);                // Tilt The View (Using The Value In 'tilt')
            gl.glRotatef(stars[loop].angle, 0.0f, 1.0f, 0.0f);    // Rotate To The Current Stars Angle
            gl.glTranslatef(stars[loop].dist, 0.0f, 0.0f);       // Move Forward On The X Plane
            gl.glRotatef(-stars[loop].angle, 0.0f, 1.0f, 0.0f);   // Cancel The Current Stars Angle
            gl.glRotatef(-tilt, 1.0f, 0.0f, 0.0f);               // Cancel The Screen Tilt

            if (twinkle) {
                gl.glColor4ub((byte) stars[(stars.length - loop) - 1].r, (byte) stars[(stars.length - loop) - 1].g, (byte) stars[(stars.length - loop) - 1].b, (byte) 255);
                gl.glBegin(GL.GL_QUADS);
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(-1.0f, -1.0f, 0.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.0f, 0.0f);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(1.0f, 1.0f, 0.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-1.0f, 1.0f, 0.0f);
                gl.glEnd();
            }

            gl.glRotatef(spin, 0.0f, 0.0f, 1.0f);
            gl.glColor4ub((byte) stars[loop].r, (byte) stars[loop].g, (byte) stars[loop].b, (byte) 255);
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, 0.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, 0.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, 0.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, 0.0f);
            gl.glEnd();

            spin += 0.01f;
            stars[loop].angle += (float) loop / stars.length;
            stars[loop].dist -= 0.01f;

            if (stars[loop].dist < 0.0f) {
                stars[loop].dist += 5.0f;
                stars[loop].r = (int) (Math.random() * 1000) % 256;
                stars[loop].g = (int) (Math.random() * 1000) % 256;
                stars[loop].b = (int) (Math.random() * 1000) % 256;
            }
        }
    }

    public void reshape(GLAutoDrawable drawable,
                        int xstart,
                        int ystart,
                        int width,
                        int height) {
        GL gl = drawable.getGL();

        height = (height == 0) ? 1 : height;

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(45, (float) width / height, 1, 1000);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable,
                               boolean modeChanged,
                               boolean deviceChanged) {
    }

    private class Star {                        // Create A Structure For Star
        int r, g, b;          // Stars Color
        float dist,           // Stars Distance From Center
        angle;          // Stars Current Angle
    }
}