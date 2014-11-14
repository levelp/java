package demos.nehe.lesson08;

import demos.common.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.io.IOException;

class Renderer implements GLEventListener {
    private boolean lightingEnabled;				// Lighting ON/OFF
    private boolean lightingChanged = false;		// Lighting changed
    private boolean blendingEnabled;				// Blending OFF/ON
    private boolean blendingChanged = false;		// Blending changed

    private int filter;				                // Which texture to use
    private int[] textures = new int[3];			// Storage For 3 Textures

    private float xrot;				// X Rotation
    private float yrot;				// Y Rotation

    private float xspeed = 0.5f;				// X Rotation Speed
    private boolean increaseX;
    private boolean decreaseX;

    private float yspeed = 0.3f;				// Y Rotation Speed
    private boolean increaseY;
    private boolean decreaseY;

    private float z = -5.0f;			// Depth Into The Screen
    private boolean zoomIn;
    private boolean zoomOut;

    private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
    private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] lightPosition = {0.0f, 0.0f, 2.0f, 1.0f};

    private GLU glu = new GLU();

    public void toggleBlending() {
        blendingEnabled = !blendingEnabled;
        blendingChanged = true;
    }

    public void toggleLighting() {
        lightingEnabled = !lightingEnabled;
        lightingChanged = true;
    }

    public void increaseXspeed(boolean increase) {
        increaseX = increase;
    }

    public void decreaseXspeed(boolean decrease) {
        decreaseX = decrease;
    }

    public void increaseYspeed(boolean increase) {
        increaseY = increase;
    }

    public void decreaseYspeed(boolean decrease) {
        decreaseY = decrease;
    }

    public void zoomIn(boolean zoom) {
        zoomIn = zoom;
    }

    public void zoomOut(boolean zoom) {
        zoomOut = zoom;
    }

    public void switchFilter() {
        filter = (filter + 1) % textures.length;
    }

    private void loadGLTextures(GLAutoDrawable gldrawable) throws IOException {
        TextureReader.Texture texture = TextureReader.readTexture("demos/data/images/glass.png");

        GL gl = gldrawable.getGL();

        //Create Nearest Filtered Texture
        gl.glGenTextures(3, textures, 0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);

        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);

        gl.glTexImage2D(GL.GL_TEXTURE_2D,
                0,
                3,
                texture.getWidth(),
                texture.getHeight(),
                0,
                GL.GL_RGB,
                GL.GL_UNSIGNED_BYTE,
                texture.getPixels());

        //Create Linear Filtered Texture
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);

        gl.glTexImage2D(GL.GL_TEXTURE_2D,
                0,
                3,
                texture.getWidth(),
                texture.getHeight(),
                0,
                GL.GL_RGB,
                GL.GL_UNSIGNED_BYTE,
                texture.getPixels());

        //Create MipMapped Texture (Only with GL4Java 2.1.2.1 and later!)
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_NEAREST);

        glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D,
                3,
                texture.getWidth(),
                texture.getHeight(),
                GL.GL_RGB,
                GL.GL_UNSIGNED_BYTE,
                texture.getPixels());
    }

    public void init(GLAutoDrawable glDrawable) {
        try {
            loadGLTextures(glDrawable);
        } catch (IOException e) {
            System.out.println("Unable to load textures,Bailing!");
            throw new RuntimeException(e);
        }

        GL gl = glDrawable.getGL();
        gl.glEnable(GL.GL_TEXTURE_2D);							// Enable Texture Mapping
        gl.glShadeModel(GL.GL_SMOOTH);                            //Enables Smooth Color Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);               //This Will Clear The Background Color To Black
        gl.glClearDepth(1.0);                                  //Enables Clearing Of The Depth Buffer
        gl.glEnable(GL.GL_DEPTH_TEST);                            //Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);                             //The Type Of Depth Test To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);  // Really Nice Perspective Calculations
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightAmbient, 0);		// Setup The Ambient Light
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, lightDiffuse, 0);		// Setup The Diffuse Light
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPosition, 0);	// Position The Light
        gl.glEnable(GL.GL_LIGHT1);								// Enable Light One

        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);					// Full Brightness.  50% Alpha (new )
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);					// Set The Blending Function For Translucency (new )
    }

    private void update() {
        if (zoomOut)
            z -= 0.02f;
        if (zoomIn)
            z += 0.02f;
        if (decreaseX)
            xspeed -= 0.01f;
        if (increaseX)
            xspeed += 0.01f;
        if (increaseY)
            yspeed += 0.01f;
        if (decreaseY)
            yspeed -= 0.01f;
    }

    public void display(GLAutoDrawable glDrawable) {
        update();
        GL gl = glDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();                                         //Reset The View
        gl.glTranslatef(0.0f, 0.0f, z);

        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);

        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[filter]);

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        // Back Face
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        // Top Face
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        // Bottom Face
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        // Right face
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        // Left Face
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        xrot += xspeed;
        yrot += yspeed;

        // toggle lighting
        if (lightingChanged) {
            if (lightingEnabled)
                gl.glEnable(GL.GL_LIGHTING);
            else
                gl.glDisable(GL.GL_LIGHTING);
            lightingChanged = false;
        }

        // toggle blending
        if (blendingChanged) {
            if (blendingEnabled) {
                gl.glEnable(GL.GL_BLEND);		// Turn Blending On
                gl.glDisable(GL.GL_DEPTH_TEST);	// Turn Depth Testing Off
            } else {
                gl.glDisable(GL.GL_BLEND);		// Turn Blending Off
                gl.glEnable(GL.GL_DEPTH_TEST);	// Turn Depth Testing On
            }
            blendingChanged = false;
        }
    }

    public void reshape(GLAutoDrawable glDrawable, int x, int y, int w, int h) {
        final GL gl = glDrawable.getGL();

        if (h <= 0) // avoid a divide by zero error!
            h = 1;
        final float a = (float) w / (float) h;
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, a, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();                                     // Reset The ModalView Matrix
    }

    public void displayChanged(GLAutoDrawable glDrawable, boolean b, boolean b1) {
    }
}
