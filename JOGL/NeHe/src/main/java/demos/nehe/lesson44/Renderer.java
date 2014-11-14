package demos.nehe.lesson44;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.io.IOException;

import demos.common.TextureReader;

class Renderer implements GLEventListener {
    private boolean infoOn = false;
    private double gCurrentTime;
    private double gStartTime;
    private float gFPS;

    private int gFrames;

    private Camera gCamera;
    private Font gFont;
    private boolean pitchCameraUp = false;
    private boolean pitchCameraDown = false;
    private boolean yawCameraLeft = false;
    private boolean yawCameraRight = false;

    private GLU glu = new GLU();

    public void setInfoDisplayed(boolean infoOn) {
        this.infoOn = infoOn;
    }

    public void moveCameraBackward() {
        gCamera.m_ForwardVelocity = -.01f;// Start moving the camera backwards 0.01 units every frame
    }

    public void moveCameraForward() {
        gCamera.m_ForwardVelocity = .01f; // Start moving the camera forward 0.01 units every frame
    }

    public void stopCamera() {
        gCamera.m_ForwardVelocity = 0.0f; // Stop the camera from moving.
    }

    public void pitchCameraDown(boolean pitchCameraDown) {
        this.pitchCameraDown = pitchCameraDown;
    }

    public void pitchCameraUp(boolean pitchCameraUp) {
        this.pitchCameraUp = pitchCameraUp;
    }

    public void yawCameraLeft(boolean yawCameraLeft) {
        this.yawCameraLeft = yawCameraLeft;
    }

    public void yawCameraRight(boolean yawCameraRight) {
        this.yawCameraRight = yawCameraRight;
    }

    private void loadTexture(String fileName, int[] texid, GLAutoDrawable drawable) throws IOException { // Creates Texture From A Bitmap File
        TextureReader.Texture texture = TextureReader.readTexture(fileName);
        GL gl = drawable.getGL();
        gl.glGenTextures(1, texid, 0);                           // Create The Texture

        gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 4); // Pixel Storage Mode (Word Alignment / 4 Bytes)

        // Typical Texture Generation Using Data From The Bitmap
        gl.glBindTexture(GL.GL_TEXTURE_2D, texid[0]);                                // Bind To The Texture ID
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);  // Linear Min Filter
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);  // Linear Mag Filter
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, texture.getWidth(), texture.getHeight(),
                0, GL.GL_BGR, GL.GL_UNSIGNED_BYTE, texture.getPixels());
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        int[] tex = {0};

        gCamera = new Camera();
        gFont = new Font();

        gl.glShadeModel(GL.GL_SMOOTH);                            // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);                  // Black Background
        gl.glClearDepth(1.0f);                                    // Depth Buffer Setup
        gl.glEnable(GL.GL_DEPTH_TEST);                            // Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);                             // The Type Of Depth Testing To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);// Really Nice Perspective Calculations

        try {
            loadTexture("demos/data/images/Font.bmp", tex, drawable);               // Load the font texture
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (tex[0] != 0) {                                          // Make sure it was loaded
            gFont.setFontTexture(tex[0]);                           // Set the font texture
            gFont.setWindowSize(1024, 768);                         // The font class needs to know the window size
            gFont.buildFont(gl, 1.0f);                                  // Build the font
        }

        gCamera.m_MaxHeadingRate = 1.0f;                          // Set our Maximum rates for the camera
        gCamera.m_HeadingDegrees = 0.0f;                          // Set our Maximum rates for the camera
        gCamera.m_MaxPitchRate = 1.0f;                          // Set our Maximum rates for the camera

        // Try and load the HardGlow texture tell the user if we can't find it then quit
        try {
            loadTexture("demos/data/images/HardGlow2.bmp", gCamera.m_GlowTexture, drawable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Try and load the BigGlow texture tell the user if we can't find it then quit
        try {
            loadTexture("demos/data/images/BigGlow3.bmp", gCamera.m_BigGlowTexture, drawable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Try and load the Halo texture tell the user if we can't find it then quit
        try {
            loadTexture("demos/data/images/Halo3.bmp", gCamera.m_HaloTexture, drawable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Try and load the Streaks texture tell the user if we can't find it then quit
        try {
            loadTexture("demos/data/images/Streaks4.bmp", gCamera.m_StreakTexture, drawable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        gStartTime = System.currentTimeMillis();
    }

    private void update() {
        if (pitchCameraUp)             // Is the W key down?
            gCamera.changePitch(-0.2f);       // Pitch the camera up 0.2 degrees

        if (pitchCameraDown)             // Is the S key down?
            gCamera.changePitch(0.2f);        // Pitch the camera down 0.2 degrees

        if (yawCameraLeft)             // Is the D key down?
            gCamera.changeHeading(0.2f);      // Yaw the camera to the left

        if (yawCameraRight)             // Is the A key down?
            gCamera.changeHeading(-0.2f);     // Yaw the camera to the right
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);  // Clear Screen And Depth Buffer
        gl.glLoadIdentity();                                          // Reset The Current Modelview Matrix

        // We want our light source to be 50 units if front
        // of the camera all the time to make it look like
        // it is infinately far away from the camera. We only
        // do this to the z coordinate because we want to see
        // the flares adjust if we fly in a straight line.
        gCamera.m_LightSourcePos.z = gCamera.m_Position.z - 50.0f;

        gCamera.setPrespective(gl);                                     // Set our perspective/oriention on the world
        gCamera.renderLensFlare(gl);                                    // Render the lens flare
        gCamera.updateFrustumFaster(gl);                                // Update the frustum as fast as possible.
        if (infoOn) {                                                   // Check to see if info has been toggled by 1,2
            drawGLInfo(gl);                                             // Info is on so draw the GL information.
        }

        update();
    }

    private void drawGLInfo(GL gl) {

        float modelMatrix[] = new float[16], // This will hold the model view matrix
                projMatrix[] = new float[16], // This will hold the projection matrix
                DiffTime;                      // This is will contain the difference in time
        String string;                        // A temporary string to use to format information
        // that will be printed to the screen.

        gl.glGetFloatv(GL.GL_PROJECTION_MATRIX, projMatrix, 0); // Grab the projection matrix
        gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, modelMatrix, 0); // Grab the modelview matrix

        // Print out the cameras position
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        string = "m_Position............. = " + twoFracsMax(gCamera.m_Position.x) + ", " + twoFracsMax(gCamera.m_Position.y) + ", " + twoFracsMax(gCamera.m_Position.z);
        gFont.glPrintf(gl, 10, 720, 1, string);

        // Print out the cameras direction
        string = "m_DirectionVector...... = " + twoFracsMax(gCamera.m_DirectionVector.x) + ", " + twoFracsMax(gCamera.m_DirectionVector.y) + ", " + twoFracsMax(gCamera.m_DirectionVector.z);
        gFont.glPrintf(gl, 10, 700, 1, string);

        // Print out the light sources position
        string = "m_LightSourcePos....... = " + twoFracsMax(gCamera.m_LightSourcePos.x) + ", " + twoFracsMax(gCamera.m_LightSourcePos.y) + ", " + twoFracsMax(gCamera.m_LightSourcePos.z);
        gFont.glPrintf(gl, 10, 680, 1, string);

        // Print out the intersection point
        string = "ptIntersect............ = " + twoFracsMax(gCamera.ptIntersect.x) + ", " + twoFracsMax(gCamera.ptIntersect.y) + ", " + twoFracsMax(gCamera.ptIntersect.x);
        gFont.glPrintf(gl, 10, 660, 1, string);

        // Print out the vector that points from the light source to the camera
        string = "vLightSourceToCamera... = " + twoFracsMax(gCamera.vLightSourceToCamera.x) + ", " + twoFracsMax(gCamera.vLightSourceToCamera.y) + ", " + twoFracsMax(gCamera.vLightSourceToCamera.z);
        gFont.glPrintf(gl, 10, 640, 1, string);

        // Print out the vector that points from the light source to the intersection point.
        string = "vLightSourceToIntersect = " + twoFracsMax(gCamera.vLightSourceToIntersect.x) + ", " + twoFracsMax(gCamera.vLightSourceToIntersect.y) + ", " + twoFracsMax(gCamera.vLightSourceToIntersect.z);
        gFont.glPrintf(gl, 10, 620, 1, string);

        // Let everyone know the below matrix is the model view matrix
        string = "GL_MODELVIEW_MATRIX";
        gFont.glPrintf(gl, 10, 580, 1, string);

        // Print out row 1 of the model view matrix
        string = twoFracsMax(modelMatrix[0]) + ", " + twoFracsMax(modelMatrix[1]) + ", " + twoFracsMax(modelMatrix[2]) + ", " + twoFracsMax(modelMatrix[3]);
        gFont.glPrintf(gl, 10, 560, 1, string);

        // Print out row 2 of the model view matrix
        string = twoFracsMax(modelMatrix[4]) + ", " + twoFracsMax(modelMatrix[5]) + ", " + twoFracsMax(modelMatrix[6]) + ", " + twoFracsMax(modelMatrix[7]);
        gFont.glPrintf(gl, 10, 540, 1, string);

        // Print out row 3 of the model view matrix
        string = twoFracsMax(modelMatrix[8]) + ", " + twoFracsMax(modelMatrix[9]) + ", " + twoFracsMax(modelMatrix[10]) + ", " + twoFracsMax(modelMatrix[11]);
        gFont.glPrintf(gl, 10, 520, 1, string);

        // Print out row 4 of the model view matrix
        string = twoFracsMax(modelMatrix[12]) + ", " + twoFracsMax(modelMatrix[13]) + ", " + twoFracsMax(modelMatrix[14]) + ", " + twoFracsMax(modelMatrix[15]);
        gFont.glPrintf(gl, 10, 500, 1, string);

        // Let everyone know the below matrix is the projection matrix
        string = "GL_PROJECTION_MATRIX";
        gFont.glPrintf(gl, 10, 460, 1, string);

        // Print out row 1 of the projection view matrix
        string = twoFracsMax(projMatrix[0]) + ", " + twoFracsMax(projMatrix[1]) + ", " + twoFracsMax(projMatrix[2]) + ", " + twoFracsMax(projMatrix[3]);
        gFont.glPrintf(gl, 10, 440, 1, string);

        // Print out row 2 of the projection view matrix
        string = twoFracsMax(projMatrix[4]) + ", " + twoFracsMax(projMatrix[5]) + ", " + twoFracsMax(projMatrix[6]) + ", " + twoFracsMax(projMatrix[7]);
        gFont.glPrintf(gl, 10, 420, 1, string);

        // Print out row 3 of the projection view matrix
        string = twoFracsMax(projMatrix[8]) + ", " + twoFracsMax(projMatrix[9]) + ", " + twoFracsMax(projMatrix[10]) + ", " + twoFracsMax(projMatrix[11]);
        gFont.glPrintf(gl, 10, 400, 1, string);

        // Print out row 4 of the projection view matrix
        string = twoFracsMax(projMatrix[12]) + ", " + twoFracsMax(projMatrix[13]) + ", " + twoFracsMax(projMatrix[14]) + ", " + twoFracsMax(projMatrix[15]);
        gFont.glPrintf(gl, 10, 380, 1, string);

        // Let everyone know the below values are the Frustum clipping planes
        gFont.glPrintf(gl, 10, 320, 1, "FRUSTUM CLIPPING PLANES");

        // Print out the right clipping plane
        string = twoFracsMax(gCamera.m_Frustum[0][0]) + ", " + twoFracsMax(gCamera.m_Frustum[0][1]) + ", " + twoFracsMax(gCamera.m_Frustum[0][2]) + ", " + twoFracsMax(gCamera.m_Frustum[0][3]);
        gFont.glPrintf(gl, 10, 300, 1, string);

        // Print out the left clipping plane
        string = twoFracsMax(gCamera.m_Frustum[1][0]) + ", " + twoFracsMax(gCamera.m_Frustum[1][1]) + ", " + twoFracsMax(gCamera.m_Frustum[1][2]) + ", " + twoFracsMax(gCamera.m_Frustum[1][3]);
        gFont.glPrintf(gl, 10, 280, 1, string);

        // Print out the bottom clipping plane
        string = twoFracsMax(gCamera.m_Frustum[2][0]) + ", " + twoFracsMax(gCamera.m_Frustum[2][1]) + ", " + twoFracsMax(gCamera.m_Frustum[2][2]) + ", " + twoFracsMax(gCamera.m_Frustum[2][3]);
        gFont.glPrintf(gl, 10, 260, 1, string);

        // Print out the top clipping plane
        string = twoFracsMax(gCamera.m_Frustum[3][0]) + ", " + twoFracsMax(gCamera.m_Frustum[3][1]) + ", " + twoFracsMax(gCamera.m_Frustum[3][2]) + ", " + twoFracsMax(gCamera.m_Frustum[3][3]);
        gFont.glPrintf(gl, 10, 240, 1, string);

        // Print out the far clipping plane
        string = twoFracsMax(gCamera.m_Frustum[4][0]) + ", " + twoFracsMax(gCamera.m_Frustum[4][1]) + ", " + twoFracsMax(gCamera.m_Frustum[4][2]) + ", " + twoFracsMax(gCamera.m_Frustum[4][3]);
        gFont.glPrintf(gl, 10, 220, 1, string);

        // Print out the near clipping plane
        string = twoFracsMax(gCamera.m_Frustum[5][0]) + ", " + twoFracsMax(gCamera.m_Frustum[5][1]) + ", " + twoFracsMax(gCamera.m_Frustum[5][2]) + ", " + twoFracsMax(gCamera.m_Frustum[5][3]);
        gFont.glPrintf(gl, 10, 200, 1, string);

        if (gFrames >= 100) {                                         // if we are due for another FPS update
            gCurrentTime = System.currentTimeMillis();                // Get the current time
            DiffTime = (float) (gCurrentTime - gStartTime);            // Find the difference between the start and end times
            gFPS = (gFrames / DiffTime) * 1000.0f;                    // Compute the FPS
            gStartTime = gCurrentTime;                                // Set the current start time to the current time
            gFrames = 1;                                              // Set the number of frames to 1
        } else {
            gFrames++;                                                // We are not due to for another update so add one to the frame count
        }

        // Print out the FPS
        string = "FPS " + twoFracsMax(gFPS);
        gFont.glPrintf(gl, 10, 160, 1, string);
    }

    private float twoFracsMax(float f) {
        return (int) (100 * f) / 100f;
    }

    public void reshape(GLAutoDrawable drawable,
                        int xstart,
                        int ystart,
                        int width,
                        int height) {
        GL gl = drawable.getGL();

        gCamera.m_WindowHeight = height;  // The camera needs to know the window height
        gCamera.m_WindowWidth = width;   // The camera needs to know the window width

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