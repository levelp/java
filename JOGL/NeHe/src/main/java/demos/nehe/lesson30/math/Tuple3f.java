package demos.nehe.lesson30.math;

/**
 * @author Abdul Bezrati
 */
public class Tuple3f {
    float x, y, z;

    public Tuple3f() {
        x = y = z = 0;
    }

    public Tuple3f(Tuple3f p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public Tuple3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Tuple3f vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public void cross(Tuple3f u, Tuple3f v) {
        this.x = (u.y * v.z) - (u.z * v.y);
        this.y = (u.z * v.x) - (u.x * v.z);
        this.z = (u.x * v.y) - (u.y * v.x);
    }

    public float Dot(Tuple3f u) {
        return u.x * this.x +
                u.y * this.y +
                u.z * this.z;
    }

    public void add(Tuple3f u, Tuple3f v) {
        x = u.x + v.x;
        y = u.y + v.y;
        z = u.z + v.z;
    }

    public void scaleAdd(float f, Tuple3f u, Tuple3f v) {
        x = f * u.x + v.x;
        y = f * u.y + v.y;
        z = f * u.z + v.z;
    }

    public void add(Tuple3f u) {
        x += u.x;
        y += u.y;
        z += u.z;
    }

    public void add(float a) {
        x += a;
        y += a;
        z += a;
    }

    public void sub(Tuple3f u, Tuple3f v) {
        x = u.x - v.x;
        y = u.y - v.y;
        z = u.z - v.z;
    }

    public void sub(Tuple3f u) {
        x -= u.x;
        y -= u.y;
        z -= u.z;
    }

    public void scale(float mul) {
        x *= mul;
        y *= mul;
        z *= mul;
    }

    public void mul(Tuple3f v1, Tuple3f v2) {
        x = v1.x * v2.x;
        y = v1.y * v2.y;
        z = v1.z * v2.z;
    }

    public void normalize() {
        float length = (float) Math.sqrt(x * x + y * y + z * z);
        x /= length;
        y /= length;
        z /= length;
    }

    public float distance(Tuple3f u) {
        return (float) Math.sqrt((this.x - u.x) * (this.x - u.x) +
                (this.y - u.y) * (this.y - u.y) +
                (this.z - u.z) * (this.z - u.z));
    }

    public void vectorMatrixMul(Tuple3f vector, Matrix4f m) {

        x = (vector.x * m.elements[0]) + (vector.y * m.elements[4]) + (vector.z * m.elements[8]) + m.elements[12];
        y = (vector.x * m.elements[1]) + (vector.y * m.elements[5]) + (vector.z * m.elements[9]) + m.elements[13];
        z = (vector.x * m.elements[2]) + (vector.y * m.elements[6]) + (vector.z * m.elements[10]) + m.elements[14];
    }

    public void vectorMatrixMul2(Tuple3f vector, Matrix4f m) {

        x = (vector.x * m.elements[0]) + (vector.y * m.elements[4]) + (vector.z * m.elements[8]);
        y = (vector.x * m.elements[1]) + (vector.y * m.elements[5]) + (vector.z * m.elements[9]);
        z = (vector.x * m.elements[2]) + (vector.y * m.elements[6]) + (vector.z * m.elements[10]);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public void negate() {
        x = -x;
        y = -y;
        z = -z;
    }
}
