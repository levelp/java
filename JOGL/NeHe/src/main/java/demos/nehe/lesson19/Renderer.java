package demos.nehe.lesson19;

import demos.common.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.io.IOException;

class Renderer implements GLEventListener {
    private static final int MAX_PARTICLES = 1000;

    private boolean rainbow = true;				// Rainbow Mode?

    private float slowdown = 2.0f;				// Slow Down Particles
    private boolean speedUp;
    private boolean slowDown;

    private float xspeed;						// Base X Speed (To Allow Keyboard Direction Of Tail)
    private boolean increaseXSpeed;
    private boolean decreaseXSpeed;

    private float yspeed;						// Base Y Speed (To Allow Keyboard Direction Of Tail)
    private boolean increaseYSpeed;
    private boolean decreaseYSpeed;

    private float zoomRate = -40.0f;				// Used To Zoom Out
    private boolean increaseParticleGrowSpeed;
    private boolean decreaseParticleGrowSpeed;

    private int col;						// Current Color Selection
    private int delay;						// Rainbow Effect Delay
    private int[] textures = new int[1];					// Storage For Our Particle Texture

    private boolean doPullLeft = false;
    private boolean doPullRight = false;
    private boolean doPullUp = false;
    private boolean doPullDown = false;
    private boolean doBurst = false;

    private GLU glu = new GLU();

    private static class Particle {
        boolean active;					// Active (Yes/No)
        float life;					// Particle Life
        float fade;					// Fade Speed
        float r;						// Red Value
        float g;						// Green Value
        float b;						// Blue Value
        float x;						// X Position
        float y;						// Y Position
        float z;						// Z Position
        float xi;						// X Direction
        float yi;						// Y Direction
        float zi;						// Z Direction
        float xg;						// X Gravity
        float yg;						// Y Gravity
        float zg;						// Z Gravity
    }

    private Particle[] particles = new Particle[1000];

    private float colors[][] =
            {
                {1.0f, 0.5f, 0.5f}, {1.0f, 0.75f, 0.5f}, {1.0f, 1.0f, 0.5f}, {0.75f, 1.0f, 0.5f},
                {0.5f, 1.0f, 0.5f}, {0.5f, 1.0f, 0.75f}, {0.5f, 1.0f, 1.0f}, {0.5f, 0.75f, 1.0f},
                {0.5f, 0.5f, 1.0f}, {0.75f, 0.5f, 1.0f}, {1.0f, 0.5f, 1.0f}, {1.0f, 0.5f, 0.75f}
            };

    public void increasePullDownwards(boolean pull) {
        doPullDown = pull;
    }

    public void increasePullUpwards(boolean pull) {
        doPullUp = pull;
    }

    public void increasePullLeft(boolean pull) {
        doPullLeft = pull;
    }

    public void increasePullRight(boolean pull) {
        doPullRight = pull;
    }

    public void createBurst() {
        doBurst = true;
    }

    public void slowDown(boolean slowDown) {
        this.slowDown = slowDown;
    }

    public void speedUp(boolean speedUp) {
        this.speedUp = speedUp;
    }

    public void decreaseParticleGrowSpeed(boolean decrease) {
        decreaseParticleGrowSpeed = decrease;
    }

    public void increaseParticleGrowSpeed(boolean increase) {
        increaseParticleGrowSpeed = increase;
    }

    public void toggleRainbowEffect() {
        rainbow = !rainbow;
    }

    public void increaseXSpeed(boolean increase) {
        increaseXSpeed = increase;
    }

    public void decreaseXSpeed(boolean decrease) {
        decreaseXSpeed = decrease;
    }

    public void increaseYSpeed(boolean increase) {
        increaseYSpeed = increase;
    }

    public void decreaseYSpeed(boolean decrease) {
        decreaseYSpeed = decrease;
    }

    public void cycleColor() {
        delay = 0;
        col = (col + 1) % colors.length;
    }

    private void loadGLTextures(GL gl) {                            // Load image And Convert To Textures
        TextureReader.Texture texture = null;
        try {
            texture = TextureReader.readTexture("demos/data/images/Particle.png");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        gl.glGenTextures(1, textures, 0);                  // Create Three Textures
        // Create Nearest Filtered Texture
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        loadGLTextures(gl);

        gl.glShadeModel(GL.GL_SMOOTH);                            //Enables Smooth Color Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);               //This Will Clear The Background Color To Black
        gl.glClearDepth(1.0);                                  //Enables Clearing Of The Depth Buffer
        gl.glDisable(GL.GL_DEPTH_TEST);                           //Disables Depth Testing
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);					// Type Of Blending To Perform
        //gl.glDepthFunc(GL.GL_LEQUAL);                             //The Type Of Depth Test To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);  // Really Nice Perspective Calculations
        gl.glHint(GL.GL_POINT_SMOOTH_HINT, GL.GL_NICEST);				// Really Nice Point Smoothing
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);			// Select Our Texture

        for (int loop = 0; loop < 1000; loop++)				// Initials All The Textures
        {
            particles[loop] = new Particle();
            particles[loop].active = true;								// Make All The Particles Active
            particles[loop].life = 1.0f;								// Give All The Particles Full Life
            particles[loop].fade = (float) (100 * Math.random()) / 1000.0f + 0.003f;	// Random Fade Speed
            particles[loop].r = colors[loop * (12 / MAX_PARTICLES)][0];	// Select Red Rainbow Color
            particles[loop].g = colors[loop * (12 / MAX_PARTICLES)][1];	// Select Red Rainbow Color
            particles[loop].b = colors[loop * (12 / MAX_PARTICLES)][2];	// Select Red Rainbow Color
            particles[loop].xi = (float) ((50 * Math.random()) - 26.0f) * 10.0f;		// Random Speed On X Axis
            particles[loop].yi = (float) ((50 * Math.random()) - 25.0f) * 10.0f;		// Random Speed On Y Axis
            particles[loop].zi = (float) ((50 * Math.random()) - 25.0f) * 10.0f;		// Random Speed On Z Axis
            particles[loop].xg = 0.0f;									// Set Horizontal Pull To Zero
            particles[loop].yg = -0.8f;								// Set Vertical Pull Downward
            particles[loop].zg = 0.0f;									// Set Pull On Z Axis To Zero
        }
    }

    private void update() {
        if (slowDown)
            slowdown = Math.min(4, slowdown + 0.01f);

        if (speedUp)
            slowdown = Math.max(1, slowdown - 0.01f);

        if (decreaseParticleGrowSpeed)
            zoomRate -= 0.1f;

        if (increaseParticleGrowSpeed)
            zoomRate += 0.1f;

        if (increaseXSpeed)
            xspeed = Math.min(200, xspeed + 1);

        if (decreaseXSpeed)
            xspeed = Math.max(-200, xspeed - 1);

        if (increaseYSpeed)
            yspeed = Math.min(200, yspeed + 1);

        if (decreaseYSpeed)
            yspeed = Math.max(-200, yspeed - 1);

        for (int loop = 0; loop < MAX_PARTICLES; loop++)					// Loop Through All The Particles
        {
            if (particles[loop].active)							// If The Particle Is Active
            {

                // If Number Pad 8 And Y Gravity Is Less Than 1.5 Increase Pull Upwards
                if (doPullUp && (particles[loop].yg < 1.5f)) {
                    particles[loop].yg += 0.01f;
                }

                // If Number Pad 2 And Y Gravity Is Greater Than -1.5 Increase Pull Downwards
                if (doPullDown && (particles[loop].yg > -1.5f)) {
                    particles[loop].yg -= 0.01f;
                }

                // If Number Pad 6 And X Gravity Is Less Than 1.5 Increase Pull Right
                if (doPullRight && (particles[loop].xg < 1.5f)) {
                    particles[loop].xg += 0.01f;
                }

                // If Number Pad 4 And X Gravity Is Greater Than -1.5 Increase Pull Left
                if (doPullLeft && (particles[loop].xg > -1.5f)) {
                    particles[loop].xg -= 0.01f;
                }

                if (doBurst)										// Tab Key Causes A Burst
                {
                    particles[loop].x = 0.0f;								// Center On X Axis
                    particles[loop].y = 0.0f;								// Center On Y Axis
                    particles[loop].z = 0.0f;								// Center On Z Axis
                    particles[loop].xi = (float) ((50 * Math.random()) - 26.0f) * 10.0f;	// Random Speed On X Axis
                    particles[loop].yi = (float) ((50 * Math.random()) - 25.0f) * 10.0f;	// Random Speed On Y Axis
                    particles[loop].zi = (float) ((50 * Math.random()) - 25.0f) * 10.0f;	// Random Speed On Z Axis
                }
            }
        }

        doBurst = false;
    }

    public void display(GLAutoDrawable drawable) {
        update();
        GL gl = drawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();                                         //Reset The View
        for (int loop = 0; loop < MAX_PARTICLES; loop++)					// Loop Through All The Particles
        {
            if (particles[loop].active)							// If The Particle Is Active
            {
                float x = particles[loop].x;						// Grab Our Particle X Position
                float y = particles[loop].y;						// Grab Our Particle Y Position
                float z = particles[loop].z + zoomRate;					// Particle Z Pos + Zoom

                // Draw The Particle Using Our RGB Values, Fade The Particle Based On It's Life
                gl.glColor4f(particles[loop].r, particles[loop].g, particles[loop].b, particles[loop].life);

                gl.glBegin(GL.GL_TRIANGLE_STRIP);						// Build Quad From A Triangle Strip
                gl.glTexCoord2d(1, 1);
                gl.glVertex3f(x + 0.5f, y + 0.5f, z); // Top Right
                gl.glTexCoord2d(0, 1);
                gl.glVertex3f(x - 0.5f, y + 0.5f, z); // Top Left
                gl.glTexCoord2d(1, 0);
                gl.glVertex3f(x + 0.5f, y - 0.5f, z); // Bottom Right
                gl.glTexCoord2d(0, 0);
                gl.glVertex3f(x - 0.5f, y - 0.5f, z); // Bottom Left
                gl.glEnd();										// Done Building Triangle Strip

                particles[loop].x += particles[loop].xi / (slowdown * 1000);// Move On The X Axis By X Speed
                particles[loop].y += particles[loop].yi / (slowdown * 1000);// Move On The Y Axis By Y Speed
                particles[loop].z += particles[loop].zi / (slowdown * 1000);// Move On The Z Axis By Z Speed

                particles[loop].xi += particles[loop].xg;			// Take Pull On X Axis Into Account
                particles[loop].yi += particles[loop].yg;			// Take Pull On Y Axis Into Account
                particles[loop].zi += particles[loop].zg;			// Take Pull On Z Axis Into Account
                particles[loop].life -= particles[loop].fade;		// Reduce Particles Life By 'Fade'

                if (particles[loop].life < 0.0f)					// If Particle Is Burned Out
                {
                    particles[loop].life = 1.0f;					// Give It New Life
                    particles[loop].fade = (float) (100 * Math.random()) / 1000.0f + 0.003f;	// Random Fade Value
                    particles[loop].x = 0.0f;						// Center On X Axis
                    particles[loop].y = 0.0f;						// Center On Y Axis
                    particles[loop].z = 0.0f;						// Center On Z Axis
                    particles[loop].xi = xspeed + (float) ((60 * Math.random()) - 32.0f);	// X Axis Speed And Direction
                    particles[loop].yi = yspeed + (float) ((60 * Math.random()) - 30.0f);	// Y Axis Speed And Direction
                    particles[loop].zi = (float) ((60 * Math.random()) - 30.0f);	// Z Axis Speed And Direction
                    particles[loop].r = colors[col][0];			// Select Red From Color Table
                    particles[loop].g = colors[col][1];			// Select Green From Color Table
                    particles[loop].b = colors[col][2];			// Select Blue From Color Table
                }
            }

        }

        delay++;
        if (delay > 25 && rainbow) {
            cycleColor();
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

        glu.gluPerspective(45, (float) width / height, .1, 100);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable,
                               boolean modeChanged,
                               boolean deviceChanged) {
    }
}