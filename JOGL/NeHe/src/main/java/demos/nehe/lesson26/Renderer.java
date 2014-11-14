package demos.nehe.lesson26;

import demos.common.TextureReader;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLUquadric;
import javax.media.opengl.glu.GLU;

import java.io.IOException;

class Renderer implements GLEventListener {
    private float xrot = 0f;				// X Rotation
    private float yrot = 0f;				// Y Rotation
    private float xspeed = 0f;														// X Rotation Speed
    private boolean increaseXspeed;
    private boolean decreaseXspeed;

    private float yspeed = 0f;														// Y Rotation Speed
    private boolean increaseYspeed;
    private boolean decreaseYspeed;

    private float zoom = -7.0f;						// Depth Into The Screen
    private boolean zoomIn;
    private boolean zoomOut;

    private float height = 2.0f;						// Height Of Ball From Floor
    private boolean increaseHeight;
    private boolean decreaseHeight;

    private int textures[] = new int[3];													// Storage For 6 Textures (Modified)

    private float lightAmbient[] = {0.2f, 0.2f, 0.2f};						// Ambient Light is 20% white
    private float lightDiffuse[] = {1.0f, 1.0f, 1.0f};						// Diffuse Light is white
    private float lightPosition[] = {0.0f, 0.0f, 2.0f};						// Position is somewhat in front of screen

    private GLUquadric q;	         // Storage For Our Quadratic Objects
    private GLU glu = new GLU();

    public Renderer() {
    }

    public float getZoom() {
        return zoom;
    }

    public void zoomIn(boolean zoom) {
        zoomIn = zoom;
    }

    public void zoomOut(boolean zoom) {
        zoomOut = zoom;
    }

    public void increaseHeight(boolean increase) {
        increaseHeight = increase;
    }

    public void decreaseHeight(boolean decrease) {
        decreaseHeight = decrease;
    }

    public void increaseXspeed(boolean increase) {
        increaseXspeed = increase;
    }

    public void decreaseXspeed(boolean decrease) {
        decreaseXspeed = decrease;
    }

    public void increaseYspeed(boolean increase) {
        increaseYspeed = increase;
    }

    public void decreaseYspeed(boolean decrease) {
        decreaseYspeed = decrease;
    }

    public void loadGLTextures(GL gl) throws IOException {
        String[] textureNames = new String[]{
            "demos/data/images/envwall.jpg",
            "demos/data/images/ball.jpg",
            "demos/data/images/envroll.jpg"
        };

        gl.glGenTextures(3, textures, 0);					// Create The Texture
        for (int loop = 0; loop < 3; loop++)				// Loop Through 5 Textures
        {
            String textureName = textureNames[loop];
            TextureReader.Texture texture = TextureReader.readTexture(textureName);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[loop]);
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        }
    }

    public void init(GLAutoDrawable glDrawable) {
        GL gl = glDrawable.getGL();

        try {
            loadGLTextures(gl);								// If Loading The Textures Failed
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gl.glShadeModel(GL.GL_SMOOTH);							// Enable Smooth Shading
        gl.glClearColor(0.2f, 0.5f, 1.0f, 1.0f);				// Background
        gl.glClearDepth(1.0f);									// Depth Buffer Setup
        gl.glClearStencil(0);									// Clear The Stencil Buffer To 0
        gl.glEnable(GL.GL_DEPTH_TEST);							// Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);								// The Type Of Depth Testing To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);	// Really Nice Perspective Calculations
        gl.glEnable(GL.GL_TEXTURE_2D);							// Enable 2D Texture Mapping

        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, lightAmbient, 0);			// Set The Ambient Lighting For Light0
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, lightDiffuse, 0);			// Set The Diffuse Lighting For Light0
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPosition, 0);		// Set The Position For Light0

        gl.glEnable(GL.GL_LIGHT0);								// Enable Light 0
        gl.glEnable(GL.GL_LIGHTING);								// Enable Lighting

        q = glu.gluNewQuadric();								// Create A New Quadratic
        glu.gluQuadricNormals(q, GL.GL_SMOOTH);					// Generate Smooth Normals For The Quad
        glu.gluQuadricTexture(q, true);						// Enable Texture Coords For The Quad

        gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_SPHERE_MAP);	// Set Up Sphere Mapping
        gl.glTexGeni(GL.GL_T, GL.GL_TEXTURE_GEN_MODE, GL.GL_SPHERE_MAP);	// Set Up Sphere Mapping
    }

    private void DrawObject(GL gl, GLU glu)										// Draw Our Ball
    {
        gl.glColor3f(1.0f, 1.0f, 1.0f);						// Set Color To White
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);			// Select Texture 2 (1)
        glu.gluSphere(q, 0.35f, 32, 16);						// Draw First Sphere

        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);			// Select Texture 3 (2)
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.4f);					// Set Color To White With 40% Alpha
        gl.glEnable(GL.GL_BLEND);									// Enable Blending
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);					// Set Blending Mode To Mix Based On SRC Alpha
        gl.glEnable(GL.GL_TEXTURE_GEN_S);							// Enable Sphere Mapping
        gl.glEnable(GL.GL_TEXTURE_GEN_T);							// Enable Sphere Mapping

        glu.gluSphere(q, 0.35f, 32, 16);						// Draw Another Sphere Using New Texture
        // Textures Will Mix Creating A MultiTexture Effect (Reflection)
        gl.glDisable(GL.GL_TEXTURE_GEN_S);						// Disable Sphere Mapping
        gl.glDisable(GL.GL_TEXTURE_GEN_T);						// Disable Sphere Mapping
        gl.glDisable(GL.GL_BLEND);								// Disable Blending
    }

    private void DrawFloor(GL gl)										// Draws The Floor
    {
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);			// Select Texture 1 (0)
        gl.glBegin(GL.GL_QUADS);									// Begin Drawing A Quad
        gl.glNormal3f(0.0f, 1.0f, 0.0f);						// Normal Pointing Up
        gl.glTexCoord2f(0.0f, 1.0f);					// Bottom Left Of Texture
        gl.glVertex3f(-2.0f, 0.0f, 2.0f);					// Bottom Left Corner Of Floor

        gl.glTexCoord2f(0.0f, 0.0f);					// Top Left Of Texture
        gl.glVertex3f(-2.0f, 0.0f, -2.0f);					// Top Left Corner Of Floor

        gl.glTexCoord2f(1.0f, 0.0f);					// Top Right Of Texture
        gl.glVertex3f(2.0f, 0.0f, -2.0f);					// Top Right Corner Of Floor

        gl.glTexCoord2f(1.0f, 1.0f);					// Bottom Right Of Texture
        gl.glVertex3f(2.0f, 0.0f, 2.0f);					// Bottom Right Corner Of Floor
        gl.glEnd();											// Done Drawing The Quad
    }

    private void update() {
        if (zoomIn)
            zoom += 0.05f;
        if (zoomOut)
            zoom -= 0.05f;
        if (increaseHeight)
            height += 0.03f;
        if (decreaseHeight)
            height -= 0.03f;
        if (increaseXspeed)
            xspeed += 0.08f;
        if (decreaseXspeed)
            xspeed -= 0.08f;
        if (increaseYspeed)
            yspeed += 0.08f;
        if (decreaseYspeed)
            yspeed -= 0.08f;
    }

    public void display(GLAutoDrawable glDrawable) {
        update();
        GL gl = glDrawable.getGL();

        // Clear Screen, Depth Buffer & Stencil Buffer
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT);

        // Clip Plane Equations
        double eqr[] = {0.0f, -1.0f, 0.0f, 0.0f};			// Plane Equation To Use For The Reflected Objects

        gl.glLoadIdentity();									// Reset The Modelview Matrix
        gl.glTranslatef(0.0f, -0.6f, zoom);					// Zoom And Raise Camera Above The Floor (Up 0.6 Units)
        gl.glColorMask(false, false, false, false);								// Set Color Mask
        gl.glEnable(GL.GL_STENCIL_TEST);							// Enable Stencil Buffer For "marking" The Floor
        gl.glStencilFunc(GL.GL_ALWAYS, 1, 1);						// Always Passes, 1 Bit Plane, 1 As Mask
        gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_REPLACE);			// We Set The Stencil Buffer To 1 Where We Draw Any Polygon
        // Keep If Test Fails, Keep If Test Passes But Buffer Test Fails
        // Replace If Test Passes
        gl.glDisable(GL.GL_DEPTH_TEST);							// Disable Depth Testing
        DrawFloor(gl);										// Draw The Floor (Draws To The Stencil Buffer)
        // We Only Want To Mark It In The Stencil Buffer
        gl.glEnable(GL.GL_DEPTH_TEST);							// Enable Depth Testing
        gl.glColorMask(true, true, true, true);								// Set Color Mask to TRUE, TRUE, TRUE, TRUE
        gl.glStencilFunc(GL.GL_EQUAL, 1, 1);						// We Draw Only Where The Stencil Is 1
        // (I.E. Where The Floor Was Drawn)
        gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);				// Don't Change The Stencil Buffer
        gl.glEnable(GL.GL_CLIP_PLANE0);							// Enable Clip Plane For Removing Artifacts
        // (When The Object Crosses The Floor)
        gl.glClipPlane(GL.GL_CLIP_PLANE0, eqr, 0);					// Equation For Reflected Objects
        gl.glPushMatrix();										// Push The Matrix Onto The Stack
        gl.glScalef(1.0f, -1.0f, 1.0f);					// Mirror Y Axis
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPosition, 0);	// Set Up Light0
        gl.glTranslatef(0.0f, height, 0.0f);				// Position The Object
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);				// Rotate Local Coordinate System On X Axis
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);				// Rotate Local Coordinate System On Y Axis
        DrawObject(gl, glu);									// Draw The Sphere (Reflection)
        gl.glPopMatrix();										// Pop The Matrix Off The Stack
        gl.glDisable(GL.GL_CLIP_PLANE0);							// Disable Clip Plane For Drawing The Floor
        gl.glDisable(GL.GL_STENCIL_TEST);							// We Don't Need The Stencil Buffer Any More (Disable)
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPosition, 0);		// Set Up Light0 Position
        gl.glEnable(GL.GL_BLEND);									// Enable Blending (Otherwise The Reflected Object Wont Show)
        gl.glDisable(GL.GL_LIGHTING);								// Since We Use Blending, We Disable Lighting
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.8f);					// Set Color To White With 80% Alpha
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);	// Blending Based On Source Alpha And 1 Minus Dest Alpha
        DrawFloor(gl);										// Draw The Floor To The Screen
        gl.glEnable(GL.GL_LIGHTING);								// Enable Lighting
        gl.glDisable(GL.GL_BLEND);								// Disable Blending
        gl.glTranslatef(0.0f, height, 0.0f);					// Position The Ball At Proper Height
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);					// Rotate On The X Axis
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);					// Rotate On The Y Axis
        DrawObject(gl, glu);										// Draw The Ball
        xrot += xspeed;									// Update X Rotation Angle By xrotspeed
        yrot += yspeed;									// Update Y Rotation Angle By yrotspeed
    }

    public void reshape(GLAutoDrawable glDrawable, int x, int y, int w, int h) {
        if (h == 0) h = 1;
        GL gl = glDrawable.getGL();

        gl.glViewport(0, 0, w, h);                       // Reset The Current Viewport And Perspective Transformation
        gl.glMatrixMode(GL.GL_PROJECTION);                           // Select The Projection Matrix
        gl.glLoadIdentity();                                      // Reset The Projection Matrix
        glu.gluPerspective(45.0f, (float) w / (float) h, 0.1f, 100.0f);  // Calculate The Aspect Ratio Of The Window
        gl.glMatrixMode(GL.GL_MODELVIEW);                            // Select The Modelview Matrix
        gl.glLoadIdentity();                                      // Reset The ModalView Matrix
    }

    public void displayChanged(GLAutoDrawable glDrawable, boolean b, boolean b1) {
    }
}
