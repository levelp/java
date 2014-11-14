package demos.nehe.lesson21;

import com.sun.opengl.util.BufferUtil;
import demos.common.ResourceRetriever;
import demos.common.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

class Renderer implements GLEventListener {
    private Random random = new Random();
    private boolean[][] vline = new boolean[11][11];										// Keeps Track Of Verticle Lines
    private boolean[][] hline = new boolean[11][11];										// Keeps Track Of Horizontal Lines
    private boolean filled;												// Done Filling In The Grid?
    private boolean gameover;											// Is The Game Over?
    private boolean anti = true;											// Antialiasing?

    private float delay;												// Enemy Delay
    private int adjust = 3;											// Speed Adjustment For Really Slow Video Cards
    private int lives = 5;											// Player Lives
    private int level = 1;											// Internal Game Level
    private int level2 = level;										// Displayed Game Level
    private int stage = 1;											// Game Stage

    private GameObject player = new GameObject();										// Player Information
    private GameObject[] enemy = new GameObject[9];									// Enemy Information
    private GameObject hourglass = new GameObject();									// Hourglass Information

    private int steps[] = {1, 2, 4, 5, 10, 20};					// Stepping Values For Slow Video Adjustment

    private int textures[] = new int[2];											// Font Texture Storage Space
    private int base;
    private boolean resetGame = false;
    private boolean moveRight = false;
    private boolean moveLeft = false;
    private boolean moveDown = false;
    private boolean moveUp = false;
    private AudioSample dieSample;
    private AudioSample hourglassSample;
    private AudioSample freezeSample;
    private AudioSample completerSample;

    private ByteBuffer stringBuffer = BufferUtil.newByteBuffer(256);

    private long lastUpdateTime;

    public void moveUp(boolean move) {
        moveUp = move;
    }

    public void moveDown(boolean move) {
        moveDown = move;
    }

    public void moveLeft(boolean move) {
        moveLeft = move;
    }

    public void moveRight(boolean move) {
        moveRight = move;
    }

    public void resetGame() {
        resetGame = true;
    }

    private void resetObjects() {										// Reset Player And Enemies
        player.x = 0;												// Reset Player X Position To Far Left Of The Screen
        player.y = 0;												// Reset Player Y Position To The Top Of The Screen
        player.fx = 0;											// Set Fine X Position To Match
        player.fy = 0;											// Set Fine Y Position To Match

        for (int i = 0; i < (stage * level); i++) {				// Loop Through All The Enemies
            enemy[i] = new GameObject();
            enemy[i].x = 5 + (int) (Math.random() * 6);							// Select A Random X Position
            enemy[i].y = (int) Math.random() * 11;							// Select A Random Y Position
            enemy[i].fx = enemy[i].x * 60;					// Set Fine X To Match
            enemy[i].fy = enemy[i].y * 40;					// Set Fine Y To Match
        }
    }

    private void loadGLTextures(GL gl) throws IOException {
        String tileNames [] = {"demos/data/images/font.png", "demos/data/images/Image.png"};

        gl.glGenTextures(2, textures, 0);

        for (int i = 0; i < 2; i++) {
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

    void buildFont(GL gl) {									// Build Our Font Display List
        base = gl.glGenLists(256);									// Creating 256 Display Lists
        for (int i = 0; i < 256; i++)						// Loop Through All 256 Lists
        {
            float cx = (float) (i % 16) / 16.0f;						// X Position Of Current Character
            float cy = (float) (i / 16) / 16.0f;						// Y Position Of Current Character

            gl.glNewList(base + i, GL.GL_COMPILE);					// Start Building A List
            gl.glBegin(GL.GL_QUADS);								// Use A Quad For Each Character
            gl.glTexCoord2f(cx, 1.0f - cy - 0.0625f);			// Texture Coord (Bottom Left)
            gl.glVertex2d(0, 16);							// Vertex Coord (Bottom Left)
            gl.glTexCoord2f(cx + 0.0625f, 1.0f - cy - 0.0625f);	// Texture Coord (Bottom Right)
            gl.glVertex2i(16, 16);							// Vertex Coord (Bottom Right)
            gl.glTexCoord2f(cx + 0.0625f, 1.0f - cy);			// Texture Coord (Top Right)
            gl.glVertex2i(16, 0);							// Vertex Coord (Top Right)
            gl.glTexCoord2f(cx, 1.0f - cy);					// Texture Coord (Top Left)
            gl.glVertex2i(0, 0);							// Vertex Coord (Top Left)
            gl.glEnd();										// Done Building Our Quad (Character)
            gl.glTranslated(15, 0, 0);							// Move To The Right Of The Character
            gl.glEndList();										// Done Building The Display List
        }														// Loop Until All 256 Are Built
    }

    private void glPrint(GL gl, int x, int y, int set, String message)	// Where The Printing Happens
    {
        if (set > 1) {												// Did User Choose An Invalid Character Set?
            set = 1;												// If So, Select Set 1 (Italic)
        }
        gl.glEnable(GL.GL_TEXTURE_2D);								// Enable Texture Mapping
        gl.glLoadIdentity();										// Reset The Modelview Matrix
        gl.glTranslated(x, y, 0);									// Position The Text (0,0 - Bottom Left)
        gl.glListBase(base - 32 + (128 * set));							// Choose The Font Set (0 or 1)

        if (set == 0) {												// If Set 0 Is Being Used Enlarge Font
            gl.glScalef(1.5f, 2.0f, 1.0f);							// Enlarge Font Width And Height
        }

        if (stringBuffer.capacity() < message.length()) {
            stringBuffer = BufferUtil.newByteBuffer(message.length());
        }

        stringBuffer.clear();
        stringBuffer.put(message.getBytes());
        stringBuffer.flip();
        gl.glCallLists(message.length(), GL.GL_UNSIGNED_BYTE, stringBuffer);		// Write The Text To The Screen
        gl.glDisable(GL.GL_TEXTURE_2D);								// Disable Texture Mapping
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        for (int i = 0; i < 9; i++)
            enemy[i] = new GameObject();

        try {
            loadGLTextures(gl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buildFont(gl);

        gl.glShadeModel(GL.GL_SMOOTH);                            //Enables Smooth Color Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);               //This Will Clear The Background Color To Black
        gl.glClearDepth(1.0);                                  //Enables Clearing Of The Depth Buffer
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);                             //The Type Of Depth Test To Do
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);  // Really Nice Perspective Calculations
        gl.glEnable(GL.GL_BLEND);										// Enable Blending
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);		// Type Of Blending To Use
        gl.glEnable(GL.GL_TEXTURE_2D);							// Enable 2D Texture Mapping
        resetObjects();							// Reset Player / Enemy Positions

        try {
            dieSample = new AudioSample(ResourceRetriever.getResourceAsStream("demos/data/samples/Die.wav"));
            completerSample = new AudioSample(ResourceRetriever.getResourceAsStream("demos/data/samples/Complete.wav"));
            freezeSample = new AudioSample(ResourceRetriever.getResourceAsStream("demos/data/samples/Freeze.wav"));
            hourglassSample = new AudioSample(ResourceRetriever.getResourceAsStream("demos/data/samples/Hourglass.wav"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void update() {
        if (!gameover)							// If Game Isn't Over And Programs Active Move Objects
        {
            for (int loop1 = 0; loop1 < (stage * level); loop1++)		// Loop Through The Different Stages
            {
                if ((enemy[loop1].x < player.x) && (enemy[loop1].fy == enemy[loop1].y * 40)) {
                    enemy[loop1].x++;						// Move The Enemy Right
                }

                if ((enemy[loop1].x > player.x) && (enemy[loop1].fy == enemy[loop1].y * 40)) {
                    enemy[loop1].x--;						// Move The Enemy Left
                }

                if ((enemy[loop1].y < player.y) && (enemy[loop1].fx == enemy[loop1].x * 60)) {
                    enemy[loop1].y++;						// Move The Enemy Down
                }

                if ((enemy[loop1].y > player.y) && (enemy[loop1].fx == enemy[loop1].x * 60)) {
                    enemy[loop1].y--;						// Move The Enemy Up
                }

                if (delay > (3 - level) && (hourglass.fx != 2))	// If Our Delay Is Done And Player Doesn't Have Hourglass
                {
                    delay = 0;								// Reset The Delay Counter Back To Zero
                    for (int loop2 = 0; loop2 < (stage * level); loop2++)	// Loop Through All The Enemies
                    {
                        if (enemy[loop2].fx < enemy[loop2].x * 60)	// Is Fine Position On X Axis Lower Than Intended Position?
                        {
                            enemy[loop2].fx += steps[adjust];	// If So, Increase Fine Position On X Axis
                            enemy[loop2].spin += steps[adjust];	// Spin Enemy Clockwise
                        }
                        if (enemy[loop2].fx > enemy[loop2].x * 60)	// Is Fine Position On X Axis Higher Than Intended Position?
                        {
                            enemy[loop2].fx -= steps[adjust];	// If So, Decrease Fine Position On X Axis
                            enemy[loop2].spin -= steps[adjust];	// Spin Enemy Counter Clockwise
                        }
                        if (enemy[loop2].fy < enemy[loop2].y * 40)	// Is Fine Position On Y Axis Lower Than Intended Position?
                        {
                            enemy[loop2].fy += steps[adjust];	// If So, Increase Fine Position On Y Axis
                            enemy[loop2].spin += steps[adjust];	// Spin Enemy Clockwise
                        }
                        if (enemy[loop2].fy > enemy[loop2].y * 40)	// Is Fine Position On Y Axis Higher Than Intended Position?
                        {
                            enemy[loop2].fy -= steps[adjust];	// If So, Decrease Fine Position On Y Axis
                            enemy[loop2].spin -= steps[adjust];	// Spin Enemy Counter Clockwise
                        }
                    }
                }

                // Are Any Of The Enemies On Top Of The Player?
                if ((enemy[loop1].fx == player.fx) && (enemy[loop1].fy == player.fy)) {
                    lives--;								// If So, Player Loses A Life

                    if (lives == 0)							// Are We Out Of Lives?
                    {
                        gameover = true;						// If So, gameover Becomes TRUE
                    }

                    resetObjects();							// Reset Player / Enemy Positions
                    dieSample.play(true, false);                   // Play The Death Sound
                }
            }

            if (moveRight && (player.x < 10) && (player.fx == player.x * 60) && (player.fy == player.y * 40)) {
                hline[player.x][player.y] = true;				// Mark The Current Horizontal Border As Filled
                player.x++;									// Move The Player Right
            }
            if (moveLeft && (player.x > 0) && (player.fx == player.x * 60) && (player.fy == player.y * 40)) {
                player.x--;									// Move The Player Left
                hline[player.x][player.y] = true;				// Mark The Current Horizontal Border As Filled
            }
            if (moveDown && (player.y < 10) && (player.fx == player.x * 60) && (player.fy == player.y * 40)) {
                vline[player.x][player.y] = true;				// Mark The Current Verticle Border As Filled
                player.y++;									// Move The Player Down
            }
            if (moveUp && (player.y > 0) && (player.fx == player.x * 60) && (player.fy == player.y * 40)) {
                player.y--;									// Move The Player Up
                vline[player.x][player.y] = true;				// Mark The Current Verticle Border As Filled
            }

            if (player.fx < player.x * 60)						// Is Fine Position On X Axis Lower Than Intended Position?
            {
                player.fx += steps[adjust];					// If So, Increase The Fine X Position
            }
            if (player.fx > player.x * 60)						// Is Fine Position On X Axis Greater Than Intended Position?
            {
                player.fx -= steps[adjust];					// If So, Decrease The Fine X Position
            }
            if (player.fy < player.y * 40)						// Is Fine Position On Y Axis Lower Than Intended Position?
            {
                player.fy += steps[adjust];					// If So, Increase The Fine Y Position
            }
            if (player.fy > player.y * 40)						// Is Fine Position On Y Axis Lower Than Intended Position?
            {
                player.fy -= steps[adjust];					// If So, Decrease The Fine Y Position
            }
        } else												// Otherwise
        {
            if (resetGame)									// If Spacebar Is Being Pressed
            {
                gameover = false;								// gameover Becomes FALSE
                filled = true;								// filled Becomes TRUE
                level = 1;									// Starting Level Is Set Back To One
                level2 = 1;									// Displayed Level Is Also Set To One
                stage = 0;									// Game Stage Is Set To Zero
                lives = 5;									// Lives Is Set To Five
                resetGame = false;
            }
        }

        if (filled)											// Is The Grid Filled In?
        {
            completerSample.play(true, false);                     // If So, Play The Level Complete Sound
            stage++;										// Increase The Stage
            if (stage > 3)									// Is The Stage Higher Than 3?
            {
                stage = 1;									// If So, Set The Stage To One
                level++;									// Increase The Level
                level2++;									// Increase The Displayed Level
                if (level > 3)								// Is The Level Greater Than 3?
                {
                    level = 3;								// If So, Set The Level To 3
                    lives++;								// Give The Player A Free Life
                    if (lives > 5)							// Does The Player Have More Than 5 Lives?
                    {
                        lives = 5;							// If So, Set Lives To Five
                    }
                }
            }

            resetObjects();									// Reset Player / Enemy Positions

            for (int loop1 = 0; loop1 < 11; loop1++)				// Loop Through The Grid X Coordinates
            {
                for (int loop2 = 0; loop2 < 11; loop2++)			// Loop Through The Grid Y Coordinates
                {
                    if (loop1 < 10)							// If X Coordinate Is Less Than 10
                    {
                        hline[loop1][loop2] = false;			// Set The Current Horizontal Value To FALSE
                    }
                    if (loop2 < 10)							// If Y Coordinate Is Less Than 10
                    {
                        vline[loop1][loop2] = false;			// Set The Current Vertical Value To FALSE
                    }
                }
            }
        }

        // If The Player Hits The Hourglass While It's Being Displayed On The Screen
        if ((player.fx == hourglass.x * 60) && (player.fy == hourglass.y * 40) && (hourglass.fx == 1)) {
            // Play Freeze Enemy Sound
            freezeSample.play(false, true);
            hourglass.fx = 2;									// Set The hourglass fx Variable To Two
            hourglass.fy = 0;									// Set The hourglass fy Variable To Zero
        }

        player.spin += 0.5f * steps[adjust];					// Spin The Player Clockwise
        if (player.spin > 360.0f)								// Is The spin Value Greater Than 360?
        {
            player.spin -= 360;								// If So, Subtract 360
        }

        hourglass.spin -= 0.25f * steps[adjust];				// Spin The Hourglass Counter Clockwise
        if (hourglass.spin < 0.0f)							// Is The spin Value Less Than 0?
        {
            hourglass.spin += 360.0f;							// If So, Add 360
        }

        hourglass.fy += steps[adjust];						// Increase The hourglass fy Variable
        if ((hourglass.fx == 0) && (hourglass.fy > 6000 / level))	// Is The hourglass fx Variable Equal To 0 And The fy
        {													// Variable Greater Than 6000 Divided By The Current Level?
            hourglassSample.play(false, false);	// If So, Play The Hourglass Appears Sound
            hourglass.x = Math.abs(random.nextInt()) % 10 + 1;						// Give The Hourglass A Random X Value
            hourglass.y = Math.abs(random.nextInt()) % 11;							// Give The Hourglass A Random Y Value
            hourglass.fx = 1;									// Set hourglass fx Variable To One (Hourglass Stage)
            hourglass.fy = 0;									// Set hourglass fy Variable To Zero (Counter)
        }

        if ((hourglass.fx == 1) && (hourglass.fy > 6000 / level))	// Is The hourglass fx Variable Equal To 1 And The fy
        {													// Variable Greater Than 6000 Divided By The Current Level?
            hourglass.fx = 0;									// If So, Set fx To Zero (Hourglass Will Vanish)
            hourglass.fy = 0;									// Set fy to Zero (Counter Is Reset)
        }

        if ((hourglass.fx == 2) && (hourglass.fy > 500 + (500 * level)))	// Is The hourglass fx Variable Equal To 2 And The fy
        {													// Variable Greater Than 500 Plus 500 Times The Current Level?
            freezeSample.stop();						// If So, Kill The Freeze Sound
            hourglass.fx = 0;									// Set hourglass fx Variable To Zero
            hourglass.fy = 0;									// Set hourglass fy Variable To Zero
        }

        delay++;											// Increase The Enemy Delay Counter
    }

    public void drawGLScene(GL gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);       //Clear The Screen And The Depth Buffer

        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);				// Select Our Font Texture
        gl.glColor3f(1.0f, 0.5f, 1.0f);								// Set Color To Purple
        glPrint(gl, 207, 24, 0, "GRID CRAZY");							// Write GRID CRAZY On The Screen
        gl.glColor3f(1.0f, 1.0f, 0.0f);								// Set Color To Yellow
        glPrint(gl, 20, 20, 1, "Level:" + level2);					// Write Actual Level Stats
        glPrint(gl, 20, 40, 1, "Stage:" + stage);						// Write Stage Stats


        if (gameover) {											// Is The Game Over?
            gl.glColor3i(random.nextInt() * 255, random.nextInt() * 255, random.nextInt() * 255);		// Pick A Random Color
            glPrint(gl, 472, 20, 1, "GAME OVER");						// Write GAME OVER To The Screen
            glPrint(gl, 456, 40, 1, "PRESS SPACE");					// Write PRESS SPACE To The Screen
        }

        for (int loop1 = 0; loop1 < lives - 1; loop1++) {					// Loop Through Lives Minus Current Life
            gl.glLoadIdentity();									// Reset The View
            gl.glTranslatef(490 + (loop1 * 40.0f), 40.0f, 0.0f);			// Move To The Right Of Our Title Text
            gl.glRotatef(-player.spin, 0.0f, 0.0f, 1.0f);				// Rotate Counter Clockwise
            gl.glColor3f(0.0f, 1.0f, 0.0f);							// Set Player Color To Light Green
            gl.glBegin(GL.GL_LINES);									// Start Drawing Our Player Using Lines
            gl.glVertex2d(-5, -5);								// Top Left Of Player
            gl.glVertex2d(5, 5);								// Bottom Right Of Player
            gl.glVertex2d(5, -5);								// Top Right Of Player
            gl.glVertex2d(-5, 5);								// Bottom Left Of Player
            gl.glEnd();											// Done Drawing The Player
            gl.glRotatef(-player.spin * 0.5f, 0.0f, 0.0f, 1.0f);		// Rotate Counter Clockwise
            gl.glColor3f(0.0f, 0.75f, 0.0f);							// Set Player Color To Dark Green
            gl.glBegin(GL.GL_LINES);									// Start Drawing Our Player Using Lines
            gl.glVertex2d(-7, 0);								// Left Center Of Player
            gl.glVertex2d(7, 0);								// Right Center Of Player
            gl.glVertex2d(0, -7);								// Top Center Of Player
            gl.glVertex2d(0, 7);								// Bottom Center Of Player
            gl.glEnd();											// Done Drawing The Player
        }

        filled = true;											// Set Filled To True Before Testing
        gl.glLineWidth(2.0f);										// Set Line Width For Cells To 2.0f
        gl.glDisable(GL.GL_LINE_SMOOTH);								// Disable Antialiasing
        gl.glLoadIdentity();										// Reset The Current Modelview Matrix
        for (int loop1 = 0; loop1 < 11; loop1++) {						// Loop From Left To Right
            for (int loop2 = 0; loop2 < 11; loop2++) {					// Loop From Top To Bottom
                gl.glColor3f(0.0f, 0.5f, 1.0f);						// Set Line Color To Blue
                if (hline[loop1][loop2]) {						// Has The Horizontal Line Been Traced
                    gl.glColor3f(1.0f, 1.0f, 1.0f);					// If So, Set Line Color To White
                }

                if (loop1 < 10) {								// Dont Draw To Far Right
                    if (!hline[loop1][loop2]) {					// If A Horizontal Line Isn't Filled
                        filled = false;							// filled Becomes False
                    }
                    gl.glBegin(GL.GL_LINES);							// Start Drawing Horizontal Cell Borders
                    gl.glVertex2d(20 + (loop1 * 60), 70 + (loop2 * 40));// Left Side Of Horizontal Line
                    gl.glVertex2d(80 + (loop1 * 60), 70 + (loop2 * 40));// Right Side Of Horizontal Line
                    gl.glEnd();									// Done Drawing Horizontal Cell Borders
                }

                gl.glColor3f(0.0f, 0.5f, 1.0f);						// Set Line Color To Blue
                if (vline[loop1][loop2]) {						// Has The Horizontal Line Been Traced
                    gl.glColor3f(1.0f, 1.0f, 1.0f);					// If So, Set Line Color To White
                }
                if (loop2 < 10) {								// Dont Draw To Far Down
                    if (!vline[loop1][loop2]) {					// If A Verticle Line Isn't Filled
                        filled = false;							// filled Becomes False
                    }
                    gl.glBegin(GL.GL_LINES);							// Start Drawing Verticle Cell Borders
                    gl.glVertex2d(20 + (loop1 * 60), 70 + (loop2 * 40));// Left Side Of Horizontal Line
                    gl.glVertex2d(20 + (loop1 * 60), 110 + (loop2 * 40));// Right Side Of Horizontal Line
                    gl.glEnd();									// Done Drawing Verticle Cell Borders
                }

                gl.glEnable(GL.GL_TEXTURE_2D);						// Enable Texture Mapping
                gl.glColor3f(1.0f, 1.0f, 1.0f);						// Bright White Color
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);		// Select The Tile Image
                if ((loop1 < 10) && (loop2 < 10)) {					// If In Bounds, Fill In Traced Boxes
                    // Are All Sides Of The Box Traced?
                    if (hline[loop1][loop2] && hline[loop1][loop2 + 1] && vline[loop1][loop2] && vline[loop1 + 1][loop2]) {
                        gl.glBegin(GL.GL_QUADS);						// Draw A Textured Quad
                        gl.glTexCoord2f((loop1 / 10.0f) + 0.1f, 1.0f - ((loop2 / 10.0f)));
                        gl.glVertex2d(20 + (loop1 * 60) + 59, (70 + loop2 * 40 + 1));	// Top Right
                        gl.glTexCoord2f((loop1 / 10.0f), 1.0f - ((loop2 / 10.0f)));
                        gl.glVertex2d(20 + (loop1 * 60) + 1, (70 + loop2 * 40 + 1));	// Top Left
                        gl.glTexCoord2f((loop1 / 10.0f), 1.0f - ((loop2 / 10.0f) + 0.1f));
                        gl.glVertex2d(20 + (loop1 * 60) + 1, (70 + loop2 * 40) + 39);	// Bottom Left
                        gl.glTexCoord2f((loop1 / 10.0f) + 0.1f, 1.0f - ((loop2 / 10.0f) + 0.1f));
                        gl.glVertex2d(20 + (loop1 * 60) + 59, (70 + loop2 * 40) + 39);	// Bottom Right
                        gl.glEnd();								// Done Texturing The Box
                    }
                }
                gl.glDisable(GL.GL_TEXTURE_2D);						// Disable Texture Mapping

            }
        }
        gl.glLineWidth(1.0f);										// Set The Line Width To 1.0f

        if (anti) {												// Is Anti TRUE?
            gl.glEnable(GL.GL_LINE_SMOOTH);							// If So, Enable Antialiasing
        }

        if (hourglass.fx == 1) {								// If fx=1 Draw The Hourglass
            gl.glLoadIdentity();									// Reset The Modelview Matrix
            gl.glTranslatef(20.0f + (hourglass.x * 60), 70.0f + (hourglass.y * 40), 0.0f);	// Move To The Fine Hourglass Position
            gl.glRotatef(hourglass.spin, 0.0f, 0.0f, 1.0f);			// Rotate Clockwise
            gl.glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());		// Set Hourglass Color To Random Color
            gl.glBegin(GL.GL_LINES);									// Start Drawing Our Hourglass Using Lines
            gl.glVertex2d(-5, -5);								// Top Left Of Hourglass
            gl.glVertex2d(5, 5);								// Bottom Right Of Hourglass
            gl.glVertex2d(5, -5);								// Top Right Of Hourglass
            gl.glVertex2d(-5, 5);								// Bottom Left Of Hourglass
            gl.glVertex2d(-5, 5);								// Bottom Left Of Hourglass
            gl.glVertex2d(5, 5);								// Bottom Right Of Hourglass
            gl.glVertex2d(-5, -5);								// Top Left Of Hourglass
            gl.glVertex2d(5, -5);								// Top Right Of Hourglass
            gl.glEnd();											// Done Drawing The Hourglass
        }

        gl.glLoadIdentity();										// Reset The Modelview Matrix
        gl.glTranslatef(player.fx + 20.0f, player.fy + 70.0f, 0.0f);		// Move To The Fine Player Position
        gl.glRotatef(player.spin, 0.0f, 0.0f, 1.0f);					// Rotate Clockwise
        gl.glColor3f(0.0f, 1.0f, 0.0f);								// Set Player Color To Light Green
        gl.glBegin(GL.GL_LINES);										// Start Drawing Our Player Using Lines
        gl.glVertex2d(-5, -5);									// Top Left Of Player
        gl.glVertex2d(5, 5);									// Bottom Right Of Player
        gl.glVertex2d(5, -5);									// Top Right Of Player
        gl.glVertex2d(-5, 5);									// Bottom Left Of Player
        gl.glEnd();												// Done Drawing The Player
        gl.glRotatef(player.spin * 0.5f, 0.0f, 0.0f, 1.0f);				// Rotate Clockwise
        gl.glColor3f(0.0f, 0.75f, 0.0f);								// Set Player Color To Dark Green
        gl.glBegin(GL.GL_LINES);										// Start Drawing Our Player Using Lines
        gl.glVertex2d(-7, 0);									// Left Center Of Player
        gl.glVertex2d(7, 0);									// Right Center Of Player
        gl.glVertex2d(0, -7);									// Top Center Of Player
        gl.glVertex2d(0, 7);									// Bottom Center Of Player
        gl.glEnd();												// Done Drawing The Player

        for (int loop1 = 0; loop1 < (stage * level); loop1++) {				// Loop To Draw Enemies
            gl.glLoadIdentity();									// Reset The Modelview Matrix
            gl.glTranslatef(enemy[loop1].fx + 20.0f, enemy[loop1].fy + 70.0f, 0.0f);
            gl.glColor3f(1.0f, 0.5f, 0.5f);							// Make Enemy Body Pink
            gl.glBegin(GL.GL_LINES);									// Start Drawing Enemy
            gl.glVertex2d(0, -7);								// Top Point Of Body
            gl.glVertex2d(-7, 0);								// Left Point Of Body
            gl.glVertex2d(-7, 0);								// Left Point Of Body
            gl.glVertex2d(0, 7);								// Bottom Point Of Body
            gl.glVertex2d(0, 7);								// Bottom Point Of Body
            gl.glVertex2d(7, 0);								// Right Point Of Body
            gl.glVertex2d(7, 0);								// Right Point Of Body
            gl.glVertex2d(0, -7);								// Top Point Of Body
            gl.glEnd();											// Done Drawing Enemy Body
            gl.glRotatef(enemy[loop1].spin, 0.0f, 0.0f, 1.0f);		// Rotate The Enemy Blade
            gl.glColor3f(1.0f, 0.0f, 0.0f);							// Make Enemy Blade Red
            gl.glBegin(GL.GL_LINES);									// Start Drawing Enemy Blade
            gl.glVertex2d(-7, -7);								// Top Left Of Enemy
            gl.glVertex2d(7, 7);								// Bottom Right Of Enemy
            gl.glVertex2d(-7, 7);								// Bottom Left Of Enemy
            gl.glVertex2d(7, -7);								// Top Right Of Enemy
            gl.glEnd();											// Done Drawing Enemy Blade
        }
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        drawGLScene(gl);

        // Update (roughly) every (steps[adjust] * 2.0) ms
        long currentTime = getTime();
        long elapsedTime = currentTime - lastUpdateTime;
        if (elapsedTime > steps[adjust] * 2) {
            update();
            lastUpdateTime = getTime();
        }
    }

    private long getTime() {
        return System.currentTimeMillis();
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
        gl.glOrtho(0.0f, width, height, 0.0f, -1.0f, 1.0f);				// Create Ortho 640x480 View (0,0 At Top Left)

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable,
                               boolean modeChanged,
                               boolean deviceChanged) {
    }

    private static class GameObject {												// Create A Structure For Our Player
        public int fx, fy;											// Fine Movement Position
        public int x, y;											// Current Player Position
        public float spin;											// Spin Direction
    }
}