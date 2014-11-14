package demos.nehe.lesson33;

import java.nio.ByteBuffer;

class Texture {
    ByteBuffer imageData;									// Image Data (Up To 32 Bits)
    int bpp;											// Image Color Depth In Bits Per Pixel
    int width;											// Image width
    int height;											// Image height
    int[] texID = new int[1];											// Texture ID Used To Select A Texture
    int type;											// Image Type (GL_RGB, GL_RGBA)
}
