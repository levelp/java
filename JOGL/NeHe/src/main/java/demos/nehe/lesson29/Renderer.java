package demos.nehe.lesson29;

import demos.common.ResourceRetriever;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.sun.opengl.util.BufferUtil;

class Renderer implements GLEventListener {
    private class TextureImage {
        int width;													// Width Of Image In Pixels
        int height;													// Height Of Image In Pixels
        int format;													// Number Of Bytes Per Pixel
        ByteBuffer data;										// Texture Data
    }

    private float xrot = 0f;				// X Rotation
    private float yrot = 0f;				// Y Rotation
    private float zrot = 0f;				// Y Rotation

    private int textures[] = new int[1];													// Storage For 6 Textures (Modified)

    private GLU glu = new GLU();

    public Renderer() {
    }

    // Allocate An Image Structure And Inside Allocate Its Memory Requirements
    private TextureImage allocateTextureBuffer(int w, int h, int f) {
        TextureImage ti = new TextureImage(); 					// Pointer To Image Struct
        ti.width = w;											// Set Width
        ti.height = h;											// Set Height
        ti.format = f;											// Set Format
        ti.data = BufferUtil.newByteBuffer(w * h * f);
        ti.data.position(0);
        ti.data.limit(ti.data.capacity());
        return ti;													// Return Pointer To Image Struct
    }

    private void readTextureData(String filename, TextureImage buffer) throws IOException {
        int i,j,k,done = 0;
        int stride = buffer.width * buffer.format;				// Size Of A Row (Width * Bytes Per Pixel)

        InputStream inputStream = ResourceRetriever.getResourceAsStream(filename);

        for (i = buffer.height - 1; i >= 0; i--) {				// Loop Through Height (Bottoms Up - Flip Image)
            int p = i * stride;					//
            for (j = 0; j < buffer.width; j++) {			// Loop Through Width
                for (k = 0; k < buffer.format - 1; k++, p++, done++) {
                    buffer.data.put(p, (byte) inputStream.read());								// Read Value From File And Store In Memory
                }
                buffer.data.put(p, (byte) 255);
                p++;									// Store 255 In Alpha Channel And Increase Pointer
            }
        }
        inputStream.close();												// Close The File
    }

    private void buildTexture(GL gl, GLU glu, TextureImage tex) {
        gl.glGenTextures(1, textures, 0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGB, tex.width, tex.height, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, tex.data);
    }

    void blit(TextureImage src, TextureImage dst, int src_xstart, int src_ystart, int src_width, int src_height,
              int dst_xstart, int dst_ystart, boolean blend, int alpha) {
        int i,j,k;
        int s, d;										// Source & Destination

        // Clamp Alpha If Value Is Out Of Range
        if (alpha > 255) alpha = 255;
        if (alpha < 0) alpha = 0;

        d = (dst_ystart * dst.width * dst.format);    // Start Row - dst (Row * Width In Pixels * Bytes Per Pixel)
        s = (src_ystart * src.width * src.format);    // Start Row - src (Row * Width In Pixels * Bytes Per Pixel)

        for (i = 0; i < src_height; i++)							// Height Loop
        {
            s = s + (src_xstart * src.format);						// Move Through Src Data By Bytes Per Pixel
            d = d + (dst_xstart * dst.format);						// Move Through Dst Data By Bytes Per Pixel
            for (j = 0; j < src_width; j++)						// Width Loop
            {
                for (k = 0; k < src.format; k++, d++, s++)		// "n" Bytes At A Time
                {
                    if (blend)										// If Blending Is On
                        dst.data.put(d, (byte) (((src.data.get(s) * alpha) + (dst.data.get(d) * (255 - alpha))) >> 8)); // Multiply Src Data*alpha Add Dst Data*(255-alpha)
                    else											// Keep in 0-255 Range With >> 8
                        dst.data.put(d, src.data.get(s));									// No Blending Just Do A Straight Copy
                }
            }
            d = d + (dst.width - (src_width + dst_xstart)) * dst.format;	// Add End Of Row */
            s = s + (src.width - (src_width + src_xstart)) * src.format;	// Add End Of Row */
        }
    }

    public void init(GLAutoDrawable glDrawable) {
        GL gl = glDrawable.getGL();

        TextureImage t1 = allocateTextureBuffer(256, 256, 4);					// Get An Image Structure
        try {
            readTextureData("demos/data/images/monitor.raw", t1);				// Fill The Image Structure With Data
        } catch (IOException e) {                                   // Nothing Read?
            System.out.println("Could not read data/monitor.raw");
            throw new RuntimeException(e);
        }

        TextureImage t2 = allocateTextureBuffer(256, 256, 4);					// Second Image Structure
        try {
            readTextureData("demos/data/images/gl.raw", t2);					// Fill The Image Structure With Data
        } catch (IOException e) {                                   // Nothing Read?
            System.out.println("Could not read data/gl.raw");
            throw new RuntimeException(e);
        }

        // Image To Blend In, Original Image, Src Start X & Y, Src Width & Height, Dst Location X & Y, Blend Flag, Alpha Value
        blit(t2, t1, 127, 127, 128, 128, 64, 64, true, 127);					// Call The Blitter Routine

        buildTexture(gl, glu, t1);											// Load The Texture Map Into Texture Memory

        gl.glEnable(GL.GL_TEXTURE_2D);									// Enable Texture Mapping

        gl.glShadeModel(GL.GL_SMOOTH);									// Enables Smooth Color Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);						// This Will Clear The Background Color To Black
        gl.glClearDepth(1.0);											// Enables Clearing Of The Depth Buffer
        gl.glEnable(GL.GL_DEPTH_TEST);									// Enables Depth Testing
        gl.glDepthFunc(GL.GL_LESS);										// The Type Of Depth Test To Do
    }

    public void display(GLAutoDrawable glDrawable) {
        GL gl = glDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);		// Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();										// Reset The View
        gl.glTranslatef(0.0f, 0.0f, -5.0f);

        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);

        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        // Back Face
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        // Top Face
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        // Bottom Face
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        // Right Face
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        // Left Face
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        xrot += 0.3f;
        yrot += 0.2f;
        zrot += 0.4f;
    }

    public void reshape(GLAutoDrawable glDrawable, int x, int y, int w, int h) {
        if (h == 0) h = 1;
        GL gl = glDrawable.getGL();

        gl.glViewport(0, 0, w, h);                       // Reset The Current Viewport And Perspective Transformation
        gl.glMatrixMode(GL.GL_PROJECTION);                           // Select The Projection Matrix
        gl.glLoadIdentity();                                      // Reset The Projection Matrix
        glu.gluPerspective(45.0f, (float) w / (float) h, 0.1f, 100.0f);  // Calculate The Aspect Ratio Of The Window
        gl.glMatrixMode(GL.GL_MODELVIEW);                            // Select The Modelview Matrix
        gl.glLoadIdentity();                                      // Reset The ModalView Matrix
    }

    public void displayChanged(GLAutoDrawable glDrawable, boolean b, boolean b1) {
    }
}
