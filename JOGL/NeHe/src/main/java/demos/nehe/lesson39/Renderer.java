package demos.nehe.lesson39;

import com.sun.opengl.util.GLUT;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.text.NumberFormat;
import java.util.Iterator;

class Renderer implements GLEventListener {
    /**
     ConstantVelocity is an object from Physics1.h. It is a container for simulating masses.
     Specifically, it creates a mass and sets its velocity as (1, 0, 0) so that the mass
     moves with 1.0f meters / second in the x direction.
     */
    private ConstantVelocity constantVelocity = new ConstantVelocity();

    /**
     MotionUnderGravitation is an object from Physics1.h. It is a container for simulating masses.
     This object applies gravitation to all masses it contains. This gravitation is set by the
     constructor which is (0.0f, -9.81f, 0.0f) for now (see below). This means a gravitational acceleration
     of 9.81 meter per (second * second) in the negative y direction. MotionUnderGravitation
     creates one mass by default and sets its position to (-10, 0, 0) and its velocity to
     (10, 15, 0)
     */
    private MotionUnderGravitation motionUnderGravitation = new MotionUnderGravitation(new Vector3D(0.0f, -9.81f, 0.0f));

    /**
     MassConnectedWithSpring is an object from Physics1.h. It is a container for simulating masses.
     This object has a member called connectionPos, which is the connection position of the spring
     it simulates. All masses in this container are pulled towards the connectionPos by a spring
     with a constant of stiffness. This constant is set by the constructor and for now it is 2.0
     (see below).
     */
    private MassConnectedWithSpring massConnectedWithSpring = new MassConnectedWithSpring(2.0f);

    private float slowMotionRatio = 10.0f;									// slowMotionRatio Is A Value To Slow Down The Simulation, Relative To Real World Time
    private float timeElapsed = 0;											// Elapsed Time In The Simulation (Not Equal To Real World's Time Unless slowMotionRatio Is 1
    private long previousTime = System.currentTimeMillis();

    private GLUT glut;
    private GLU glu = new GLU();
    private static final int FONT = GLUT.STROKE_MONO_ROMAN;
    private NumberFormat numberFormat;

    public Renderer() {
        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
    }

    public float getSlowMotionRatio() {
        return slowMotionRatio;
    }

    public void setSlowMotionRatio(float slowMotionRatio) {
        this.slowMotionRatio = slowMotionRatio;
    }

    private void glPrint(GL gl, float x, float y, float z, int font, String string)	// Custom GL "Print" Routine
    {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);								// Position Text On The Screen
        gl.glScalef(0.005f, 0.005f, 0.005f);
        int width = glut.glutStrokeLength(font, string);
        gl.glTranslatef(-width, 0, 0);								// Right align text with position
        for (int i = 0; i < string.length(); i++) {
            glut.glutStrokeCharacter(font, string.charAt(i));
        }
        gl.glPopMatrix();
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        glut = new GLUT();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);						// Black Background
        gl.glShadeModel(GL.GL_SMOOTH);									// Select Smooth Shading
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);			// Set Perspective Calculations To Most Accurate
    }

    public void display(GLAutoDrawable drawable) {
        long currentTime = System.currentTimeMillis();
        update(currentTime - previousTime);
        previousTime = currentTime;

        GL gl = drawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);		// Clear Screen And Depth Buffer

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();											// Reset The Modelview Matrix

        // Position Camera 40 Meters Up In Z-Direction.
        // Set The Up Vector In Y-Direction So That +X Directs To Right And +Y Directs To Up On The Window.
        gl.glTranslatef(0, 0, -40);

        // Drawing The Coordinate Plane Starts Here.
        // We Will Draw Horizontal And Vertical Lines With A Space Of 1 Meter Between Them.
        gl.glColor3f(0, 0, 1);										// Draw In Blue
        gl.glBegin(GL.GL_LINES);

        // Draw The Vertical Lines
        for (float x = -20; x <= 20; x += 1.0f)						// x += 1.0f Stands For 1 Meter Of Space In This Example
        {
            gl.glVertex3f(x, 20, 0);
            gl.glVertex3f(x, -20, 0);
        }

        // Draw The Horizontal Lines
        for (float y = -20; y <= 20; y += 1.0f)						// y += 1.0f Stands For 1 Meter Of Space In This Example
        {
            gl.glVertex3f(20, y, 0);
            gl.glVertex3f(-20, y, 0);
        }

        gl.glEnd();
        // Drawing The Coordinate Plane Ends Here.

        // Draw All Masses In constantVelocity Simulation (Actually There Is Only One Mass In This Example Of Code)
        gl.glColor3f(1, 0, 0);										// Draw In Red
        Iterator masses = constantVelocity.getMasses();
        while (masses.hasNext()) {
            Mass mass = (Mass) masses.next();
            Vector3D pos = mass.pos;

            // Center Our Text On The Screen
            glPrint(gl, pos.x, pos.y + 1, pos.z, FONT, "Mass with constant vel");

            gl.glPointSize(4);
            gl.glBegin(GL.GL_POINTS);
            gl.glVertex3f(pos.x, pos.y, pos.z);
            gl.glEnd();
        }
        // Drawing Masses In constantVelocity Simulation Ends Here.

        // Draw All Masses In motionUnderGravitation Simulation (Actually There Is Only One Mass In This Example Of Code)
        gl.glColor3f(1, 1, 0);									// Draw In Yellow
        masses = motionUnderGravitation.getMasses();
        while (masses.hasNext()) {
            Mass mass = (Mass) masses.next();
            Vector3D pos = mass.pos;

            // Center Our Text On The Screen
            glPrint(gl, pos.x, pos.y + 1, pos.z, FONT, "Motion under gravitation");

            gl.glPointSize(4);
            gl.glBegin(GL.GL_POINTS);
            gl.glVertex3f(pos.x, pos.y, pos.z);
            gl.glEnd();
        }
        // Drawing Masses In motionUnderGravitation Simulation Ends Here.

        // Draw All Masses In massConnectedWithSpring Simulation (Actually There Is Only One Mass In This Example Of Code)
        gl.glColor3f(0, 1, 0);										// Draw In Green
        masses = massConnectedWithSpring.getMasses();
        while (masses.hasNext()) {
            Mass mass = (Mass) masses.next();
            Vector3D pos = mass.pos;

            // Center Our Text On The Screen
            glPrint(gl, pos.x, pos.y + 1, pos.z, FONT, "Mass connected with spring");

            gl.glPointSize(4);
            gl.glBegin(GL.GL_POINTS);
            gl.glVertex3f(pos.x, pos.y, pos.z);
            gl.glEnd();

            // Draw A Line From The Mass Position To Connection Position To Represent The Spring
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3f(pos.x, pos.y, pos.z);
            pos = massConnectedWithSpring.connectionPos;
            gl.glVertex3f(pos.x, pos.y, pos.z);
            gl.glEnd();
        }
        // Drawing Masses In massConnectedWithSpring Simulation Ends Here.


        gl.glColor3f(1, 1, 1);									// Draw In White
        glPrint(gl, -5.0f, 14, 0, FONT, "Time elapsed (seconds): " + numberFormat.format(timeElapsed));	// Print timeElapsed
        glPrint(gl, -5.0f, 13, 0, FONT, "Slow motion ratio: " + numberFormat.format(slowMotionRatio));	// Print slowMotionRatio
        glPrint(gl, -5.0f, 12, 0, FONT, "Press F2 for normal motion");
        glPrint(gl, -5.0f, 11, 0, FONT, "Press F3 for slow motion");
    }

    private void update(long milliseconds)								// Perform Motion Updates Here
    {
        // dt Is The Time Interval (As Seconds) From The Previous Frame To The Current Frame.
        // dt Will Be Used To Iterate Simulation Values Such As Velocity And Position Of Masses.

        float dt = milliseconds / 1000.0f;							// Let's Convert Milliseconds To Seconds

        dt /= slowMotionRatio;										// Divide dt By slowMotionRatio And Obtain The New dt

        timeElapsed += dt;											// Iterate Elapsed Time

        float maxPossible_dt = 0.1f;								// Say That The Maximum Possible dt Is 0.1 Seconds
        // This Is Needed So We Do Not Pass Over A Non Precise dt Value

        int numOfIterations = (int) (dt / maxPossible_dt) + 1;		// Calculate Number Of Iterations To Be Made At This Update Depending On maxPossible_dt And dt
        if (numOfIterations != 0)									// Avoid Division By Zero
            dt = dt / numOfIterations;								// dt Should Be Updated According To numOfIterations

        for (int a = 0; a < numOfIterations; ++a)					// We Need To Iterate Simulations "numOfIterations" Times
        {
            constantVelocity.operate(dt);							// Iterate constantVelocity Simulation By dt Seconds
            motionUnderGravitation.operate(dt);					// Iterate motionUnderGravitation Simulation By dt Seconds
            massConnectedWithSpring.operate(dt);					// Iterate massConnectedWithSpring Simulation By dt Seconds
        }
    }

    public void reshape(GLAutoDrawable drawable,
                        int x,
                        int y,
                        int width,
                        int height) {
        GL gl = drawable.getGL();

        height = (height == 0) ? 1 : height;

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(45, (float) width / height, 1, 100);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable,
                               boolean modeChanged,
                               boolean deviceChanged) {
    }
}