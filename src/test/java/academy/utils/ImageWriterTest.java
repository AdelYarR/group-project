package academy.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.models.config.Configuration;
import academy.models.image.Color;
import academy.models.image.Image;
import academy.models.image.Pixel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ImageWriterTest {

    @Test
    @DisplayName("Создание PNG файла")
    void writeToPng_createsValidPngFile(@TempDir Path tempDir) throws Exception {
        String outputPath = tempDir.resolve("test.png").toString();
        Configuration config = Configuration.builder()
                .width(10)
                .height(10)
                .outputPath(outputPath)
                .affineParameters("<1>,<0>,<0>,<0>,<1>,<0>")
                .functions("linear:1.0")
                .build();

        Image image = new Image(10, 10);
        Pixel[][] pixels = image.getPixels();
        pixels[0][0] = new Pixel(new Color(255, 0, 0));

        ImageWriter.writeToPng(image, config);

        File file = new File(outputPath);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        BufferedImage img = ImageIO.read(file);
        assertEquals(10, img.getWidth());
        assertEquals(10, img.getHeight());
    }

    @Test
    @DisplayName("Проверка кодирования цветов")
    void writeToPng_colorEncoding(@TempDir Path tempDir) throws Exception {
        String outputPath = tempDir.resolve("colors.png").toString();
        Configuration config = Configuration.builder()
                .width(3)
                .height(1)
                .outputPath(outputPath)
                .affineParameters("<1>,<0>,<0>,<0>,<1>,<0>")
                .functions("linear:1.0")
                .build();

        Image image = new Image(3, 1);
        Pixel[][] pixels = image.getPixels();

        pixels[0][0] = new Pixel(new Color(255, 0, 0));
        pixels[0][1] = new Pixel(new Color(0, 255, 0));
        pixels[0][2] = new Pixel(new Color(0, 0, 255));

        ImageWriter.writeToPng(image, config);

        BufferedImage img = ImageIO.read(new File(outputPath));

        int redPixel = img.getRGB(0, 0);
        assertEquals(255, (redPixel >> 16) & 0xFF);
        assertEquals(0, (redPixel >> 8) & 0xFF);
        assertEquals(0, redPixel & 0xFF);

        int greenPixel = img.getRGB(1, 0);
        assertEquals(0, (greenPixel >> 16) & 0xFF);
        assertEquals(255, (greenPixel >> 8) & 0xFF);
        assertEquals(0, greenPixel & 0xFF);

        int bluePixel = img.getRGB(2, 0);
        assertEquals(0, (bluePixel >> 16) & 0xFF);
        assertEquals(0, (bluePixel >> 8) & 0xFF);
        assertEquals(255, bluePixel & 0xFF);
    }

    @Test
    @DisplayName("Несуществующий путь должен бросать исключение")
    void writeToPng_whenInvalidPath_shouldThrowException() {
        Configuration config = Configuration.builder()
                .width(100)
                .height(100)
                .outputPath("/invalid/path/image.png")
                .affineParameters("<1>,<0>,<0>,<0>,<1>,<0>")
                .functions("linear:1.0")
                .build();

        Image image = new Image(100, 100);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ImageWriter.writeToPng(image, config));

        assertTrue(exception.getMessage().contains("failed to save image"));
    }

    @Test
    @DisplayName("Десятичные значения цвета округляются")
    void writeToPng_whenFractionalColors_rounded(@TempDir Path tempDir) throws Exception {
        String outputPath = tempDir.resolve("fractional.png").toString();
        Configuration config = Configuration.builder()
                .width(1)
                .height(1)
                .outputPath(outputPath)
                .affineParameters("<1>,<0>,<0>,<0>,<1>,<0>")
                .functions("linear:1.0")
                .build();

        Image image = new Image(1, 1);
        Pixel[][] pixels = image.getPixels();
        pixels[0][0] = new Pixel(new Color(127.3, 255.7, 0.4));

        ImageWriter.writeToPng(image, config);

        BufferedImage img = ImageIO.read(new File(outputPath));
        int rgb = img.getRGB(0, 0);

        assertEquals(127, (rgb >> 16) & 0xFF);
        assertEquals(255, (rgb >> 8) & 0xFF);
        assertEquals(0, rgb & 0xFF);
    }

    @Test
    @DisplayName("Значения цвета больше 255, обрезаются до 255")
    void writeToPng_whenColorsAbove255_clippedTo255(@TempDir Path tempDir) throws Exception {
        String outputPath = tempDir.resolve("above255.png").toString();
        Configuration config = Configuration.builder()
                .width(1)
                .height(1)
                .outputPath(outputPath)
                .affineParameters("<1>,<0>,<0>,<0>,<1>,<0>")
                .functions("linear:1.0")
                .build();

        Image image = new Image(1, 1);
        Pixel[][] pixels = image.getPixels();
        pixels[0][0] = new Pixel(new Color(300, 500, 1000));

        ImageWriter.writeToPng(image, config);

        BufferedImage img = ImageIO.read(new File(outputPath));
        int rgb = img.getRGB(0, 0);

        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        assertTrue(red >= 0 && red <= 255);
        assertTrue(green >= 0 && green <= 255);
        assertTrue(blue >= 0 && blue <= 255);
    }

    @Test
    @DisplayName("Null конфигурация должна бросать NullPointerException")
    void writeToPng_whenNullConfig_shouldThrowException() {
        Image image = new Image(1, 1);
        assertThrows(NullPointerException.class, () -> ImageWriter.writeToPng(image, null));
    }

    @Test
    @DisplayName("Null изображение должно бросать NullPointerException")
    void writeToPng_whenNullImage_shouldThrowsException(@TempDir Path tempDir) {
        String outputPath = tempDir.resolve("test.png").toString();
        Configuration config = Configuration.builder()
                .width(1)
                .height(1)
                .outputPath(outputPath)
                .affineParameters("<1>,<0>,<0>,<0>,<1>,<0>")
                .functions("linear:1.0")
                .build();

        assertThrows(NullPointerException.class, () -> ImageWriter.writeToPng(null, config));
    }
}
