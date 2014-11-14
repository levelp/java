package demos.nehe.lesson34;

import demos.common.ResourceRetriever;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.io.IOException;
import java.io.InputStream;

class Renderer implements GLEventListener {
    private static final int MAP_SIZE = 1024;                        // Size Of Our .RAW Height Map (NEW)
    private static final int STEP_SIZE = 16;                        // Width And Height Of Each Quad (NEW)
    private byte[] heightMap = new byte[MAP_SIZE * MAP_SIZE]; // Holds The Height Map Data (NEW)
    private float scaleValue = .15f;                        // Scale Value For The Terrain (NEW)
    private boolean zoomIn;
    private boolean zoomOut;

    private float HEIGHT_RATIO = 1.5f;                        // Ratio That The Y Is Scaled According To The X And Z (NEW)
    private RenderMode renderMode = RenderMode.QUADS;                        // Polygon Flag Set To TRUE By Default (NEW)

    private GLU glu = new GLU();

    public void zoomOut(boolean zoom) {
        zoomOut = zoom;
    }

    public void zoomIn(boolean zoom) {
        zoomIn = zoom;
    }

    public RenderMode getRenderMode() {
        return renderMode;
    }

    public void setRenderMode(RenderMode renderMode) {
        if (renderMode == null)
            throw new IllegalArgumentException();

        this.renderMode = renderMode;
    }

    private void loadRawFile(String strName, byte[] pHeightMap) throws IOException {
        InputStream input = ResourceRetriever.getResourceAsStream(strName);
        readBuffer(input, pHeightMap);
        input.close();

        for (int i = 0; i < pHeightMap.length; i++)
            pHeightMap[i] &= 0xFF;                 //Quick fix
    }

    private static void readBuffer(InputStream in, byte[] buffer) throws IOException {
        int bytesRead = 0;
        int bytesToRead = buffer.length;
        while (bytesToRead > 0) {
            int read = in.read(buffer, bytesRead, bytesToRead);
            bytesRead += read;
            bytesToRead -= read;
        }
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        gl.glShadeModel(GL.GL_SMOOTH);                                      // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);                            // Black Background
        gl.glClearDepth(1.0f);                                              // Depth Buffer Setup
        gl.glEnable(GL.GL_DEPTH_TEST);                                      // Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);                                       // The Type Of Depth Testing To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);          // Really Nice Perspective Calculations

        try {
            loadRawFile("demos/data/models/Terrain.raw", heightMap);  // (NEW)
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderHeightMap(GL gl, byte[] pHeightMap) {              // This Renders The Height Map As Quads
        gl.glBegin(renderMode.getValue());

        for (int X = 0; X < (MAP_SIZE - STEP_SIZE); X += STEP_SIZE)
            for (int Y = 0; Y < (MAP_SIZE - STEP_SIZE); Y += STEP_SIZE) {
                // Get The (X, Y, Z) Value For The Bottom Left Vertex
                int x = X;
                int y = height(pHeightMap, X, Y);
                int z = Y;

                // Set The Color Value Of The Current Vertex
                setVertexColor(gl, pHeightMap, x, z);

                gl.glVertex3i(x, y, z);                          // Send This Vertex To OpenGL To Be Rendered (Integer Points Are Faster)

                // Get The (X, Y, Z) Value For The Top Left Vertex
                x = X;
                y = height(pHeightMap, X, Y + STEP_SIZE);
                z = Y + STEP_SIZE;

                // Set The Color Value Of The Current Vertex
                setVertexColor(gl, pHeightMap, x, z);

                gl.glVertex3i(x, y, z);	                         // Send This Vertex To OpenGL To Be Rendered

                // Get The (X, Y, Z) Value For The Top Right Vertex
                x = X + STEP_SIZE;
                y = height(pHeightMap, X + STEP_SIZE, Y + STEP_SIZE);
                z = Y + STEP_SIZE;

                // Set The Color Value Of The Current Vertex
                setVertexColor(gl, pHeightMap, x, z);
                gl.glVertex3i(x, y, z);                          // Send This Vertex To OpenGL To Be Rendered

                // Get The (X, Y, Z) Value For The Bottom Right Vertex
                x = X + STEP_SIZE;
                y = height(pHeightMap, X + STEP_SIZE, Y);
                z = Y;

                // Set The Color Value Of The Current Vertex
                setVertexColor(gl, pHeightMap, x, z);
                gl.glVertex3i(x, y, z);                          // Send This Vertex To OpenGL To Be Rendered
            }
        gl.glEnd();
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);                // Reset The Color
    }

    private void setVertexColor(GL gl, byte[] pHeightMap, int x, int y) {                 // Sets The Color Value For A Particular Index, Depending On The Height Index
        float fColor = -0.15f + (height(pHeightMap, x, y) / 256.0f);
        // Assign This Blue Shade To The Current Vertex
        gl.glColor3f(0, 0, fColor);
    }

    private int height(byte[] pHeightMap, int X, int Y) {                         // This Returns The Height From A Height Map Index

        int x = X % MAP_SIZE;                                               // Error Check Our x Value
        int y = Y % MAP_SIZE;                                               // Error Check Our y Value
        return pHeightMap[x + (y * MAP_SIZE)] & 0xFF;                        // Index Into Our Height Array And Return The Height
    }

    private void update() {
        if (zoomIn)
            scaleValue += 0.001f;
        if (zoomOut)
            scaleValue -= 0.001f;
    }

    public void display(GLAutoDrawable drawable) {
        update();
        GL gl = drawable.getGL();

        // Clear Color Buffer, Depth Buffer
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();                                 // Reset The Matrix
        //              Position         View      Up Vector
        glu.gluLookAt(212, 60, 194, 186, 55, 171, 0, 1, 0);// This Determines Where The Camera's Position And View Is
        gl.glScalef(scaleValue, scaleValue * HEIGHT_RATIO, scaleValue);
        renderHeightMap(gl, heightMap);                        // Render The Height Map
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