package demos.nehe.lesson27;

import demos.common.ResourceRetriever;
import javax.media.opengl.GL;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

class Object3D {
    private int nPlanes, nPoints;
    private Point points[] = new Point[100];
    private Plane planes[] = new Plane[100];

    public Object3D() {
        for (int i = 0; i < 100; i++) {
            points[i] = new Point();
            planes[i] = new Plane();
        }
    }

    // load object
    public static Object3D readObject(String st) throws IOException {
        StringBuffer data = new StringBuffer();
        InputStream inputStream = ResourceRetriever.getResourceAsStream(st);
        int info;
        while ((info = inputStream.read()) != -1)
            data.append((char) info);
        inputStream.close();

        Object3D o = new Object3D();

        //points
        StringTokenizer tokenizer = new StringTokenizer(data.toString());
        o.nPoints = Integer.parseInt(tokenizer.nextToken());

        for (int i = 1; i <= o.nPoints; i++) {
            o.points[i].x = Float.parseFloat(tokenizer.nextToken());
            o.points[i].y = Float.parseFloat(tokenizer.nextToken());
            o.points[i].z = Float.parseFloat(tokenizer.nextToken());
        }

        //planes
        o.nPlanes = Integer.parseInt(tokenizer.nextToken());

        for (int i = 0; i < o.nPlanes; i++) {
            o.planes[i].p[0] = Integer.parseInt(tokenizer.nextToken());
            o.planes[i].p[1] = Integer.parseInt(tokenizer.nextToken());
            o.planes[i].p[2] = Integer.parseInt(tokenizer.nextToken());

            o.planes[i].normals[0].x = Float.parseFloat(tokenizer.nextToken());
            o.planes[i].normals[0].y = Float.parseFloat(tokenizer.nextToken());
            o.planes[i].normals[0].z = Float.parseFloat(tokenizer.nextToken());
            o.planes[i].normals[1].x = Float.parseFloat(tokenizer.nextToken());
            o.planes[i].normals[1].y = Float.parseFloat(tokenizer.nextToken());
            o.planes[i].normals[1].z = Float.parseFloat(tokenizer.nextToken());
            o.planes[i].normals[2].x = Float.parseFloat(tokenizer.nextToken());
            o.planes[i].normals[2].y = Float.parseFloat(tokenizer.nextToken());
            o.planes[i].normals[2].z = Float.parseFloat(tokenizer.nextToken());
        }

        o.setConnectivity();                     // Set Face To Face Connectivity
        o.calcPlanes();

        return o;
    }

    // connectivity procedure - based on Gamasutra's article
    // hard to explain here
    public void setConnectivity() {
        for (int i = 0; i < nPlanes - 1; i++)
            for (int j = i + 1; j < nPlanes; j++)
                for (int ki = 0; ki < 3; ki++)
                    if (planes[i].neigh[ki] == 0) {
                        for (int kj = 0; kj < 3; kj++) {
                            int p1i = ki;
                            int p1j = kj;
                            int p2i = (ki + 1) % 3;
                            int p2j = (kj + 1) % 3;

                            p1i = planes[i].p[p1i];
                            p2i = planes[i].p[p2i];
                            p1j = planes[j].p[p1j];
                            p2j = planes[j].p[p2j];

                            int P1i = ((p1i + p2i) - Math.abs(p1i - p2i)) / 2;
                            int P2i = ((p1i + p2i) + Math.abs(p1i - p2i)) / 2;
                            int P1j = ((p1j + p2j) - Math.abs(p1j - p2j)) / 2;
                            int P2j = ((p1j + p2j) + Math.abs(p1j - p2j)) / 2;

                            if ((P1i == P1j) && (P2i == P2j)) {  //they are neighbours
                                planes[i].neigh[ki] = j + 1;
                                planes[j].neigh[kj] = i + 1;
                            }
                        }
                    }
    }

    private void calcPlanes() {
        for (int i = 0; i < nPlanes; i++)                   // Loop Through All Object Planes
            calcPlane(planes[i]);          // Compute Plane Equations For All Faces
    }

    // function for computing a plane equation given 3 points
    private void calcPlane(Plane plane) {
        Point v[] = new Point[4];

        for (int i = 0; i < 3; i++) {
            v[i + 1] = new Point();
            v[i + 1].x = points[plane.p[i]].x;
            v[i + 1].y = points[plane.p[i]].y;
            v[i + 1].z = points[plane.p[i]].z;
        }
        plane.PlaneEq.a = v[1].y * (v[2].z - v[3].z) + v[2].y * (v[3].z - v[1].z) + v[3].y * (v[1].z - v[2].z);
        plane.PlaneEq.b = v[1].z * (v[2].x - v[3].x) + v[2].z * (v[3].x - v[1].x) + v[3].z * (v[1].x - v[2].x);
        plane.PlaneEq.c = v[1].x * (v[2].y - v[3].y) + v[2].x * (v[3].y - v[1].y) + v[3].x * (v[1].y - v[2].y);
        plane.PlaneEq.d = -(v[1].x * (v[2].y * v[3].z - v[3].y * v[2].z) +
                v[2].x * (v[3].y * v[1].z - v[1].y * v[3].z) +
                v[3].x * (v[1].y * v[2].z - v[2].y * v[1].z));
    }

    // procedure for drawing the object - very simple
    public void draw(GL gl) {
        gl.glBegin(GL.GL_TRIANGLES);
        for (int i = 0; i < nPlanes; i++) {
            for (int j = 0; j < 3; j++) {
                gl.glNormal3f(planes[i].normals[j].x,
                        planes[i].normals[j].y,
                        planes[i].normals[j].z);
                gl.glVertex3f(points[planes[i].p[j]].x,
                        points[planes[i].p[j]].y,
                        points[planes[i].p[j]].z);
            }
        }
        gl.glEnd();
    }

    public void castShadow(GL gl, float[] lp) {
        Point v1 = new Point();
        Point v2 = new Point();

        //set visual parameter
        for (int i = 0; i < nPlanes; i++) {
            // chech to see if light is in front or behind the plane (face plane)
            float side = planes[i].PlaneEq.a * lp[0] +
                    planes[i].PlaneEq.b * lp[1] +
                    planes[i].PlaneEq.c * lp[2] +
                    planes[i].PlaneEq.d * lp[3];
            if (side > 0)
                planes[i].visible = true;
            else
                planes[i].visible = false;
        }

        gl.glDisable(GL.GL_LIGHTING);
        gl.glDepthMask(false);
        gl.glDepthFunc(GL.GL_LEQUAL);

        gl.glEnable(GL.GL_STENCIL_TEST);
        gl.glColorMask(false, false, false, false);
        gl.glStencilFunc(GL.GL_ALWAYS, 1, 0xffffffff);

        // first pass, stencil operation increases stencil value
        gl.glFrontFace(GL.GL_CCW);
        gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_INCR);

        for (int i = 0; i < nPlanes; i++) {
            if (planes[i].visible)
                for (int j = 0; j < 3; j++) {
                    int k = planes[i].neigh[j];
                    if ((k == 0) || (!planes[k - 1].visible)) {
                        // here we have an edge, we must draw a polygon
                        int p1 = planes[i].p[j];
                        int jj = (j + 1) % 3;
                        int p2 = planes[i].p[jj];

                        //calculate the length of the vector
                        v1.x = (points[p1].x - lp[0]) * 100;
                        v1.y = (points[p1].y - lp[1]) * 100;
                        v1.z = (points[p1].z - lp[2]) * 100;

                        v2.x = (points[p2].x - lp[0]) * 100;
                        v2.y = (points[p2].y - lp[1]) * 100;
                        v2.z = (points[p2].z - lp[2]) * 100;

                        //draw the polygon
                        gl.glBegin(GL.GL_TRIANGLE_STRIP);
                        gl.glVertex3f(points[p1].x,
                                points[p1].y,
                                points[p1].z);
                        gl.glVertex3f(points[p1].x + v1.x,
                                points[p1].y + v1.y,
                                points[p1].z + v1.z);

                        gl.glVertex3f(points[p2].x,
                                points[p2].y,
                                points[p2].z);
                        gl.glVertex3f(points[p2].x + v2.x,
                                points[p2].y + v2.y,
                                points[p2].z + v2.z);
                        gl.glEnd();
                    }
                }
        }

        // second pass, stencil operation decreases stencil value
        gl.glFrontFace(GL.GL_CW);
        gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_DECR);

        for (int i = 0; i < nPlanes; i++) {
            if (planes[i].visible)
                for (int j = 0; j < 3; j++) {
                    int k = planes[i].neigh[j];
                    if ((k == 0) || (!planes[k - 1].visible)) {
                        // here we have an edge, we must draw a polygon
                        int p1 = planes[i].p[j];
                        int jj = (j + 1) % 3;
                        int p2 = planes[i].p[jj];

                        //calculate the length of the vector
                        v1.x = (points[p1].x - lp[0]) * 100;
                        v1.y = (points[p1].y - lp[1]) * 100;
                        v1.z = (points[p1].z - lp[2]) * 100;

                        v2.x = (points[p2].x - lp[0]) * 100;
                        v2.y = (points[p2].y - lp[1]) * 100;
                        v2.z = (points[p2].z - lp[2]) * 100;

                        //draw the polygon
                        gl.glBegin(GL.GL_TRIANGLE_STRIP);
                        gl.glVertex3f(points[p1].x,
                                points[p1].y,
                                points[p1].z);
                        gl.glVertex3f(points[p1].x + v1.x,
                                points[p1].y + v1.y,
                                points[p1].z + v1.z);

                        gl.glVertex3f(points[p2].x,
                                points[p2].y,
                                points[p2].z);
                        gl.glVertex3f(points[p2].x + v2.x,
                                points[p2].y + v2.y,
                                points[p2].z + v2.z);
                        gl.glEnd();
                    }
                }
        }

        gl.glFrontFace(GL.GL_CCW);
        gl.glColorMask(true, true, true, true);

        //draw a shadowing rectangle covering the entire screen
        gl.glColor4f(0.0f, 0.0f, 0.0f, 0.4f);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glStencilFunc(GL.GL_NOTEQUAL, 0, 0xffffffff);
        gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glBegin(GL.GL_TRIANGLE_STRIP);
        gl.glVertex3f(-1f, 1f, -1f);
        gl.glVertex3f(-1f, -1f, -1f);
        gl.glVertex3f(1f, 1f, -1f);
        gl.glVertex3f(1f, -1f, -1f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);

        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glDepthMask(true);
        gl.glEnable(GL.GL_LIGHTING);
        gl.glDisable(GL.GL_STENCIL_TEST);
        gl.glShadeModel(GL.GL_SMOOTH);
    }

    // vertex in 3d-coordinate system
    private static class Point {
        float x, y, z;
    }

    // plane equation
    private static class PlaneEq {
        float a, b, c, d;
    }

    // structure describing an object's face
    private static class Plane {
        int p[] = new int[3],
        neigh[] = new int[3];
        Point normals[] = new Point[3];
        boolean visible;
        PlaneEq PlaneEq;

        Plane() {
            PlaneEq = new PlaneEq();
            for (int i = 0; i < 3; i++)
                normals[i] = new Point();
        }
    }
}