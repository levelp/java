package demos.nehe.lesson10;

import demos.common.ResourceRetriever;
import demos.common.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Renderer implements GLEventListener {
    private final float PI_180 = (float) (Math.PI / 180.0);

    private boolean blendingEnabled;				// Blending ON/OFF

    private float heading;
    private float xpos;
    private float zpos;
    private boolean stepForward;
    private boolean stepBackward;
    private boolean turnRight;
    private boolean turnLeft;

    private float yrot;				// Y Rotation

    private float walkbias = 0;
    private float walkbiasangle = 0;

    private float lookupdown = 0.0f;
    private boolean lookUp;
    private boolean lookDown;

    private int filter;				// Which Filter To Use
    private int[] textures = new int[3];			// Storage For 3 Textures

    private Sector sector1;				// Our Model Goes Here:

    private GLU glu = new GLU();

    public void toggleBlending() {
        blendingEnabled = !blendingEnabled;
    }

    public void switchFilter() {
        filter = (filter + 1) % 3;
    }

    public void stepForward(boolean step) {
        stepForward = step;
    }

    public void stepBackward(boolean step) {
        stepBackward = step;
    }

    public void turnRight(boolean turn) {
        turnRight = turn;
    }

    public void turnLeft(boolean turn) {
        turnLeft = turn;
    }

    public void lookUp(boolean look) {
        lookUp = look;
    }

    public void lookDown(boolean look) {
        lookDown = look;
    }

    private void setupWorld() throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(ResourceRetriever.getResourceAsStream("demos/data/models/World.txt")));
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.trim().length() == 0 || line.trim().startsWith("//"))
                    continue;

                if (line.startsWith("NUMPOLLIES")) {
                    int numTriangles;

                    numTriangles = Integer.parseInt(line.substring(line.indexOf("NUMPOLLIES") + "NUMPOLLIES".length() + 1));
                    sector1 = new Sector(numTriangles);

                    break;
                }
            }

            for (int i = 0; i < sector1.numtriangles; i++) {
                for (int vert = 0; vert < 3; vert++) {

                    while ((line = in.readLine()) != null) {
                        if (line.trim().length() == 0 || line.trim().startsWith("//"))
                            continue;

                        break;
                    }

                    if (line != null) {
                        StringTokenizer st = new StringTokenizer(line, " ");

                        sector1.triangles[i].vertex[vert].x = Float.valueOf(st.nextToken()).floatValue();
                        sector1.triangles[i].vertex[vert].y = Float.valueOf(st.nextToken()).floatValue();
                        sector1.triangles[i].vertex[vert].z = Float.valueOf(st.nextToken()).floatValue();
                        sector1.triangles[i].vertex[vert].u = Float.valueOf(st.nextToken()).floatValue();
                        sector1.triangles[i].vertex[vert].v = Float.valueOf(st.nextToken()).floatValue();
                    }
                }
            }
        } finally {
            if (in != null)
                in.close();
        }
    }

    private void loadGLTextures(GL gl, GLU glu) throws IOException {
        TextureReader.Texture texture = TextureReader.readTexture("demos/data/images/mud.png");

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

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        try {
            loadGLTextures(gl, glu);
            setupWorld();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        gl.glEnable(GL.GL_TEXTURE_2D);							// Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);					// Set The Blending Function For Translucency
        gl.glShadeModel(GL.GL_SMOOTH);                            //Enables Smooth Color Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);               //This Will Clear The Background Color To Black
        gl.glClearDepth(1.0);                                  //Enables Clearing Of The Depth Buffer
        gl.glEnable(GL.GL_DEPTH_TEST);                            //Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);                             //The Type Of Depth Test To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);  // Really Nice Perspective Calculations
    }

    private void update() {
        if (stepForward) {
            xpos -= (float) Math.sin(heading * PI_180) * 0.05f;
            zpos -= (float) Math.cos(heading * PI_180) * 0.05f;
            if (walkbiasangle >= 359.0f) {
                walkbiasangle = 0.0f;
            } else {
                walkbiasangle += 10;
            }
            walkbias = (float) Math.sin(walkbiasangle * PI_180) / 20.0f;
        }

        if (stepBackward) {
            xpos += (float) Math.sin(heading * PI_180) * 0.05f;
            zpos += (float) Math.cos(heading * PI_180) * 0.05f;
            if (walkbiasangle <= 1.0f) {
                walkbiasangle = 359.0f;
            } else {
                walkbiasangle -= 10;
            }
            walkbias = (float) Math.sin(walkbiasangle * PI_180) / 20.0f;
        }

        if (turnRight) {
            heading -= 1.0f;
            yrot = heading;
        }

        if (turnLeft) {
            heading += 1.0f;
            yrot = heading;
        }

        if (lookUp) {
            lookupdown -= 1.0f;
        }

        if (lookDown) {
            lookupdown += 1.0f;
        }
    }

    public void display(GLAutoDrawable drawable) {
        update();
        GL gl = drawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();   	                                     //Reset The View

        if (!blendingEnabled) {
            gl.glDisable(GL.GL_BLEND);
            gl.glEnable(GL.GL_DEPTH_TEST);
        } else {
            gl.glEnable(GL.GL_BLEND);
            gl.glDisable(GL.GL_DEPTH_TEST);
        }

        float x = 0,y = 0,z = 0,u = 0,v = 0;


        float xtrans = -xpos;
        float ztrans = -zpos;
        float ytrans = -walkbias - 0.25f;
        float sceneroty = 360.0f - yrot;

        gl.glRotatef(lookupdown, 1.0f, 0, 0);
        gl.glRotatef(sceneroty, 0, 1.0f, 0);

        gl.glTranslatef(xtrans, ytrans, ztrans);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[filter]);

        // Process Each Triangle
        for (int i = 0; i < sector1.numtriangles; i++) {

            gl.glBegin(GL.GL_TRIANGLES);
            gl.glNormal3f(0.0f, 0.0f, 1.0f);
            x = sector1.triangles[i].vertex[0].x;
            y = sector1.triangles[i].vertex[0].y;
            z = sector1.triangles[i].vertex[0].z;
            u = sector1.triangles[i].vertex[0].u;
            v = sector1.triangles[i].vertex[0].v;
            gl.glTexCoord2f(u, v);
            gl.glVertex3f(x, y, z);

            x = sector1.triangles[i].vertex[1].x;
            y = sector1.triangles[i].vertex[1].y;
            z = sector1.triangles[i].vertex[1].z;
            u = sector1.triangles[i].vertex[1].u;
            v = sector1.triangles[i].vertex[1].v;
            gl.glTexCoord2f(u, v);
            gl.glVertex3f(x, y, z);

            x = sector1.triangles[i].vertex[2].x;
            y = sector1.triangles[i].vertex[2].y;
            z = sector1.triangles[i].vertex[2].z;
            u = sector1.triangles[i].vertex[2].u;
            v = sector1.triangles[i].vertex[2].v;
            gl.glTexCoord2f(u, v);
            gl.glVertex3f(x, y, z);
            gl.glEnd();
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

        glu.gluPerspective(45, (float) width / height, 0.1, 100);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable,
                               boolean modeChanged,
                               boolean deviceChanged) {
    }

    private static class Vertex {
        public float x, y, z;
        public float u, v;
    }

    private static class Triangle {
        public Vertex[] vertex = new Vertex[3];

        public Triangle() {
            for (int i = 0; i < 3; i++)
                vertex[i] = new Vertex();

        }
    }

    private static class Sector {
        public int numtriangles;
        public Triangle[] triangles;

        public Sector(int inTri) {
            numtriangles = inTri;
            triangles = new Triangle[inTri];
            for (int i = 0; i < inTri; i++)
                triangles[i] = new Triangle();
        }
    }
}