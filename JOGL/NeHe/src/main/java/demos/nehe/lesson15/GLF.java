package demos.nehe.lesson15;

import javax.media.opengl.GL;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * GLF Library
 * Version 1.4
 *
 * Author: Roman Podobedov
 * Email: romka@ut.ee
 * WEB: http://romka.demonews.com
 * Release Date: 18 May 2001
 *
 * Copyright (C) 2000-2001, Romka Graphics
 * This library is freely distributable without any license or permissions
 * for non-commercial usage. You can use this library in any non-commercial
 * program. In each program, where you use this library you should keep
 * this header (author name and coordinates)!
 * For commercial usage, please contact me: romka@ut.ee
 *
 * @author Pepijn Van Eeckhoudt
 */
public class GLF {
    private static final int MAX_FONTS = 256;
//    private static final int SEEK_SET_POS = 4195;

    public static final int GLF_ERROR = -1;
    public static final int GLF_OK = 0;
    public static final int GLF_YES = 1;
    public static final int GLF_NO = 2;

    public static final int GLF_CONSOLE_MESSAGES = 10;
    public static final int GLF_TEXTURING = 11;
    public static final int GLF_CONTOURING = 12;

    public static final int GLF_LEFT_UP = 20;
    public static final int GLF_LEFT_CENTER = 21;
    public static final int GLF_LEFT_DOWN = 22;
    public static final int GLF_CENTER_UP = 23;
    public static final int GLF_CENTER = 24;
    public static final int GLF_CENTER_CENTER = 24;
    public static final int GLF_CENTER_DOWN = 25;
    public static final int GLF_RIGHT_UP = 26;
    public static final int GLF_RIGHT_CENTER = 27;
    public static final int GLF_RIGHT_DOWN = 28;

    public static final int GLF_LEFT = 1;
    public static final int GLF_RIGHT = 2;
    public static final int GLF_UP = 3;
    public static final int GLF_DOWN = 4;

    public static final int GLF_CONSOLE_CURSOR = 30;

/* ------------- Main variables -------------- */

    private int refNumber = 0;

    private float SymbolDist = 0.2f;  /* Distance between symbols (Variable constant) */
    private float SymbolDepth = 0.2f; /* Symbol Depth in 3D space (Variable constant) */
    private float SpaceSize = 2.0f;   /* Space size (Variable constant)               */
    private float RotateAngle = 0.0f; /* Rotate angle for string (vector font) */
//    private float RotateAngleB = 0.0f; /* Rotate angle for string (bitmap font) */

/* Array of font pointers, up to MAX_FONTS fonts can be loaded at once */
/* if (fonts[i] == NULL) then this font is not present in memory */

    private GLFFont[] fonts = new GLFFont[MAX_FONTS];
    private int curfont;			/* Index of current font pointer */
    private int ap = GLF_CENTER;	/* Anchor point */
    private boolean m_string_center;	/* String centering (vector fonts) */
//    private boolean m_bitmap_string_center;	/* String centering (bitmap fonts) */

    private int m_direction;	/* String direction (vector fonts) */

    private char console_msg = GLF_NO;
    private char texturing = GLF_NO;
    private char contouring = GLF_NO;
    private float[] contouring_color;

/* Console mode variables */
    private int conWidth, conHeight;	/* Console width and height */
    private int conx = 0, cony = 0;		/* Console current X and Y */
    private StringBuffer[] conData;				/* Console data */
    private int conFont;				/* Console font */
    private char conCursor = GLF_NO;		/* Console cursor Enabled|Disabled */
    private int conCursorBlink;         /* Console cursor blink rate */
    private int conCursorCount;         /* Console cursor blink counter */
    private char conCursorMode = GLF_NO; /* Console Cursor mode (on/off screen) */

/* ----------- Variables for bitmap font ------------- */

    private static class coord_rect {
        float x, y, width, height;
    };

    private static class widths {
        float[] width;
    };

/* Constants */
    private float sym_space = 0.001f;

/* One font variables */
    private coord_rect[] Symbols;
    private boolean bmf_texturing;
    private int bmf_curfont;
    private int[] bmf_texture = new int[MAX_FONTS]; /* All fonts */
    private int[] bmf_mask = new int[MAX_FONTS]; /* All fonts (masked) */
    private boolean[] bmf_in_use = new boolean[MAX_FONTS]; /* 1 - if font is used, 0 - otherwise */
    private int[] list_base = new int[MAX_FONTS];
    private float[] m_max_height = new float[MAX_FONTS]; /* Maximal height of each font */
    private widths[] m_widths = new widths[MAX_FONTS]; /* Width of each symbols in each font */

    private static interface DrawSymbol {
        void drawCharacter(GL gl, char s);
    }

    private DrawSymbol drawWiredSymbol = new DrawSymbol() {
        public void drawCharacter(GL gl, char s) {
            glfDrawWiredSymbol(gl, s);
        }
    };
    private DrawSymbol draw3DWiredSymbol = new DrawSymbol() {
        public void drawCharacter(GL gl, char s) {
            glfDraw3DWiredSymbol(gl, s);
        }
    };
    private DrawSymbol drawSolidSymbol = new DrawSymbol() {
        public void drawCharacter(GL gl, char s) {
            glfDrawSolidSymbol(gl, s);
        }
    };
    private DrawSymbol draw3DSolidSymbol = new DrawSymbol() {
        public void drawCharacter(GL gl, char s) {
            glfDraw3DSolidSymbol(gl, s);
        }
    };

/* Initialization of GLF library, should be called before use of library */
    public void glfInit() {
        int i;

        refNumber++;
        if (refNumber > 1) return;

        for (i = 0; i < MAX_FONTS; i++) {
            fonts[i] = null;
            bmf_in_use[i] = false;
            m_max_height[i] = 0;
        }

        curfont = -1;
        bmf_curfont = -1;
        console_msg = GLF_NO;
        ap = GLF_CENTER;		/* Set anchor point to center of each symbol */
        texturing = GLF_NO;		/* By default texturing is NOT Enabled */
        contouring = GLF_NO;	/* By default contouring is NOT Enabled */
        contouring_color = new float[]{0, 0, 0, 1};
        conData = null;
        glfSetConsoleParam(40, 20);
        glfConsoleClear();
        glfEnable(GLF_CONSOLE_CURSOR);
        glfSetCursorBlinkRate(10);
        glfStringCentering(false);
//        glfBitmapStringCentering(false);
        glfStringDirection(GLF_LEFT);
    }

/* Closing library usage */
    public void glfClose() {
        int i;

        refNumber--;
        if (refNumber > 0) return;

        conData = null;

        for (i = 0; i < MAX_FONTS; i++) glfUnloadFontD(i);
//        for (i = 0; i < MAX_FONTS; i++) glfUnloadBFontD(gl, i);
    }

/*
   ---------------------------------------------------------------------------------
   ------------------------ Work with vector fonts ---------------------------------
   ---------------------------------------------------------------------------------
*/

    /**
     * This function read font file and store information in memory
     * @return GLF_OK if succesfull; GLF_ERROR otherwise
     */
    private int readFont(String font_name, GLFFont glff) {
        LittleEndianInputStream inputStream = null;
        try {
            // Try to load resource from jar
            InputStream stream = ClassLoader.getSystemResourceAsStream(font_name);
            // If not found in jar, then load from disk
            if (stream == null) {
                inputStream = new LittleEndianInputStream(new BufferedInputStream(new FileInputStream(font_name)));
            } else {
                inputStream = new LittleEndianInputStream(new BufferedInputStream(stream));
            }

            byte[] buffer = new byte[3];
            inputStream.readFully(buffer);
            byte[] header = new byte[]{'G', 'L', 'F'};
            if (!Arrays.equals(buffer, header)) {
                return GLF_ERROR;
            }

            StringBuffer name = new StringBuffer();
            for (int i = 0; i < 96; i++) {
                char readChar = (char) inputStream.readUnsignedByte();
                if (readChar != 0)
                    name.append(readChar);
            }
            glff.font_name = name.toString();

            glff.sym_total = inputStream.readUnsignedByte(); /* Read total symbols in font */
            for (int i = 0; i < glff.symbols.length; i++)
                glff.symbols[i] = null;

            for (int i = 0; i < 28; i++)
                inputStream.read();  /* Skip unused data */

            /* Now start to read font data */

            for (int i = 0; i < glff.sym_total; i++) {
                int code = inputStream.readUnsignedByte();
                int verts = inputStream.readUnsignedByte();
                int fcets = inputStream.readUnsignedByte();
                int lns = inputStream.readUnsignedByte();

                if (glff.symbols[code] != null) {
                    return GLF_ERROR;
                }

                glff.symbols[code] = new Symbol();
                glff.symbols[code].vdata = new float[8 * verts];
                glff.symbols[code].fdata = new int[3 * fcets];
                glff.symbols[code].ldata = new int[lns];

                glff.symbols[code].vertexs = verts;
                glff.symbols[code].facets = fcets;
                glff.symbols[code].lines = lns;

                /* Read vertexs data */
                glff.symbols[code].leftx = 10;
                glff.symbols[code].rightx = -10;
                glff.symbols[code].topy = 10;
                glff.symbols[code].bottomy = -10;

                for (int j = 0; j < verts; j++) {
                    float tempfx = inputStream.readFloat();
                    float tempfy = inputStream.readFloat();

                    glff.symbols[code].vdata[j * 2] = tempfx;
                    glff.symbols[code].vdata[j * 2 + 1] = tempfy;

                    if (tempfx < glff.symbols[code].leftx) glff.symbols[code].leftx = tempfx;
                    if (tempfx > glff.symbols[code].rightx) glff.symbols[code].rightx = tempfx;
                    if (tempfy < glff.symbols[code].topy) glff.symbols[code].topy = tempfy;
                    if (tempfy > glff.symbols[code].bottomy) glff.symbols[code].bottomy = tempfy;
                }
                for (int j = 0; j < fcets; j++) {
                    glff.symbols[code].fdata[j * 3] = inputStream.readUnsignedByte();
                    glff.symbols[code].fdata[j * 3 + 1] = inputStream.readUnsignedByte();
                    glff.symbols[code].fdata[j * 3 + 2] = inputStream.readUnsignedByte();
                }
                for (int j = 0; j < lns; j++) {
                    glff.symbols[code].ldata[j] = inputStream.readUnsignedByte();
                }

            }
            return GLF_OK;
        } catch (IOException e) {
            return GLF_ERROR;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
            }
        }
    }


/*
| Function loads font to memory from file
| Return value: GLF_ERROR  - if error
|               >=0 - returned font descriptor (load success)
*/
    public int glfLoadFont(String fontf) {
        /* First we find free font descriptor */
        boolean flag = false; /* Descriptor not found yet */
        int i = 0;
        for (; i < MAX_FONTS; i++)
            if (fonts[i] == null) {
                /* Initialize this font */
                fonts[i] = new GLFFont();
                flag = true;
                break;
            }

        if (!flag) return GLF_ERROR; /* Free font not found */
        if (readFont(fontf, fonts[i]) == GLF_OK) {
            curfont = i; /* Set curfont to just loaded font */
            return i;
        }

        if (fonts[i] != null) {
            fonts[i] = null;
        }
        return GLF_ERROR;
    }

/*
| Unload current font from memory
| Return value: GLF_OK  - if all OK
|               GLF_ERROR -  if error
*/
    public int glfUnloadFont() {
        int i;

        if ((curfont < 0) || (fonts[curfont] == null)) return GLF_ERROR;

        for (i = 0; i < 256; i++) {
            if (fonts[curfont].symbols[i] != null) {
                fonts[curfont].symbols[i].vdata = null;
                fonts[curfont].symbols[i].fdata = null;
                fonts[curfont].symbols[i].ldata = null;
                fonts[curfont].symbols[i] = null;
            }
        }

        fonts[curfont] = null;
        curfont = -1;
        return GLF_OK;
    }

/* Unload font by font descriptor */
    public int glfUnloadFontD(int font_descriptor) {
        int temp;

        if ((font_descriptor < 0) || (fonts[font_descriptor] == null)) return GLF_ERROR;

        temp = curfont;
        curfont = font_descriptor;
        glfUnloadFont();
        if (temp != font_descriptor)
            curfont = temp;
        else
            curfont = -1;
        return GLF_OK;
    }

    public void glfDrawWiredSymbol(GL gl, char s) {
        int i, cur_line;
        int tvp; /* temporary vertex pointer */
        float x, y;

        if ((curfont < 0) || (fonts[curfont] == null)) return;
        if (fonts[curfont].symbols[s] == null) return;

        gl.glBegin(GL.GL_LINE_LOOP);
        tvp = 0;
        cur_line = 0;
        for (i = 0; i < fonts[curfont].symbols[s].vertexs; i++) {
            x = fonts[curfont].symbols[s].vdata[tvp++];
            y = fonts[curfont].symbols[s].vdata[tvp++];
            gl.glVertex2f(x, y);
            if (fonts[curfont].symbols[s].ldata[cur_line] == i) {
                gl.glEnd();
                cur_line++;
                if (cur_line < fonts[curfont].symbols[s].lines)
                    gl.glBegin(GL.GL_LINE_LOOP);
                else
                    break; /* No more lines */
            }
        }
    }

/* Draw wired symbol by font_descriptor */
    public void glfDrawWiredSymbolF(GL gl, int font_descriptor, char s) {
        int temp;

        temp = curfont;
        curfont = font_descriptor;
        glfDrawWiredSymbol(gl, s);
        curfont = temp;
    }

    private void DrawString(GL gl, String s, DrawSymbol funct) {
        int i;
        float sda, sdb;
        float distance = 0f;

        if (s == null) return;
        if (s.length() == 0) return;
        if (curfont == -1) return;

        /* Calculate correction (if string centering enabled) */
        if (m_string_center) {
            distance = 0;
            for (i = 0; i < s.length(); i++) {
                if ((fonts[curfont].symbols[s.charAt(i)] == null) || (s.charAt(i) == ' ')) {
                    if (m_direction == GLF_LEFT || m_direction == GLF_UP)
                        distance += SpaceSize;
                    else
                        distance -= SpaceSize;
                } else if (i < s.length() - 1) {
                    if (s.charAt(i + 1) == ' ') {
                        if (m_direction == GLF_LEFT || m_direction == GLF_UP)
                            distance += SymbolDist;
                        else
                            distance -= SymbolDist;
                    } else {
                        if (fonts[curfont].symbols[s.charAt(i + 1)] == null) continue;

                        if (m_direction == GLF_LEFT || m_direction == GLF_RIGHT) {
                            sda = Math.abs(fonts[curfont].symbols[s.charAt(i)].rightx);
                            sdb = Math.abs(fonts[curfont].symbols[s.charAt(i + 1)].leftx);
                            if (m_direction == GLF_LEFT)
                                distance += sda + sdb + SymbolDist;
                            else
                                distance -= sda + sdb + SymbolDist;
                        } else {
                            sda = Math.abs(fonts[curfont].symbols[s.charAt(i)].topy);
                            sdb = Math.abs(fonts[curfont].symbols[s.charAt(i)].bottomy);
                            if (m_direction == GLF_DOWN)
                                distance -= sda + sdb + SymbolDist;
                            else
                                distance += sda + sdb + SymbolDist;
                        }
                    }
                }
            }
        }

        gl.glPushMatrix();

        /* Rotate if needed */
        if (RotateAngle != 0.0f) gl.glRotatef(RotateAngle, 0, 0, 1);

        /* Correct string position */
        if (m_string_center) {
            switch (m_direction) {
                case GLF_LEFT:
                    gl.glTranslatef(-distance / 2, 0, 0);
                    break;
                case GLF_RIGHT:
                    gl.glTranslatef(distance / 2, 0, 0);
                    break;
                case GLF_UP:
                    gl.glTranslatef(0, distance / 2, 0);
                    break;
                case GLF_DOWN:
                    gl.glTranslatef(0, -distance / 2, 0);
                    break;
            }
        } else if (s.charAt(0) != ' ') {
            switch (m_direction) {
                case GLF_LEFT:
                    gl.glTranslatef(-(1 - Math.abs(fonts[curfont].symbols[s.charAt(0)].leftx)), 0, 0);
                    break;
                case GLF_RIGHT:
                    gl.glTranslatef((1 - Math.abs(fonts[curfont].symbols[s.charAt(0)].rightx)), 0, 0);
                    break;
                case GLF_UP:
                    gl.glTranslatef(0, (1 - Math.abs(fonts[curfont].symbols[s.charAt(0)].topy)), 0);
                    break;
                case GLF_DOWN:
                    gl.glTranslatef(0, -(1 - Math.abs(fonts[curfont].symbols[s.charAt(0)].bottomy)), 0);
                    break;
            }
        }

        /* Start to draw our string */
        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') funct.drawCharacter(gl, s.charAt(i));
            if ((fonts[curfont].symbols[s.charAt(i)] == null) || (s.charAt(i) == ' ')) {
                switch (m_direction) {
                    case GLF_LEFT:
                        gl.glTranslatef(SpaceSize, 0, 0);
                        break;
                    case GLF_RIGHT:
                        gl.glTranslatef(-SpaceSize, 0, 0);
                        break;
                    case GLF_UP:
                        gl.glTranslatef(0, SpaceSize, 0);
                        break;
                    case GLF_DOWN:
                        gl.glTranslatef(0, -SpaceSize, 0);
                        break;
                }
            } else {
                if (i < (s.length() - 1)) {
                    if (s.charAt(i + 1) == ' ') {
                        switch (m_direction) {
                            case GLF_LEFT:
                                gl.glTranslatef(SymbolDist, 0, 0);
                                break;
                            case GLF_RIGHT:
                                gl.glTranslatef(-SymbolDist, 0, 0);
                                break;
                            case GLF_UP:
                                gl.glTranslatef(0, SymbolDist, 0);
                                break;
                            case GLF_DOWN:
                                gl.glTranslatef(0, -SymbolDist, 0);
                                break;
                        }
                    } else {
                        if (fonts[curfont].symbols[s.charAt(i + 1)] == null) continue;

                        if (m_direction == GLF_LEFT || m_direction == GLF_RIGHT) {
                            if (m_direction == GLF_LEFT) {
                                sda = Math.abs(fonts[curfont].symbols[s.charAt(i)].rightx);
                                sdb = Math.abs(fonts[curfont].symbols[s.charAt(i + 1)].leftx);
                            } else {
                                sda = Math.abs(fonts[curfont].symbols[s.charAt(i + 1)].rightx);
                                sdb = Math.abs(fonts[curfont].symbols[s.charAt(i)].leftx);
                            }

                            if (m_direction == GLF_LEFT)
                                gl.glTranslatef(sda + sdb + SymbolDist, 0, 0);
                            else
                                gl.glTranslatef(-(sda + sdb + SymbolDist), 0, 0);
                        } else {
                            if (m_direction == GLF_DOWN) {
                                sda = Math.abs(fonts[curfont].symbols[s.charAt(i)].topy);
                                sdb = Math.abs(fonts[curfont].symbols[s.charAt(i + 1)].bottomy);
                            } else {
                                sda = Math.abs(fonts[curfont].symbols[s.charAt(i + 1)].topy);
                                sdb = Math.abs(fonts[curfont].symbols[s.charAt(i)].bottomy);
                            }

                            if (m_direction == GLF_DOWN)
                                gl.glTranslatef(0, -(sda + sdb + SymbolDist), 0);
                            else
                                gl.glTranslatef(0, sda + sdb + SymbolDist, 0);
                        }

                    }
                }
            }
        }
        gl.glPopMatrix();
    }

    public void glfDrawWiredString(GL gl, String s) {
        DrawString(gl, s, drawWiredSymbol);
    }

/* Draw wired string by font_descriptor */
    public void glfDrawWiredStringF(GL gl, int font_descriptor, String s) {
        int temp;

        temp = curfont;
        curfont = font_descriptor;
        DrawString(gl, s, drawWiredSymbol);
        curfont = temp;
    }

    public void glfDrawSolidSymbol(GL gl, char s) {
        int b; /* Face pointer   */
        int i, j;
        float x, y;
        float[] temp_color = new float[4];

        if ((curfont < 0) || (fonts[curfont] == null)) return;

        if (fonts[curfont].symbols[s] == null) return;

        b = 0;

        gl.glBegin(GL.GL_TRIANGLES);
        for (i = 0; i < fonts[curfont].symbols[s].facets; i++) {
            for (j = 0; j < 3; j++) {
                x = fonts[curfont].symbols[s].vdata[fonts[curfont].symbols[s].fdata[b] * 2];
                y = fonts[curfont].symbols[s].vdata[fonts[curfont].symbols[s].fdata[b] * 2 + 1];
                if (texturing == GLF_YES) gl.glTexCoord2f((x + 1) / 2, (y + 1) / 2);
                gl.glVertex2f(x, y);
                b++;
            }
        }
        gl.glEnd();

        /* Draw contour, if enabled */
        if (contouring == GLF_YES) {
            gl.glGetFloatv(GL.GL_CURRENT_COLOR, temp_color, 0);
            gl.glColor4fv(contouring_color, 0);
            glfDrawWiredSymbol(gl, s);
            gl.glColor4fv(temp_color, 0);
        }
    }

/* Draw solid symbol by font_descriptor */
    public void glfDrawSolidSymbolF(GL gl, int font_descriptor, char s) {
        int temp;

        temp = curfont;
        curfont = font_descriptor;
        glfDrawSolidSymbol(gl, s);
        curfont = temp;
    }

    public void glfDrawSolidString(GL gl, String s) {
        DrawString(gl, s, drawSolidSymbol);
    }

/* Draw solid string by font_descriptor */
    public void glfDrawSolidStringF(GL gl, int font_descriptor, String s) {
        int temp;

        temp = curfont;
        curfont = font_descriptor;
        DrawString(gl, s, drawSolidSymbol);
        curfont = temp;
    }


/* ------------ 3D Wired text drawing ---------------------- */

    public void glfDraw3DWiredSymbol(GL gl, char s) {
        int i, cur_line;
        int tvp; /* temp vertex pointer */
        float x, y;

        if ((curfont < 0) || (fonts[curfont] == null)) return;
        if (fonts[curfont].symbols[(int) s] == null) return;

        /* Draw front symbol */
        gl.glBegin(GL.GL_LINE_LOOP);
        tvp = 0;
        cur_line = 0;
        for (i = 0; i < fonts[curfont].symbols[s].vertexs; i++) {
            x = fonts[curfont].symbols[s].vdata[tvp++];
            y = fonts[curfont].symbols[s].vdata[tvp++];
            gl.glVertex3f(x, y, 1);
            if (fonts[curfont].symbols[s].ldata[cur_line] == i) {
                gl.glEnd();
                cur_line++;
                if (cur_line < fonts[curfont].symbols[s].lines)
                    gl.glBegin(GL.GL_LINE_LOOP);
                else
                    break; /* No more lines */
            }
        }

        /* Draw back symbol */
        gl.glBegin(GL.GL_LINE_LOOP);
        tvp = 0;
        cur_line = 0;
        for (i = 0; i < fonts[curfont].symbols[s].vertexs; i++) {
            x = fonts[curfont].symbols[s].vdata[tvp++];
            y = fonts[curfont].symbols[s].vdata[tvp++];
            gl.glVertex3f(x, y, 1 + SymbolDepth);
            if (fonts[curfont].symbols[s].ldata[cur_line] == i) {
                gl.glEnd();
                cur_line++;
                if (cur_line < fonts[curfont].symbols[s].lines)
                    gl.glBegin(GL.GL_LINE_LOOP);
                else
                    break; /* No more lines */
            }
        }

        /* Draw lines between back and front symbols */
        gl.glBegin(GL.GL_LINES);
        tvp = 0;
        for (i = 0; i < fonts[curfont].symbols[s].vertexs; i++) {
            x = fonts[curfont].symbols[s].vdata[tvp++];
            y = fonts[curfont].symbols[s].vdata[tvp++];
            gl.glVertex3f(x, y, 1);
            gl.glVertex3f(x, y, 1 + SymbolDepth);
        }
        gl.glEnd();
    }

/* Draw 3D wired symbol by font_descriptor */
    public void glfDraw3DWiredSymbolF(GL gl, int font_descriptor, char s) {
        int temp;

        temp = curfont;
        curfont = font_descriptor;
        glfDraw3DWiredSymbol(gl, s);
        curfont = temp;
    }

    public void glfDraw3DWiredString(GL gl, String s) {
        DrawString(gl, s, draw3DWiredSymbol);
    }

/* Draw 3D wired string by font_descriptor */
    public void glfDraw3DWiredStringF(GL gl, int font_descriptor, String s) {
        int temp;

        temp = curfont;
        curfont = font_descriptor;
        DrawString(gl, s, draw3DWiredSymbol);
        curfont = temp;
    }

/* ------------ 3D Solid text drawing ---------------------- */

    public void glfDraw3DSolidSymbol(GL gl, char s) {
        int i, j, cur_line;
        boolean flag;
        float x, y;
        float bx = 0f;
        float by = 0f;
        int b; /* Face pointer   */
        int tvp;       /* temp vertex pointer */
        float[] temp_color = new float[4];
        byte[] light_temp = new byte[1];

        if ((curfont < 0) || (fonts[curfont] == null)) return;
        if (fonts[curfont].symbols[(int) s] == null) return;

        b = 0;

        gl.glBegin(GL.GL_TRIANGLES);
        gl.glNormal3f(0, 0, 1);
        for (i = 0; i < fonts[curfont].symbols[s].facets; i++) {
            b += 2;
            for (j = 0; j < 3; j++) {
                x = fonts[curfont].symbols[s].vdata[fonts[curfont].symbols[s].fdata[b] * 2];
                y = fonts[curfont].symbols[s].vdata[fonts[curfont].symbols[s].fdata[b] * 2 + 1];
                gl.glVertex3f(x, y, 1 + SymbolDepth);
                b--;
            }
            b += 4;
        }
        gl.glEnd();

        b = 0;

        gl.glBegin(GL.GL_TRIANGLES);
        gl.glNormal3f(0, 0, -1);
        for (i = 0; i < fonts[curfont].symbols[s].facets; i++) {
            for (j = 0; j < 3; j++) {
                x = fonts[curfont].symbols[s].vdata[fonts[curfont].symbols[s].fdata[b] * 2];
                y = fonts[curfont].symbols[s].vdata[fonts[curfont].symbols[s].fdata[b] * 2 + 1];
                gl.glVertex3f(x, y, 1);
                b++;
            }
        }
        gl.glEnd();

        flag = false;
        gl.glBegin(GL.GL_QUAD_STRIP);
        tvp = 0;
        cur_line = 0;
        for (i = 0; i < fonts[curfont].symbols[s].vertexs; i++) {
            x = fonts[curfont].symbols[s].vdata[tvp++];
            y = fonts[curfont].symbols[s].vdata[tvp++];
            if (!flag) {
                bx = x;
                by = y;
                flag = true;
            }
            gl.glNormal3f(x, y, 0);
            gl.glVertex3f(x, y, 1);
            gl.glVertex3f(x, y, 1 + SymbolDepth);
            if (fonts[curfont].symbols[s].ldata[cur_line] == i) {
                gl.glVertex3f(bx, by, 1);
                gl.glVertex3f(bx, by, 1 + SymbolDepth);
                flag = false;
                gl.glEnd();
                cur_line++;
                if (cur_line < fonts[curfont].symbols[s].lines)
                    gl.glBegin(GL.GL_QUAD_STRIP);
                else
                    break; /* No more lines */
            }
        }

        /* Draw contour, if enabled */
        if (contouring == GLF_YES) {
            gl.glGetBooleanv(GL.GL_LIGHTING, light_temp, 0);
            gl.glDisable(GL.GL_LIGHTING);
            gl.glGetFloatv(GL.GL_CURRENT_COLOR, temp_color, 0);
            gl.glColor4fv(contouring_color, 0);
            glfDraw3DWiredSymbol(gl, s);
            gl.glColor4fv(temp_color, 0);
            if (light_temp[0] != 0) gl.glEnable(GL.GL_LIGHTING);
        }
    }

/* Draw 3D solid symbol by font_descriptor */
    public void glfDraw3DSolidSymbolF(GL gl, int font_descriptor, char s) {
        int temp;

        temp = curfont;
        curfont = font_descriptor;
        glfDraw3DSolidSymbol(gl, s);
        curfont = temp;
    }

    public void glfDraw3DSolidString(GL gl, String s) {
        DrawString(gl, s, draw3DSolidSymbol);
    }

/* Draw 3D solid string by font_descriptor */
    public void glfDraw3DSolidStringF(GL gl, int font_descriptor, String s) {
        int temp;

        temp = curfont;
        curfont = font_descriptor;
        DrawString(gl, s, draw3DSolidSymbol);
        curfont = temp;
    }

/* Get the size a string will have on screen */
    public void glfGetStringBoundsF(int fd, String s, float[] minx, float[] miny, float[] maxx, float[] maxy) {
        GLFFont font;
        int i;
        float sda, sdb, cw = 0, minxx = 10;
        float top = 10, bottom = -10;

        if (fd < 0 || fd > (MAX_FONTS - 1)) return;
        font = fonts[fd];

        if (font == null) return;

        if (font.symbols[s.charAt(0)] != null)
            minxx = font.symbols[s.charAt(0)].leftx;
        else
            minxx = 0f;

        for (i = 0; i < s.length(); i++) {
            char charI = s.charAt(i);
            if ((font.symbols[charI] == null) || (charI == ' '))
                cw += SpaceSize;
            else {
                sdb = -font.symbols[charI].leftx;
                sda = font.symbols[charI].rightx;

                cw += sda + sdb + SymbolDist;

                /* Update top/bottom bounds */
                if (font.symbols[charI].bottomy > bottom)
                    bottom = font.symbols[charI].bottomy;

                if (font.symbols[charI].topy < top)
                    top = font.symbols[charI].topy;
            }
        }

        cw += minxx;

        if ((maxx != null) && (maxy != null)) {
            maxx[0] = cw;
            maxy[0] = bottom;
        }

        if ((minx != null) && (miny != null)) {
            minx[0] = minxx;
            miny[0] = top;
        }
    }

    public void glfGetStringBounds(String s, float[] minx, float[] miny, float[] maxx, float[] maxy) {
        glfGetStringBoundsF(curfont, s, minx, miny, maxx, maxy);
    }

    public void glfSetSymbolSpace(float sp) {
        SymbolDist = sp;
    }

    public float glfGetSymbolSpace() {
        return SymbolDist;
    }

    public void glfSetSpaceSize(float sp) {
        SpaceSize = sp;
    }

    public float glfGetSpaceSize() {
        return SpaceSize;
    }

    public void glfSetSymbolDepth(float dpth) {
        SymbolDepth = dpth;
    }

    public float glfGetSymbolDepth() {
        return SymbolDepth;
    }

    public int glfSetCurrentFont(int Font_Descriptor) {
        if ((Font_Descriptor < 0) || (fonts[Font_Descriptor] == null)) return GLF_ERROR;

        curfont = Font_Descriptor;
        return GLF_OK;
    }

    public int glfGetCurrentFont() {
        return curfont;
    }

    public void glfSetAnchorPoint(int anchp) {
        if ((anchp >= GLF_LEFT_UP) && (anchp <= GLF_RIGHT_DOWN))
            ap = anchp;
    }

    public void glfSetContourColor(float r, float g, float b, float a) {
        contouring_color[0] = r;
        contouring_color[1] = g;
        contouring_color[2] = b;
        contouring_color[3] = a;
    }

    public void glfEnable(int what) {
        switch (what) {
            case GLF_CONSOLE_MESSAGES:
                console_msg = GLF_YES;
                break;
            case GLF_TEXTURING:
                texturing = GLF_YES;
                break;
            case GLF_CONSOLE_CURSOR:
                conCursor = GLF_YES;
                break;
            case GLF_CONTOURING:
                contouring = GLF_YES;
                break;
        }
    }

    public void glfDisable(int what) {
        switch (what) {
            case GLF_CONSOLE_MESSAGES:
                console_msg = GLF_NO;
                break;
            case GLF_TEXTURING:
                texturing = GLF_NO;
                break;
            case GLF_CONSOLE_CURSOR:
                conCursor = GLF_NO;
                break;
            case GLF_CONTOURING:
                contouring = GLF_NO;
                break;
        }
    }

/* ---------------- Console functions ---------------------- */

    public void glfSetConsoleParam(int width, int height) {
        conWidth = width;
        conHeight = height;
        conData = new StringBuffer[height];
        glfConsoleClear();
    }

    public int glfSetConsoleFont(int Font_Descriptor) {
        if ((Font_Descriptor < 0) || (fonts[Font_Descriptor] == null)) return GLF_ERROR;

        conFont = Font_Descriptor;
        return GLF_OK;
    }

    public void glfConsoleClear() {
        Arrays.fill(conData, null);
        conx = 0;
        cony = 0;
    }

    public void glfPrint(String s, int lenght) {
        for (int i = 0; i < lenght; i++) {
            char charI = s.charAt(i);
            if (charI > 31) {
                conData[cony].append(charI);
                conx++;
            } else if (s.charAt(i) == '\n') conx = conWidth;
            if (conx >= conWidth) {
                conx = 0;
                cony++;
                if (cony >= conHeight) {
                    /* Shift all console contents up */
                    System.arraycopy(conData, 1, conData, 0, conHeight - 1);
                    /* Fill bottom line by spaces */
                    conData[conHeight - 1] = new StringBuffer();
                    cony = conHeight - 1;
                }
            }
        }
    }

    public void glfPrintString(String s) {
        glfPrint(s, s.length());
    }

    public void glfPrintChar(char s) {
        glfPrint(Character.toString(s), 1);
    }

    public void glfConsoleDraw(GL gl) {
        boolean drawCursor = false;

        for (int i = 0; i < conHeight; i++) {
            StringBuffer s = conData[i];
            if ((conCursor == GLF_YES) && (i == cony)) {
                conCursorCount--;
                if (conCursorCount < 0) {
                    conCursorCount = conCursorBlink;
                    if (conCursorMode == GLF_YES)
                        conCursorMode = GLF_NO;
                    else
                        conCursorMode = GLF_YES;
                }

                if (conCursorMode == GLF_YES) {
                    drawCursor = s.length() < conWidth;
                }
            }
            glfDrawSolidStringF(gl, conFont, new String(s));
            if (drawCursor)
                glfDrawSolidSymbolF(gl, conFont, '_');
            gl.glTranslatef(0, -2, 0);
        }
    }

    public void glfSetCursorBlinkRate(int Rate) {
        if (Rate > 0) {
            conCursorBlink = Rate;
            conCursorCount = Rate;
            conCursorMode = GLF_YES;
        }
    }

/* Set string centering for vector fonts */
    public void glfStringCentering(boolean center) {
        m_string_center = center;
    }

/* String direction for vector font (GLF_LEFT, GLF_RIGHT, GLF_UP, GLF_DOWN) */
/* GLF_LEFT by default */
    public void glfStringDirection(int direction) {
        if (direction == GLF_LEFT || direction == GLF_RIGHT ||
                direction == GLF_UP || direction == GLF_DOWN)
            m_direction = direction;
    }

/* Get current text direction */
    public int glfGetStringDirection() {
        return m_direction;
    }


/* Get string centering for vector fonts */
    public boolean glfGetStringCentering() {
        return m_string_center;
    }

/* Set rotate angle for vector fonts */
    public void glfSetRotateAngle(float angle) {
        RotateAngle = angle;
    }


/*
   ---------------------------------------------------------------------------------
   ------------------------ Work with bitmapped fonts ------------------------------
   ---------------------------------------------------------------------------------
*/


///* Some color conversions */
//    static void bwtorgba(unsigned char *b,unsigned char *l,int n)
//    {
//        while (n--) {
//            l[0] = *b;
//            l[1] = *b;
//            l[2] = *b;
//            l[3] = 0xff;
//            l += 4;
//            b++;
//        }
//    }
//
//    static void latorgba(unsigned char *b, unsigned char *a,unsigned char *l,int n)
//    {
//        while (n--) {
//            l[0] = *b;
//            l[1] = *b;
//            l[2] = *b;
//            l[3] = *a;
//            l += 4;
//            b++;
//            a++;
//        }
//    }
//
//    static void rgbtorgba(unsigned char *r,unsigned char *g,unsigned char *b,unsigned char *l,int n)
//    {
//        while (n--) {
//            l[0] = r[0];
//            l[1] = g[0];
//            l[2] = b[0];
//            l[3] = 0xff;
//            l += 4;
//            r++;
//            g++;
//            b++;
//        }
//    }
//
//    static void rgbatorgba(unsigned char *r,unsigned char *g,unsigned char *b,unsigned char *a,unsigned char *l,int n)
//    {
//        while (n--) {
//            l[0] = r[0];
//            l[1] = g[0];
//            l[2] = b[0];
//            l[3] = a[0];
//            l += 4;
//            r++;
//            g++;
//            b++;
//            a++;
//        }
//    }
//
//    private class ImageRec {
//        int imagic;
//        int type;
//        int dim;
//        int xsize, ysize, zsize;
//        int min, max;
//        int wasteBytes;
//        String name;
//        long colorMap;
//        int[] tmp;
//        int[] tmpR;
//        int[] tmpG;
//        int[] tmpB;
//        long rleEnd;
//        int[] rowStart;
//        int[] rowSize;
//    }
//
//    static void ConvertShort(unsigned short *array, long length)
//    {
//        unsigned b1, b2;
//        unsigned char *ptr;
//
//        ptr = (unsigned char *)array;
//        while (length--) {
//            b1 = *ptr++;
//            b2 = *ptr++;
//            *array++ = (b1 << 8) | (b2);
//        }
//    }
//
//    static void ConvertLong(unsigned *array, long length)
//    {
//        unsigned b1, b2, b3, b4;
//        unsigned char *ptr;
//
//        ptr = (unsigned char *)array;
//        while (length--) {
//            b1 = *ptr++;
//            b2 = *ptr++;
//            b3 = *ptr++;
//            b4 = *ptr++;
//            *array++ = (b1 << 24) | (b2 << 16) | (b3 << 8) | (b4);
//        }
//    }
//
///* Open RGB Image */
//    static ImageRec *ImageOpen(FILE *f)
//    {
//        union
//        {
//            int testWord;
//            char testByte[
//            4];
//        }
//        endianTest;
//
//        ImageRec * image;
//        int swapFlag;
//        int x;
//
//        endianTest.testWord = 1;
//        if (endianTest.testByte[0] == 1)
//            swapFlag = 1;
//        else
//            swapFlag = 0;
//
//        image = (ImageRec *)
//        malloc(sizeof(ImageRec));
//        if (image == null) {
//            fprintf(stderr, "Out of memory!\n");
//            exit(1);
//        }
//
//        image.file = f;
//
//        fread(image, 1, 12, image.file);
//
//        if (swapFlag) ConvertShort(      & image.imagic, 6);
//
//        image.tmp = (unsigned char *)malloc(image.xsize * 256);
//        image.tmpR = (unsigned char *)malloc(image.xsize * 256);
//        image.tmpG = (unsigned char *)malloc(image.xsize * 256);
//        image.tmpB = (unsigned char *)malloc(image.xsize * 256);
//        if (image.tmp == null || image.tmpR == null || image.tmpG == null ||
//                image.tmpB == null) {
//            fprintf(stderr, "Out of memory!\n");
//            exit(1);
//        }
//
//        if ((image.type & 0xFF00) == 0x0100) {
//            x = image.ysize * image.zsize * sizeof(unsigned);
//            image.rowStart = (unsigned *)
//            malloc(x);
//            image.rowSize = (int *)malloc(x);
//            if (image.rowStart == null || image.rowSize == null) {
//                fprintf(stderr, "Out of memory!\n");
//                exit(1);
//            }
//            image.rleEnd = 512 + (2 * x);
//            fseek(image.file, 512 + SEEK_SET_POS, SEEK_SET);
//            fread(image.rowStart, 1, x, image.file);
//            fread(image.rowSize, 1, x, image.file);
//            if (swapFlag) {
//                ConvertLong(image.rowStart, x / (int) sizeof(unsigned));
//                ConvertLong((unsigned *)
//                image.rowSize, x / (int) sizeof(int));
//            }
//        } else {
//            image.rowStart = null;
//            image.rowSize = null;
//        }
//        return image;
//    }
//
///* Close Image and free data */
//    static void ImageClose(ImageRec image)
//    {
//        image.tmp = null;
//        image.tmpR = null;
//        image.tmpG = null;
//        image.tmpB = null;
//        image.rowSize = null;
//        image.rowStart = null;
//    }
//
///* Pixels row decoding (if used RLE encoding) */
//    static void ImageGetRow(ImageRec *image, unsigned char *buf, int y, int z)
//    {
//        unsigned char *iPtr, *oPtr, pixel;
//        int count;
//
//        if ((image.type & 0xFF00) == 0x0100) {
//            fseek(image.file, (long) image.rowStart[y + z * image.ysize] + SEEK_SET_POS, SEEK_SET);
//            fread(image.tmp, 1, (unsigned int)image.rowSize[y + z * image.ysize], image.file);
//
//            iPtr = image.tmp;
//            oPtr = buf;
//            for (; ;) {
//                pixel = *iPtr++;
//                count = (int) (pixel & 0x7F);
//                if (!count) return;
//                if (pixel & 0x80) while (count--) *oPtr++ = *iPtr++;
//                else
//                {
//                    pixel = *iPtr++;
//                    while (count--) *oPtr++ = pixel;
//                }
//            }
//        } else {
//            fseek(image.file, 512 + (y * image.xsize) + (z * image.xsize * image.ysize) + SEEK_SET_POS, SEEK_SET);
//            fread(buf, 1, image.xsize, image.file);
//        }
//    }
//
///* Read SGI (RGB) Image from file */
//    static int[] read_texture(LittleEndianInputStream in, int width, int height, int components)
//    {
//        unsigned * base, *lptr;
//        unsigned char *rbuf, *gbuf, *bbuf, *abuf;
//        ImageRec * image;
//        int y;
//
//        image = ImageOpen(f);
//
//        if (!image) return null;
//        ( * width) = image.xsize;
//        ( * height) = image.ysize;
//        ( * components) = image.zsize;
//
//        base = (unsigned *)
//        malloc(image.xsize * image.ysize * sizeof(unsigned));
//        rbuf = (unsigned char *)malloc(image.xsize * sizeof(unsigned char));
//        gbuf = (unsigned char *)malloc(image.xsize * sizeof(unsigned char));
//        bbuf = (unsigned char *)malloc(image.xsize * sizeof(unsigned char));
//        abuf = (unsigned char *)malloc(image.xsize * sizeof(unsigned char));
//
//        if (!base || !rbuf || !gbuf || !bbuf) return null;
//        lptr = base;
//        for (y = 0; y < image.ysize; y++) {
//            if (image.zsize >= 4) {
//                ImageGetRow(image, rbuf, y, 0);
//                ImageGetRow(image, gbuf, y, 1);
//                ImageGetRow(image, bbuf, y, 2);
//                ImageGetRow(image, abuf, y, 3);
//                rgbatorgba(rbuf, gbuf, bbuf, abuf, (unsigned char *)lptr, image.xsize);
//                lptr += image.xsize;
//            } else if (image.zsize == 3) {
//                ImageGetRow(image, rbuf, y, 0);
//                ImageGetRow(image, gbuf, y, 1);
//                ImageGetRow(image, bbuf, y, 2);
//                rgbtorgba(rbuf, gbuf, bbuf, (unsigned char *)lptr, image.xsize);
//                lptr += image.xsize;
//            } else if (image.zsize == 2) {
//                ImageGetRow(image, rbuf, y, 0);
//                ImageGetRow(image, abuf, y, 1);
//                latorgba(rbuf, abuf, (unsigned char *)lptr, image.xsize);
//                lptr += image.xsize;
//            } else {
//                ImageGetRow(image, rbuf, y, 0);
//                bwtorgba(rbuf, (unsigned char *)lptr, image.xsize);
//                lptr += image.xsize;
//            }
//        }
//        ImageClose(image);
//        free(rbuf);
//        free(gbuf);
//        free(bbuf);
//        free(abuf);
//
//        return (unsigned *)
//        base;
//    }
//
///* Font texture conversion to mask texture */
//    int[] texture_to_mask(int[] tex, int width, int height)
//    {
//        int nSize, i;
//        int[] ret;
//
//        nSize = width * height;
//        ret = new int[nSize];
//        for (i = 0; i < nSize; i++)
//            ret[i] = tex[i] & 0x00ffffff ? 0 : 0x00ffffff;
//
//        return ret;
//    }
//
///* Load BMF file format, function return bitmap font descriptor */
//    int glfLoadBFont(GL gl, String fontf) {
//        byte[] Header = new byte[3];
//        byte[] FontName = new byte[96];
//        int LEndian;
//        float tx, ty, tw, th;
//        int temp, tp;
//        int[] texture;	/* Texture image */
//        int[] mask;	/* Mask texture */
//        int twidth, theight, tcomp;	/* Image parameters */
//        float[] temp_width;
//
//        LittleEndianInputStream in = null;
//
//        /* Get header */
//        in.readFully(Header);
//        Header[3] = 0;
//        if (Arrays.equals(Header, new byte[]{'B','M','F'}))
//            return GLF_ERROR; /* Not BMF format */
//
//        /* Get font name */
//        in.readFully(FontName);
//
//        /* Allocate space for temp widths */
//
//        temp_width = new float[256];
//
//        /* Read all 256 symbols information */
//        for (int i = 0; i < 256; i++) {
//            tx = in.readFloat();
//            ty = in.readFloat();
//            tw = in.readFloat();
//            th = in.readFloat();
//
//            Symbols[i].x = tx;
//            Symbols[i].y = ty;
//            Symbols[i].width = tw;
//            Symbols[i].height = th;
//            temp_width[i] = tw;
//        }
//
//        /* Read texture image from file and build texture */
//        texture = read_texture(fontf, & twidth, &theight, &tcomp);
//        /* Generate mask texture */
//        mask = texture_to_mask(texture, twidth, theight);
//
//        /* Find unused font descriptor */
//        boolean flag = false;
//        for (int i = 0; i < MAX_FONTS; i++)
//            if (bmf_in_use[i] == false) {
//                /* Initialize this font */
//                bmf_in_use[i] = true;
//                bmf_curfont = i;
//                flag = true;
//                break;
//            }
//        if (!flag) /* Not enought space for new texture */ {
//            return GLF_ERROR;
//        }
//
//        m_widths[bmf_curfont].width = temp_width;
//
//        /* Generating textures for font and mask */
//        int[] gltexture = new int[1];
//        gl.glGenTextures(1, gltexture);
//        bmf_texture[bmf_curfont] = gltexture[0];
//        gl.glGenTextures(1, gltexture);
//        bmf_mask[bmf_curfont] = gltexture[0];
//
//        gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
//
//        /* Build font texture */
//        gl.glBindTexture(GL.GL_TEXTURE_2D, bmf_texture[bmf_curfont]);
//        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, twidth, theight, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, texture);
//
//        /* Linear filtering for better quality */
//        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
//        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
//
//        /* Build mask texture */
//        gl.glBindTexture(GL.GL_TEXTURE_2D, bmf_mask[bmf_curfont]);
//        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, twidth, theight, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, mask);
//        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
//        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
//
//        /* Now build list for each symbol */
//        list_base[bmf_curfont] = gl.glGenLists(256);
//        for (int i = 0; i < 256; i++) {
//            gl.glNewList(list_base[bmf_curfont] + i, GL.GL_COMPILE);
//
//            gl.glBegin(GL.GL_QUADS);
//            gl.glTexCoord2f(Symbols[i].x, Symbols[i].y);
//            gl.glVertex2f(0, 0);
//            gl.glTexCoord2f(Symbols[i].x + Symbols[i].width, Symbols[i].y);
//            gl.glVertex2f(Symbols[i].width, 0);
//            gl.glTexCoord2f(Symbols[i].x + Symbols[i].width, Symbols[i].y + Symbols[i].height);
//            gl.glVertex2f(Symbols[i].width, Symbols[i].height);
//            gl.glTexCoord2f(Symbols[i].x, Symbols[i].y + Symbols[i].height);
//            gl.glVertex2f(0, Symbols[i].height);
//            gl.glEnd();
//            gl.glTranslatef(Symbols[i].width + sym_space, 0, 0);
//
//            gl.glEndList();
//            if (Symbols[i].height > m_max_height[bmf_curfont]) m_max_height[bmf_curfont] = Symbols[i].height;
//        }
//        return bmf_curfont;
//    }
//
///* Unloads bitmap font by descriptor */
//    int glfUnloadBFontD(GL gl, int bmf_descriptor) {
//        if ((bmf_descriptor < 0) || (bmf_in_use[bmf_descriptor] == false)) return GLF_ERROR;
//
//        bmf_in_use[bmf_descriptor] = false;
//
//        int[] texture = new int[1];
//        texture[0] = bmf_texture[bmf_descriptor];
//        gl.glDeleteTextures(1, texture );
//        texture[0] = bmf_mask[bmf_descriptor];
//        gl.glDeleteTextures(1, texture);
//
//        if (bmf_descriptor == bmf_curfont) bmf_curfont = -1;
//
//        return GLF_OK;
//    }
//
///* Unloads current bitmap font */
//    int glfUnloadBFont(GL gl) {
//        return glfUnloadBFontD(gl, bmf_curfont);
//    }
//
//
///* Start bitmap drawing function */
//    int glfBeginBFont(GL gl, int Font_Descriptor) {
//        int ret = glfSetCurrentBFont(Font_Descriptor);
//        if (ret != GLF_OK) return ret;
//
//        /* Enable 2D Texturing */
//        byte[] texturing = new byte[1];
//        gl.glGetBooleanv(GL.GL_TEXTURE_2D, texturing);
//        bmf_texturing = texturing[0] != 0;
//        gl.glEnable(GL.GL_TEXTURE_2D);
//
//        gl.glBindTexture(GL.GL_TEXTURE_2D, bmf_texture[bmf_curfont]);
//
//        return GLF_OK;
//    }
//
///* Stop bitmap drawing function */
//    void glfEndBFont(GL gl) {
//        /* Return previuos state of texturing */
//        if (bmf_texturing)
//            gl.glEnable(GL.GL_TEXTURE_2D);
//        else
//            gl.glDisable(GL.GL_TEXTURE_2D);
//    }
//
///* Select current BMF font */
//    int glfSetCurrentBFont(int Font_Descriptor) {
//        if ((Font_Descriptor < 0) || (bmf_in_use[Font_Descriptor] == false)) return GLF_ERROR;
//
//        bmf_curfont = Font_Descriptor;
//        return GLF_OK;
//    }
//
///* Get current BMF font */
//    int glfGetCurrentBFont() {
//        return bmf_curfont;
//    }
//
///* Draw one bitmapped symbol by current font */
//    void glfDrawBSymbol(GL gl, char s) {
//        if ((bmf_curfont < 0) || (bmf_in_use[bmf_curfont] == false)) return;
//
//        gl.glCallList(list_base[bmf_curfont] + s);
//    }
//
///* Draw bitmapped string */
//    void glfDrawBString(GL gl, String s)
//    {
//        float temp_trans;
//        int i;
//
//        temp_trans = 0;
//
//        if ((bmf_curfont < 0) || (bmf_in_use[bmf_curfont] == false)) return;
//
//        /* Calculate length of all string */
//        for (i = 0; i < s.length(); i++)
//            temp_trans += m_widths[bmf_curfont].width[s.charAt(i)] + sym_space;
//
//        gl.glListBase(list_base[bmf_curfont]);
//        if (m_bitmap_string_center == true) {
//            gl.glPushMatrix();
//            gl.glTranslatef(-temp_trans / 2, 0, 0);
//        }
//        gl.glCallLists(s.length(), GL.GL_UNSIGNED_BYTE, s.toCharArray());
//        if (m_bitmap_string_center == true) gl.glPopMatrix();
//    }
//
//    void glfDrawBMaskSymbol(GL gl, char s) {
//        if ((bmf_curfont < 0) || (bmf_in_use[bmf_curfont] == false)) return;
//
//        gl.glPushMatrix();
//        gl.glPushAttrib(GL.GL_CURRENT_BIT);
//
//        /* Draw the text as a mask in black */
//        gl.glColor3i(0xff, 0xff, 0xff);
//        gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);
//        gl.glBindTexture(GL.GL_TEXTURE_2D, bmf_mask[bmf_curfont]);
//
//        gl.glCallList(list_base[bmf_curfont] + s);
//
//        gl.glPopAttrib();
//        gl.glPopMatrix();
//
//        gl.glBindTexture(GL.GL_TEXTURE_2D, bmf_texture[bmf_curfont]);
//
//        /* Now draw the text over only the black bits in the requested color */
//        gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
//
//        gl.glCallList(list_base[bmf_curfont] + s);
//    }
//
//    void glfDrawBMaskString(GL gl, String s)
//    {
//        float temp_trans;
//        int i;
//
//        temp_trans = 0;
//
//        if ((bmf_curfont < 0) || (bmf_in_use[bmf_curfont] == false)) return;
//
//        /* Calculate length of all string */
//        for (i = 0; i < s.length(); i++)
//            temp_trans += m_widths[bmf_curfont].width[s.charAt(i)] + sym_space;
//
//        gl.glPushMatrix();
//        gl.glPushAttrib(GL.GL_CURRENT_BIT);
//
//        /* Draw the text as a mask in black */
//        gl.glColor3i(0xff, 0xff, 0xff);
//        gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);
//        gl.glBindTexture(GL.GL_TEXTURE_2D, bmf_mask[bmf_curfont]);
//
//        gl.glListBase(list_base[bmf_curfont]);
//        if (m_bitmap_string_center == true) {
//            gl.glPushMatrix();
//            gl.glTranslatef(-temp_trans / 2, 0, 0);
//        }
//        gl.glCallLists(s.length(), GL.GL_UNSIGNED_BYTE, s.toCharArray());
//        if (m_bitmap_string_center == true) gl.glPopMatrix();
//
//        gl.glPopAttrib();
//        gl.glPopMatrix();
//
//        gl.glBindTexture(GL.GL_TEXTURE_2D, bmf_texture[bmf_curfont]);
//
//        /* Now draw the text over only the black bits in the requested color */
//        gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
//        gl.glListBase(list_base[bmf_curfont]);
//        if (m_bitmap_string_center == true) {
//            gl.glPushMatrix();
//            gl.glTranslatef(-temp_trans / 2, 0, 0);
//        }
//        gl.glCallLists(s.length(), GL.GL_UNSIGNED_BYTE, s.toCharArray());
//        if (m_bitmap_string_center == true) gl.glPopMatrix();
//    }
//
///* Set string centering for bitmap fonts */
//    void glfBitmapStringCentering(boolean center) {
//        m_bitmap_string_center = center;
//    }
//
///* Set string centering for bitmap fonts */
//    boolean glfBitmapGetStringCentering() {
//        return m_bitmap_string_center;
//    }
//
///* Set rotate angle for bitmap fonts */
//    void glfSetBRotateAngle(float angle) {
//        RotateAngleB = angle;
//    }

    /* One symbol of font */
    private static class Symbol {
        int vertexs; /* Number of vertexs         */
        int facets;  /* Number of facets          */
        int lines;   /* Number of lines in symbol */

        float[] vdata;          /* Pointer to Vertex data    */
        int[] fdata;  /* Pointer to Facets data    */
        int[] ldata;  /* Pointer to Line data      */

        float leftx;           /* Smaller x coordinate      */
        float rightx;          /* Right x coordinate        */
        float topy;            /* Top y coordinate          */
        float bottomy;         /* Bottom y coordinate       */
    };

/* Font structure */
    private static class GLFFont {
        String font_name;
        int sym_total;          /* Total symbols in font */
        Symbol[] symbols = new Symbol[MAX_FONTS];  /* Pointers to symbols   */
    };
}
