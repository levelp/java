package demos.nehe.lesson33;

import com.sun.opengl.util.BufferUtil;
import demos.common.ResourceRetriever;

import javax.media.opengl.GL;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

class TGALoader {
    private static final ByteBuffer uTGAcompare;	// Uncompressed TGA Header
    private static final ByteBuffer cTGAcompare;	// Compressed TGA Header

    static {
        byte[] uncompressedTgaHeader = new byte[]{0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] compressedTgaHeader = new byte[]{0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        uTGAcompare = BufferUtil.newByteBuffer(uncompressedTgaHeader.length);
        uTGAcompare.put(uncompressedTgaHeader);	// Uncompressed TGA Header
        uTGAcompare.flip();

        cTGAcompare = BufferUtil.newByteBuffer(compressedTgaHeader.length);
        cTGAcompare.put(compressedTgaHeader);	// Compressed TGA Header
        cTGAcompare.flip();
    }

    public static void loadTGA(Texture texture, String filename) throws IOException				// Load a TGA file
    {
        ByteBuffer header = BufferUtil.newByteBuffer(12);
        ReadableByteChannel in = Channels.newChannel(ResourceRetriever.getResourceAsStream(filename));
        readBuffer(in, header);

        if (uTGAcompare.equals(header))				// See if header matches the predefined header of
        {																		// an Uncompressed TGA image
            loadUncompressedTGA(texture, in);						// If so, jump to Uncompressed TGA loading code
        } else if (cTGAcompare.equals(header))		// See if header matches the predefined header of
        {																		// an RLE compressed TGA image
            loadCompressedTGA(texture, in);							// If so, jump to Compressed TGA loading code
        } else																	// If header matches neither type
        {
            in.close();
            throw new IOException("TGA file be type 2 or type 10 ");	// Display an error

        }
    }

    private static void readBuffer(ReadableByteChannel in, ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            in.read(buffer);
        }
        buffer.flip();
    }

    private static void loadUncompressedTGA(Texture texture, ReadableByteChannel in) throws IOException	// Load an uncompressed TGA (note, much of this code is based on NeHe's
    {																			// TGA Loading code nehe.gamedev.net)
        TGA tga = new TGA();
        readBuffer(in, tga.header);

        texture.width = (unsignedByteToInt(tga.header.get(1)) << 8) + unsignedByteToInt(tga.header.get(0));					// Determine The TGA width	(highbyte*256+lowbyte)
        texture.height = (unsignedByteToInt(tga.header.get(3)) << 8) + unsignedByteToInt(tga.header.get(2));					// Determine The TGA height	(highbyte*256+lowbyte)
        texture.bpp = unsignedByteToInt(tga.header.get(4));										// Determine the bits per pixel
        tga.width = texture.width;										// Copy width into local structure
        tga.height = texture.height;										// Copy height into local structure
        tga.bpp = texture.bpp;											// Copy BPP into local structure

        if ((texture.width <= 0) || (texture.height <= 0) || ((texture.bpp != 24) && (texture.bpp != 32)))	// Make sure all information is valid
        {
            throw new IOException("Invalid texture information");	// Display Error
        }

        if (texture.bpp == 24)													// If the BPP of the image is 24...
            texture.type = GL.GL_RGB;											// Set Image type to GL_RGB
        else																	// Else if its 32 BPP
            texture.type = GL.GL_RGBA;											// Set image type to GL_RGBA

        tga.bytesPerPixel = (tga.bpp / 8);									// Compute the number of BYTES per pixel
        tga.imageSize = (tga.bytesPerPixel * tga.width * tga.height);		// Compute the total amout ofmemory needed to store data
        texture.imageData = BufferUtil.newByteBuffer(tga.imageSize);					// Allocate that much memory

        readBuffer(in, texture.imageData);

        for (int cswap = 0; cswap < tga.imageSize; cswap += tga.bytesPerPixel) {
            byte temp = texture.imageData.get(cswap);
            texture.imageData.put(cswap, texture.imageData.get(cswap + 2));
            texture.imageData.put(cswap + 2, temp);
        }
    }

    private static void loadCompressedTGA(Texture texture, ReadableByteChannel fTGA) throws IOException		// Load COMPRESSED TGAs
    {
        TGA tga = new TGA();
        readBuffer(fTGA, tga.header);

        texture.width = (unsignedByteToInt(tga.header.get(1)) << 8) + unsignedByteToInt(tga.header.get(0));					// Determine The TGA width	(highbyte*256+lowbyte)
        texture.height = (unsignedByteToInt(tga.header.get(3)) << 8) + unsignedByteToInt(tga.header.get(2));					// Determine The TGA height	(highbyte*256+lowbyte)
        texture.bpp = unsignedByteToInt(tga.header.get(4));										// Determine Bits Per Pixel
        tga.width = texture.width;										// Copy width to local structure
        tga.height = texture.height;										// Copy width to local structure
        tga.bpp = texture.bpp;											// Copy width to local structure

        if ((texture.width <= 0) || (texture.height <= 0) || ((texture.bpp != 24) && (texture.bpp != 32)))	//Make sure all texture info is ok
        {
            throw new IOException("Invalid texture information");	// If it isnt...Display error
        }

        if (texture.bpp == 24)													// If the BPP of the image is 24...
            texture.type = GL.GL_RGB;											// Set Image type to GL_RGB
        else																	// Else if its 32 BPP
            texture.type = GL.GL_RGBA;											// Set image type to GL_RGBA

        tga.bytesPerPixel = (tga.bpp / 8);									// Compute BYTES per pixel
        tga.imageSize = (tga.bytesPerPixel * tga.width * tga.height);		// Compute amout of memory needed to store image
        texture.imageData = BufferUtil.newByteBuffer(tga.imageSize);					// Allocate that much memory
        texture.imageData.position(0);
        texture.imageData.limit(texture.imageData.capacity());

        int pixelcount = tga.height * tga.width;							// Nuber of pixels in the image
        int currentpixel = 0;												// Current pixel being read
        int currentbyte = 0;												// Current byte
        ByteBuffer colorbuffer = BufferUtil.newByteBuffer(tga.bytesPerPixel);			// Storage for 1 pixel

        do {
            int chunkheader;											// Storage for "chunk" header
            try {
                ByteBuffer chunkHeaderBuffer = ByteBuffer.allocate(1);
                fTGA.read(chunkHeaderBuffer);
                chunkHeaderBuffer.flip();
                chunkheader = unsignedByteToInt(chunkHeaderBuffer.get());
            } catch (IOException e) {
                throw new IOException("Could not read RLE header");	// Display Error
            }

            if (chunkheader < 128)												// If the ehader is < 128, it means the that is the number of RAW color packets minus 1
            {																	// that follow the header
                chunkheader++;													// add 1 to get number of following color values
                for (short counter = 0; counter < chunkheader; counter++)		// Read RAW color values
                {
                    readBuffer(fTGA, colorbuffer);
                    // write to memory
                    texture.imageData.put(currentbyte, colorbuffer.get(2));				    // Flip R and B vcolor values around in the process
                    texture.imageData.put(currentbyte + 1, colorbuffer.get(1));
                    texture.imageData.put(currentbyte + 2, colorbuffer.get(0));

                    if (tga.bytesPerPixel == 4)												// if its a 32 bpp image
                    {
                        texture.imageData.put(currentbyte + 3, colorbuffer.get(3));				// copy the 4th byte
                    }

                    currentbyte += tga.bytesPerPixel;										// Increase thecurrent byte by the number of bytes per pixel
                    currentpixel++;															// Increase current pixel by 1

                    if (currentpixel > pixelcount)											// Make sure we havent read too many pixels
                    {
                        throw new IOException("Too many pixels read");			// if there is too many... Display an error!
                    }
                }
            } else																			// chunkheader > 128 RLE data, next color reapeated chunkheader - 127 times
            {
                chunkheader -= 127;															// Subteact 127 to get rid of the ID bit
                readBuffer(fTGA, colorbuffer);

                for (short counter = 0; counter < chunkheader; counter++)					// copy the color into the image data as many times as dictated
                {																			// by the header
                    texture.imageData.put(currentbyte, colorbuffer.get(2));				    // Flip R and B vcolor values around in the process
                    texture.imageData.put(currentbyte + 1, colorbuffer.get(1));
                    texture.imageData.put(currentbyte + 2, colorbuffer.get(0));

                    if (tga.bytesPerPixel == 4)												// if its a 32 bpp image
                    {
                        texture.imageData.put(currentbyte + 3, colorbuffer.get(3));				// copy the 4th byte
                    }

                    currentbyte += tga.bytesPerPixel;										// Increase current byte by the number of bytes per pixel
                    currentpixel++;															// Increase pixel count by 1

                    if (currentpixel > pixelcount)											// Make sure we havent written too many pixels
                    {
                        throw new IOException("Too many pixels read");			// if there is too many... Display an error!
                    }
                }
            }
        } while (currentpixel < pixelcount);													// Loop while there are still pixels left
    }

    private static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }
}
