package academy.utils;

import academy.models.Color;
import academy.models.Configuration;
import academy.models.Image;
import academy.models.Pixel;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriter {

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
            File outputFile = new File(config.getOutputPath());
            ImageIO.write(bufferedImage, "png", outputFile);
            System.out.println("Изображение сохранено: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении изображения", e);
        }
    }
}
