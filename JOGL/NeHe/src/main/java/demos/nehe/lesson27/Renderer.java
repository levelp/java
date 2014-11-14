package demos.nehe.lesson27;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLUquadric;
import javax.media.opengl.glu.GLU;

import java.io.IOException;

class Renderer implements GLEventListener {
    private GLUquadric quadric;
    private Object3D object3D;

    private float[] LightPos = {0.0f, 5.0f, -4.0f, 1.0f};   // Light Position
    private boolean LightUp, LightDown, LightLeft, LightRight, LightForward, LightBackward;
    private float[] LightAmb = {0.2f, 0.2f, 0.2f, 1.0f};   // Ambient Light Values
    private float[] LightDif = {0.6f, 0.6f, 0.6f, 1.0f};   // Diffuse Light Values
    private float[] LightSpc = {-0.2f, -0.2f, -0.2f, 1.0f}; // Specular Light Values

    private float[] MatAmb = {0.4f, 0.4f, 0.4f, 1.0f};    // Material - Ambient Values
    private float[] MatDif = {0.2f, 0.6f, 0.9f, 1.0f};    // Material - Diffuse Values
    private float[] MatSpc = {0.0f, 0.0f, 0.0f, 1.0f};    // Material - Specular Values
    private float[] MatShn = {0.0f};                      // Material - Shininess

    private float[] SpherePos = {-4.0f, -5.0f, -6.0f};
    private boolean SphereUp, SphereDown, SphereLeft, SphereRight, SphereForward, SphereBackward;
    private float[] ObjPos = {-2.0f, -2.0f, -5.0f};         // Object Position
    private boolean ObjUp, ObjDown, ObjLeft, ObjRight, ObjForward, ObjBackward;

    private float xspeed;                                    // X Rotation Speed
    private boolean increaseXspeed;
    private boolean decreaseXspeed;

    private float yspeed;                                    // Y Rotation Speed
    private boolean increaseYspeed;
    private boolean decreaseYspeed;

    private float xrot;                                      // X Rotation
    private float yrot;                                      // Y Rotation

    private GLU glu = new GLU();

    private boolean initGLObjects() throws IOException {                               // Initialize Objects
        object3D = Object3D.readObject("demos/data/models/Object2.txt");
        return object3D != null;                                      // If Failed Return False
    }

    public void translateSphereUp(boolean state) {
        SphereUp = state;
    }

    public void translateSphereDown(boolean state) {
        SphereDown = state;
    }

    public void translateSphereLeft(boolean state) {
        SphereLeft = state;
    }

    public void translateSphereRight(boolean state) {
        SphereRight = state;
    }

    public void translateSphereForward(boolean state) {
        SphereForward = state;
    }

    public void translateSphereBackward(boolean state) {
        SphereBackward = state;
    }

    public void translateLightUp(boolean state) {
        LightUp = state;
    }

    public void translateLightDown(boolean state) {
        LightDown = state;
    }

    public void translateLightLeft(boolean state) {
        LightLeft = state;
    }

    public void translateLightRight(boolean state) {
        LightRight = state;
    }

    public void translateLightForward(boolean state) {
        LightForward = state;
    }

    public void translateLightBackward(boolean state) {
        LightBackward = state;
    }

    public void translateObjectUp(boolean state) {
        ObjUp = state;
    }

    public void translateObjectDown(boolean state) {
        ObjDown = state;
    }

    public void translateObjectLeft(boolean state) {
        ObjLeft = state;
    }

    public void translateObjectRight(boolean state) {
        ObjRight = state;
    }

    public void translateObjectForward(boolean state) {
        ObjForward = state;
    }

    public void translateObjectBackward(boolean state) {
        ObjBackward = state;
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

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        try {
            initGLObjects();
        } catch (IOException e) {
            System.out.println("Couldn't load model");
            throw new RuntimeException(e);
        }

        gl.glShadeModel(GL.GL_SMOOTH);                              // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);                    // Black Background
        gl.glClearDepth(1.0f);                                      // Depth Buffer Setup
        gl.glClearStencil(0);                                       // Stencil Buffer Setup
        gl.glEnable(GL.GL_DEPTH_TEST);                              // Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);                               // The Type Of Depth Testing To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);  // Really Nice Perspective Calculations

        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, LightPos, 0);        // Set Light1 Position
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, LightAmb, 0);        // Set Light1 Ambience
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, LightDif, 0);        // Set Light1 Diffuse
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, LightSpc, 0);        // Set Light1 Specular
        gl.glEnable(GL.GL_LIGHT1);                                  // Enable Light1
        gl.glEnable(GL.GL_LIGHTING);                                // Enable Lighting

        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, MatAmb, 0);         // Set Material Ambience
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, MatDif, 0);         // Set Material Diffuse
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, MatSpc, 0);        // Set Material Specular
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, MatShn, 0);       // Set Material Shininess

        gl.glCullFace(GL.GL_BACK);                                  // Set Culling Face To Back Face
        gl.glEnable(GL.GL_CULL_FACE);                               // Enable Culling
        gl.glClearColor(0.1f, 1.0f, 0.5f, 1.0f);                    // Set Clear Color (Greenish Color)

        quadric = glu.gluNewQuadric();                              // Initialize Quadratic
        glu.gluQuadricNormals(quadric, GL.GL_SMOOTH);                      // Enable Smooth Normal Generation
        glu.gluQuadricTexture(quadric, false);                             // Disable Auto Texture Coords
    }

    private void drawRoom(GL gl) {                        // Draw The Room (Box)
        gl.glBegin(GL.GL_QUADS);                // Begin Drawing Quads
        // Floor
        gl.glNormal3f(0.0f, 1.0f, 0.0f);      // Normal Pointing Up
        gl.glVertex3f(-10.0f, -10.0f, -20.0f);  // Back Left
        gl.glVertex3f(-10.0f, -10.0f, 20.0f);  // Front Left
        gl.glVertex3f(10.0f, -10.0f, 20.0f);  // Front Right
        gl.glVertex3f(10.0f, -10.0f, -20.0f);  // Back Right
        // Ceiling
        gl.glNormal3f(0.0f, -1.0f, 0.0f);      // Normal Point Down
        gl.glVertex3f(-10.0f, 10.0f, 20.0f);  // Front Left
        gl.glVertex3f(-10.0f, 10.0f, -20.0f);  // Back Left
        gl.glVertex3f(10.0f, 10.0f, -20.0f);  // Back Right
        gl.glVertex3f(10.0f, 10.0f, 20.0f);  // Front Right
        // Front Wall
        gl.glNormal3f(0.0f, 0.0f, 1.0f);      // Normal Pointing Away From Viewer
        gl.glVertex3f(-10.0f, 10.0f, -20.0f);  // Top Left
        gl.glVertex3f(-10.0f, -10.0f, -20.0f);  // Bottom Left
        gl.glVertex3f(10.0f, -10.0f, -20.0f);  // Bottom Right
        gl.glVertex3f(10.0f, 10.0f, -20.0f);  // Top Right
        // Back Wall
        gl.glNormal3f(0.0f, 0.0f, -1.0f);      // Normal Pointing Towards Viewer
        gl.glVertex3f(10.0f, 10.0f, 20.0f);  // Top Right
        gl.glVertex3f(10.0f, -10.0f, 20.0f);  // Bottom Right
        gl.glVertex3f(-10.0f, -10.0f, 20.0f);  // Bottom Left
        gl.glVertex3f(-10.0f, 10.0f, 20.0f);  // Top Left
        // Left Wall
        gl.glNormal3f(1.0f, 0.0f, 0.0f);      // Normal Pointing Right
        gl.glVertex3f(-10.0f, 10.0f, 20.0f);  // Top Front
        gl.glVertex3f(-10.0f, -10.0f, 20.0f);  // Bottom Front
        gl.glVertex3f(-10.0f, -10.0f, -20.0f);  // Bottom Back
        gl.glVertex3f(-10.0f, 10.0f, -20.0f);  // Top Back
        // Right Wall
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);     // Normal Pointing Left
        gl.glVertex3f(10.0f, 10.0f, -20.0f);  // Top Back
        gl.glVertex3f(10.0f, -10.0f, -20.0f);  // Bottom Back
        gl.glVertex3f(10.0f, -10.0f, 20.0f);  // Bottom Front
        gl.glVertex3f(10.0f, 10.0f, 20.0f);  // Top Front
        gl.glEnd();                             // Done Drawing Quads
    }

    private void update() {
        if (increaseXspeed)
            xspeed += 0.08f;
        if (decreaseXspeed)
            xspeed -= 0.08f;
        if (increaseYspeed)
            yspeed += 0.08f;
        if (decreaseYspeed)
            yspeed -= 0.08f;

        translate(LightPos, LightUp, LightDown, LightLeft, LightRight, LightForward, LightBackward);
        translate(ObjPos, ObjUp, ObjDown, ObjLeft, ObjRight, ObjForward, ObjBackward);
        translate(SpherePos, SphereUp, SphereDown, SphereLeft, SphereRight, SphereForward, SphereBackward);
    }

    private void translate(float[] position, boolean up, boolean down, boolean left, boolean right, boolean forward, boolean backward) {
        float x = 0;
        float y = 0;
        float z = 0;
        if (left)
            x -= 0.05f;
        if (right)
            x += 0.05f;
        if (up)
            y += 0.05f;
        if (down)
            y -= 0.05f;
        if (forward)
            z += 0.05f;
        if (backward)
            z -= 0.05f;

        position[0] += x;
        position[1] += y;
        position[2] += z;
    }

    public void display(GLAutoDrawable drawable) {
        update();
        GL gl = drawable.getGL();

        // Clear Color Buffer, Depth Buffer, Stencil Buffer
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT);
        GLmatrix16f Minv = new GLmatrix16f();
        GLvector4f wlp = new GLvector4f();
        GLvector4f lp = new GLvector4f();

        gl.glLoadIdentity();                                        // Reset Modelview Matrix
        gl.glTranslatef(0.0f, 0.0f, -20.0f);                        // Zoom Into Screen 20 Units
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, LightPos, 0);        // Position Light1
        gl.glTranslatef(SpherePos[0], SpherePos[1], SpherePos[2]);  // Position The Sphere
        glu.gluSphere(quadric, 1.5f, 32, 16);                             // Draw A Sphere

        // calculate light's position relative to local coordinate system
        // dunno if this is the best way to do it, but it actually works
        // if u find another aproach, let me know ;)

        // we build the inversed matrix by doing all the actions in reverse order
        // and with reverse parameters (notice -xrot, -yrot, -ObjPos[], etc.)
        gl.glLoadIdentity();                                // Reset Matrix
        gl.glRotatef(-yrot, 0.0f, 1.0f, 0.0f);              // Rotate By -yrot On Y Axis
        gl.glRotatef(-xrot, 1.0f, 0.0f, 0.0f);              // Rotate By -xrot On X Axis
        gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, Minv.e, 0);      // Retrieve ModelView Matrix (Stores In Minv)
        lp.e[0] = LightPos[0];                              // Store Light Position X In lp[0]
        lp.e[1] = LightPos[1];                              // Store Light Position Y In lp[1]
        lp.e[2] = LightPos[2];                              // Store Light Position Z In lp[2]
        lp.e[3] = LightPos[3];                              // Store Light Direction In lp[3]
        VMatMult(Minv, lp);                                 // We Store Rotated Light Vector In 'lp' Array
        gl.glTranslatef(-ObjPos[0], -ObjPos[1], -ObjPos[2]);// Move Negative On All Axis Based On ObjPos[] Values (X, Y, Z)
        gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, Minv.e, 0);      // Retrieve ModelView Matrix From Minv
        wlp.e[0] = 0.0f;                                    // World Local Coord X To 0
        wlp.e[1] = 0.0f;                                    // World Local Coord Y To 0
        wlp.e[2] = 0.0f;                                    // World Local Coord Z To 0
        wlp.e[3] = 1.0f;
        VMatMult(Minv, wlp);                                // We Store The Position Of The World Origin Relative To The
        // Local Coord. System In 'wlp' Array
        lp.e[0] += wlp.e[0];                                // Adding These Two Gives Us The
        lp.e[1] += wlp.e[1];                                // Position Of The Light Relative To
        lp.e[2] += wlp.e[2];                                // The Local Coordinate System

        gl.glColor4f(0.7f, 0.4f, 0.0f, 1.0f);               // Set Color To An Orange
        gl.glLoadIdentity();                                // Reset Modelview Matrix
        gl.glTranslatef(0.0f, 0.0f, -20.0f);                // Zoom Into The Screen 20 Units
        drawRoom(gl);                                       // Draw The Room
        gl.glTranslatef(ObjPos[0], ObjPos[1], ObjPos[2]);   // Position The Object
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);               // Spin It On The X Axis By xrot
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);               // Spin It On The Y Axis By yrot
        object3D.draw(gl);                           // Procedure For Drawing The Loaded Object
        object3D.castShadow(gl, lp.e);                       // Procedure For Casting The Shadow Based On The Silhouette

        gl.glColor4f(0.7f, 0.4f, 0.0f, 1.0f);               // Set Color To Purplish Blue
        gl.glDisable(GL.GL_LIGHTING);                       // Disable Lighting
        gl.glDepthMask(false);                              // Disable Depth Mask
        gl.glTranslatef(lp.e[0], lp.e[1], lp.e[2]);         // Translate To Light's Position
        // Notice We're Still In Local Coordinate System
        glu.gluSphere(quadric, 0.2f, 16, 8);                      // Draw A Little Yellow Sphere (Represents Light)
        gl.glEnable(GL.GL_LIGHTING);                        // Enable Lighting
        gl.glDepthMask(true);                               // Enable Depth Mask

        xrot += xspeed;                                     // Increase xrot By xspeed
        yrot += yspeed;                                     // Increase yrot By yspeed

        gl.glFlush();                                       // Flush The OpenGL Pipeline
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

    private static class GLvector4f {
        float[] e = new float[4];
    };  // Typedef's For VMatMult Procedure
    private static class GLmatrix16f {
        float[] e = new float[16];
    }; // Typedef's For VMatMult Procedure

    private static void VMatMult(GLmatrix16f M, GLvector4f v) {
        float res[] = new float[4];  // Hold Calculated Results
        res[0] = M.e[0] * v.e[0] + M.e[4] * v.e[1] + M.e[8] * v.e[2] + M.e[12] * v.e[3];
        res[1] = M.e[1] * v.e[0] + M.e[5] * v.e[1] + M.e[9] * v.e[2] + M.e[13] * v.e[3];
        res[2] = M.e[2] * v.e[0] + M.e[6] * v.e[1] + M.e[10] * v.e[2] + M.e[14] * v.e[3];
        res[3] = M.e[3] * v.e[0] + M.e[7] * v.e[1] + M.e[11] * v.e[2] + M.e[15] * v.e[3];
        v.e[0] = res[0];               // Results Are Stored Back In v[]
        v.e[1] = res[1];
        v.e[2] = res[2];
        v.e[3] = res[3];               // Homogenous Coordinate
    }
}