package demos.nehe.lesson25;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.io.*;
import java.util.StringTokenizer;

import demos.common.ResourceRetriever;

class Renderer implements GLEventListener {
    private static final int STEPS = 200;                          // Maximum Number Of Steps

    private float xrot,yrot,zrot;								// X, Y & Z Rotation
    private float xspeed,yspeed,zspeed;						// X, Y & Z Spin Speed
    private boolean increaseXspeed, decreaseXspeed;
    private boolean increaseYspeed, decreaseYspeed;
    private boolean increaseZspeed, decreaseZspeed;
    private float cx,cy,cz = -15;								// X, Y & Z Position
    private boolean moveObjectFarther, moveObjectCloser;
    private boolean moveObjectUp, moveObjectDown;
    private boolean moveObjectLeft, moveObjectRight;

    private int step = 0;							// Step Counter
    private boolean morph = false;								// Default morph To False (Not Morphing)

    private int maxver;										// Will Eventually Hold The Maximum Number Of Vertices
    private Object3D objects[] = new Object3D[4];				// Our 4 Morphable Objects (morph1,2,3 & 4)
    private final Object morphLock = new Object();
    private Object3D helper,sour,dest;							// Helper Object, Source Object, Destination Object

    private GLU glu = new GLU();

    public void morphTo(int morphTarget) {
        if (morphTarget < 1 || morphTarget > 4) {
            throw new IllegalArgumentException("Morph target must lie between 1 and " + objects.length);
        }
        synchronized (morphLock) {
            if (morph)
                return;
            morph = true;
            dest = objects[morphTarget - 1];
        }
    }

    public void increaseXspeed(boolean increaseXspeed) {
        this.increaseXspeed = increaseXspeed;
    }

    public void decreaseXspeed(boolean decreaseXspeed) {
        this.decreaseXspeed = decreaseXspeed;
    }

    public void increaseYspeed(boolean increaseYspeed) {
        this.increaseYspeed = increaseYspeed;
    }

    public void decreaseYspeed(boolean decreaseYspeed) {
        this.decreaseYspeed = decreaseYspeed;
    }

    public void increaseZspeed(boolean increaseZspeed) {
        this.increaseZspeed = increaseZspeed;
    }

    public void decreaseZspeed(boolean decreaseZspeed) {
        this.decreaseZspeed = decreaseZspeed;
    }

    public void moveObjectUp(boolean moveObjectUp) {
        this.moveObjectUp = moveObjectUp;
    }

    public void moveObjectDown(boolean moveObjectDown) {
        this.moveObjectDown = moveObjectDown;
    }

    public void moveObjectLeft(boolean moveObjectLeft) {
        this.moveObjectLeft = moveObjectLeft;
    }

    public void moveObjectRight(boolean moveObjectRight) {
        this.moveObjectRight = moveObjectRight;
    }

    public void moveObjectFarther(boolean moveObjectFarther) {
        this.moveObjectFarther = moveObjectFarther;
    }

    public void moveObjectCloser(boolean moveObjectCloser) {
        this.moveObjectCloser = moveObjectCloser;
    }

    private Object3D loadObject(InputStream inputStream) throws IOException {						// Loads Object From File (name)
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String oneline = in.readLine();
        StringTokenizer tokenizer = new StringTokenizer(oneline);
        tokenizer.nextToken(); // Vertices:
        int nbVertices = Integer.parseInt(tokenizer.nextToken()); // Scans Text For "Vertices: ".  Number After Is Stored In ver
        Object3D object = new Object3D(nbVertices);         // Sets Objects verts Variable To Equal The Value Of ver

        for (int i = 0; i < nbVertices; i++) {								// Loops Through The Vertices
            oneline = in.readLine();                 // Reads In The Next Line Of Text
            tokenizer = new StringTokenizer(oneline);// Searches For 3 Floating Point Numbers, Store In rx,ry & rz
            float rx = Float.parseFloat(tokenizer.nextToken());
            float ry = Float.parseFloat(tokenizer.nextToken());
            float rz = Float.parseFloat(tokenizer.nextToken());
            object.points[i] = new Vertex();
            object.points[i].x = rx;							// Sets Objects (k) points.x Value To rx
            object.points[i].y = ry;							// Sets Objects (k) points.y Value To ry
            object.points[i].z = rz;							// Sets Objects (k) points.z Value To rz
        }

        if (nbVertices > maxver) maxver = nbVertices;							// If ver Is Greater Than maxver Set maxver Equal To ver

        return object;
    }

    void calculate(int i, Vertex a) {								// Calculates Movement Of Points During Morphing
        a.x = (sour.points[i].x - dest.points[i].x) / STEPS;	// a.x Value Equals Source x - Destination x Divided By Steps
        a.y = (sour.points[i].y - dest.points[i].y) / STEPS;	// a.y Value Equals Source y - Destination y Divided By Steps
        a.z = (sour.points[i].z - dest.points[i].z) / STEPS;	// a.z Value Equals Source z - Destination z Divided By Steps
    }														// This Makes Points Move At A Speed So They All Get To Their
    // Destination At The Same Time

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);					// Set The Blending Function For Translucency
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);				// This Will Clear The Background Color To Black
        gl.glClearDepth(1.0);									// Enables Clearing Of The Depth Buffer
        gl.glDepthFunc(GL.GL_LESS);								// The Type Of Depth Test To Do
        gl.glEnable(GL.GL_DEPTH_TEST);							// Enables Depth Testing
        gl.glShadeModel(GL.GL_SMOOTH);							// Enables Smooth Color Shading
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);	// Really Nice Perspective Calculations

        maxver = 0;											// Sets Max Vertices To 0 By Default
        try {
            objects[0] = loadObject(ResourceRetriever.getResourceAsStream("demos/data/models/Sphere.txt"));					// Load The First Object Into morph1 From File sphere.txt
            objects[1] = loadObject(ResourceRetriever.getResourceAsStream("demos/data/models/Torus.txt"));					// Load The Second Object Into morph2 From File torus.txt
            objects[2] = loadObject(ResourceRetriever.getResourceAsStream("demos/data/models/Tube.txt"));					// Load The Third Object Into morph3 From File tube.txt
            helper = loadObject(ResourceRetriever.getResourceAsStream("demos/data/models/Sphere.txt"));// Load sphere.txt Object Into Helper (Used As Starting Point)
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        objects[3] = new Object3D(486);							// Manually Reserver Ram For A 4th 468 Vertice Object (morph4)
        for (int i = 0; i < 486; i++)								// Loop Through All 468 Vertices
        {
            objects[3].points[i] = new Vertex();
            objects[3].points[i].x = (float) (Math.random() - 0.5) * 7;	// morph4 x Point Becomes A Random Float Value From -7 to 7
            objects[3].points[i].y = (float) (Math.random() - 0.5) * 7;	// morph4 y Point Becomes A Random Float Value From -7 to 7
            objects[3].points[i].z = (float) (Math.random() - 0.5) * 7;	// morph4 z Point Becomes A Random Float Value From -7 to 7
        }

        sour = dest = objects[0];									// Source & Destination Are Set To Equal First Object (morph1)
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        drawGLScene(gl);
        update();
    }

    private void drawGLScene(GL gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);	// Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();									// Reset The View
        gl.glTranslatef(cx, cy, cz);								// Translate The The Current Position To Start Drawing
        gl.glRotatef(xrot, 1, 0, 0);								// Rotate On The X Axis By xrot
        gl.glRotatef(yrot, 0, 1, 0);								// Rotate On The Y Axis By yrot
        gl.glRotatef(zrot, 0, 0, 1);								// Rotate On The Z Axis By zrot

        xrot += xspeed;
        yrot += yspeed;
        zrot += zspeed;			// Increase xrot,yrot & zrot by xspeed, yspeed & zspeed

        float tx,ty,tz;									// Temp X, Y & Z Variables
        Vertex q = new Vertex();											// Holds Returned Calculated Values For One Vertex

        gl.glBegin(GL.GL_POINTS);									// Begin Drawing Points
        for (int i = 0; i < maxver; i++) {				// Loop Through All The Verts
            if (morph)
                calculate(i, q);
            else
                q.x = q.y = q.z = 0;	// If morph Is True Calculate Movement Otherwise Movement=0
            helper.points[i].x -= q.x;					// Subtract q.x Units From helper.points[i].x (Move On X Axis)
            helper.points[i].y -= q.y;					// Subtract q.y Units From helper.points[i].y (Move On Y Axis)
            helper.points[i].z -= q.z;					// Subtract q.z Units From helper.points[i].z (Move On Z Axis)
            tx = helper.points[i].x;						// Make Temp X Variable Equal To Helper's X Variable
            ty = helper.points[i].y;						// Make Temp Y Variable Equal To Helper's Y Variable
            tz = helper.points[i].z;						// Make Temp Z Variable Equal To Helper's Z Variable

            gl.glColor3f(0, 1, 1);							// Set Color To A Bright Shade Of Off Blue
            gl.glVertex3f(tx, ty, tz);						// Draw A Point At The Current Temp Values (Vertex)
            gl.glColor3f(0, 0.5f, 1);						// Darken Color A Bit
            tx -= 2 * q.x;
            ty -= 2 * q.y;
            ty -= 2 * q.y;			// Calculate Two Positions Ahead
            gl.glVertex3f(tx, ty, tz);						// Draw A Second Point At The Newly Calculate Position
            gl.glColor3f(0, 0, 1);							// Set Color To A Very Dark Blue
            tx -= 2 * q.x;
            ty -= 2 * q.y;
            ty -= 2 * q.y;			// Calculate Two More Positions Ahead
            gl.glVertex3f(tx, ty, tz);						// Draw A Third Point At The Second New Position
        }												// This Creates A Ghostly Tail As Points Move
        gl.glEnd();											// Done Drawing Points

        // If We're Morphing And We Haven't Gone Through All 200 Steps Increase Our Step Counter
        // Otherwise Set Morphing To False, Make Source=Destination And Set The Step Counter Back To Zero.
        if (morph && step <= STEPS)
            step++;
        else {
            synchronized (morphLock) {
                morph = false;
                sour = dest;
                step = 0;
            }
        }
    }

    private void update() {
        if (increaseZspeed)								// Is Page Up Being Pressed?
            zspeed += 0.001f;								// Increase zspeed

        if (decreaseZspeed)								// Is Page Down Being Pressed?
            zspeed -= 0.001f;								// Decrease zspeed

        if (increaseXspeed)								// Is Page Up Being Pressed?
            xspeed += 0.001f;								// Increase xspeed

        if (decreaseXspeed)									// Is Page Up Being Pressed?
            xspeed -= 0.001f;								// Decrease xspeed

        if (increaseYspeed)								// Is Page Up Being Pressed?
            yspeed += 0.001f;								// Increase yspeed

        if (decreaseYspeed)								// Is Page Up Being Pressed?
            yspeed -= 0.001f;								// Decrease yspeed

        if (moveObjectFarther)									// Is Q Key Being Pressed?
            cz -= 0.01f;										// Move Object Away From Viewer

        if (moveObjectCloser)									// Is Z Key Being Pressed?
            cz += 0.01f;										// Move Object Towards Viewer

        if (moveObjectUp)									// Is W Key Being Pressed?
            cy += 0.01f;										// Move Object Up

        if (moveObjectDown)									// Is S Key Being Pressed?
            cy -= 0.01f;										// Move Object Down

        if (moveObjectRight)									// Is D Key Being Pressed?
            cx += 0.01f;										// Move Object Right

        if (moveObjectLeft)									// Is A Key Being Pressed?
            cx -= 0.01f;										// Move Object Left
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

        glu.gluPerspective(45, (float) width / height, 0.1, 100);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable,
                               boolean modeChanged,
                               boolean deviceChanged) {
    }

    private static class Vertex {											// Structure For 3D Points
        public float x, y, z;									// X, Y & Z Points
    }

    private static class Object3D {
        public Vertex[] points;									// One Vertice (Vertex x,y & z)

        public Object3D(int nbPoints) {
            points = new Vertex[nbPoints];
        }
    }
}