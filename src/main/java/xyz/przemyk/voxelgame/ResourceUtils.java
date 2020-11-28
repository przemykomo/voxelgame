package xyz.przemyk.voxelgame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class ResourceUtils {
    private ResourceUtils() {}

    private static final ClassLoader classLoader = ResourceUtils.class.getClassLoader();

    public static String loadStringResource(String fileName) throws IOException {
        URL url = classLoader.getResource(fileName);
        if (url == null) {
            throw new IOException("Cannot get resource with file name: " + fileName);
        }
        return new String(Files.readAllBytes(new File(url.getFile()).toPath()));
    }

    public static byte[] loadImageResource(String fileName) throws IOException {
        URL url = classLoader.getResource(fileName);
        if (url == null) {
            throw new IOException("Cannot get resource with file name: " + fileName);
        }
        BufferedImage bufferedImage = ImageIO.read(new File(url.getFile()));
        ImageIO.write(bufferedImage, "png", new ByteArrayOutputStream());

        byte[] imageBytes = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        byte temp;

        // for some reason opengl renders texture with different order of bytes
        for (int i = 0; i < imageBytes.length; i += 4) {
            temp = imageBytes[i];
            imageBytes[i] = imageBytes[i + 3];
            imageBytes[i + 3] = temp;

            temp = imageBytes[i + 1];
            imageBytes[i + 1] =  imageBytes[i + 2];
            imageBytes[i + 2] = temp;
        }

        return imageBytes;
    }
}
