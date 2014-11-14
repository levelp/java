package demos.nehe.lesson30.math;

/**
 * @author Abdul Bezrati
 */
public class Matrix4f {
    float elements[] = new float[16];

    public Matrix4f() {
    }

    public Matrix4f(float elements[]) {
        for (int i = 0; i < 16; i++)
            this.elements[i] = elements[i];
    }

    public Matrix4f(Tuple3f t1, Tuple3f t2, Tuple3f t3, boolean row_column) {

        if (row_column) {
            elements[0] = t1.x;
            elements[1] = t1.y;
            elements[2] = t1.z;
            elements[4] = t2.x;
            elements[5] = t2.y;
            elements[6] = t2.z;
            elements[8] = t3.x;
            elements[9] = t3.y;
            elements[10] = t3.z;
        } else {
            elements[0] = t1.x;
            elements[1] = t2.x;
            elements[2] = t3.x;
            elements[4] = t1.y;
            elements[5] = t2.y;
            elements[6] = t3.y;
            elements[8] = t1.z;
            elements[9] = t2.z;
            elements[10] = t3.z;
        }

        elements[15] = 1;
    }

    void load_identity() {

        elements[0] = 1;
        elements[1] = 0;
        elements[2] = 0;
        elements[3] = 0;
        elements[4] = 0;
        elements[5] = 1;
        elements[6] = 0;
        elements[7] = 0;
        elements[8] = 0;
        elements[9] = 0;
        elements[10] = 1;
        elements[11] = 0;
        elements[12] = 0;
        elements[13] = 0;
        elements[14] = 0;
        elements[15] = 1;
    }

    void add(Matrix4f m) {

        for (int i = 0; i < 16; i++)
            elements[i] += m.elements[i];
    }

    void sub(Matrix4f m) {

        for (int i = 0; i < 16; i++)
            elements[i] -= m.elements[i];
    }

    void mul(float s) {

        for (int i = 0; i < 16; i++)
            elements[i] *= s;
    }

    void mul(Matrix4f m) {

        float copy[] = new float[16];
        for (int i = 0; i < 16; i++)
            copy[i] = elements[i];

        for (int i = 0; i < 16; i += 4) {
            elements[i + 0] = copy[i] * m.elements[0] + copy[i + 1] * m.elements[4] + copy[i + 2] * m.elements[8] + copy[i + 3] * m.elements[12];
            elements[i + 1] = copy[i] * m.elements[1] + copy[i + 1] * m.elements[5] + copy[i + 2] * m.elements[9] + copy[i + 3] * m.elements[13];
            elements[i + 2] = copy[i] * m.elements[2] + copy[i + 1] * m.elements[6] + copy[i + 2] * m.elements[10] + copy[i + 3] * m.elements[14];
            elements[i + 3] = copy[i] * m.elements[3] + copy[i + 1] * m.elements[7] + copy[i + 2] * m.elements[11] + copy[i + 3] * m.elements[15];
        }
    }

    void scale(float x, float y, float z) {

        elements[0] *= x;
        elements[5] *= y;
        elements[10] *= z;
    }

    void translate(float x, float y, float z) {

        elements[3] += x;
        elements[7] += y;
        elements[11] += z;
    }

    void add(Matrix4f m, Matrix4f n) {

        for (int i = 0; i < 16; i++)
            elements[i] = m.elements[i] + n.elements[i];
    }

    void sub(Matrix4f m, Matrix4f n) {

        for (int i = 0; i < 16; i++)
            elements[i] = m.elements[i] - n.elements[i];
    }

    void mul(Matrix4f m, Matrix4f n) {

        for (int i = 0; i < 16; i += 4) {
            elements[i + 0] = m.elements[i] * n.elements[0] + m.elements[i + 1] * n.elements[4] + m.elements[i + 2] * n.elements[8] + m.elements[i + 3] * n.elements[12];
            elements[i + 1] = m.elements[i] * n.elements[1] + m.elements[i + 1] * n.elements[5] + m.elements[i + 2] * n.elements[9] + m.elements[i + 3] * n.elements[13];
            elements[i + 2] = m.elements[i] * n.elements[2] + m.elements[i + 1] * n.elements[6] + m.elements[i + 2] * n.elements[10] + m.elements[i + 3] * n.elements[14];
            elements[i + 3] = m.elements[i] * n.elements[3] + m.elements[i + 1] * n.elements[7] + m.elements[i + 2] * n.elements[11] + m.elements[i + 3] * n.elements[15];
        }
    }

    void invert(Matrix4f m) {

        elements[0] = m.elements[0];
        elements[1] = m.elements[4];
        elements[2] = m.elements[8];
        elements[3] = 0;
        elements[4] = m.elements[1];
        elements[5] = m.elements[5];
        elements[6] = m.elements[9];
        elements[7] = 0;
        elements[8] = m.elements[2];
        elements[9] = m.elements[6];
        elements[10] = m.elements[10];
        elements[11] = 0;

        elements[12] = -(m.elements[12] * m.elements[0]) - (m.elements[13] * m.elements[1]) - (m.elements[14] * m.elements[2]);
        elements[13] = -(m.elements[12] * m.elements[4]) - (m.elements[13] * m.elements[5]) - (m.elements[14] * m.elements[6]);
        elements[14] = -(m.elements[12] * m.elements[8]) - (m.elements[13] * m.elements[9]) - (m.elements[14] * m.elements[10]);
        elements[15] = 1;
    }

    void transpose() {

        float copy[] = new float[16];
        for (int i = 0; i < 16; i++)
            copy[i] = elements[i];

        elements[0] = copy[0];
        elements[1] = copy[4];
        elements[2] = copy[8];
        elements[3] = copy[12];
        elements[4] = copy[1];
        elements[5] = copy[5];
        elements[6] = copy[9];
        elements[7] = copy[13];
        elements[8] = copy[2];
        elements[9] = copy[6];
        elements[10] = copy[10];
        elements[11] = copy[14];
        elements[12] = copy[3];
        elements[13] = copy[7];
        elements[14] = copy[11];
        elements[15] = copy[15];
    }

    void rotateX(float angle) {

        angle *= (float) Math.PI / 180;

        elements[0] = 1;
        elements[1] = 0;
        elements[2] = 0;
        elements[3] = 0;
        elements[4] = 0;
        elements[5] = (float) Math.cos(angle);
        elements[6] = -(float) Math.sin(angle);
        elements[7] = 0;
        elements[8] = 0;
        elements[9] = (float) Math.sin(angle);
        elements[10] = (float) Math.cos(angle);
        elements[11] = 0;
        elements[12] = 0;
        elements[13] = 0;
        elements[14] = 0;
        elements[15] = 1;
    }

    void rotateY(float angle) {

        angle *= (float) Math.PI / 180;

        elements[0] = (float) Math.cos(angle);
        elements[1] = 0;
        elements[2] = (float) Math.sin(angle);
        elements[3] = 0;
        elements[4] = 0;
        elements[5] = 1;
        elements[6] = 0;
        elements[7] = 0;
        elements[8] = -(float) Math.sin(angle);
        elements[9] = 0;
        elements[10] = (float) Math.cos(angle);
        elements[11] = 0;
        elements[12] = 0;
        elements[13] = 0;
        elements[14] = 0;
        elements[15] = 1;
    }

    void rotateZ(float angle) {

        angle *= (float) Math.PI / 180;

        elements[0] = (float) Math.cos(angle);
        elements[1] = (float) Math.sin(angle);
        elements[2] = 0;
        elements[3] = 0;
        elements[4] = -(float) Math.sin(angle);
        elements[5] = (float) Math.cos(angle);
        elements[6] = 0;
        elements[7] = 0;
        elements[8] = 0;
        elements[9] = 0;
        elements[10] = 1;
        elements[11] = 0;
        elements[12] = 0;
        elements[13] = 0;
        elements[14] = 0;
        elements[15] = 1;
    }
}
