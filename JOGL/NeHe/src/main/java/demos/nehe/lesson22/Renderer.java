package demos.nehe.lesson22;

import demos.common.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.sun.opengl.util.BufferUtil;

class Renderer implements GLEventListener {
    private boolean multitextureSupported;                 // Flag Indicating Whether Multitexturing Is Supported
    private boolean multitextureEnabled = true;                // Use It If It Is Supported?
    private boolean __ARB_ENABLE = true;                // Used To Disable ARB Extensions Entirely
    private boolean embossEnabled = false;                        // Emboss Only, No Basetexture?
    private boolean bumpsEnabled = true;                         // Do Bumpmapping?

    private int[] maxTexelUnits = new int[1];          // Number Of Texel-Pipelines. This Is At Least 1.
    private int[] multiLogo = new int[1];          // Handle For Multitexture-Enabled-Logo
    private int[] invbump = new int[3];          // Inverted Bumpmap
    private int[] textures = new int[3];          // Storage For 3 Textures
    private int[] glLogo = new int[1];          // Handle For OpenGL-Logo
    private int filter = 1;          // Which Filter To Use
    private int[] bump = new int[3];          // Our Bumpmappings

    private float[] lightPosition = {0.0f, 0.0f, 2.0f}; // Position is somewhat in front of screen
    private float[] lightAmbient = {0.2f, 0.2f, 0.2f}; // Ambient Light is 20% white
    private float[] lightDiffuse = {1.0f, 1.0f, 1.0f}; // Diffuse Light is white
    private float MAX_EMBOSS = 8e-3f;               // Maximum Emboss-Translate. Increase To Get Higher Immersion
    // At A Cost Of Lower Quality (More Artifacts Will Occur!)
    private float[] gray = Color.gray.getRGBComponents(null); // Gray Color

    // Data Contains The Faces For The Cube In Format 2xTexCoord, 3xVertex;
    // Note That The Tesselation Of The Cube Is Only Absolute Minimum.

    private float[] data = {0f, 0f, -1f, -1f, 1f, // FRONT FACE
                            1f, 0f, 1f, -1f, 1f,
                            1f, 1f, 1f, 1f, 1f,
                            0f, 1f, -1f, 1f, 1f,

                            1f, 0f, -1f, -1f, -1f, // BACK FACE
                            1f, 1f, -1f, 1f, -1f,
                            0f, 1f, 1f, 1f, -1f,
                            0f, 0f, 1f, -1f, -1f,

                            0f, 1f, -1f, 1f, -1f, // Top Face
                            0f, 0f, -1f, 1f, 1f,
                            1f, 0f, 1f, 1f, 1f,
                            1f, 1f, 1f, 1f, -1f,

                            1f, 1f, -1f, -1f, -1f, // Bottom Face
                            0f, 1f, 1f, -1f, -1f,
                            0f, 0f, 1f, -1f, 1f,
                            1f, 0f, -1f, -1f, 1f,

                            1f, 0f, 1f, -1f, -1f, // Right Face
                            1f, 1f, 1f, 1f, -1f,
                            0f, 1f, 1f, 1f, 1f,
                            0f, 0f, 1f, -1f, 1f,

                            0f, 0f, -1f, -1f, -1f, // Left Face
                            1f, 0f, -1f, -1f, 1f,
                            1f, 1f, -1f, 1f, 1f,
                            0f, 1f, -1f, 1f, -1f};

    private float yspeed;                                // Y Rotation Speed
    private boolean increaseY;
    private boolean decreaseY;

    private float xspeed;                                // X Rotation Speed
    private boolean increaseX;
    private boolean decreaseX;

    private float xrot;                                  // X Rotation
    private float yrot;                                  // Y Rotation

    private float z = -5;                                // Depth Into The Screen
    private boolean zoomIn;
    private boolean zoomOut;

    private GLU glu = new GLU();

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

    public void zoomOut(boolean zoom) {
        zoomOut = zoom;
    }

    public void zoomIn(boolean zoom) {
        zoomIn = zoom;
    }

    public void switchFilter() {
        filter = (filter + 1) % 2;
    }

    public void toggleMultitexture() {
        this.multitextureEnabled = !multitextureEnabled && multitextureSupported;
    }

    public void toggleEmboss() {
        this.embossEnabled = !embossEnabled;
    }

    public void toggleBumps() {
        bumpsEnabled = !bumpsEnabled;
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        gl.glEnable(GL.GL_TEXTURE_2D);                              // Enable Texture Mapping
        gl.glShadeModel(GL.GL_SMOOTH);                              // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);                    // Black Background
        gl.glClearDepth(1.0f);                                      // Depth Buffer Setup
        gl.glEnable(GL.GL_DEPTH_TEST);                              // Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);                               // The Type Of Depth Testing To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // Really Nice Perspective Calculations

        multitextureSupported = initMultitexture(gl);
        try {
            loadGLTextures(gl, glu);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initLights(gl);                                               // Initialize OpenGL Light
    }

    // isMultitextureSupported() Checks At Run-Time If Multitexturing Is Supported
    private boolean initMultitexture(GL gl) {

        String extensions;
        extensions = gl.glGetString(GL.GL_EXTENSIONS);                // Fetch Extension String

        int multiTextureAvailable = extensions.indexOf("GL_ARB_multitexture");
        int textureEnvCombineAvailable = extensions.indexOf("GL_ARB_texture_env_combine");
        if (multiTextureAvailable != -1             // Is Multitexturing Supported?
                && __ARB_ENABLE                                            // Override-Flag
                && textureEnvCombineAvailable != -1) {// Is texture_env_combining Supported?
            gl.glGetIntegerv(GL.GL_MAX_TEXTURE_UNITS, maxTexelUnits, 0);
            return true;
        }
        multitextureEnabled = false;                                        // We Can't Use It If It Isn't Supported!
        return false;
    }

    private void loadGLTextures(GL gl, GLU glu) throws IOException {                                   // Load PNGs And Convert To Textures
        TextureReader.Texture texture = TextureReader.readTexture("demos/data/images/Base.bmp");

        gl.glGenTextures(3, textures, 0);                          // Create Three Textures

        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);        // Create Nearest Filtered Texture
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());

        //                             ========
        // Use GL_RGB8 Instead Of "3" In glTexImage2D. Also Defined By GL: GL_RGBA8 Etc.
        // NEW: Now Creating GL_RGBA8 Textures, Alpha Is 1.0f Where Not Specified By Format.

        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);         // Create Linear Filtered Texture
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());

        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);         // Create MipMapped Texture
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_NEAREST);
        glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());

        texture = TextureReader.readTexture("demos/data/images/Bump.bmp");  // Load The Bumpmaps
        gl.glPixelTransferf(GL.GL_RED_SCALE, 0.5f);              // Scale RGB By 50%, So That We Have Only
        gl.glPixelTransferf(GL.GL_GREEN_SCALE, 0.5f);            // Half Intenstity
        gl.glPixelTransferf(GL.GL_BLUE_SCALE, 0.5f);

        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP);  // No Wrapping, Please!
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
        gl.glTexParameterfv(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_BORDER_COLOR, gray, 0);

        gl.glGenTextures(3, bump, 0);                              // Create Three Textures

        gl.glBindTexture(GL.GL_TEXTURE_2D, bump[0]);            // Create Nearest Filtered Texture
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());

        gl.glBindTexture(GL.GL_TEXTURE_2D, bump[1]);            // Create Linear Filtered Texture
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());

        gl.glBindTexture(GL.GL_TEXTURE_2D, bump[2]);            // Create MipMapped Texture
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_NEAREST);
        glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());

        ByteBuffer pixels = texture.getPixels();
        for (int i = 0; i < pixels.limit(); i++)                  // Invert The Bumpmap
            pixels.put(i, (byte) (255 - pixels.get(i)));
        pixels.flip();

        gl.glGenTextures(3, invbump, 0);                           // Create Three Textures

        gl.glBindTexture(GL.GL_TEXTURE_2D, invbump[0]);         // Create Nearest Filtered Texture
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());

        gl.glBindTexture(GL.GL_TEXTURE_2D, invbump[1]);         // Create Linear Filtered Texture
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());

        gl.glBindTexture(GL.GL_TEXTURE_2D, invbump[2]);         // Create MipMapped Texture
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_NEAREST);
        glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());

        gl.glPixelTransferf(GL.GL_RED_SCALE, 1.0f);              // Scale RGB Back To 100% Again
        gl.glPixelTransferf(GL.GL_GREEN_SCALE, 1.0f);
        gl.glPixelTransferf(GL.GL_BLUE_SCALE, 1.0f);

        texture = TextureReader.readTexture("demos/data/images/OpenGL_Alpha.bmp", true); // Load The Logo-Pngs
        ByteBuffer alpha = BufferUtil.newByteBuffer(texture.getPixels().limit());                            // Create Memory For RGBA8-Texture

        for (int a = 0; a < alpha.capacity(); a += 4)
            alpha.put(a + 3, texture.getPixels().get(a));                                  // Pick Only Red Value As Alpha!

        texture = TextureReader.readTexture("demos/data/images/OpenGL.bmp", true);

        for (int a = 0; a < texture.getPixels().limit(); a += 4) {
            alpha.put(a, texture.getPixels().get(a));                       // R
            alpha.put(a + 1, texture.getPixels().get(a + 1));                       // G
            alpha.put(a + 2, texture.getPixels().get(a + 2));                       // B
        }

        alpha.position(0);
        alpha.limit(alpha.capacity());

        gl.glGenTextures(1, glLogo, 0);                                    // Create One Textures

        gl.glBindTexture(GL.GL_TEXTURE_2D, glLogo[0]);                  // Create Linear Filtered RGBA8-Texture
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, alpha);

        texture = TextureReader.readTexture("demos/data/images/Multi_On_Alpha.bmp", true);// Load The "Extension Enabled"-Logo
        alpha = BufferUtil.newByteBuffer(texture.getPixels().limit());                             // Create Memory For RGBA8-Texture

        for (int a = 0; a < alpha.capacity(); a += 4)
            alpha.put(a + 3, texture.getPixels().get(a));                                  // Pick Only Red Value As Alpha!

        texture = TextureReader.readTexture("demos/data/images/Multi_On.bmp", true);

        for (int a = 0; a < texture.getPixels().limit(); a += 4) {
            alpha.put(a, texture.getPixels().get(a));                       // R
            alpha.put(a + 1, texture.getPixels().get(a + 1));                       // G
            alpha.put(a + 2, texture.getPixels().get(a + 2));                       // B
        }

        alpha.position(0);
        alpha.limit(alpha.capacity());

        gl.glGenTextures(1, multiLogo, 0);                                 // Create One Textures
        gl.glBindTexture(GL.GL_TEXTURE_2D, multiLogo[0]);               // Create Linear Filtered RGBA8-Texture
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, alpha);
    }

    private void initLights(GL gl) {
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightAmbient, 0);       // Load Light-Parameters Into GL_LIGHT1
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPosition, 0);
        gl.glEnable(GL.GL_LIGHT1);
    }

    void doCube(GL gl) {

        int i;
        gl.glBegin(GL.GL_QUADS);

        // Front Face
        gl.glNormal3f(0f, 0f, 1f);
        for (i = 0; i < 4; i++) {
            gl.glTexCoord2f(data[5 * i], data[5 * i + 1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }
        // Back Face
        gl.glNormal3f(0f, 0f, -1f);
        for (i = 4; i < 8; i++) {
            gl.glTexCoord2f(data[5 * i], data[5 * i + 1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }
        // Top Face
        gl.glNormal3f(0f, 1f, 0f);
        for (i = 8; i < 12; i++) {
            gl.glTexCoord2f(data[5 * i], data[5 * i + 1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }
        // Bottom Face
        gl.glNormal3f(0f, -1f, 0f);
        for (i = 12; i < 16; i++) {
            gl.glTexCoord2f(data[5 * i], data[5 * i + 1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }
        // Right face
        gl.glNormal3f(1f, 0f, 0f);
        for (i = 16; i < 20; i++) {
            gl.glTexCoord2f(data[5 * i], data[5 * i + 1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }
        // Left Face
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        for (i = 20; i < 24; i++) {
            gl.glTexCoord2f(data[5 * i], data[5 * i + 1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }
        gl.glEnd();
    }

    void VMatMult(float[] M, float[] v) {

        float res[] = new float[3];

        res[0] = M[0] * v[0] + M[1] * v[1] + M[2] * v[2] + M[3] * v[3];
        res[1] = M[4] * v[0] + M[5] * v[1] + M[6] * v[2] + M[7] * v[3];
        res[2] = M[8] * v[0] + M[9] * v[1] + M[10] * v[2] + M[11] * v[3];

        v[0] = res[0];
        v[1] = res[1];
        v[2] = res[2];
        v[3] = M[15]; // Homogenous Coordinate
    }

    void SetUpBumps(float[] n, float[] c, float[] l, float[] s, float[] t) {

        float v[] = new float[3],  // Vertex From Current Position To Light
                lenQ;                // Used To Normalize

        // Calculate v From Current Vector c To Lightposition And Normalize v
        v[0] = l[0] - c[0];
        v[1] = l[1] - c[1];
        v[2] = l[2] - c[2];

        lenQ = (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        v[0] /= lenQ;
        v[1] /= lenQ;
        v[2] /= lenQ;

        // Project v Such That We Get Two Values Along Each Texture-Coordinat Axis.
        c[0] = (s[0] * v[0] + s[1] * v[1] + s[2] * v[2]) * MAX_EMBOSS;
        c[1] = (t[0] * v[0] + t[1] * v[1] + t[2] * v[2]) * MAX_EMBOSS;
    }

    void doLogo(GL gl) {// MUST CALL THIS LAST!!!, Billboards The Two Logos.

        gl.glDepthFunc(GL.GL_ALWAYS);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL.GL_BLEND);
        gl.glDisable(GL.GL_LIGHTING);
        gl.glLoadIdentity();
        gl.glBindTexture(GL.GL_TEXTURE_2D, glLogo[0]);

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0.23f, -0.4f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(0.53f, -0.4f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(0.53f, -0.25f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0.23f, -0.25f, -1.0f);
        gl.glEnd();

        if (multitextureEnabled) {
            gl.glBindTexture(GL.GL_TEXTURE_2D, multiLogo[0]);
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-0.53f, -0.4f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(-0.33f, -0.4f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(-0.33f, -0.3f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-0.53f, -0.3f, -1.0f);
            gl.glEnd();
        }
        gl.glDepthFunc(GL.GL_LEQUAL);
    }

    void doMesh1TexelUnits(GL gl) {

        float c[] = {0f, 0f, 0f, 1f},     // Holds Current Vertex
                n[] = {0f, 0f, 0f, 1f},     // Normalized Normal Of Current Surface
                s[] = {0f, 0f, 0f, 1f},     // s-Texture Coordinate Direction, Normalized
                t[] = {0f, 0f, 0f, 1f},     // t-Texture Coordinate Direction, Normalized
                l[] = {0f, 0f, 0f, 0f},     // Holds Our Lightposition To Be Transformed Into Object Space
                Minv[] = new float[16];        // Holds The Inverted Modelview Matrix To Do So.
        int i;

        // Build Inverse Modelview Matrix First. This Substitutes One Push/Pop With One glLoadIdentity();
        // Simply Build It By Doing All Transformations Negated And In Reverse Order.
        gl.glLoadIdentity();
        gl.glRotatef(-yrot, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-xrot, 1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, -z);
        gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, Minv, 0);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, z);

        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);

        // Transform The Lightposition Into Object Coordinates:
        l[0] = lightPosition[0];
        l[1] = lightPosition[1];
        l[2] = lightPosition[2];
        l[3] = 1.0f;                           // Homogenous Coordinate
        VMatMult(Minv, l);

        /*  PASS#1: Use Texture "Bump"
            No Blend
            No Lighting
            No Offset Texture-Coordinates
        */

        gl.glBindTexture(GL.GL_TEXTURE_2D, bump[filter]);
        gl.glDisable(GL.GL_BLEND);
        gl.glDisable(GL.GL_LIGHTING);
        doCube(gl);

        /* PASS#2:  Use Texture "Invbump"
            Blend GL_ONE To GL_ONE
            No Lighting
            Offset Texture Coordinates
        */

        gl.glBindTexture(GL.GL_TEXTURE_2D, invbump[filter]);
        gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glEnable(GL.GL_BLEND);

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        n[0] = 0.0f;
        n[1] = 0.0f;
        n[2] = 1.0f;
        s[0] = 1.0f;
        s[1] = 0.0f;
        s[2] = 0.0f;
        t[0] = 0.0f;
        t[1] = 1.0f;
        t[2] = 0.0f;

        for (i = 0; i < 4; i++) {
            c[0] = data[5 * i + 2];
            c[1] = data[5 * i + 3];
            c[2] = data[5 * i + 4];
            SetUpBumps(n, c, l, s, t);
            gl.glTexCoord2f(data[5 * i] + c[0], data[5 * i + 1] + c[1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }

        // Back Face
        n[0] = 0.0f;
        n[1] = 0.0f;
        n[2] = -1.0f;
        s[0] = -1f;
        s[1] = 0.0f;
        s[2] = 0.0f;
        t[0] = 0.0f;
        t[1] = 1.0f;
        t[2] = 0.0f;

        for (i = 4; i < 8; i++) {
            c[0] = data[5 * i + 2];
            c[1] = data[5 * i + 3];
            c[2] = data[5 * i + 4];
            SetUpBumps(n, c, l, s, t);
            gl.glTexCoord2f(data[5 * i] + c[0], data[5 * i + 1] + c[1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }

        // Top Face
        n[0] = 0.0f;
        n[1] = 1.0f;
        n[2] = 0.0f;
        s[0] = 1.0f;
        s[1] = 0.0f;
        s[2] = 0.0f;
        t[0] = 0.0f;
        t[1] = 0.0f;
        t[2] = -1.0f;

        for (i = 8; i < 12; i++) {
            c[0] = data[5 * i + 2];
            c[1] = data[5 * i + 3];
            c[2] = data[5 * i + 4];
            SetUpBumps(n, c, l, s, t);
            gl.glTexCoord2f(data[5 * i] + c[0], data[5 * i + 1] + c[1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }
        // Bottom Face
        n[0] = 0f;
        n[1] = -1f;
        n[2] = 0f;
        s[0] = -1f;
        s[1] = 0f;
        s[2] = 0f;
        t[0] = 0f;
        t[1] = 0f;
        t[2] = -1f;

        for (i = 12; i < 16; i++) {
            c[0] = data[5 * i + 2];
            c[1] = data[5 * i + 3];
            c[2] = data[5 * i + 4];
            SetUpBumps(n, c, l, s, t);
            gl.glTexCoord2f(data[5 * i] + c[0], data[5 * i + 1] + c[1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }
        // Right Face
        n[0] = 1.0f;
        n[1] = 0.0f;
        n[2] = 0.0f;
        s[0] = 0.0f;
        s[1] = 0.0f;
        s[2] = -1.0f;
        t[0] = 0.0f;
        t[1] = 1.0f;
        t[2] = 0.0f;

        for (i = 16; i < 20; i++) {
            c[0] = data[5 * i + 2];
            c[1] = data[5 * i + 3];
            c[2] = data[5 * i + 4];
            SetUpBumps(n, c, l, s, t);
            gl.glTexCoord2f(data[5 * i] + c[0], data[5 * i + 1] + c[1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }
        // Left Face
        n[0] = -1.0f;
        n[1] = 0.0f;
        n[2] = 0.0f;
        s[0] = 0.0f;
        s[1] = 0.0f;
        s[2] = 1.0f;
        t[0] = 0.0f;
        t[1] = 1.0f;
        t[2] = 0.0f;

        for (i = 20; i < 24; i++) {
            c[0] = data[5 * i + 2];
            c[1] = data[5 * i + 3];
            c[2] = data[5 * i + 4];
            SetUpBumps(n, c, l, s, t);
            gl.glTexCoord2f(data[5 * i] + c[0], data[5 * i + 1] + c[1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }
        gl.glEnd();

        /* PASS#3:	Use Texture "Base"
           Blend GL_DST_COLOR To GL_SRC_COLOR (Multiplies By 2)
           Lighting Enabled
           No Offset Texture-Coordinates
         */

        if (!embossEnabled) {
            gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[filter]);
            gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_SRC_COLOR);
            gl.glEnable(GL.GL_LIGHTING);
            doCube(gl);
        }

        xrot += xspeed;
        yrot += yspeed;
        if (xrot > 360.0f) xrot -= 360.0f;
        if (xrot < 0.0f) xrot += 360.0f;
        if (yrot > 360.0f) yrot -= 360.0f;
        if (yrot < 0.0f) yrot += 360.0f;

        doLogo(gl);
    }

    void doMesh2TexelUnits(GL gl) {

        float c[] = {0f, 0f, 0f, 1f},     // Holds Current Vertex
                n[] = {0f, 0f, 0f, 1f},     // Normalized Normal Of Current Surface
                s[] = {0f, 0f, 0f, 1f},     // s-Texture Coordinate Direction, Normalized
                t[] = {0f, 0f, 0f, 1f},     // t-Texture Coordinate Direction, Normalized
                l[] = {0f, 0f, 0f, 0f},     // Holds Our Lightposition To Be Transformed Into Object Space
                Minv[] = new float[16];        // Holds The Inverted Modelview Matrix To Do So.
        int i;

        // Build Inverse Modelview Matrix First. This Substitutes One Push/Pop With One glLoadIdentity();
        // Simply Build It By Doing All Transformations Negated And In Reverse Order.
        gl.glLoadIdentity();
        gl.glRotatef(-yrot, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-xrot, 1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, -z);
        gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, Minv, 0);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, z);

        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);

        // Transform The Lightposition Into Object Coordinates:
        l[0] = lightPosition[0];
        l[1] = lightPosition[1];
        l[2] = lightPosition[2];
        l[3] = 1.0f;                           // Homogenous Coordinate
        VMatMult(Minv, l);

        /* PASS#1: Texel-Unit 0: Use Texture "Bump"
                                 No Blend
                                 No Lighting
                                 No Offset Texture-Coordinates
                                 Texture-Operation "Replace"
                   Texel-Unit 1: Use Texture "Invbump"
                                 No Lighting
                                 Offset Texture Coordinates
                                 Texture-Operation "Replace"
        */

        // TEXTURE-UNIT #0
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBindTexture(GL.GL_TEXTURE_2D, bump[filter]);
        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_COMBINE);
        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_COMBINE_RGB, GL.GL_REPLACE);
        // TEXTURE-UNIT #1:
        gl.glActiveTexture(GL.GL_TEXTURE1);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBindTexture(GL.GL_TEXTURE_2D, invbump[filter]);
        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_COMBINE);
        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_COMBINE_RGB, GL.GL_ADD);
        // General Switches:
        gl.glDisable(GL.GL_BLEND);
        gl.glDisable(GL.GL_LIGHTING);
        gl.glBegin(GL.GL_QUADS);

        // Front Face
        n[0] = 0.0f;
        n[1] = 0.0f;
        n[2] = 1.0f;
        s[0] = 1.0f;
        s[1] = 0.0f;
        s[2] = 0.0f;
        t[0] = 0.0f;
        t[1] = 1.0f;
        t[2] = 0.0f;

        for (i = 0; i < 4; i++) {
            c[0] = data[5 * i + 2];
            c[1] = data[5 * i + 3];
            c[2] = data[5 * i + 4];
            SetUpBumps(n, c, l, s, t);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, data[5 * i], data[5 * i + 1]);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, data[5 * i] + c[0], data[5 * i + 1] + c[1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }

        // Back Face
        n[0] = 0.0f;
        n[1] = 0.0f;
        n[2] = -1.0f;
        s[0] = -1f;
        s[1] = 0.0f;
        s[2] = 0.0f;
        t[0] = 0.0f;
        t[1] = 1.0f;
        t[2] = 0.0f;

        for (i = 4; i < 8; i++) {
            c[0] = data[5 * i + 2];
            c[1] = data[5 * i + 3];
            c[2] = data[5 * i + 4];
            SetUpBumps(n, c, l, s, t);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, data[5 * i], data[5 * i + 1]);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, data[5 * i] + c[0], data[5 * i + 1] + c[1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }

        // Top Face
        n[0] = 0.0f;
        n[1] = 1.0f;
        n[2] = 0.0f;
        s[0] = 1.0f;
        s[1] = 0.0f;
        s[2] = 0.0f;
        t[0] = 0.0f;
        t[1] = 0.0f;
        t[2] = -1.0f;

        for (i = 8; i < 12; i++) {
            c[0] = data[5 * i + 2];
            c[1] = data[5 * i + 3];
            c[2] = data[5 * i + 4];
            SetUpBumps(n, c, l, s, t);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, data[5 * i], data[5 * i + 1]);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, data[5 * i] + c[0], data[5 * i + 1] + c[1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }

        // Bottom Face
        n[0] = 0f;
        n[1] = -1f;
        n[2] = 0f;
        s[0] = -1f;
        s[1] = 0f;
        s[2] = 0f;
        t[0] = 0f;
        t[1] = 0f;
        t[2] = -1f;

        for (i = 12; i < 16; i++) {
            c[0] = data[5 * i + 2];
            c[1] = data[5 * i + 3];
            c[2] = data[5 * i + 4];
            SetUpBumps(n, c, l, s, t);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, data[5 * i], data[5 * i + 1]);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, data[5 * i] + c[0], data[5 * i + 1] + c[1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }

        // Right Face
        n[0] = 1.0f;
        n[1] = 0.0f;
        n[2] = 0.0f;
        s[0] = 0.0f;
        s[1] = 0.0f;
        s[2] = -1.0f;
        t[0] = 0.0f;
        t[1] = 1.0f;
        t[2] = 0.0f;

        for (i = 16; i < 20; i++) {
            c[0] = data[5 * i + 2];
            c[1] = data[5 * i + 3];
            c[2] = data[5 * i + 4];
            SetUpBumps(n, c, l, s, t);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, data[5 * i], data[5 * i + 1]);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, data[5 * i] + c[0], data[5 * i + 1] + c[1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }

        // Left Face
        n[0] = -1.0f;
        n[1] = 0.0f;
        n[2] = 0.0f;
        s[0] = 0.0f;
        s[1] = 0.0f;
        s[2] = 1.0f;
        t[0] = 0.0f;
        t[1] = 1.0f;
        t[2] = 0.0f;

        for (i = 20; i < 24; i++) {
            c[0] = data[5 * i + 2];
            c[1] = data[5 * i + 3];
            c[2] = data[5 * i + 4];
            SetUpBumps(n, c, l, s, t);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE0, data[5 * i], data[5 * i + 1]);
            gl.glMultiTexCoord2f(GL.GL_TEXTURE1, data[5 * i] + c[0], data[5 * i + 1] + c[1]);
            gl.glVertex3f(data[5 * i + 2], data[5 * i + 3], data[5 * i + 4]);
        }
        gl.glEnd();

        /* PASS#2  Use Texture "Base"
                   Blend GL_DST_COLOR To GL_SRC_COLOR (Multiplies By 2)
                   Lighting Enabled
                   No Offset Texture-Coordinates
         */

        gl.glActiveTexture(GL.GL_TEXTURE1);
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glActiveTexture(GL.GL_TEXTURE0);

        if (!embossEnabled) {
            gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[filter]);
            gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_SRC_COLOR);
            gl.glEnable(GL.GL_BLEND);
            gl.glEnable(GL.GL_LIGHTING);
            doCube(gl);
        }

        xrot += xspeed;
        yrot += yspeed;
        if (xrot > 360.0f) xrot -= 360.0f;
        if (xrot < 0.0f) xrot += 360.0f;
        if (yrot > 360.0f) yrot -= 360.0f;
        if (yrot < 0.0f) yrot += 360.0f;

        /* LAST PASS:	Do The Logos! */
        doLogo(gl);
    }

    private void doMeshNoBumps(GL gl) {

        gl.glLoadIdentity();                // Reset The View
        gl.glTranslatef(0.0f, 0.0f, z);

        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);

        gl.glActiveTexture(GL.GL_TEXTURE1);
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glActiveTexture(GL.GL_TEXTURE0);

        gl.glDisable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[filter]);
        gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_SRC_COLOR);
        gl.glEnable(GL.GL_LIGHTING);
        doCube(gl);

        xrot += xspeed;
        yrot += yspeed;
        if (xrot > 360.0f) xrot -= 360.0f;
        if (xrot < 0.0f) xrot += 360.0f;
        if (yrot > 360.0f) yrot -= 360.0f;
        if (yrot < 0.0f) yrot += 360.0f;

        /* LAST PASS:	Do The Logos! */
        doLogo(gl);
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

    public void display(GLAutoDrawable drawable) {
        update();
        GL gl = drawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        if (bumpsEnabled) {
            if (multitextureEnabled && maxTexelUnits[0] > 1)
                doMesh2TexelUnits(gl);
            else
                doMesh1TexelUnits(gl);
        } else
            doMeshNoBumps(gl);
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