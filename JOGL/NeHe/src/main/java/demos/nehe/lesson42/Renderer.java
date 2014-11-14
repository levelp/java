package demos.nehe.lesson42;

import com.sun.opengl.util.BufferUtil;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLUquadric;
import javax.media.opengl.glu.GLU;

import java.nio.ByteBuffer;

class Renderer implements GLEventListener {
    private static final int MAZE_WIDTH = 128; // Maze Width  (Must Be A Power Of 2)
    private static final int MAZE_HEIGHT = 128; // Maze Height (Must Be A Power Of 2)

    private int mx,my;																// General Loops (Used For Seeking)
    private ByteBuffer tex_data = BufferUtil.newByteBuffer(MAZE_WIDTH * MAZE_HEIGHT * 3);
    private byte[] r = new byte[4];												// Random Colors (4 Red, 4 Green, 4 Blue)
    private byte[] g = new byte[4];
    private byte[] b = new byte[4];
    private GLU glu = new GLU();
    private GLUquadric quadric;
    private float xrot, yrot, zrot;												// Use For Rotation Of Objects

    private long previousTime = System.currentTimeMillis();

    private boolean resetMaze = false;

    public void resetMaze() {
        resetMaze = true;
    }

    private void updateTex(int dmx, int dmy)										// Update Pixel dmx, dmy On The Texture
    {
        tex_data.put(((dmx + (MAZE_WIDTH * dmy)) * 3), (byte) 255);								// Set Red Pixel To Full Bright
        tex_data.put(1 + ((dmx + (MAZE_WIDTH * dmy)) * 3), (byte) 255);								// Set Green Pixel To Full Bright
        tex_data.put(2 + ((dmx + (MAZE_WIDTH * dmy)) * 3), (byte) 255);								// Set Blue Pixel To Full Bright
    }

    private void reset()														// Reset The Maze, Colors, Start Point, Etc
    {
        tex_data.clear();
        while(tex_data.hasRemaining()) {
            tex_data.put((byte)0);
        }
        tex_data.flip();

        for (int loop = 0; loop < 4; loop++)									// Loop So We Can Assign 4 Random Colors
        {
            r[loop] = (byte) ((int) (Math.random() * Integer.MAX_VALUE) % 128 + 128);											// Pick A Random Red Color (Bright)
            g[loop] = (byte) ((int) (Math.random() * Integer.MAX_VALUE) % 128 + 128);											// Pick A Random Green Color (Bright)
            b[loop] = (byte) ((int) (Math.random() * Integer.MAX_VALUE) % 128 + 128);											// Pick A Random Blue Color (Bright)
        }

        setRandomMazePosition();
    }

    private void setRandomMazePosition() {
        mx = (int) Math.round(Math.random() * ((MAZE_WIDTH - 1) / 2)) * 2; // Pick A New Random X Position
        my = (int) Math.round(Math.random() * ((MAZE_HEIGHT - 1) / 2)) * 2; // Pick A New Random Y Position
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        reset();															// Call Reset To Build Our Initial Texture, Etc.

        // Start Of User Initialization
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, MAZE_WIDTH, MAZE_HEIGHT, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, tex_data);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);								// Black Background
        gl.glClearDepth(1.0f);												// Depth Buffer Setup

        gl.glDepthFunc(GL.GL_LEQUAL);											// The Type Of Depth Testing
        gl.glEnable(GL.GL_DEPTH_TEST);											// Enable Depth Testing

        gl.glEnable(GL.GL_COLOR_MATERIAL);										// Enable Color Material (Allows Us To Tint Textures)

        gl.glEnable(GL.GL_TEXTURE_2D);											// Enable Texture Mapping

        quadric = glu.gluNewQuadric();											// Create A Pointer To The Quadric Object
        glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);								// Create Smooth Normals
        glu.gluQuadricTexture(quadric, true);								// Create Texture Coords

        gl.glEnable(GL.GL_LIGHT0);												// Enable Light0 (Default GL Light)
    }

    private void update(float milliseconds)										// Perform Motion Updates Here
    {
        int dir;														// Will Hold Current Direction

        xrot += milliseconds * 0.02f;									// Increase Rotation On The X-Axis
        yrot += milliseconds * 0.03f;									// Increase Rotation On The Y-Axis
        zrot += milliseconds * 0.015f;									// Increase Rotation On The Z-Axis

        // Check To Make Sure We Are Not Trapped (Nowhere Else To Move)
        if ((mx > (MAZE_WIDTH - 4) || (tex_data.get((((mx + 2) + (MAZE_WIDTH * my)) * 3)) != 0)) &&
                (mx < 2 || (tex_data.get((((mx - 2) + (MAZE_WIDTH * my)) * 3)) != 0)) &&
                (my > (MAZE_HEIGHT - 4) || (tex_data.get(((mx + (MAZE_WIDTH * (my + 2))) * 3)) != 0)) &&
                (my < 2 || (tex_data.get(((mx + (MAZE_WIDTH * (my - 2))) * 3)) != 0))
        ) {
            do																// If We Are Trapped
            {
                setRandomMazePosition();
            } while (tex_data.get(((mx + (MAZE_WIDTH * my)) * 3)) == 0);						// Keep Picking A Random Position Until We Find
        }																	// One That Has Already Been Tagged (Safe Starting Point)

        dir = (int) Math.round(Math.random() * 3f);

        if ((dir == 0) && (mx <= (MAZE_WIDTH - 4)))									// If The Direction Is 0 (Right) And We Are Not At The Far Right
        {
            if (tex_data.get((((mx + 2) + (MAZE_WIDTH * my)) * 3)) == 0)						// And If The Room To The Right Has Not Already Been Visited
            {
                updateTex(mx + 1, my);											// Update The Texture To Show Path Cut Out Between Rooms
                mx += 2;														// Move To The Right (Room To The Right)
            }
        }

        if ((dir == 1) && (my <= (MAZE_HEIGHT - 4)))									// If The Direction Is 1 (Down) And We Are Not At The Bottom
        {
            if (tex_data.get(((mx + (MAZE_WIDTH * (my + 2))) * 3)) == 0)						// And If The Room Below Has Not Already Been Visited
            {
                updateTex(mx, my + 1);											// Update The Texture To Show Path Cut Out Between Rooms
                my += 2;														// Move Down (Room Below)
            }
        }

        if ((dir == 2) && (mx >= 2))											// If The Direction Is 2 (Left) And We Are Not At The Far Left
        {
            if (tex_data.get((((mx - 2) + (MAZE_WIDTH * my)) * 3)) == 0)						// And If The Room To The Left Has Not Already Been Visited
            {
                updateTex(mx - 1, my);											// Update The Texture To Show Path Cut Out Between Rooms
                mx -= 2;														// Move To The Left (Room To The Left)
            }
        }

        if ((dir == 3) && (my >= 2))											// If The Direction Is 3 (Up) And We Are Not At The Top
        {
            if (tex_data.get(((mx + (MAZE_WIDTH * (my - 2))) * 3)) == 0)						// And If The Room Above Has Not Already Been Visited
            {
                updateTex(mx, my - 1);											// Update The Texture To Show Path Cut Out Between Rooms
                my -= 2;														// Move Up (Room Above)
            }
        }

        updateTex(mx, my);													// Update Current Room
    }

    public void display(GLAutoDrawable drawable) {
        if (resetMaze) {
            reset();
            resetMaze = false;
        }

        long currentTime = System.currentTimeMillis();
        update(currentTime - previousTime);
        previousTime = currentTime;

        GL gl = drawable.getGL();

        int windowWidth = drawable.getWidth();
        int windowHeight = drawable.getHeight();

        // Update Our Texture... This Is The Key To The Programs Speed... Much Faster Than Rebuilding The Texture Each Time
        gl.glTexSubImage2D(GL.GL_TEXTURE_2D, 0, 0, 0, MAZE_WIDTH, MAZE_HEIGHT, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, tex_data);

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);										// Clear Screen

        for (int loop = 0; loop < 4; loop++)									// Loop To Draw Our 4 Views
        {
            gl.glColor3ub(r[loop], g[loop], b[loop]);							// Assign Color To Current View

            if (loop == 0)													// If We Are Drawing The First Scene
            {
                // Set The Viewport To The Top Left.  It Will Take Up Half The Screen Width And Height
                gl.glViewport(0, windowHeight / 2, windowWidth / 2, windowHeight / 2);
                gl.glMatrixMode(GL.GL_PROJECTION);								// Select The Projection Matrix
                gl.glLoadIdentity();											// Reset The Projection Matrix
                // Set Up Ortho Mode To Fit 1/4 The Screen (Size Of A Viewport)
                glu.gluOrtho2D(0, windowWidth / 2, windowHeight / 2, 0);
            }

            if (loop == 1)													// If We Are Drawing The Second Scene
            {
                // Set The Viewport To The Top Right.  It Will Take Up Half The Screen Width And Height
                gl.glViewport(windowWidth / 2, windowHeight / 2, windowWidth / 2, windowHeight / 2);
                gl.glMatrixMode(GL.GL_PROJECTION);								// Select The Projection Matrix
                gl.glLoadIdentity();											// Reset The Projection Matrix
                // Set Up Perspective Mode To Fit 1/4 The Screen (Size Of A Viewport)
                glu.gluPerspective(45.0, (float) (MAZE_WIDTH) / (float) (MAZE_HEIGHT), 0.1f, 500.0);
            }

            if (loop == 2)													// If We Are Drawing The Third Scene
            {
                // Set The Viewport To The Bottom Right.  It Will Take Up Half The Screen Width And Height
                gl.glViewport(windowWidth / 2, 0, windowWidth / 2, windowHeight / 2);
                gl.glMatrixMode(GL.GL_PROJECTION);								// Select The Projection Matrix
                gl.glLoadIdentity();											// Reset The Projection Matrix
                // Set Up Perspective Mode To Fit 1/4 The Screen (Size Of A Viewport)
                glu.gluPerspective(45.0, (float) (MAZE_WIDTH) / (float) (MAZE_HEIGHT), 0.1f, 500.0);
            }

            if (loop == 3)													// If We Are Drawing The Fourth Scene
            {
                // Set The Viewport To The Bottom Left.  It Will Take Up Half The Screen Width And Height
                gl.glViewport(0, 0, windowWidth / 2, windowHeight / 2);
                gl.glMatrixMode(GL.GL_PROJECTION);								// Select The Projection Matrix
                gl.glLoadIdentity();											// Reset The Projection Matrix
                // Set Up Perspective Mode To Fit 1/4 The Screen (Size Of A Viewport)
                glu.gluPerspective(45.0, (float) (MAZE_WIDTH) / (float) (MAZE_HEIGHT), 0.1f, 500.0);
            }

            gl.glMatrixMode(GL.GL_MODELVIEW);									// Select The Modelview Matrix
            gl.glLoadIdentity();												// Reset The Modelview Matrix

            gl.glClear(GL.GL_DEPTH_BUFFER_BIT);									// Clear Depth Buffer

            if (loop == 0)													// Are We Drawing The First Image?  (Original Texture... Ortho)
            {
                gl.glBegin(GL.GL_QUADS);											// Begin Drawing A Singl.gle Quad
                // We Fill The Entire 1/4 Section With A Singl.gle Textured Quad.
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex2i(windowWidth / 2, 0);
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex2i(0, 0);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex2i(0, windowHeight / 2);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex2i(windowWidth / 2, windowHeight / 2);
                gl.glEnd();													// Done Drawing The Textured Quad
            }

            if (loop == 1)													// Are We Drawing The Second Image?  (3D Texture Mapped Sphere... Perspective)
            {
                gl.glTranslatef(0.0f, 0.0f, -14.0f);								// Move 14 Units Into The Screen

                gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);								// Rotate By xrot On The X-Axis
                gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);								// Rotate By yrot On The Y-Axis
                gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);								// Rotate By zrot On The Z-Axis

                gl.glEnable(GL.GL_LIGHTING);										// Enable Lighting
                glu.gluSphere(quadric, 4.0f, 32, 32);								// Draw A Sphere
                gl.glDisable(GL.GL_LIGHTING);										// Disable Lighting
            }

            if (loop == 2)													// Are We Drawing The Third Image?  (Texture At An Angl.gle... Perspective)
            {
                gl.glTranslatef(0.0f, 0.0f, -2.0f);								// Move 2 Units Into The Screen
                gl.glRotatef(-45.0f, 1.0f, 0.0f, 0.0f);							// Tilt The Quad Below Back 45 Degrees.
                gl.glRotatef(zrot / 1.5f, 0.0f, 0.0f, 1.0f);						// Rotate By zrot/1.5 On The Z-Axis

                gl.glBegin(GL.GL_QUADS);											// Begin Drawing A Singl.gle Quad
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(1.0f, 1.0f, 0.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-1.0f, 1.0f, 0.0f);
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(-1.0f, -1.0f, 0.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.0f, 0.0f);
                gl.glEnd();													// Done Drawing The Textured Quad
            }

            if (loop == 3)													// Are We Drawing The Fourth Image?  (3D Texture Mapped Cylinder... Perspective)
            {
                gl.glTranslatef(0.0f, 0.0f, -7.0f);								// Move 7 Units Into The Screen
                gl.glRotatef(-xrot / 2, 1.0f, 0.0f, 0.0f);							// Rotate By -xrot/2 On The X-Axis
                gl.glRotatef(-yrot / 2, 0.0f, 1.0f, 0.0f);							// Rotate By -yrot/2 On The Y-Axis
                gl.glRotatef(-zrot / 2, 0.0f, 0.0f, 1.0f);							// Rotate By -zrot/2 On The Z-Axis

                gl.glEnable(GL.GL_LIGHTING);										// Enable Lighting
                gl.glTranslatef(0.0f, 0.0f, -2.0f);								// Translate -2 On The Z-Axis (To Rotate Cylinder Around The Center, Not An End)
                glu.gluCylinder(quadric, 1.5f, 1.5f, 4.0f, 32, 16);					// Draw A Cylinder
                gl.glDisable(GL.GL_LIGHTING);										// Disable Lighting
            }
        }

        gl.glFlush();															// Flush The GL.GL Rendering Pipeline
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