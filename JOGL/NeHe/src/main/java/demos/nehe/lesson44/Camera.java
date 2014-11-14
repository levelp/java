package demos.nehe.lesson44;

import javax.media.opengl.GL;

/**
 * I don't mind if you use this class in your own code. All I ask is
 * that you give me credit for it if you do.  And plug NeHe while your
 * at it! :P  Thanks go to David Steere, Cameron Tidwell, Bert Sammons,
 * and Brannon Martindale for helping me test all the code!  Enjoy.
 * @author Vic Hollis
 * @author Abdul Bezrati
 */
class Camera {
    Tuple3f vLightSourceToIntersect;
    Tuple3f vLightSourceToCamera;
    Tuple3f m_DirectionVector;
    Tuple3f m_LightSourcePos;
    Tuple3f ptIntersect;
    Tuple3f m_Position;
    Tuple3f pt;

    float m_MaxForwardVelocity;
    float m_ForwardVelocity;
    float m_MaxHeadingRate;
    float m_HeadingDegrees;
    float m_PitchDegrees;
    float m_MaxPitchRate;
    float m_MaxPointSize;
    float[][] m_Frustum;

    int[] m_BigGlowTexture;
    int[] m_StreakTexture;
    int[] m_HaloTexture;
    int[] m_GlowTexture;
    int m_WindowHeight;
    int m_WindowWidth;

    public Camera() {
        // Initalize all our member varibles.
        vLightSourceToIntersect = new Tuple3f();
        vLightSourceToCamera = new Tuple3f();
        m_DirectionVector = new Tuple3f();
        m_LightSourcePos = new Tuple3f();
        ptIntersect = new Tuple3f();
        m_Position = new Tuple3f();
        pt = new Tuple3f();
        m_Frustum = new float[6][4];
        m_MaxForwardVelocity = 0;
        m_LightSourcePos.x = 0;
        m_LightSourcePos.y = 0;
        m_LightSourcePos.z = 0;
        m_ForwardVelocity = 0;
        m_MaxHeadingRate = 0;
        m_HeadingDegrees = 0;
        m_BigGlowTexture = new int[1];
        m_StreakTexture = new int[1];
        m_PitchDegrees = 0;
        m_MaxPointSize = 0;
        m_MaxPitchRate = 0;
        m_GlowTexture = new int[1];
        m_HaloTexture = new int[1];
    }

    public void setPrespective(GL gl) {
        Tuple3f v = new Tuple3f();  // A vector to hold our cameras direction * the forward velocity
        float Matrix[] = new float[16];  // A array to hold the model view matrix.
        // we don't want to destory the Direction vector by using it instead.

        // Going to use glRotate to calculate our direction vector
        gl.glRotatef(m_HeadingDegrees, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(m_PitchDegrees, 1.0f, 0.0f, 0.0f);

        // Get the resulting matrix from OpenGL it will have our
        // direction vector in the 3rd row.
        gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, Matrix, 0);

        // Get the direction vector from the matrix. Element 10 must
        // be inverted!
        m_DirectionVector.x = Matrix[8];
        m_DirectionVector.y = Matrix[9];
        m_DirectionVector.z = -Matrix[10];

        // Ok erase the results of the last computation.
        gl.glLoadIdentity();

        // Rotate the scene to get the right orientation.
        gl.glRotatef(m_PitchDegrees, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(m_HeadingDegrees, 0.0f, 1.0f, 0.0f);

        // Scale the direction by our speed.
        v.scale(m_ForwardVelocity, m_DirectionVector);

        // Increment our position by the vector
        m_Position.add(v);

        // Translate to our new position.
        gl.glTranslatef(-m_Position.x, -m_Position.y, -m_Position.z);
    }

    public void changePitch(float degrees) {

        if (Math.abs(degrees) < Math.abs(m_MaxPitchRate)) {
            // Our pitch is less than the max pitch rate that we
            // defined so lets increment it.
            m_PitchDegrees += degrees;
        } else {
            // Our pitch is greater than the max pitch rate that
            // we defined so we can only increment our pitch by the
            // maximum allowed value.
            if (degrees < 0) {
                // We are pitching down so decrement
                m_PitchDegrees -= m_MaxPitchRate;
            } else {
                // We are pitching up so increment
                m_PitchDegrees += m_MaxPitchRate;
            }
        }

        // We don't want our pitch to run away from us. Although it
        // really doesn't matter I prefer to have my pitch degrees
        // within the range of -360.0f to 360.0f
        if (m_PitchDegrees > 360.0f) {
            m_PitchDegrees -= 360.0f;
        } else if (m_PitchDegrees < -360.0f) {
            m_PitchDegrees += 360.0f;
        }
    }

    public void changeHeading(float degrees) {

        if (Math.abs(degrees) < Math.abs(m_MaxHeadingRate)) {
            // Our Heading is less than the max heading rate that we
            // defined so lets increment it but first we must check
            // to see if we are inverted so that our heading will not
            // become inverted.
            if (m_PitchDegrees > 90 && m_PitchDegrees < 270 || (m_PitchDegrees < -90 && m_PitchDegrees > -270)) {
                m_HeadingDegrees -= degrees;
            } else {
                m_HeadingDegrees += degrees;
            }
        } else {
            // Our heading is greater than the max heading rate that
            // we defined so we can only increment our heading by the
            // maximum allowed value.
            if (degrees < 0) {
                // Check to see if we are upside down.
                if ((m_PitchDegrees > 90 && m_PitchDegrees < 270) || (m_PitchDegrees < -90 && m_PitchDegrees > -270)) {
                    // Ok we would normally decrement here but since we are upside
                    // down then we need to increment our heading
                    m_HeadingDegrees += m_MaxHeadingRate;
                } else {
                    // We are not upside down so decrement as usual
                    m_HeadingDegrees -= m_MaxHeadingRate;
                }
            } else {
                // Check to see if we are upside down.
                if (m_PitchDegrees > 90 && m_PitchDegrees < 270 || (m_PitchDegrees < -90 && m_PitchDegrees > -270)) {
                    // Ok we would normally increment here but since we are upside
                    // down then we need to decrement our heading.
                    m_HeadingDegrees -= m_MaxHeadingRate;
                } else {
                    // We are not upside down so increment as usual.
                    m_HeadingDegrees += m_MaxHeadingRate;
                }
            }
        }

        // We don't want our heading to run away from us either. Although it
        // really doesn't matter I prefer to have my heading degrees
        // within the range of -360.0f to 360.0f
        if (m_HeadingDegrees > 360.0f) {
            m_HeadingDegrees -= 360.0f;
        } else if (m_HeadingDegrees < -360.0f) {
            m_HeadingDegrees += 360.0f;
        }
    }

    public void changeVelocity(float vel) {

        if (Math.abs(vel) < Math.abs(m_MaxForwardVelocity)) {
            // Our velocity is less than the max velocity increment that we
            // defined so lets increment it.
            m_ForwardVelocity += vel;
        } else {
            // Our velocity is greater than the max velocity increment that
            // we defined so we can only increment our velocity by the
            // maximum allowed value.
            if (vel < 0) {
                // We are slowing down so decrement
                m_ForwardVelocity -= -m_MaxForwardVelocity;
            } else {
                // We are speeding up so increment
                m_ForwardVelocity += m_MaxForwardVelocity;
            }
        }
    }

    // I found this code here: http://www.markmorley.com/opengl/frustumculling.html
    // and decided to make it part of
    // the camera class just in case I might want to rotate
    // and translate the projection matrix. This code will
    // make sure that the Frustum is updated correctly but
    // this member is computational expensive with:
    // 82 muliplications, 72 additions, 24 divisions, and
    // 12 subtractions for a total of 190 operations. Ouch!
    private void updateFrustum(GL gl) {

        float clip[] = new float[16],
                proj[] = new float[16],
                modl[] = new float[16],
                t;

        /* Get the current PROJECTION matrix from OpenGL */
        gl.glGetFloatv(GL.GL_PROJECTION_MATRIX, proj, 0);

        /* Get the current MODELVIEW matrix from OpenGL */
        gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, modl, 0);

        /* Combine the two matrices (multiply projection by modelview) */
        clip[0] = modl[0] * proj[0] + modl[1] * proj[4] + modl[2] * proj[8] + modl[3] * proj[12];
        clip[1] = modl[0] * proj[1] + modl[1] * proj[5] + modl[2] * proj[9] + modl[3] * proj[13];
        clip[2] = modl[0] * proj[2] + modl[1] * proj[6] + modl[2] * proj[10] + modl[3] * proj[14];
        clip[3] = modl[0] * proj[3] + modl[1] * proj[7] + modl[2] * proj[11] + modl[3] * proj[15];

        clip[4] = modl[4] * proj[0] + modl[5] * proj[4] + modl[6] * proj[8] + modl[7] * proj[12];
        clip[5] = modl[4] * proj[1] + modl[5] * proj[5] + modl[6] * proj[9] + modl[7] * proj[13];
        clip[6] = modl[4] * proj[2] + modl[5] * proj[6] + modl[6] * proj[10] + modl[7] * proj[14];
        clip[7] = modl[4] * proj[3] + modl[5] * proj[7] + modl[6] * proj[11] + modl[7] * proj[15];

        clip[8] = modl[8] * proj[0] + modl[9] * proj[4] + modl[10] * proj[8] + modl[11] * proj[12];
        clip[9] = modl[8] * proj[1] + modl[9] * proj[5] + modl[10] * proj[9] + modl[11] * proj[13];
        clip[10] = modl[8] * proj[2] + modl[9] * proj[6] + modl[10] * proj[10] + modl[11] * proj[14];
        clip[11] = modl[8] * proj[3] + modl[9] * proj[7] + modl[10] * proj[11] + modl[11] * proj[15];

        clip[12] = modl[12] * proj[0] + modl[13] * proj[4] + modl[14] * proj[8] + modl[15] * proj[12];
        clip[13] = modl[12] * proj[1] + modl[13] * proj[5] + modl[14] * proj[9] + modl[15] * proj[13];
        clip[14] = modl[12] * proj[2] + modl[13] * proj[6] + modl[14] * proj[10] + modl[15] * proj[14];
        clip[15] = modl[12] * proj[3] + modl[13] * proj[7] + modl[14] * proj[11] + modl[15] * proj[15];

        /* Extract the numbers for the RIGHT plane */
        m_Frustum[0][0] = clip[3] - clip[0];
        m_Frustum[0][1] = clip[7] - clip[4];
        m_Frustum[0][2] = clip[11] - clip[8];
        m_Frustum[0][3] = clip[15] - clip[12];

        /* Normalize the result */
        t = (float) (Math.sqrt(m_Frustum[0][0] * m_Frustum[0][0] + m_Frustum[0][1] * m_Frustum[0][1] + m_Frustum[0][2] * m_Frustum[0][2]));
        m_Frustum[0][0] /= t;
        m_Frustum[0][1] /= t;
        m_Frustum[0][2] /= t;
        m_Frustum[0][3] /= t;

        /* Extract the numbers for the LEFT plane */
        m_Frustum[1][0] = clip[3] + clip[0];
        m_Frustum[1][1] = clip[7] + clip[4];
        m_Frustum[1][2] = clip[11] + clip[8];
        m_Frustum[1][3] = clip[15] + clip[12];

        /* Normalize the result */
        t = (float) (Math.sqrt(m_Frustum[1][0] * m_Frustum[1][0] + m_Frustum[1][1] * m_Frustum[1][1] + m_Frustum[1][2] * m_Frustum[1][2]));
        m_Frustum[1][0] /= t;
        m_Frustum[1][1] /= t;
        m_Frustum[1][2] /= t;
        m_Frustum[1][3] /= t;

        /* Extract the BOTTOM plane */
        m_Frustum[2][0] = clip[3] + clip[1];
        m_Frustum[2][1] = clip[7] + clip[5];
        m_Frustum[2][2] = clip[11] + clip[9];
        m_Frustum[2][3] = clip[15] + clip[13];

        /* Normalize the result */
        t = (float) (Math.sqrt(m_Frustum[2][0] * m_Frustum[2][0] + m_Frustum[2][1] * m_Frustum[2][1] + m_Frustum[2][2] * m_Frustum[2][2]));
        m_Frustum[2][0] /= t;
        m_Frustum[2][1] /= t;
        m_Frustum[2][2] /= t;
        m_Frustum[2][3] /= t;

        /* Extract the TOP plane */
        m_Frustum[3][0] = clip[3] - clip[1];
        m_Frustum[3][1] = clip[7] - clip[5];
        m_Frustum[3][2] = clip[11] - clip[9];
        m_Frustum[3][3] = clip[15] - clip[13];

        /* Normalize the result */
        t = (float) (Math.sqrt(m_Frustum[3][0] * m_Frustum[3][0] + m_Frustum[3][1] * m_Frustum[3][1] + m_Frustum[3][2] * m_Frustum[3][2]));
        m_Frustum[3][0] /= t;
        m_Frustum[3][1] /= t;
        m_Frustum[3][2] /= t;
        m_Frustum[3][3] /= t;

        /* Extract the FAR plane */
        m_Frustum[4][0] = clip[3] - clip[2];
        m_Frustum[4][1] = clip[7] - clip[6];
        m_Frustum[4][2] = clip[11] - clip[10];
        m_Frustum[4][3] = clip[15] - clip[14];

        /* Normalize the result */
        t = (float) (Math.sqrt(m_Frustum[4][0] * m_Frustum[4][0] + m_Frustum[4][1] * m_Frustum[4][1] + m_Frustum[4][2] * m_Frustum[4][2]));
        m_Frustum[4][0] /= t;
        m_Frustum[4][1] /= t;
        m_Frustum[4][2] /= t;
        m_Frustum[4][3] /= t;

        /* Extract the NEAR plane */
        m_Frustum[5][0] = clip[3] + clip[2];
        m_Frustum[5][1] = clip[7] + clip[6];
        m_Frustum[5][2] = clip[11] + clip[10];
        m_Frustum[5][3] = clip[15] + clip[14];

        /* Normalize the result */
        t = (float) (Math.sqrt(m_Frustum[5][0] * m_Frustum[5][0] + m_Frustum[5][1] * m_Frustum[5][1] + m_Frustum[5][2] * m_Frustum[5][2]));
        m_Frustum[5][0] /= t;
        m_Frustum[5][1] /= t;
        m_Frustum[5][2] /= t;
        m_Frustum[5][3] /= t;
    }

    // This is the much faster version of the above member
    // function, however the speed increase is not gained
    // without a cost. If you rotate or translate the projection
    // matrix then this member will not work correctly. That is acceptable
    // in my book considering I very rarely do such a thing.
    // This function has far fewer operations in it and I
    // shaved off 2 square root functions by passing in the
    // near and far values. This member has:
    // 38 muliplications, 28 additions, 24 divisions, and
    // 12 subtractions for a total of 102 operations. Still hurts
    // but at least it is decent now. In practice this will
    // run about 2 times faster than the above function.
    public void updateFrustumFaster(GL gl) {

        float clip[] = new float[16],
                proj[] = new float[16],
                modl[] = new float[16],
                t;

        /* Get the current PROJECTION matrix from OpenGL */
        gl.glGetFloatv(GL.GL_PROJECTION_MATRIX, proj, 0);

        /* Get the current MODELVIEW matrix from OpenGL */
        gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, modl, 0);

        /* Combine the two matrices (multiply projection by modelview)
           but keep in mind this function will only work if you do NOT
          rotate or translate your projection matrix                  */
        clip[0] = modl[0] * proj[0];
        clip[1] = modl[1] * proj[5];
        clip[2] = modl[2] * proj[10] + modl[3] * proj[14];
        clip[3] = modl[2] * proj[11];

        clip[4] = modl[4] * proj[0];
        clip[5] = modl[5] * proj[5];
        clip[6] = modl[6] * proj[10] + modl[7] * proj[14];
        clip[7] = modl[6] * proj[11];

        clip[8] = modl[8] * proj[0];
        clip[9] = modl[9] * proj[5];
        clip[10] = modl[10] * proj[10] + modl[11] * proj[14];
        clip[11] = modl[10] * proj[11];

        clip[12] = modl[12] * proj[0];
        clip[13] = modl[13] * proj[5];
        clip[14] = modl[14] * proj[10] + modl[15] * proj[14];
        clip[15] = modl[14] * proj[11];

        /* Extract the numbers for the RIGHT plane */
        m_Frustum[0][0] = clip[3] - clip[0];
        m_Frustum[0][1] = clip[7] - clip[4];
        m_Frustum[0][2] = clip[11] - clip[8];
        m_Frustum[0][3] = clip[15] - clip[12];

        /* Normalize the result */
        t = (float) (Math.sqrt(m_Frustum[0][0] * m_Frustum[0][0] + m_Frustum[0][1] * m_Frustum[0][1] + m_Frustum[0][2] * m_Frustum[0][2]));
        m_Frustum[0][0] /= t;
        m_Frustum[0][1] /= t;
        m_Frustum[0][2] /= t;
        m_Frustum[0][3] /= t;

        /* Extract the numbers for the LEFT plane */
        m_Frustum[1][0] = clip[3] + clip[0];
        m_Frustum[1][1] = clip[7] + clip[4];
        m_Frustum[1][2] = clip[11] + clip[8];
        m_Frustum[1][3] = clip[15] + clip[12];

        /* Normalize the result */
        t = (float) (Math.sqrt(m_Frustum[1][0] * m_Frustum[1][0] + m_Frustum[1][1] * m_Frustum[1][1] + m_Frustum[1][2] * m_Frustum[1][2]));
        m_Frustum[1][0] /= t;
        m_Frustum[1][1] /= t;
        m_Frustum[1][2] /= t;
        m_Frustum[1][3] /= t;

        /* Extract the BOTTOM plane */
        m_Frustum[2][0] = clip[3] + clip[1];
        m_Frustum[2][1] = clip[7] + clip[5];
        m_Frustum[2][2] = clip[11] + clip[9];
        m_Frustum[2][3] = clip[15] + clip[13];

        /* Normalize the result */
        t = (float) (Math.sqrt(m_Frustum[2][0] * m_Frustum[2][0] + m_Frustum[2][1] * m_Frustum[2][1] + m_Frustum[2][2] * m_Frustum[2][2]));
        m_Frustum[2][0] /= t;
        m_Frustum[2][1] /= t;
        m_Frustum[2][2] /= t;
        m_Frustum[2][3] /= t;

        /* Extract the TOP plane */
        m_Frustum[3][0] = clip[3] - clip[1];
        m_Frustum[3][1] = clip[7] - clip[5];
        m_Frustum[3][2] = clip[11] - clip[9];
        m_Frustum[3][3] = clip[15] - clip[13];

        /* Normalize the result */
        t = (float) (Math.sqrt(m_Frustum[3][0] * m_Frustum[3][0] + m_Frustum[3][1] * m_Frustum[3][1] + m_Frustum[3][2] * m_Frustum[3][2]));
        m_Frustum[3][0] /= t;
        m_Frustum[3][1] /= t;
        m_Frustum[3][2] /= t;
        m_Frustum[3][3] /= t;

        /* Extract the FAR plane */
        m_Frustum[4][0] = clip[3] - clip[2];
        m_Frustum[4][1] = clip[7] - clip[6];
        m_Frustum[4][2] = clip[11] - clip[10];
        m_Frustum[4][3] = clip[15] - clip[14];

        /* Normalize the result */
        t = (float) (Math.sqrt(m_Frustum[4][0] * m_Frustum[4][0] + m_Frustum[4][1] * m_Frustum[4][1] + m_Frustum[4][2] * m_Frustum[4][2]));
        m_Frustum[4][0] /= t;
        m_Frustum[4][1] /= t;
        m_Frustum[4][2] /= t;
        m_Frustum[4][3] /= t;

        /* Extract the NEAR plane */
        m_Frustum[5][0] = clip[3] + clip[2];
        m_Frustum[5][1] = clip[7] + clip[6];
        m_Frustum[5][2] = clip[11] + clip[10];
        m_Frustum[5][3] = clip[15] + clip[14];

        /* Normalize the result */
        t = (float) (Math.sqrt(m_Frustum[5][0] * m_Frustum[5][0] + m_Frustum[5][1] * m_Frustum[5][1] + m_Frustum[5][2] * m_Frustum[5][2]));
        m_Frustum[5][0] /= t;
        m_Frustum[5][1] /= t;
        m_Frustum[5][2] /= t;
        m_Frustum[5][3] /= t;
    }

    // This member function checks to see if a sphere is in
    // the viewing volume.
    private boolean sphereInFrustum(Tuple3f p, float Radius) {

        int i;
        // The idea here is the same as the PointInFrustum function.

        for (i = 0; i < 6; i++) {
            // If the point is outside of the plane then its not in the viewing volume.
            if (m_Frustum[i][0] * p.x + m_Frustum[i][1] * p.y + m_Frustum[i][2] * p.z + m_Frustum[i][3] <= -Radius) {
                return false;
            }
        }
        return true;
    }

    // This member fuction checks to see if a point is in
    // the viewing volume.
    public boolean pointInFrustum(Tuple3f p) {

        int i;

        // The idea behind this algorithum is that if the point
        // is inside all 6 clipping planes then it is inside our
        // viewing volume so we can return true.

        for (i = 0; i < 6; i++) {
            if (m_Frustum[i][0] * p.x + m_Frustum[i][1] * p.y + m_Frustum[i][2] * p.z + m_Frustum[i][3] <= 0) {
                return false;
            }
        }
        return true;
    }

    // This member function checks to see if a sphere is in
    // the viewing volume.
    private boolean sphereInFrustum(float x, float y, float z, float Radius) {

        int i;
        // The idea here is the same as the PointInFrustum function.

        for (i = 0; i < 6; i++) {
            // If the point is outside of the plane then its not in the viewing volume.
            if (m_Frustum[i][0] * x + m_Frustum[i][1] * y + m_Frustum[i][2] * z + m_Frustum[i][3] <= -Radius) {
                return false;
            }
        }
        return true;
    }

    // This member fuction checks to see if a point is in
    // the viewing volume.

    public boolean pointInFrustum(float x, float y, float z) {

        int i;
        // The idea behind this algorithum is that if the point
        // is inside all 6 clipping planes then it is inside our
        // viewing volume so we can return true.

        for (i = 0; i < 6; i++) {	// Loop through all our clipping planes
            // If the point is outside of the plane then its not in the viewing volume.
            if (m_Frustum[i][0] * x + m_Frustum[i][1] * y + m_Frustum[i][2] * z + m_Frustum[i][3] <= 0) {
                return false;
            }
        }
        return true;
    }

    public void renderLensFlare(GL gl) {

        float Length = 0.0f;

        // Draw the flare only If the light source is in our line of sight
        if (sphereInFrustum(m_LightSourcePos, 1.0f)) {

            vLightSourceToCamera.sub(m_Position, m_LightSourcePos);     // Lets compute the vector that points to the camera from
            // the light source.
            Length = vLightSourceToCamera.length();                     // Save the length we will need it in a minute
            ptIntersect.scale(Length, m_DirectionVector);                // Now lets find an point along the cameras direction
            // vector that we can use as an intersection point.
            // Lets translate down this vector the same distance
            // that the camera is away from the light source.
            ptIntersect.add(m_Position);
            vLightSourceToIntersect.sub(ptIntersect, m_LightSourcePos); // Lets compute the vector that points to the Intersect
            // point from the light source
            Length = vLightSourceToIntersect.length();                  // Save the length we will need it later.
            vLightSourceToIntersect.normalize();                        // Normalize the vector so its unit length

            gl.glEnable(GL.GL_BLEND);                                   // You should already know what this does
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);                  // You should already know what this does
            gl.glDisable(GL.GL_DEPTH_TEST);                             // You should already know what this does
            gl.glEnable(GL.GL_TEXTURE_2D);                              // You should already know what this does

            /////////// Differenet Color Glows & Streaks /////////////////////
            //RenderBigGlow(1.0f, 1.0f, 1.0f, 1.0f, m_LightSourcePos, 1.0f);
            //RenderStreaks(1.0f, 1.0f, 0.8f, 1.0f, m_LightSourcePos, 0.7f);
            //
            //RenderBigGlow(1.0f, 0.9f, 1.0f, 1.0f, m_LightSourcePos, 1.0f);
            //RenderStreaks(1.0f, 0.9f, 1.0f, 1.0f, m_LightSourcePos, 0.7f);
            //////////////////////////////////////////////////////////////////

            // Render the large hazy glow
            renderBigGlow(gl, 0.60f, 0.60f, 0.8f, 1.0f, m_LightSourcePos, 16.0f);
            // Render the streaks
            renderStreaks(gl, 0.60f, 0.60f, 0.8f, 1.0f, m_LightSourcePos, 16.0f);
            // Render the small Glow
            renderGlow(gl, 0.8f, 0.8f, 1.0f, 0.5f, m_LightSourcePos, 3.5f);

            pt.scaleAdd(Length * .1f, vLightSourceToIntersect, m_LightSourcePos);      // Lets compute a point that is 20%
            // away from the light source in the
            // direction of the intersection point.
            renderGlow(gl, 0.9f, 0.6f, 0.4f, 0.5f, pt, 0.6f);                         // Render the small Glow

            pt.scaleAdd(Length * .15f, vLightSourceToIntersect, m_LightSourcePos);  // Lets compute a point that is 30%
            // away from the light source in the
            // direction of the intersection point.
            renderHalo(gl, 0.8f, 0.5f, 0.6f, 0.5f, pt, 1.7f);                         // Render the a Halo

            pt.scaleAdd(Length * .175f, vLightSourceToIntersect, m_LightSourcePos); // Lets compute a point that is 35%
            // away from the light source in the
            // direction of the intersection point.
            renderHalo(gl, 0.9f, 0.2f, 0.1f, 0.5f, pt, 0.83f);                        // Render the a Halo

            pt.scaleAdd(Length * .285f, vLightSourceToIntersect, m_LightSourcePos); // Lets compute a point that is 57%
            // away from the light source in the
            // direction of the intersection point.
            renderHalo(gl, 0.7f, 0.7f, 0.4f, 0.5f, pt, 1.6f);                         // Render the a Halo

            pt.scaleAdd(Length * .2755f, vLightSourceToIntersect, m_LightSourcePos);// Lets compute a point that is 55.1%
            // away from the light source in the
            // direction of the intersection point.
            renderGlow(gl, 0.9f, 0.9f, 0.2f, 0.5f, pt, 0.8f);                         // Render the small Glow

            pt.scaleAdd(Length * .4775f, vLightSourceToIntersect, m_LightSourcePos);// Lets compute a point that is 95.5%
            // away from the light source in the
            // direction of the intersection point.
            renderGlow(gl, 0.93f, 0.82f, 0.73f, 0.5f, pt, 1.0f);                      // Render the small Glow

            pt.scaleAdd(Length * .49f, vLightSourceToIntersect, m_LightSourcePos);  // Lets compute a point that is 98%
            // away from the light source in the
            // direction of the intersection point.
            renderHalo(gl, 0.7f, 0.6f, 0.5f, 0.5f, pt, 1.4f);                         // Render the a Halo

            pt.scaleAdd(Length * .65f, vLightSourceToIntersect, m_LightSourcePos);  // Lets compute a point that is 130%
            // away from the light source in the
            // direction of the intersection point.
            renderGlow(gl, 0.7f, 0.8f, 0.3f, 0.5f, pt, 1.8f);                         // Render the small Glow

            pt.scaleAdd(Length * 0.63f, vLightSourceToIntersect, m_LightSourcePos); // Lets compute a point that is 126%
            // away from the light source in the
            // direction of the intersection point.
            renderGlow(gl, 0.4f, 0.3f, 0.2f, 0.5f, pt, 1.4f);                         // Render the small Glow

            pt.scaleAdd(Length * 0.8f, vLightSourceToIntersect, m_LightSourcePos);  // Lets compute a point that is 160%
            // away from the light source in the
            // direction of the intersection point.
            renderHalo(gl, 0.7f, 0.5f, 0.5f, 0.5f, pt, 1.4f);                         // Render the a Halo

            pt.scaleAdd(Length * .7825f, vLightSourceToIntersect, m_LightSourcePos);// Lets compute a point that is 156.5%
            // away from the light source in the
            // direction of the intersection point.
            renderGlow(gl, 0.8f, 0.5f, 0.1f, 0.5f, pt, 0.6f);                         // Render the small Glow

            pt.scaleAdd(Length * 1.0f, vLightSourceToIntersect, m_LightSourcePos);  // Lets compute a point that is 200%
            // away from the light source in the
            // direction of the intersection point.
            renderHalo(gl, 0.5f, 0.5f, 0.7f, 0.5f, pt, 1.7f);                         // Render the a Halo

            pt.scaleAdd(Length * 0.975f, vLightSourceToIntersect, m_LightSourcePos);// Lets compute a point that is 195%
            // away from the light source in the
            // direction of the intersection point.
            renderGlow(gl, 0.4f, 0.1f, 0.9f, 0.5f, pt, 2.0f);                         // Render the small Glow

            gl.glDisable(GL.GL_BLEND);                                           // You should already know what this does
            gl.glEnable(GL.GL_DEPTH_TEST);                                        // You should already know what this does
            gl.glDisable(GL.GL_TEXTURE_2D);                                       // You should already know what this does
        }
    }

    private void renderHalo(GL gl, float r, float g, float b, float a, Tuple3f p, float scale) {

        Tuple3f[] q = new Tuple3f[4];
        for (int i = 3; i >= 0; i--)
            q[i] = new Tuple3f();
        // Basically we are just going to make a 2D box
        // from four points we don't need a z coord because
        // we are rotating the camera by the inverse so the
        // texture mapped quads will always face us.
        q[0].x = (p.x - scale);            // Set the x coordinate -scale units from the center point.
        q[0].y = (p.y - scale);            // Set the y coordinate -scale units from the center point.

        q[1].x = (p.x - scale);            // Set the x coordinate -scale units from the center point.
        q[1].y = (p.y + scale);            // Set the y coordinate scale units from the center point.

        q[2].x = (p.x + scale);            // Set the x coordinate scale units from the center point.
        q[2].y = (p.y - scale);            // Set the y coordinate -scale units from the center point.

        q[3].x = (p.x + scale);            // Set the x coordinate scale units from the center point.
        q[3].y = (p.y + scale);            // Set the y coordinate scale units from the center point.

        gl.glPushMatrix();                                    // Save the model view matrix
        gl.glTranslatef(p.x, p.y, p.z);                       // Translate to our point
        gl.glRotatef(-m_HeadingDegrees, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-m_PitchDegrees, 1.0f, 0.0f, 0.0f);
        gl.glBindTexture(GL.GL_TEXTURE_2D, m_HaloTexture[0]); // Bind to the Big Glow texture
        gl.glColor4f(r, g, b, a);                             // Set the color since the texture is a gray scale

        gl.glBegin(GL.GL_TRIANGLE_STRIP);                     // Draw the Big Glow on a Triangle Strip
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(q[0].x, q[0].y);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(q[1].x, q[1].y);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(q[2].x, q[2].y);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(q[3].x, q[3].y);
        gl.glEnd();
        gl.glPopMatrix();                                 // Restore the model view matrix
    }

    private void renderGlow(GL gl, float r, float g, float b, float a, Tuple3f p, float scale) {

        Tuple3f[] q = new Tuple3f[4];
        for (int i = 3; i >= 0; i--)
            q[i] = new Tuple3f();

        // Basically we are just going to make a 2D box
        // from four points we don't need a z coord because
        // we are rotating the camera by the inverse so the
        // texture mapped quads will always face us.
        q[0].x = (p.x - scale);            // Set the x coordinate -scale units from the center point.
        q[0].y = (p.y - scale);            // Set the y coordinate -scale units from the center point.

        q[1].x = (p.x - scale);            // Set the x coordinate -scale units from the center point.
        q[1].y = (p.y + scale);            // Set the y coordinate scale units from the center point.

        q[2].x = (p.x + scale);            // Set the x coordinate scale units from the center point.
        q[2].y = (p.y - scale);            // Set the y coordinate -scale units from the center point.

        q[3].x = (p.x + scale);            // Set the x coordinate scale units from the center point.
        q[3].y = (p.y + scale);            // Set the y coordinate scale units from the center point.

        gl.glPushMatrix();                                 // Save the model view matrix
        gl.glTranslatef(p.x, p.y, p.z);                    // Translate to our point
        gl.glRotatef(-m_HeadingDegrees, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-m_PitchDegrees, 1.0f, 0.0f, 0.0f);
        gl.glBindTexture(GL.GL_TEXTURE_2D, m_GlowTexture[0]); // Bind to the Big Glow texture
        gl.glColor4f(r, g, b, a);                             // Set the color since the texture is a gray scale

        gl.glBegin(GL.GL_TRIANGLE_STRIP);                     // Draw the Big Glow on a Triangle Strip
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(q[0].x, q[0].y);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(q[1].x, q[1].y);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(q[2].x, q[2].y);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(q[3].x, q[3].y);
        gl.glEnd();
        gl.glPopMatrix();                                 // Restore the model view matrix
    }

    private void renderBigGlow(GL gl, float r, float g, float b, float a, Tuple3f p, float scale) {

        Tuple3f[] q = new Tuple3f[4];
        for (int i = 3; i >= 0; i--)
            q[i] = new Tuple3f();

        // Basically we are just going to make a 2D box
        // from four points we don't need a z coord because
        // we are rotating the camera by the inverse so the
        // texture mapped quads will always face us.
        q[0].x = (p.x - scale);            // Set the x coordinate -scale units from the center point.
        q[0].y = (p.y - scale);            // Set the y coordinate -scale units from the center point.

        q[1].x = (p.x - scale);            // Set the x coordinate -scale units from the center point.
        q[1].y = (p.y + scale);            // Set the y coordinate scale units from the center point.

        q[2].x = (p.x + scale);            // Set the x coordinate scale units from the center point.
        q[2].y = (p.y - scale);            // Set the y coordinate -scale units from the center point.

        q[3].x = (p.x + scale);            // Set the x coordinate scale units from the center point.
        q[3].y = (p.y + scale);            // Set the y coordinate scale units from the center point.

        gl.glPushMatrix();                 // Save the model view matrix
        gl.glTranslatef(p.x, p.y, p.z);    // Translate to our point
        gl.glRotatef(-m_HeadingDegrees, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-m_PitchDegrees, 1.0f, 0.0f, 0.0f);
        gl.glBindTexture(GL.GL_TEXTURE_2D, m_BigGlowTexture[0]); // Bind to the Big Glow texture
        gl.glColor4f(r, g, b, a);                                // Set the color since the texture is a gray scale

        gl.glBegin(GL.GL_TRIANGLE_STRIP);                        // Draw the Big Glow on a Triangle Strip
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(q[0].x, q[0].y);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(q[1].x, q[1].y);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(q[2].x, q[2].y);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(q[3].x, q[3].y);
        gl.glEnd();
        gl.glPopMatrix();                                     // Restore the model view matrix
    }

    private void renderStreaks(GL gl, float r, float g, float b, float a, Tuple3f p, float scale) {

        Tuple3f[] q = new Tuple3f[4];
        for (int i = 3; i >= 0; i--)
            q[i] = new Tuple3f();
        // Basically we are just going to make a 2D box
        // from four points we don't need a z coord because
        // we are rotating the camera by the inverse so the
        // texture mapped quads will always face us.
        q[0].x = (p.x - scale);            // Set the x coordinate -scale units from the center point.
        q[0].y = (p.y - scale);            // Set the y coordinate -scale units from the center point.

        q[1].x = (p.x - scale);            // Set the x coordinate -scale units from the center point.
        q[1].y = (p.y + scale);            // Set the y coordinate scale units from the center point.

        q[2].x = (p.x + scale);            // Set the x coordinate scale units from the center point.
        q[2].y = (p.y - scale);            // Set the y coordinate -scale units from the center point.

        q[3].x = (p.x + scale);            // Set the x coordinate scale units from the center point.
        q[3].y = (p.y + scale);            // Set the y coordinate scale units from the center point.

        gl.glPushMatrix();                 // Save the model view matrix
        gl.glTranslatef(p.x, p.y, p.z);    // Translate to our point
        gl.glRotatef(-m_HeadingDegrees, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-m_PitchDegrees, 1.0f, 0.0f, 0.0f);
        gl.glBindTexture(GL.GL_TEXTURE_2D, m_StreakTexture[0]); // Bind to the Big Glow texture
        gl.glColor4f(r, g, b, a);                               // Set the color since the texture is a gray scale

        gl.glBegin(GL.GL_TRIANGLE_STRIP);                       // Draw the Big Glow on a Triangle Strip
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(q[0].x, q[0].y);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(q[1].x, q[1].y);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(q[2].x, q[2].y);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(q[3].x, q[3].y);
        gl.glEnd();
        gl.glPopMatrix();  // Restore the model view matrix
    }
}