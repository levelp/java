package demos.nehe.lesson36;

import com.sun.opengl.util.BufferUtil;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.nio.ByteBuffer;

class Renderer implements GLEventListener {
    private static final int x = 0;										// Define X Coord
    private static final int y = 1;										// Define Y Coord
    private static final int z = 2;										// Define Z Coord


// User Defined Variables
    private float angle; // Used To Rotate The Helix
    private float[][] vertexes = new float[4][3]; // Holds Float Info For 4 Sets Of Vertices
    private float[] normal = new float[3]; // An Array To Store The Normal Data
    private int[] blurTexture = new int[1]; // An Unsigned Int To Store The Texture Number
    private long previousTime = System.currentTimeMillis();

    private GLU glu = new GLU();

    private int emptyTexture(GL gl) {											// Create An Empty Texture
        ByteBuffer data = BufferUtil.newByteBuffer(128 * 128 * 4); // Create Storage Space For Texture Data (128x128x4)
        data.limit(data.capacity());

        int[] txtnumber = new int[1];
        gl.glGenTextures(1, txtnumber, 0);								// Create 1 Texture
        gl.glBindTexture(GL.GL_TEXTURE_2D, txtnumber[0]);					// Bind The Texture
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 4, 128, 128, 0,
                GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, data);						// Build Texture Using Information In data
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

        return txtnumber[0];											// Return The Texture ID
    }

    private void reduceToUnit(float[] vector) {                 // Reduces A Normal Vector (3 Coordinates)
        // To A Unit Normal Vector With A Length Of One.
        // Calculates The Length Of The Vector
        float length = (float) Math.sqrt((vector[0] * vector[0]) + (vector[1] * vector[1]) + (vector[2] * vector[2]));

        if (length == 0.0f)											// Prevents Divide By 0 Error By Providing
            length = 1.0f;											// An Acceptable Value For Vectors To Close To 0.

        vector[0] /= length;										// Dividing Each Element By
        vector[1] /= length;										// The Length Results In A
        vector[2] /= length;										// Unit Normal Vector.
    }

    private void calcNormal(float[][] v/*[3][3]*/, float[] out/*[3]*/) {					// Calculates Normal For A Quad Using 3 Points
        float[] v1 = new float[3];
        float[] v2 = new float[3];											// Vector 1 (x,y,z) & Vector 2 (x,y,z)

        // Finds The Vector Between 2 Points By Subtracting
        // The x,y,z Coordinates From One Point To Another.

        // Calculate The Vector From Point 1 To Point 0
        v1[x] = v[0][x] - v[1][x];									// Vector 1.x=Vertex[0].x-Vertex[1].x
        v1[y] = v[0][y] - v[1][y];									// Vector 1.y=Vertex[0].y-Vertex[1].y
        v1[z] = v[0][z] - v[1][z];									// Vector 1.z=Vertex[0].y-Vertex[1].z
        // Calculate The Vector From Point 2 To Point 1
        v2[x] = v[1][x] - v[2][x];									// Vector 2.x=Vertex[0].x-Vertex[1].x
        v2[y] = v[1][y] - v[2][y];									// Vector 2.y=Vertex[0].y-Vertex[1].y
        v2[z] = v[1][z] - v[2][z];									// Vector 2.z=Vertex[0].z-Vertex[1].z
        // Compute The Cross Product To Give Us A Surface Normal
        out[x] = v1[y] * v2[z] - v1[z] * v2[y];							// Cross Product For Y - Z
        out[y] = v1[z] * v2[x] - v1[x] * v2[z];							// Cross Product For X - Z
        out[z] = v1[x] * v2[y] - v1[y] * v2[x];							// Cross Product For X - Y

        reduceToUnit(out);											// Normalize The Vectors
    }

    private void processHelix(GL gl, GLU glu) {								// Draws A Helix
        float x;													// Helix x Coordinate
        float y;													// Helix y Coordinate
        float z;													// Helix z Coordinate
        float phi;												// Angle
        float theta;												// Angle
        float v,u;												// Angles
        float r;													// Radius Of Twist
        int twists = 5;												// 5 Twists

        float[] glfMaterialColor = new float[]{0.4f, 0.2f, 0.8f, 1.0f};			// Set The Material Color
        float[] specular = new float[]{1.0f, 1.0f, 1.0f, 1.0f};					// Sets Up Specular Lighting

        gl.glLoadIdentity();											// Reset The Modelview Matrix
        glu.gluLookAt(0, 5, 50, 0, 0, 0, 0, 1, 0);						// Eye Position (0,5,50) Center Of Scene (0,0,0), Up On Y Axis

        gl.glPushMatrix();												// Push The Modelview Matrix

        gl.glTranslatef(0, 0, -50);										// Translate 50 Units Into The Screen
        gl.glRotatef(angle / 2.0f, 1, 0, 0);								// Rotate By angle/2 On The X-Axis
        gl.glRotatef(angle / 3.0f, 0, 1, 0);								// Rotate By angle/3 On The Y-Axis

        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, glfMaterialColor, 0);
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, specular, 0);

        r = 1.5f;														// Radius

        gl.glBegin(GL.GL_QUADS);											// Begin Drawing Quads
        for (phi = 0; phi <= 360; phi += 20.0)							// 360 Degrees In Steps Of 20
        {
            for (theta = 0; theta <= 360 * twists; theta += 20.0)			// 360 Degrees * Number Of Twists In Steps Of 20
            {
                v = (phi / 180.0f * 3.142f);								// Calculate Angle Of First Point	(  0 )
                u = (theta / 180.0f * 3.142f);							// Calculate Angle Of First Point	(  0 )

                x = (float) (Math.cos(u) * (2.0f + Math.cos(v))) * r;					// Calculate x Position (1st Point)
                y = (float) (Math.sin(u) * (2.0f + Math.cos(v))) * r;					// Calculate y Position (1st Point)
                z = (float) (((u - (2.0f * 3.142f)) + Math.sin(v)) * r);		// Calculate z Position (1st Point)

                vertexes[0][0] = x;									// Set x Value Of First Vertex
                vertexes[0][1] = y;									// Set y Value Of First Vertex
                vertexes[0][2] = z;									// Set z Value Of First Vertex

                v = (phi / 180.0f * 3.142f);								// Calculate Angle Of Second Point	(  0 )
                u = ((theta + 20) / 180.0f * 3.142f);						// Calculate Angle Of Second Point	( 20 )

                x = (float) (Math.cos(u) * (2.0f + Math.cos(v))) * r;					// Calculate x Position (2nd Point)
                y = (float) (Math.sin(u) * (2.0f + Math.cos(v))) * r;					// Calculate y Position (2nd Point)
                z = (float) (((u - (2.0f * 3.142f)) + Math.sin(v)) * r);		// Calculate z Position (2nd Point)

                vertexes[1][0] = x;									// Set x Value Of Second Vertex
                vertexes[1][1] = y;									// Set y Value Of Second Vertex
                vertexes[1][2] = z;									// Set z Value Of Second Vertex

                v = ((phi + 20) / 180.0f * 3.142f);							// Calculate Angle Of Third Point	( 20 )
                u = ((theta + 20) / 180.0f * 3.142f);						// Calculate Angle Of Third Point	( 20 )

                x = (float) (Math.cos(u) * (2.0f + Math.cos(v))) * r;					// Calculate x Position (3rd Point)
                y = (float) (Math.sin(u) * (2.0f + Math.cos(v))) * r;					// Calculate y Position (3rd Point)
                z = (float) (((u - (2.0f * 3.142f)) + Math.sin(v)) * r);		// Calculate z Position (3rd Point)

                vertexes[2][0] = x;									// Set x Value Of Third Vertex
                vertexes[2][1] = y;									// Set y Value Of Third Vertex
                vertexes[2][2] = z;									// Set z Value Of Third Vertex

                v = ((phi + 20) / 180.0f * 3.142f);							// Calculate Angle Of Fourth Point	( 20 )
                u = ((theta) / 180.0f * 3.142f);							// Calculate Angle Of Fourth Point	(  0 )

                x = (float) (Math.cos(u) * (2.0f + Math.cos(v))) * r;					// Calculate x Position (4th Point)
                y = (float) (Math.sin(u) * (2.0f + Math.cos(v))) * r;					// Calculate y Position (4th Point)
                z = (float) (((u - (2.0f * 3.142f)) + Math.sin(v)) * r);		// Calculate z Position (4th Point)

                vertexes[3][0] = x;									// Set x Value Of Fourth Vertex
                vertexes[3][1] = y;									// Set y Value Of Fourth Vertex
                vertexes[3][2] = z;									// Set z Value Of Fourth Vertex

                calcNormal(vertexes, normal);						// Calculate The Quad Normal

                gl.glNormal3f(normal[0], normal[1], normal[2]);			// Set The Normal

                // Render The Quad
                gl.glVertex3f(vertexes[0][0], vertexes[0][1], vertexes[0][2]);
                gl.glVertex3f(vertexes[1][0], vertexes[1][1], vertexes[1][2]);
                gl.glVertex3f(vertexes[2][0], vertexes[2][1], vertexes[2][2]);
                gl.glVertex3f(vertexes[3][0], vertexes[3][1], vertexes[3][2]);
            }
        }
        gl.glEnd();													// Done Rendering Quads

        gl.glPopMatrix();												// Pop The Matrix
    }

    private void viewOrtho(GL gl)												// Set Up An Ortho View
    {
        gl.glMatrixMode(GL.GL_PROJECTION);								// Select Projection
        gl.glPushMatrix();												// Push The Matrix
        gl.glLoadIdentity();											// Reset The Matrix
        gl.glOrtho(0, 640, 480, 0, -1, 1);							// Select Ortho Mode (640x480)
        gl.glMatrixMode(GL.GL_MODELVIEW);									// Select Modelview Matrix
        gl.glPushMatrix();												// Push The Matrix
        gl.glLoadIdentity();											// Reset The Matrix
    }

    private void viewPerspective(GL gl)											// Set Up A Perspective View
    {
        gl.glMatrixMode(GL.GL_PROJECTION);								// Select Projection
        gl.glPopMatrix();												// Pop The Matrix
        gl.glMatrixMode(GL.GL_MODELVIEW);								// Select Modelview
        gl.glPopMatrix();												// Pop The Matrix
    }

    private void renderToTexture(GL gl, GLU glu)											// Renders To A Texture
    {
        gl.glViewport(0, 0, 128, 128);									// Set Our Viewport (Match Texture Size)

        processHelix(gl, glu);												// Render The Helix

        gl.glBindTexture(GL.GL_TEXTURE_2D, blurTexture[0]);					// Bind To The Blur Texture

        // Copy Our ViewPort To The Blur Texture (From 0,0 To 128,128... No Border)
        gl.glCopyTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_LUMINANCE, 0, 0, 128, 128, 0);

        gl.glClearColor(0.0f, 0.0f, 0.5f, 0.5f);						// Set The Clear Color To Medium Blue
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);			// Clear The Screen And Depth Buffer

        gl.glViewport(0, 0, 640, 480);									// Set Viewport (0,0 to 640x480)
    }

    private void drawBlur(GL gl, int times, float inc)								// Draw The Blurred Image
    {
        float spost = 0.0f;											// Starting Texture Coordinate Offset
        float alpha = 0.2f;											// Starting Alpha Value

        // Disable AutoTexture Coordinates
        gl.glDisable(GL.GL_TEXTURE_GEN_S);
        gl.glDisable(GL.GL_TEXTURE_GEN_T);

        gl.glEnable(GL.GL_TEXTURE_2D);									// Enable 2D Texture Mapping
        gl.glDisable(GL.GL_DEPTH_TEST);									// Disable Depth Testing
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);							// Set Blending Mode
        gl.glEnable(GL.GL_BLEND);											// Enable Blending
        gl.glBindTexture(GL.GL_TEXTURE_2D, blurTexture[0]);					// Bind To The Blur Texture
        viewOrtho(gl);												// Switch To An Ortho View

        float alphainc = alpha / times;									// alphainc=0.2f / Times To Render Blur

        gl.glBegin(GL.GL_QUADS);											// Begin Drawing Quads
        for (int num = 0; num < times; num++)						// Number Of Times To Render Blur
        {
            gl.glColor4f(1.0f, 1.0f, 1.0f, alpha);					// Set The Alpha Value (Starts At 0.2)
            gl.glTexCoord2f(0 + spost, 1 - spost);						// Texture Coordinate	( 0, 1 )
            gl.glVertex2f(0, 0);									// First Vertex		(   0,   0 )

            gl.glTexCoord2f(0 + spost, 0 + spost);						// Texture Coordinate	( 0, 0 )
            gl.glVertex2f(0, 480);									// Second Vertex	(   0, 480 )

            gl.glTexCoord2f(1 - spost, 0 + spost);						// Texture Coordinate	( 1, 0 )
            gl.glVertex2f(640, 480);								// Third Vertex		( 640, 480 )

            gl.glTexCoord2f(1 - spost, 1 - spost);						// Texture Coordinate	( 1, 1 )
            gl.glVertex2f(640, 0);									// Fourth Vertex	( 640,   0 )

            spost += inc;										// Gradually Increase spost (Zooming Closer To Texture Center)
            alpha = alpha - alphainc;							// Gradually Decrease alpha (Gradually Fading Image Out)
        }
        gl.glEnd();													// Done Drawing Quads

        viewPerspective(gl);											// Switch To A Perspective View

        gl.glEnable(GL.GL_DEPTH_TEST);									// Enable Depth Testing
        gl.glDisable(GL.GL_TEXTURE_2D);									// Disable 2D Texture Mapping
        gl.glDisable(GL.GL_TEXTURE_2D);									// Disable 2D Texture Mapping
        gl.glDisable(GL.GL_TEXTURE_2D);									// Disable 2D Texture Mapping
        gl.glDisable(GL.GL_BLEND);										// Disable Blending
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);								// Unbind The Blur Texture
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        // Start Of User Initialization
        angle = 0.0f;											// Set Starting Angle To Zero

        blurTexture[0] = emptyTexture(gl);								// Create Our Empty Texture

        gl.glEnable(GL.GL_DEPTH_TEST);									// Enable Depth Testing

        float[] global_ambient = new float[]{0.2f, 0.2f, 0.2f, 1.0f};		// Set Ambient Lighting To Fairly Dark Light (No Color)
        float[] light0pos = new float[]{0.0f, 5.0f, 10.0f, 1.0f};		// Set The Light Position
        float[] light0ambient = new float[]{0.2f, 0.2f, 0.2f, 1.0f};		// More Ambient Light
        float[] light0diffuse = new float[]{0.3f, 0.3f, 0.3f, 1.0f};		// Set The Diffuse Light A Bit Brighter
        float[] light0specular = new float[]{0.8f, 0.8f, 0.8f, 1.0f};		// Fairly Bright Specular Lighting

        float[] lmodel_ambient = new float[]{0.2f, 0.2f, 0.2f, 1.0f};			// And More Ambient Light
        gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);		// Set The Ambient Light Model

        gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, global_ambient, 0);		// Set The Global Ambient Light Model
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light0pos, 0);				// Set The Lights Position
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, light0ambient, 0);			// Set The Ambient Light
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, light0diffuse, 0);			// Set The Diffuse Light
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, light0specular, 0);			// Set Up Specular Lighting
        gl.glEnable(GL.GL_LIGHTING);										// Enable Lighting
        gl.glEnable(GL.GL_LIGHT0);										// Enable Light0

        gl.glShadeModel(GL.GL_SMOOTH);									// Select Smooth Shading

        gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 128);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);						// Set The Clear Color To Black
    }

    private void update(long milliseconds) {								// Perform Motion Updates Here
        angle += (float) (milliseconds) / 5.0f;						// Update angle Based On The Clock
    }

    public void display(GLAutoDrawable drawable) {
        long currentTime = System.currentTimeMillis();
        update(currentTime - previousTime);
        previousTime = currentTime;

        GL gl = drawable.getGL();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);						// Set The Clear Color To Black
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);		// Clear Screen And Depth Buffer
        gl.glLoadIdentity();											// Reset The View
        renderToTexture(gl, glu);											// Render To A Texture
        processHelix(gl, glu);												// Draw Our Helix
        drawBlur(gl, 25, 0.02f);											// Draw The Blur Effect
        gl.glFlush();													// Flush The GL Rendering Pipeline
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

        glu.gluPerspective(50, (float) width / height, 5, 2000);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable,
                               boolean modeChanged,
                               boolean deviceChanged) {
    }
}