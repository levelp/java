package demos.nehe.lesson39;

class Vector3D {
    public float x;									// the x value of this Vector3D
    public float y;									// the y value of this Vector3D
    public float z;									// the z value of this Vector3D

    public Vector3D()									// Constructor to set x = y = z = 0
    {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3D(Vector3D vector)									// Constructor to set x = y = z = 0
    {
        x = vector.x;
        y = vector.y;
        z = vector.z;
    }

    public Vector3D(float x, float y, float z)			// Constructor that initializes this Vector3D to the intended values of x, y and z
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D add(Vector3D v)			// operator+= is used to add another Vector3D to this Vector3D.
    {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public Vector3D sub(Vector3D v)			// operator-= is used to subtract another Vector3D from this Vector3D.
    {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public Vector3D scale(float value)			// operator*= is used to scale this Vector3D by a value.
    {
        x *= value;
        y *= value;
        z *= value;
        return this;
    }

    public Vector3D inverse()						// operator- is used to set this Vector3D's x, y, and z to the negative of them.
    {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public float length()								// length() returns the length of this Vector3D
    {
        return (float) Math.sqrt(x * x + y * y + z * z);
    };

    public void normalize()								// normalize() normalizes this Vector3D that its direction remains the same but its length is 1.
    {
        float length = length();

        if (length == 0)
            return;

        x /= length;
        y /= length;
        z /= length;
    }

    public String toString() {
        return x + ", " + y + ", " + z;
    }
}
