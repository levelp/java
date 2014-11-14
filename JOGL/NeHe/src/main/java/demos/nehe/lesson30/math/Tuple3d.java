package demos.nehe.lesson30.math;

/**
 * @author Abdul Bezrati
 */
public class Tuple3d {
    public double x, y, z;

    public Tuple3d() {
        x = y = z = 0;
    }

    public Tuple3d(Tuple3d p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public Tuple3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Tuple3d vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public void cross(Tuple3d u, Tuple3d v) {
        this.x = (u.y * v.z) - (u.z * v.y);
        this.y = (u.z * v.x) - (u.x * v.z);
        this.z = (u.x * v.y) - (u.y * v.x);
    }

    public double dot(Tuple3d u) {
        return u.x * this.x +
                u.y * this.y +
                u.z * this.z;
    }

    public void add(Tuple3d u, Tuple3d v) {
        x = u.x + v.x;
        y = u.y + v.y;
        z = u.z + v.z;
    }

    public void scaleAdd(double d, Tuple3d u, Tuple3d v) {
        x = d * u.x + v.x;
        y = d * u.y + v.y;
        z = d * u.z + v.z;
    }

    public void scaleAdd(double d, Tuple3d u) {
        x = d * u.x;
        y = d * u.y;
        z = d * u.z;
    }

    public void add(Tuple3d u) {
        x += u.x;
        y += u.y;
        z += u.z;
    }

    public void add(double a) {
        x += a;
        y += a;
        z += a;
    }

    public void sub(Tuple3d u, Tuple3d v) {
        x = u.x - v.x;
        y = u.y - v.y;
        z = u.z - v.z;
    }

    public void sub(Tuple3d u) {
        x -= u.x;
        y -= u.y;
        z -= u.z;
    }

    public void scale(double mul) {
        x *= mul;
        y *= mul;
        z *= mul;
    }

    public void mul(Tuple3d v1, Tuple3d v2) {
        x = v1.x * v2.x;
        y = v1.y * v2.y;
        z = v1.z * v2.z;
    }

    public void normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
        }
    }

    public double distance(Tuple3d u) {
        return Math.sqrt((this.x - u.x) * (this.x - u.x) +
                (this.y - u.y) * (this.y - u.y) +
                (this.z - u.z) * (this.z - u.z));
    }

    public void vectorMatrixMul(Tuple3d vector, Matrix4f m) {

        x = (vector.x * m.elements[0]) + (vector.y * m.elements[4]) + (vector.z * m.elements[8]) + m.elements[12];
        y = (vector.x * m.elements[1]) + (vector.y * m.elements[5]) + (vector.z * m.elements[9]) + m.elements[13];
        z = (vector.x * m.elements[2]) + (vector.y * m.elements[6]) + (vector.z * m.elements[10]) + m.elements[14];
    }

    public void vectorMatrixMul2(Tuple3d vector, Matrix4f m) {

        x = (vector.x * m.elements[0]) + (vector.y * m.elements[4]) + (vector.z * m.elements[8]);
        y = (vector.x * m.elements[1]) + (vector.y * m.elements[5]) + (vector.z * m.elements[9]);
        z = (vector.x * m.elements[2]) + (vector.y * m.elements[6]) + (vector.z * m.elements[10]);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void negate() {
        x = -x;
        y = -y;
        z = -z;
    }
}
