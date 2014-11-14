package demos.nehe.lesson20;

import demos.common.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.io.IOException;

class Renderer implements GLEventListener {
    boolean maskingEnabled = true;		// Masking On/Off
    boolean scene;				// Which Scene To Draw

    int[] textures = new int[5];			// Storage For Our Five Textures
    int loop;				// Generic Loop Variable
    float roll;				// Rolling Texture

    private GLU glu = new GLU();

    public Renderer() {
    }

    public void toggleMasking() {
        maskingEnabled = !maskingEnabled;
    }

    public void switchScene() {
        scene = !scene;
    }

    public void loadGLTextures(GL gl) throws IOException {
        String tileNames [] =
                {"demos/data/images/logo.png",
                 "demos/data/images/mask1.png",
                 "demos/data/images/image1.png",
                 "demos/data/images/mask2.png",
                 "demos/data/images/image2.png"
                };

        gl.glGenTextures(5, textures, 0);

        for (int i = 0; i < 5; i++) {
            TextureReader.Texture texture = TextureReader.readTexture(tileNames[i]);
            //Create Nearest Filtered Texture
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

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


        }
    }

    public void init(GLAutoDrawable glDrawable) {
        GL gl = glDrawable.getGL();
        try {
            loadGLTextures(gl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        gl.glShadeModel(GL.GL_SMOOTH);                            //Enables Smooth Color Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);               //This Will Clear The Background Color To Black
        gl.glClearDepth(1.0);                                  //Enables Clearing Of The Depth Buffer
        gl.glEnable(GL.GL_DEPTH_TEST);                            //Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);                             //The Type Of Depth Test To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);  // Really Nice Perspective Calculations
        gl.glEnable(GL.GL_TEXTURE_2D);
    }

    public void display(GLAutoDrawable glDrawable) {
        GL gl = glDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();                                         //Reset The View
        gl.glTranslatef(0.0f, 0.0f, -2.0f);						// Move Into The Screen 5 Units

        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);			// Select Our Logo Texture
        gl.glBegin(GL.GL_QUADS);									// Start Drawing A Textured Quad
        gl.glTexCoord2f(0.0f, -roll + 0.0f);
        gl.glVertex3f(-1.1f, -1.1f, 0.0f);	// Bottom Left
        gl.glTexCoord2f(3.0f, -roll + 0.0f);
        gl.glVertex3f(1.1f, -1.1f, 0.0f);	// Bottom Right
        gl.glTexCoord2f(3.0f, -roll + 3.0f);
        gl.glVertex3f(1.1f, 1.1f, 0.0f);	// Top Right
        gl.glTexCoord2f(0.0f, -roll + 3.0f);
        gl.glVertex3f(-1.1f, 1.1f, 0.0f);	// Top Left
        gl.glEnd();											// Done Drawing The Quad

        gl.glEnable(GL.GL_BLEND);									// Enable Blending
        gl.glDisable(GL.GL_DEPTH_TEST);							// Disable Depth Testing

        if (maskingEnabled)										// Is Masking Enabled?
        {
            gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);				// Blend Screen Color With Zero (Black)
        }

        if (scene)											// Are We Drawing The Second Scene?
        {
            gl.glTranslatef(0.0f, 0.0f, -1.0f);					// Translate Into The Screen One Unit
            gl.glRotatef(roll * 360, 0.0f, 0.0f, 1.0f);				// Rotate On The Z Axis 360 Degrees.
            if (maskingEnabled)									// Is Masking On?
            {
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[3]);	// Select The Second Mask Texture
                gl.glBegin(GL.GL_QUADS);							// Start Drawing A Textured Quad
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(-1.1f, -1.1f, 0.0f);	// Bottom Left
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(1.1f, -1.1f, 0.0f);	// Bottom Right
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(1.1f, 1.1f, 0.0f);	// Top Right
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-1.1f, 1.1f, 0.0f);	// Top Left
                gl.glEnd();									// Done Drawing The Quad
            }

            gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);					// Copy Image 2 Color To The Screen
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);		// Select The Second Image Texture
            gl.glBegin(GL.GL_QUADS);								// Start Drawing A Textured Quad
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.1f, -1.1f, 0.0f);	// Bottom Left
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.1f, -1.1f, 0.0f);	// Bottom Right
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.1f, 1.1f, 0.0f);	// Top Right
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.1f, 1.1f, 0.0f);	// Top Left
            gl.glEnd();										// Done Drawing The Quad
        } else												// Otherwise
        {
            if (maskingEnabled)									// Is Masking On?
            {
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);	// Select The First Mask Texture
                gl.glBegin(GL.GL_QUADS);							// Start Drawing A Textured Quad
                gl.glTexCoord2f(roll + 0.0f, 0.0f);
                gl.glVertex3f(-1.1f, -1.1f, 0.0f);	// Bottom Left
                gl.glTexCoord2f(roll + 4.0f, 0.0f);
                gl.glVertex3f(1.1f, -1.1f, 0.0f);	// Bottom Right
                gl.glTexCoord2f(roll + 4.0f, 4.0f);
                gl.glVertex3f(1.1f, 1.1f, 0.0f);	// Top Right
                gl.glTexCoord2f(roll + 0.0f, 4.0f);
                gl.glVertex3f(-1.1f, 1.1f, 0.0f);	// Top Left
                gl.glEnd();									// Done Drawing The Quad
            }

            gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);					// Copy Image 1 Color To The Screen
            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);		// Select The First Image Texture
            gl.glBegin(GL.GL_QUADS);								// Start Drawing A Textured Quad
            gl.glTexCoord2f(roll + 0.0f, 0.0f);
            gl.glVertex3f(-1.1f, -1.1f, 0.0f);	// Bottom Left
            gl.glTexCoord2f(roll + 4.0f, 0.0f);
            gl.glVertex3f(1.1f, -1.1f, 0.0f);	// Bottom Right
            gl.glTexCoord2f(roll + 4.0f, 4.0f);
            gl.glVertex3f(1.1f, 1.1f, 0.0f);	// Top Right
            gl.glTexCoord2f(roll + 0.0f, 4.0f);
            gl.glVertex3f(-1.1f, 1.1f, 0.0f);	// Top Left
            gl.glEnd();										// Done Drawing The Quad
        }

        gl.glEnable(GL.GL_DEPTH_TEST);							// Enable Depth Testing
        gl.glDisable(GL.GL_BLEND);								// Disable Blending

        roll += 0.002f;										// Increase Our Texture Roll Variable
        if (roll > 1.0f)										// Is Roll Greater Than One
        {
            roll -= 1.0f;										// Subtract 1 From Roll
        }
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
