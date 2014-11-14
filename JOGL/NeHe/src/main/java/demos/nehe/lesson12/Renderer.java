package demos.nehe.lesson12;

import demos.common.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.io.IOException;

class Renderer implements GLEventListener {
    private float[][] boxcol = {{1.0f, 0.0f, 0.0f},
                                {1.0f, 0.5f, 0.0f},
                                {1.0f, 1.0f, 0.0f},
                                {0.0f, 1.0f, 0.0f},
                                {0.0f, 1.0f, 1.0f}};
    private float[][] topcol = {{0.5f, 0.0f, 0.0f},
                                {0.5f, .25f, 0.0f},
                                {0.5f, 0.5f, 0.0f},
                                {0.0f, 0.5f, 0.0f},
                                {0.0f, 0.5f, 0.5f}};
    private float xrot;                           // Rotates Cube On The X Axis
    private boolean increaseX;
    private boolean decreaseX;

    private float yrot;                           // Rotates Cube On The Y Axis
    private boolean increaseY;
    private boolean decreaseY;

    private int[] textures = new int[1];         // Storage For 1 Texture
    private int xloop;                          // Loop For X Axis
    private int yloop;                          // Loop For Y Axis
    private int box;                            // Storage For The Box Display List
    private int top;                            // Storage For The Top Display List

    private GLU glu = new GLU();

    public void increaseXrot(boolean increase) {
        increaseX = increase;
    }

    public void decreaseXrot(boolean decrease) {
        decreaseX = decrease;
    }

    public void increaseYrot(boolean increase) {
        increaseY = increase;
    }

    public void decreaseYrot(boolean decrease) {
        decreaseY = decrease;
    }

    private void loadGLTexture(GL gl) {
        TextureReader.Texture texture = null;
        try {
            texture = TextureReader.readTexture("demos/data/images/Cube.bmp");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        gl.glGenTextures(1, textures, 0);  // Create The Texture
        // Typical Texture Generation Using Data From The Bitmap
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture.getWidth(), texture.getHeight(),
                0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
    }

    private void buildLists(GL gl) {
        box = gl.glGenLists(2);           // Generate 2 Different Lists
        gl.glNewList(box, GL.GL_COMPILE);  // Start With The Box List

        gl.glBegin(GL.GL_QUADS);
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);  // Bottom Face
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);

        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);  // Front Face
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);

        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);  // Back Face
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);

        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);  // Right face
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);

        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);  // Left Face
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        gl.glEndList();

        top = box + 1;                        // Storage For "Top" Is "Box" Plus One
        gl.glNewList(top, GL.GL_COMPILE);  // Now The "Top" Display List

        gl.glBegin(GL.GL_QUADS);
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);// Top Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glEndList();
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glEnable(GL.GL_TEXTURE_2D);                              // Enable Texture Mapping
        gl.glShadeModel(GL.GL_SMOOTH);                              // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);                    // Black Background
        gl.glClearDepth(1.0f);                                      // Depth Buffer Setup
        gl.glEnable(GL.GL_DEPTH_TEST);                              // Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);                               // The Type Of Depth Testing To Do
        gl.glEnable(GL.GL_LIGHT0);                                  // Quick And Dirty Lighting (Assumes Light0 Is Set Up)
        gl.glEnable(GL.GL_LIGHTING);                                // Enable Lighting
        gl.glEnable(GL.GL_COLOR_MATERIAL);                          // Enable Material Coloring
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);  // Really Nice Perspective Calculations

        loadGLTexture(gl);
        buildLists(gl);
    }

    private void update() {
        if (decreaseX)
            xrot -= 8f;
        if (increaseX)
            xrot += 8f;
        if (decreaseY)
            yrot -= 8f;
        if (increaseY)
            yrot += 8f;
    }

    public void display(GLAutoDrawable drawable) {
        update();
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
        for (yloop = 1; yloop < 6; yloop++) {
            for (xloop = 0; xloop < yloop; xloop++) {
                gl.glLoadIdentity();          // Reset The View
                gl.glTranslatef(1.4f + ((float) xloop * 2.8f) - ((float) yloop * 1.4f), ((6.0f - (float) yloop) * 2.4f) - 7.0f, -20.0f);
                gl.glRotatef(45.0f - (2.0f * yloop) + xrot, 1.0f, 0.0f, 0.0f);
                gl.glRotatef(45.0f + yrot, 0.0f, 1.0f, 0.0f);
                gl.glColor3fv(boxcol[yloop - 1], 0);
                gl.glCallList(box);
                gl.glColor3fv(topcol[yloop - 1], 0);
                gl.glCallList(top);
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
}