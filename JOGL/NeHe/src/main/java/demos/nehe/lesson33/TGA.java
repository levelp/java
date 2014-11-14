package demos.nehe.lesson33;

import com.sun.opengl.util.BufferUtil;

import java.nio.ByteBuffer;

class TGA {
    ByteBuffer header = BufferUtil.newByteBuffer(6);								// First 6 Useful Bytes From The Header
    int bytesPerPixel;							// Holds Number Of Bytes Per Pixel Used In The TGA File
    int imageSize;								// Used To Store The Image Size When Setting Aside Ram
    int temp;									// Temporary Variable
    int type;
    int height;									//height of Image
    int width;									//width ofImage
    int bpp;									// Bits Per Pixel
}
