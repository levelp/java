package demos.nehe.lesson30;

import demos.common.ResourceRetriever;
import demos.common.TextureReader;
import demos.nehe.lesson30.math.Tuple3d;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLUquadric;
import javax.media.opengl.glu.GLU;

import java.io.IOException;

class Renderer implements GLEventListener {
    private static final float EPSILON = 1e-8f;

    private Tuple3d[] arrayVel = new Tuple3d[10];            //holds velocity of balls
    private Tuple3d[] arrayPos = new Tuple3d[10];            //position of balls
    private Tuple3d[] oldPos = new Tuple3d[10];            //old position of balls
    private Tuple3d veloc = new Tuple3d(.5, -.1, .5);  //initial velocity of balls
    private Tuple3d accel = new Tuple3d(0, -.05, 0);  //acceleration ie. gravity of balls
    private Tuple3d dir = new Tuple3d(0, 0, -10);  //initial direction of camera
    private Tuple3d cameraPosition = new Tuple3d(0, -50, 1000);  //initial position of cameraT
    private boolean increaseCameraZ;
    private boolean decreaseCameraZ;

    private double timeStep = .6;                               //timestep of simulation
    private boolean increaseTimeStep;
    private boolean decreaseTimeStep;

    private float cameraRotation = 0;                           //holds rotation around the Y axis
    private boolean increaseCameraRotation;
    private boolean decreaseCameraRotation;

    private float[] spec = {1.0f, 1.0f, 1.0f, 1.0f};      //sets specular highlight of balls
    private float[] posl = {0.0f, 400f, 0.0f, 1.0f};      //position of ligth source
    private float[] amb2 = {0.3f, 0.3f, 0.3f, 1.0f};      //ambient of lightsource
    private float[] amb = {0.2f, 0.2f, 0.2f, 1.0f};      //global ambient

    private int dlist;                       //stores display list
    private int[][] texture = new int[2][2];//stores texture objects
    private int nrOfBalls;                   //sets the number of balls,
    private boolean cameraAttachedToBall = false;            //hook camera on ball, and sound on/off
    private boolean soundsEnabled = true;            //hook camera on ball, and sound on/off

    private Plane pl1,pl2,pl3,pl4,pl5;                  //the 5 planes of the room
    private Cylinder cyl1,cyl2,cyl3;                       //the 2 cylinders of the room
    private Explosion[] explosions = new Explosion[20]; //holds max 20 explosions at once
    private GLUquadric cylinder;                         //Quadratic object to render the cylinders

    private AudioSample audioSample;

    private GLU glu = new GLU();

    public void zoomOut(boolean increase) {
        increaseCameraZ = increase;
    }

    public void zoomIn(boolean decrease) {
        decreaseCameraZ = decrease;
    }

    public void increaseTimeStep(boolean increase) {
        increaseTimeStep = increase;
    }

    public void decreaseTimeStep(boolean decrease) {
        decreaseTimeStep = decrease;
    }

    public boolean isSoundsEnabled() {
        return soundsEnabled;
    }

    public void toggleSounds() {
        this.soundsEnabled = !soundsEnabled;
    }

    public boolean isCameraAttachedToBall() {
        return cameraAttachedToBall;
    }

    public void toggleCameraAttachedToBall() {
        this.cameraAttachedToBall = !cameraAttachedToBall;
        cameraRotation = 0;
    }

    public void increaseCameraRotation(boolean increase) {
        increaseCameraRotation = increase;
    }

    public void decreaseCameraRotation(boolean decrease) {
        decreaseCameraRotation = decrease;
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        float df[] = {100f};

        gl.glClearDepth(1.0f);                                      // Depth Buffer Setup
        gl.glEnable(GL.GL_DEPTH_TEST);                              // Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);                               // The Type Of Depth Testing To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // Really Nice Perspective Calculations

        gl.glClearColor(0, 0, 0, 0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glEnable(GL.GL_DEPTH_TEST);

        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, spec, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, df, 0);

        gl.glEnable(GL.GL_LIGHTING);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, posl, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, amb2, 0);
        gl.glEnable(GL.GL_LIGHT0);

        gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, amb, 0);
        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);

        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);

        gl.glEnable(GL.GL_TEXTURE_2D);
        loadGLTextures(gl);

        //Construct billboarded explosion primitive as display list
        //4 quads at right angles to each other
        gl.glNewList(dlist = gl.glGenLists(1), GL.GL_COMPILE);
        gl.glBegin(GL.GL_QUADS);
        gl.glRotatef(-45, 0, 1, 0);
        gl.glNormal3f(0, 0, 1);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-50, -40, 0);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(50, -40, 0);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(50, 40, 0);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-50, 40, 0);
        gl.glNormal3f(0, 0, -1);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-50, 40, 0);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(50, 40, 0);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(50, -40, 0);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-50, -40, 0);

        gl.glNormal3f(1, 0, 0);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, -40, 50);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, -40, -50);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(0, 40, -50);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(0, 40, 50);
        gl.glNormal3f(-1, 0, 0);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0, 40, 50);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0, 40, -50);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(0, -40, -50);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(0, -40, 50);
        gl.glEnd();
        gl.glEndList();

        try {
            audioSample = new AudioSample(
                    ResourceRetriever.getResourceAsStream("demos/data/samples/Explode.wav")
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        initVars(glu);
    }

    private void loadGLTextures(GL gl) {
        TextureReader.Texture image1 = null;
        TextureReader.Texture image2 = null;
        TextureReader.Texture image3 = null;
        TextureReader.Texture image4 = null;
        try {
            image1 = TextureReader.readTexture("demos/data/images/Marble.bmp");
            image2 = TextureReader.readTexture("demos/data/images/Spark.bmp");
            image3 = TextureReader.readTexture("demos/data/images/Boden.bmp");
            image4 = TextureReader.readTexture("demos/data/images/Wand.bmp");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        /* Create Texture	*****************************************/
        gl.glGenTextures(2, texture[0], 0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0][0]);   /* 2d texture (x and y size)*/

        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR); /* scale linearly when image bigger than texture*/
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR); /* scale linearly when image smalled than texture*/
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

        /* 2d texture, level of detail 0 (normal), 3 components (red, green, blue), x size from image, y size from image, */
        /* border 0 (normal), rgb color data, unsigned byte data, and finally the data itself.*/
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, image1.getWidth(), image1.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image1.getPixels());

        /* Create Texture	******************************************/
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0][1]);   /* 2d texture (x and y size)*/

        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR); /* scale linearly when image bigger than texture*/
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR); /* scale linearly when image smalled than texture*/
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

        /* 2d texture, level of detail 0 (normal), 3 components (red, green, blue), x size from image, y size from image, */
        /* border 0 (normal), rgb color data, unsigned byte data, and finally the data itself.*/
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, image2.getWidth(), image2.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image2.getPixels());


        /* Create Texture	********************************************/
        gl.glGenTextures(2, texture[1], 0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture[1][0]);   /* 2d texture (x and y size)*/

        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR); /* scale linearly when image bigger than texture*/
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR); /* scale linearly when image smalled than texture*/
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

        /* 2d texture, level of detail 0 (normal), 3 components (red, green, blue), x size from image, y size from image, */
        /* border 0 (normal), rgb color data, unsigned byte data, and finally the data itself.*/
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, image3.getWidth(), image3.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image3.getPixels());

        /* Create Texture	*********************************************/
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture[1][1]);   /* 2d texture (x and y size)*/

        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR); /* scale linearly when image bigger than texture*/
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR); /* scale linearly when image smalled than texture*/
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

        /* 2d texture, level of detail 0 (normal), 3 components (red, green, blue), x size from image, y size from image, */
        /* border 0 (normal), rgb color data, unsigned byte data, and finally the data itself.*/
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, image4.getWidth(), image4.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image4.getPixels());
    }

    private void initVars(GLU glu) {
        //create palnes
        pl1 = new Plane();
        pl1.position = new Tuple3d(0, -300, 0);
        pl1.normal = new Tuple3d(0, 1, 0);

        pl2 = new Plane();
        pl2.position = new Tuple3d(300, 0, 0);
        pl2.normal = new Tuple3d(-1, 0, 0);

        pl3 = new Plane();
        pl3.position = new Tuple3d(-300, 0, 0);
        pl3.normal = new Tuple3d(1, 0, 0);

        pl4 = new Plane();
        pl4.position = new Tuple3d(0, 0, 300);
        pl4.normal = new Tuple3d(0, 0, -1);

        pl5 = new Plane();
        pl5.position = new Tuple3d(0, 0, -300);
        pl5.normal = new Tuple3d(0, 0, 1);

        cyl1 = new Cylinder();
        cyl1.position = new Tuple3d(0, 0, 0);
        cyl1.axis = new Tuple3d(0, 1, 0);
        cyl1.radius = 60 + 20;

        cyl2 = new Cylinder();
        cyl2.position = new Tuple3d(200, -300, 0);
        cyl2.axis = new Tuple3d(0, 0, 1);
        cyl2.radius = 60 + 20;

        cyl3 = new Cylinder();
        cyl3.position = new Tuple3d(-200, 0, 0);
        cyl3.axis = new Tuple3d(0, 1, 1);
        cyl3.axis.normalize();
        cyl3.radius = 30 + 20;

        //create quadratic object to render cylinders
        cylinder = glu.gluNewQuadric();
        glu.gluQuadricTexture(cylinder, true);

        //Set initial positions and velocities of balls
        //also initialize array which holds explosions
        nrOfBalls = 10;
        arrayVel[0] = new Tuple3d(veloc);
        arrayPos[0] = new Tuple3d(199, 180, 10);

        explosions[0] = new Explosion();
        explosions[0].alpha = 0;
        explosions[0].scale = 1;
        arrayVel[1] = new Tuple3d(veloc);
        arrayPos[1] = new Tuple3d(0, 150, 100);

        explosions[1] = new Explosion();
        explosions[1].alpha = 0;
        explosions[1].scale = 1;
        arrayVel[2] = new Tuple3d(veloc);
        arrayPos[2] = new Tuple3d(-100, 180, -100);

        explosions[2] = new Explosion();
        explosions[2].alpha = 0;
        explosions[2].scale = 1;

        for (int i = 3; i < 10; i++) {
            arrayVel[i] = new Tuple3d(veloc);
            arrayPos[i] = new Tuple3d(-500 + i * 75, 300, -500 + i * 50);

            explosions[i] = new Explosion();
            explosions[i].alpha = 0;
            explosions[i].scale = 1;
        }

        for (int i = 10; i < 20; i++) {
            explosions[i] = new Explosion();
            explosions[i].alpha = 0;
            explosions[i].scale = 1;
        }
    }

    private void update() {
        if (decreaseCameraZ)
            cameraPosition.z -= 10;
        if (increaseCameraZ)
            cameraPosition.z += 10;
        if (increaseCameraRotation)
            cameraRotation += 10;
        if (decreaseCameraRotation)
            cameraRotation -= 10;
        if (increaseTimeStep)
            timeStep += 0.1;
        if (decreaseTimeStep)
            timeStep -= 0.1;
    }

    public void display(GLAutoDrawable drawable) {
        update();
        GL gl = drawable.getGL();

        int i;
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        //set camera in hookmode
        if (cameraAttachedToBall)
            glu.gluLookAt(arrayPos[0].x + 250, arrayPos[0].y + 250, arrayPos[0].z,
                    arrayPos[0].x + arrayVel[0].x, arrayPos[0].y + arrayVel[0].y, arrayPos[0].z + arrayVel[0].z
                    , 0, 1, 0);
        else
            glu.gluLookAt(cameraPosition.x, cameraPosition.y, cameraPosition.z,
                    cameraPosition.x + dir.x, cameraPosition.y + dir.y, cameraPosition.z + dir.z,
                    0, 1.0, 0.0);

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glRotatef(cameraRotation, 0, 1, 0);

        //render balls
        for (i = 0; i < nrOfBalls; i++) {
            switch (i) {
                case 1:
                    gl.glColor3f(1.0f, 1.0f, 1.0f);
                    break;
                case 2:
                    gl.glColor3f(1.0f, 1.0f, 0.0f);
                    break;
                case 3:
                    gl.glColor3f(0.0f, 1.0f, 1.0f);
                    break;
                case 4:
                    gl.glColor3f(0.0f, 1.0f, 0.0f);
                    break;
                case 5:
                    gl.glColor3f(0.0f, 0.0f, 1.0f);
                    break;
                case 6:
                    gl.glColor3f(0.65f, 0.2f, 0.3f);
                    break;
                case 7:
                    gl.glColor3f(1.0f, 0.0f, 1.0f);
                    break;
                case 8:
                    gl.glColor3f(0.0f, 0.7f, 0.4f);
                    break;
                default:
                    gl.glColor3f(1.0f, 0, 0);
            }

            gl.glPushMatrix();
            gl.glTranslated(arrayPos[i].x, arrayPos[i].y, arrayPos[i].z);
            glu.gluSphere(cylinder, 20, 20, 20);
            gl.glPopMatrix();
        }

        gl.glEnable(GL.GL_TEXTURE_2D);
        //render walls(planes) with texture
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture[1][1]);
        gl.glColor3f(1, 1, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(320, 320, 320);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(320, -320, 320);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-320, -320, 320);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-320, 320, 320);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-320, 320, -320);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-320, -320, -320);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(320, -320, -320);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(320, 320, -320);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(320, 320, -320);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(320, -320, -320);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(320, -320, 320);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(320, 320, 320);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-320, 320, 320);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-320, -320, 320);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-320, -320, -320);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-320, 320, -320);
        gl.glEnd();

        //render floor (plane) with colours
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture[1][0]);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-320, -320, 320);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(320, -320, 320);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(320, -320, -320);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-320, -320, -320);
        gl.glEnd();

        //render columns(cylinders)
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0][0]);   /* choose the texture to use.*/
        gl.glColor3f(.5f, .5f, .5f);
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        gl.glTranslatef(0, 0, -500);
        glu.gluCylinder(cylinder, 60, 60, 1000, 20, 2);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(200, -300, -500);
        glu.gluCylinder(cylinder, 60, 60, 1000, 20, 2);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-200, 0, 0);
        gl.glRotatef(135, 1, 0, 0);
        gl.glTranslatef(0, 0, -500);
        glu.gluCylinder(cylinder, 30, 30, 1000, 20, 2);
        gl.glPopMatrix();

        //render/blend explosions
        gl.glEnable(GL.GL_BLEND);
        gl.glDepthMask(false);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0][1]);

        for (i = 0; i < 20; i++) {
            if (explosions[i].alpha >= 0) {
                gl.glPushMatrix();
                explosions[i].alpha -= 0.01f;
                explosions[i].scale += 0.03f;
                gl.glColor4f(1, 1, 0, explosions[i].alpha);
                gl.glScalef(explosions[i].scale, explosions[i].scale, explosions[i].scale);
                gl.glTranslatef((float) explosions[i].position.x / explosions[i].scale,
                        (float) explosions[i].position.y / explosions[i].scale,
                        (float) explosions[i].position.z / explosions[i].scale);
                gl.glCallList(dlist);
                gl.glPopMatrix();
            }
        }
        gl.glDepthMask(true);
        gl.glDisable(GL.GL_BLEND);
        gl.glDisable(GL.GL_TEXTURE_2D);
        idle();
    }

    /**
     * Main loop of the simulation. Moves, finds the collisions and responses of the objects in the
     * current time step.
     */
    private void idle() {
        Tuple3d uveloc = new Tuple3d(),
                normal = new Tuple3d(),
                point = new Tuple3d(),
                norm = new Tuple3d(),
                Pos2 = new Tuple3d(),
                Nc = new Tuple3d();

        double rt2,
                rt4,
                rt[] = {0},
                lamda[] = {10000},
                RestTime[] = {0},
                BallTime[] = {0};

        int BallNr = 0,
                BallColNr1[] = {0},
                BallColNr2[] = {0};

        if (!cameraAttachedToBall) {
            cameraRotation += 0.1f;
            if (cameraRotation > 360)
                cameraRotation = 0;
        }

        RestTime[0] = this.timeStep;
        lamda[0] = 1000;

        //Compute velocity for next timestep using Euler equations
        for (int j = 0; j < nrOfBalls; j++)
            arrayVel[j].scaleAdd(RestTime[0], accel, arrayVel[j]);

        //While timestep not over
        while (RestTime[0] > EPSILON) {

            lamda[0] = 10000;   //initialize to very large value
            //For all the balls find closest intersection between balls and planes/cylinders
            for (int i = 0; i < nrOfBalls; i++) {
                //compute new position and distance
                oldPos[i] = new Tuple3d();
                oldPos[i].set(arrayPos[i]);
                uveloc.set(arrayVel[i]);
                uveloc.normalize();
                arrayPos[i].scaleAdd(RestTime[0], arrayVel[i], arrayPos[i]);
                rt2 = oldPos[i].distance(arrayPos[i]);
                //Test if collision occured between ball and all 5 planes

                if (TestIntersionPlane(pl1, oldPos[i], uveloc, rt, norm)) {
                    //Find intersection time
                    rt4 = rt[0] * RestTime[0] / rt2;
                    //if smaller than the one already stored replace and in timestep
                    if (rt4 <= lamda[0]) {
                        if (rt4 <= RestTime[0] + EPSILON)
                            if (!((rt[0] <= EPSILON) && (uveloc.dot(norm) > EPSILON))) {
                                normal.set(norm);
                                point.scaleAdd(rt[0], uveloc, oldPos[i]);
                                lamda[0] = rt4;
                                BallNr = i;
                            }
                    }
                }

                if (TestIntersionPlane(pl2, oldPos[i], uveloc, rt, norm)) {
                    rt4 = rt[0] * RestTime[0] / rt2;
                    if (rt4 <= lamda[0]) {
                        if (rt4 <= RestTime[0] + EPSILON)
                            if (!((rt[0] <= EPSILON) && (uveloc.dot(norm) > EPSILON))) {
                                normal.set(norm);
                                point.scaleAdd(rt[0], uveloc, oldPos[i]);
                                lamda[0] = rt4;
                                BallNr = i;
                            }
                    }
                }

                if (TestIntersionPlane(pl3, oldPos[i], uveloc, rt, norm)) {
                    rt4 = rt[0] * RestTime[0] / rt2;
                    if (rt4 <= lamda[0]) {
                        if (rt4 <= RestTime[0] + EPSILON)
                            if (!((rt[0] <= EPSILON) && (uveloc.dot(norm) > EPSILON))) {
                                normal.set(norm);
                                point.scaleAdd(rt[0], uveloc, oldPos[i]);
                                lamda[0] = rt4;
                                BallNr = i;
                            }
                    }
                }

                if (TestIntersionPlane(pl4, oldPos[i], uveloc, rt, norm)) {
                    rt4 = rt[0] * RestTime[0] / rt2;
                    if (rt4 <= lamda[0]) {
                        if (rt4 <= RestTime[0] + EPSILON)
                            if (!((rt[0] <= EPSILON) && (uveloc.dot(norm) > EPSILON))) {
                                normal.set(norm);
                                point.scaleAdd(rt[0], uveloc, oldPos[i]);
                                lamda[0] = rt4;
                                BallNr = i;
                            }
                    }
                }

                if (TestIntersionPlane(pl5, oldPos[i], uveloc, rt, norm)) {
                    rt4 = rt[0] * RestTime[0] / rt2;
                    if (rt4 <= lamda[0]) {
                        if (rt4 <= RestTime[0] + EPSILON)
                            if (!((rt[0] <= EPSILON) && (uveloc.dot(norm) > EPSILON))) {
                                normal.set(norm);
                                point.scaleAdd(rt[0], uveloc, oldPos[i]);
                                lamda[0] = rt4;
                                BallNr = i;
                            }
                    }
                }

                //Now test intersection with the 3 cylinders
                if (TestIntersionCylinder(cyl1, oldPos[i], uveloc, rt, norm, Nc)) {
                    rt4 = rt[0] * RestTime[0] / rt2;
                    if (rt4 <= lamda[0]) {
                        if (rt4 <= RestTime[0] + EPSILON)
                            if (!((rt[0] <= EPSILON) && (uveloc.dot(norm) > EPSILON))) {
                                normal.set(norm);
                                point.set(Nc);
                                lamda[0] = rt4;
                                BallNr = i;
                            }
                    }
                }

                if (TestIntersionCylinder(cyl2, oldPos[i], uveloc, rt, norm, Nc)) {
                    rt4 = rt[0] * RestTime[0] / rt2;
                    if (rt4 <= lamda[0]) {
                        if (rt4 <= RestTime[0] + EPSILON)
                            if (!((rt[0] <= EPSILON) && (uveloc.dot(norm) > EPSILON))) {
                                normal.set(norm);
                                point.set(Nc);
                                lamda[0] = rt4;
                                BallNr = i;
                            }
                    }
                }

                if (TestIntersionCylinder(cyl3, oldPos[i], uveloc, rt, norm, Nc)) {
                    rt4 = rt[0] * RestTime[0] / rt2;
                    if (rt4 <= lamda[0]) {
                        if (rt4 <= RestTime[0] + EPSILON)
                            if (!((rt[0] <= EPSILON) && (uveloc.dot(norm) > EPSILON))) {
                                normal.set(norm);
                                point.set(Nc);
                                lamda[0] = rt4;
                                BallNr = i;
                            }
                    }
                }
            }

            //After all balls were tested with planes/cylinders test for collision
            //between them and replace if collision time smaller
            if (findBallCol(Pos2, BallTime, RestTime, BallColNr1, BallColNr2) == 1) {
                if (soundsEnabled)
                    audioSample.play();

                if ((lamda[0] == 10000) || (lamda[0] > BallTime[0])) {
                    RestTime[0] = RestTime[0] - BallTime[0];
                    Tuple3d pb1 = new Tuple3d(),
                            pb2 = new Tuple3d(),
                            xaxis = new Tuple3d(),
                            U1x = new Tuple3d(),
                            U1y = new Tuple3d(),
                            U2x = new Tuple3d(),
                            U2y = new Tuple3d(),
                            V1x = new Tuple3d(),
                            V1y = new Tuple3d(),
                            V2x = new Tuple3d(),
                            V2y = new Tuple3d();
                    double a,b;

                    pb1.scaleAdd(BallTime[0], arrayVel[BallColNr1[0]], oldPos[BallColNr1[0]]);
                    pb2.scaleAdd(BallTime[0], arrayVel[BallColNr2[0]], oldPos[BallColNr2[0]]);
                    xaxis.sub(pb2, pb1);
                    xaxis.normalize();

                    a = xaxis.dot(arrayVel[BallColNr1[0]]);
                    U1x.scaleAdd(a, xaxis);
                    U1y.sub(arrayVel[BallColNr1[0]], U1x);

                    xaxis.sub(pb1, pb2);
                    xaxis.normalize();

                    b = xaxis.dot(arrayVel[BallColNr2[0]]);
                    U2x.scaleAdd(b, xaxis);
                    U2y.sub(arrayVel[BallColNr2[0]], U2x);

                    V1x.add(U1x, U2x);
                    V1x.sub(new Tuple3d(U1x.x - U2x.x, U1x.y - U2x.y, U1x.z - U2x.z));
                    V1x.scale(.5);

                    V2x.add(U1x, U2x);
                    V2x.sub(new Tuple3d(U2x.x - U1x.x, U2x.y - U1x.y, U2x.z - U1x.z));
                    V2x.scale(.5);

                    V1y.set(U1y);
                    V2y.set(U2y);

                    for (int j = 0; j < nrOfBalls; j++)
                        arrayPos[j].scaleAdd(BallTime[0], arrayVel[j], oldPos[j]);

                    arrayVel[BallColNr1[0]].add(V1x, V1y);
                    arrayVel[BallColNr2[0]].add(V2x, V2y);

                    //Update explosion array
                    for (int j = 0; j < 20; j++) {
                        if (explosions[j].alpha <= 0) {
                            explosions[j].alpha = 1;
                            explosions[j].position = arrayPos[BallColNr1[0]];
                            explosions[j].scale = 1;
                            break;
                        }
                    }
                    continue;
                }
            }
            //End of tests
            //If test occured move simulation for the correct timestep
            //and compute response for the colliding ball
            if (lamda[0] != 10000) {
                RestTime[0] -= lamda[0];

                for (int j = 0; j < nrOfBalls; j++)
                    arrayPos[j].scaleAdd(lamda[0], arrayVel[j], oldPos[j]);

                rt2 = arrayVel[BallNr].length();
                arrayVel[BallNr].normalize();

                normal.scale(-2 * normal.dot(arrayVel[BallNr]));
                arrayVel[BallNr].add(normal, arrayVel[BallNr]);
                arrayVel[BallNr].normalize();
                arrayVel[BallNr].scale(rt2);

                // Update explosion array
                for (int j = 0; j < 20; j++) {
                    if (explosions[j].alpha <= 0) {
                        explosions[j].alpha = 1;
                        explosions[j].position = point;
                        explosions[j].scale = 1;
                        break;
                    }
                }
            } else
                RestTime[0] = 0;
        }
    }

    public void reshape(GLAutoDrawable drawable,
                        int xstart,
                        int ystart,
                        int width,
                        int height) {
        GL gl = drawable.getGL();

        height = (height == 0) ? 1 : height;

        gl.glViewport(0, 0, width, height);         // Reset The Current Viewport
        gl.glMatrixMode(GL.GL_PROJECTION);       // Select The Projection Matrix
        gl.glLoadIdentity();                     // Reset The Projection Matrix

        // Calculate The Aspect Ratio Of The Window
        glu.gluPerspective(50.0f, (float) width / height, 10.f, 1700.0f);

        gl.glMatrixMode(GL.GL_MODELVIEW);        // Select The Modelview Matrix
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable,
                               boolean modeChanged,
                               boolean deviceChanged) {
    }

    /*
     * Intersetion tests
     */

    /**
     * Fast Intersection Function between ray/plane
     */
    private boolean TestIntersionPlane(Plane plane, Tuple3d position, Tuple3d direction,
                                       double[] lamda, Tuple3d pNormal) {

        double dotProduct = direction.dot(plane.normal);
        double l2;

        //determine if ray paralle to plane
        if ((dotProduct < EPSILON) && (dotProduct > -EPSILON))
            return false;

        Tuple3d substract = new Tuple3d(plane.position);
        substract.sub(position);
        l2 = (plane.normal.dot(substract)) / dotProduct;

        if (l2 < -EPSILON)
            return false;

        pNormal.set(plane.normal);
        lamda[0] = l2;
        return true;
    }

    /**
     * Fast Intersection Function between ray/cylinder
     */
    private boolean TestIntersionCylinder(Cylinder cylinder, Tuple3d position,
                                          Tuple3d direction, double[] lamda,
                                          Tuple3d pNormal, Tuple3d newposition) {
        Tuple3d RC = new Tuple3d(),
                HB = new Tuple3d(),
                n = new Tuple3d(),
                O = new Tuple3d();
        double d,t,s,
                ln,in,out;

        RC.sub(position, cylinder.position);
        n.cross(direction, cylinder.axis);

        ln = n.length();

        if ((ln < EPSILON) && (ln > -EPSILON))
            return false;

        n.normalize();
        d = Math.abs(RC.dot(n));

        if (d <= cylinder.radius) {

            O.cross(RC, cylinder.axis);
            t = -O.dot(n) / ln;
            O.cross(n, cylinder.axis);
            O.normalize();
            s = Math.abs(Math.sqrt(cylinder.radius * cylinder.radius - d * d) / direction.dot(O));

            in = t - s;
            out = t + s;

            if (in < -EPSILON) {
                if (out < -EPSILON)
                    return false;
                else
                    lamda[0] = out;
            } else if (out < -EPSILON) {
                lamda[0] = in;
            } else if (in < out)
                lamda[0] = in;
            else
                lamda[0] = out;

            newposition.scaleAdd(lamda[0], direction, position);
            HB.sub(newposition, cylinder.position);
            pNormal.scaleAdd(-HB.dot(cylinder.axis), cylinder.axis, HB);
            pNormal.normalize();
            return true;
        }
        return false;
    }

    /**
     * Find if any of the current balls intersect with each other in the current timestep.
     * @return the index of the 2 itersecting balls, the point and time of intersection
     */
    private int findBallCol(Tuple3d point, double[] TimePoint,
                            double[] Time2, int[] BallNr1, int[] BallNr2) {

        Tuple3d RelativeVClone = new Tuple3d(),
                RelativeV = new Tuple3d(),
                posi = new Tuple3d();

        double Timedummy = 10000,
                MyTime = 0,
                Add = Time2[0] / 150;
        TRay rays;

        //Test all balls against eachother in 150 small steps
        for (int i = 0; i < nrOfBalls - 1; i++) {
            for (int j = i + 1; j < nrOfBalls; j++) {

                RelativeV.sub(arrayVel[i], arrayVel[j]);
                RelativeVClone.set(RelativeV);
                RelativeVClone.normalize();
                rays = new TRay(oldPos[i], RelativeVClone);
                MyTime = 0;

                if ((rays.dist(oldPos[j])) > 40)
                    continue;

                while (MyTime < Time2[0]) {

                    MyTime += Add;
                    posi.scaleAdd(MyTime, RelativeV, oldPos[i]);
                    if (posi.distance(oldPos[j]) <= 40) {
                        point.set(posi);
                        if (Timedummy > (MyTime - Add))
                            Timedummy = MyTime - Add;

                        BallNr1[0] = i;
                        BallNr2[0] = j;
                        break;
                    }
                }
            }
        }

        if (Timedummy != 10000) {
            TimePoint[0] = Timedummy;
            return 1;
        }
        return 0;
    }

    private static class Explosion {
        public Tuple3d position = new Tuple3d();
        public float alpha;
        public float scale;
    }

    private static class Cylinder {
        public Tuple3d position;
        public Tuple3d axis;
        public double radius;
    }

    private static class Plane {
        public Tuple3d position;
        public Tuple3d normal;
    }

    private static class TRay {
        public Tuple3d p;                     // Any point on the line
        public Tuple3d v;                     // Direction of the line

        public TRay(Tuple3d p,
                    Tuple3d v) {
            this.p = new Tuple3d(p);
            this.v = new Tuple3d(v);
        }

        public double dist(Tuple3d point) {
            double lambda = v.dot(new Tuple3d(point.x - p.x,
                    point.y - p.y,
                    point.z - p.z));
            Tuple3d point2 = new Tuple3d();
            point2.scaleAdd(lambda, v, p);
            return point.distance(point2);
        }
    }
}