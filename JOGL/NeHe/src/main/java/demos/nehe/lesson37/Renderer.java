package demos.nehe.lesson37;

import demos.common.ResourceRetriever;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.nio.FloatBuffer;

import com.sun.opengl.util.BufferUtil;

class Renderer implements GLEventListener {
    private boolean outlineSmooth;                           // Flag To Anti-Alias The Lines ( NEW )
    private boolean outlineDraw = true;                      // Flag To Draw The Outline ( NEW )
    private boolean modelRotate = false;                     // Flag To Rotate The Model ( NEW )

    // User Defined Variables
    private float[] outlineColor = {0.0f, 0.0f, 0.0f};   // Color Of The Lines ( NEW )
    private float outlineWidth = 3f;                     // Width Of The Lines ( NEW )
    private float modelAngle = 0f;                     // Y-Axis Angle Of The Model ( NEW )

    private Polygon[] polyData;                              // Polygon Data ( NEW )
    private Vector lightAngle = new Vector();               // The Direction Of The Light ( NEW )

    private int polyNum = 0;                     // Number Of Polygons ( NEW )
    private int[] shaderTexture = new int[1];            // Storage For One Texture ( NEW )
    private boolean increaseWidth;
    private boolean decreaseWidth;

    private GLU glu = new GLU();

    public void toggleOutlineSmooth() {
        this.outlineSmooth = !outlineSmooth;
    }

    public void toggelOutlineDraw() {
        this.outlineDraw = !outlineDraw;
    }

    public void increaseOutlineWidth(boolean increase) {
        increaseWidth = increase;
    }

    public void decreaseOutlineWidth(boolean decrease) {
        decreaseWidth = decrease;
    }

    public void toggleModelRotation() {
        this.modelRotate = !modelRotate;
    }

    private void readMesh() throws IOException {
        InputStream in = ResourceRetriever.getResourceAsStream("demos/data/models/Model.txt");

        polyNum = byteToInt(readNextFourBytes(in));
        polyData = new Polygon[polyNum];

        for (int i = 0; i < polyData.length; i++) {
            polyData[i] = new Polygon();
            for (int j = 0; j < 3; j++) {
                polyData[i].Verts[j].Nor.X = byteToFloat(readNextFourBytes(in));
                polyData[i].Verts[j].Nor.Y = byteToFloat(readNextFourBytes(in));
                polyData[i].Verts[j].Nor.Z = byteToFloat(readNextFourBytes(in));

                polyData[i].Verts[j].Pos.X = byteToFloat(readNextFourBytes(in));
                polyData[i].Verts[j].Pos.Y = byteToFloat(readNextFourBytes(in));
                polyData[i].Verts[j].Pos.Z = byteToFloat(readNextFourBytes(in));
            }
        }
    }

    private static byte[] readNextFourBytes(InputStream in) throws IOException {
        byte[] bytes = new byte[4];

        for (int i = 0; i < 4; i++)
            bytes[i] = (byte) in.read();
        return bytes;
    }

    private static int byteToInt(byte[] array) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int b = array[i];
            b &= 0xff;
            value |= (b << (i * 8));
        }
        return value;
    }

    private static float byteToFloat(byte[] array) {
        int value = 0;
        for (int i = 3; i >= 0; i--) {
            int b = array[i];
            b &= 0xff;
            value |= (b << (i * 8));
        }
        return Float.intBitsToFloat(value);
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        FloatBuffer shaderData = BufferUtil.newFloatBuffer(96);                        // Storate For The 96 Shader Values ( NEW )

        // Start Of User Initialization
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // Realy Nice perspective calculations
        gl.glClearColor(0.7f, 0.7f, 0.7f, 0.0f);                    // Light Grey Background
        gl.glClearDepth(1.0f);                                      // Depth Buffer Setup

        gl.glEnable(GL.GL_DEPTH_TEST);                              // Enable Depth Testing
        gl.glDepthFunc(GL.GL_LESS);                                 // The Type Of Depth Test To Do

        gl.glShadeModel(GL.GL_SMOOTH);                              // Enables Smooth Color Shading ( NEW )
        gl.glDisable(GL.GL_LINE_SMOOTH);                            // Initially Disable Line Smoothing ( NEW )

        gl.glEnable(GL.GL_CULL_FACE);                               // Enable OpenGL Face Culling ( NEW )

        gl.glDisable(GL.GL_LIGHTING);                               // Disable OpenGL Lighting ( NEW )

        StringBuffer readShaderData = new StringBuffer();

        try {
            InputStream inputStream = ResourceRetriever.getResourceAsStream("demos/data/models/Shader.txt");
            int info;
            while ((info = inputStream.read()) != -1)
                readShaderData.append((char) info);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        StringTokenizer tokenizer = new StringTokenizer(readShaderData.toString());

        while (tokenizer.hasMoreTokens()) {                                      // Loop Though The 32 Greyscale Values ( NEW )
            float value = Float.parseFloat(tokenizer.nextToken());
            shaderData.put(value);
            shaderData.put(value);
            shaderData.put(value);
        }
        shaderData.flip();

        gl.glGenTextures(1, shaderTexture, 0);                           // Get A Free Texture ID ( NEW )
        gl.glBindTexture(GL.GL_TEXTURE_1D, shaderTexture[0]);         // Bind This Texture. From Now On It Will Be 1D ( NEW )

        // For Crying Out Loud Don't Let OpenGL Use Bi/Trilinear Filtering! ( NEW )
        gl.glTexParameteri(GL.GL_TEXTURE_1D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_1D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);

        gl.glTexImage1D(GL.GL_TEXTURE_1D, 0, GL.GL_RGB, 32, 0, GL.GL_RGB, GL.GL_FLOAT, shaderData); // Upload ( NEW )

        lightAngle.X = 0.0f;                                          // Set The X Direction ( NEW )
        lightAngle.Y = 0.0f;                                          // Set The Y Direction ( NEW )
        lightAngle.Z = 1.0f;                                          // Set The Z Direction ( NEW )
        lightAngle.normalize();

        try {
            readMesh();                                                  // Return The Value Of ReadMesh ( NEW )
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void update() {
        if (increaseWidth)
            outlineWidth += 1;                                   // Increase Line Width ( NEW )
        if (decreaseWidth)
            outlineWidth -= 1;                                   // Decrease Line Width ( NEW )
    }

    public void display(GLAutoDrawable drawable) {
        update();

        GL gl = drawable.getGL();
        // Clear Color Buffer, Depth Buffer
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        Matrix TmpMatrix = new Matrix();                            // Temporary MATRIX Structure ( NEW )
        Vector TmpVector = new Vector(),
                TmpNormal = new Vector();                            // Temporary VECTOR Structures ( NEW )

        gl.glLoadIdentity();                                        // Reset The Matrix

        if (outlineSmooth) {                                          // Check To See If We Want Anti-Aliased Lines ( NEW )
            gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);           // Use The Good Calculations ( NEW )
            gl.glEnable(GL.GL_LINE_SMOOTH);                           // Enable Anti-Aliasing ( NEW )
        } else                                                        // We Don't Want Smooth Lines ( NEW )
            gl.glDisable(GL.GL_LINE_SMOOTH);                          // Disable Anti-Aliasing ( NEW )

        gl.glTranslatef(0.0f, 0.0f, -2.0f);                        // Move 2 Units Away From The Screen ( NEW )
        gl.glRotatef(modelAngle, 0.0f, 1.0f, 0.0f);                 // Rotate The Model On It's Y-Axis ( NEW )

        gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, TmpMatrix.Data, 0);     // Get The Generated Matrix ( NEW )

        // Cel-Shading Code //
        gl.glEnable(GL.GL_TEXTURE_1D);                              // Enable 1D Texturing ( NEW )
        gl.glBindTexture(GL.GL_TEXTURE_1D, shaderTexture[0]);       // Bind Our Texture ( NEW )
        gl.glColor3f(1.0f, 1.0f, 1.0f);                             // Set The Color Of The Model ( NEW )

        gl.glBegin(GL.GL_TRIANGLES);                                // Tell OpenGL That We're Drawing Triangles

        for (int i = 0; i < polyNum; i++)                                // Loop Through Each Polygon ( NEW )
            for (int j = 0; j < 3; j++) {                                   // Loop Through Each Vertex ( NEW )

                TmpNormal.X = polyData[i].Verts[j].Nor.X;               // Fill Up The TmpNormal Structure With
                TmpNormal.Y = polyData[i].Verts[j].Nor.Y;               // The Current Vertices' Normal Values ( NEW )
                TmpNormal.Z = polyData[i].Verts[j].Nor.Z;
                TmpMatrix.rotateVector(TmpNormal, TmpVector);         // Rotate This By The Matrix ( NEW )
                TmpVector.normalize();                                   // Normalize The New Normal ( NEW )

                float TmpShade = Vector.dotProduct(TmpVector, lightAngle);           // Calculate The Shade Value ( NEW )
                if (TmpShade < 0.0f)
                    TmpShade = 0.0f;                                      // Clamp The Value to 0 If Negative ( NEW )

                gl.glTexCoord1f(TmpShade);                              // Set The Texture Co-ordinate As The Shade Value ( NEW )
                gl.glVertex3f(polyData[i].Verts[j].Pos.X,
                        polyData[i].Verts[j].Pos.Y,
                        polyData[i].Verts[j].Pos.Z);              // Send The Vertex Position ( NEW )
            }

        gl.glEnd();                                                // Tell OpenGL To Finish Drawing
        gl.glDisable(GL.GL_TEXTURE_1D);                             // Disable 1D Textures ( NEW )

        // Outline Code //
        if (outlineDraw) {                                            // Check To See If We Want To Draw The Outline ( NEW )
            gl.glEnable(GL.GL_BLEND);                                 // Enable Blending ( NEW )
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);// Set The Blend Mode ( NEW )

            gl.glPolygonMode(GL.GL_BACK, GL.GL_LINE);                  // Draw Backfacing Polygons As Wireframes ( NEW )
            gl.glLineWidth(outlineWidth);                             // Set The Line Width ( NEW )
            gl.glCullFace(GL.GL_FRONT);                               // Don't Draw Any Front-Facing Polygons ( NEW )

            gl.glDepthFunc(GL.GL_LEQUAL);                             // Change The Depth Mode ( NEW )
            gl.glColor3fv(outlineColor, 0);                              // Set The Outline Color ( NEW )

            gl.glBegin(GL.GL_TRIANGLES);                              // Tell OpenGL What We Want To Draw

            for (int i = 0; i < polyNum; i++)                              // Loop Through Each Polygon ( NEW )
                for (int j = 0; j < 3; j++)                                  // Loop Through Each Vertex ( NEW )
                    gl.glVertex3f(polyData[i].Verts[j].Pos.X,
                            polyData[i].Verts[j].Pos.Y,
                            polyData[i].Verts[j].Pos.Z);            // Send The Vertex Position ( NEW )

            gl.glEnd();                                               // Tell OpenGL We've Finished
            gl.glDepthFunc(GL.GL_LESS);                               // Reset The Depth-Testing Mode ( NEW )
            gl.glCullFace(GL.GL_BACK);                                // Reset The Face To Be Culled ( NEW )
            gl.glPolygonMode(GL.GL_BACK, GL.GL_FILL);                  // Reset Back-Facing Polygon Drawing Mode ( NEW )
            gl.glDisable(GL.GL_BLEND);                                // Disable Blending ( NEW )
        }

        if (modelRotate)                                              // Check To See If Rotation Is Enabled ( NEW )
            modelAngle += .2f;                                      // Update Angle Based On The Clock
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

    private static class Matrix {
        float Data[] = new float[16];

        public void rotateVector(Vector V, Vector D) {              // Rotate A Vector Using The Supplied Matrix ( NEW )
            D.X = (Data[0] * V.X) + (Data[4] * V.Y) + (Data[8] * V.Z);  // Rotate Around The X Axis ( NEW )
            D.Y = (Data[1] * V.X) + (Data[5] * V.Y) + (Data[9] * V.Z);  // Rotate Around The Y Axis ( NEW )
            D.Z = (Data[2] * V.X) + (Data[6] * V.Y) + (Data[10] * V.Z);  // Rotate Around The Z Axis ( NEW )
        }
    }               // A Structure To Hold An OpenGL Matrix ( NEW )

    // We Use [16] Due To OpenGL's Matrix Format ( NEW )
    private static class Vector {
        float X, Y, Z;

        // Math Functions
        public static float dotProduct(Vector V1, Vector V2) {                       // Calculate The Angle Between The 2 Vectors ( NEW )
            return V1.X * V2.X + V1.Y * V2.Y + V1.Z * V2.Z;             // Return The Angle ( NEW )
        }

        public float magnitude() {                                    // Calculate The Length Of The Vector ( NEW )
            return (float) Math.sqrt(X * X + Y * Y + Z * Z); // Return The Length Of The Vector ( NEW )
        }

        public void normalize() {                                     // Creates A Vector With A Unit Length Of 1 ( NEW )
            float M = magnitude();                                    // Calculate The Length Of The Vector  ( NEW )

            if (M != 0.0f) {                                              // Make Sure We Don't Divide By 0  ( NEW )
                X /= M;                                                 // Normalize The 3 Components  ( NEW )
                Y /= M;
                Z /= M;
            }
        }
    }                               // A Structure To Hold A Single Vector ( NEW )

    private static class Vertex {                                              // A Structure To Hold A Single Vertex ( NEW )
        Vector Nor = new Vector(),                   // Vertex Normal ( NEW )
        Pos = new Vector();                   // Vertex Position ( NEW )
    }

    private static class Polygon {                                             // A Structure To Hold A Single Polygon ( NEW )
        Vertex Verts[] = new Vertex[3];                         // Array Of 3 VERTEX Structures ( NEW )

        public Polygon() {
            for (int i = 0; i < 3; i++)
                Verts[i] = new Vertex();
        }
    }
}