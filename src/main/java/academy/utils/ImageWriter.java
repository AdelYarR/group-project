package academy.utils;

import academy.models.config.Configuration;
import academy.models.image.Color;
import academy.models.image.Image;
import academy.models.image.Pixel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class ImageWriter {

    private ImageWriter() {}

    public static void writeToPng(Image image, Configuration config) {
        int width = config.getSize().getWidth();
        int height = config.getSize().getHeight();
        Pixel[][] pixels = image.getPixels();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixel pixel = pixels[y][x];
                if (pixel != null) {
                    Color color = pixel.getColor();
                    if (color != null) {
                        int red = (int) color.getRed();
                        int green = (int) color.getGreen();
                        int blue = (int) color.getBlue();
                        int rgb = (red << 16) | (green << 8) | blue;
                        bufferedImage.setRGB(x, y, rgb);
                    }
                }
            }
        }

        try {
            File outputFile = Path.of(config.getOutputPath()).toFile();
            ImageIO.write(bufferedImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException("failed to save image", e);
        }
    }
}
