package academy.service;

import academy.models.image.Color;
import academy.models.image.Image;
import academy.models.image.Pixel;

/**
 * Класс, предоставляющий методы для гамма-коррекции изображения. Процесс состоит из двух этапов: Логарифмическая
 * нормализация значений счётчиков пикселей. Применение функции гамма-коррекции к цветам пикселей.
 */
public class GammaCorrector {

    private GammaCorrector() {}

    public static void correct(Image image, double gamma) {
        int width = image.getWidth();
        int height = image.getHeight();
        Pixel[][] pixels = image.getPixels();

        double max = logNormalize(pixels, width, height);

        applyGammaCorrection(pixels, gamma, max, width, height);
    }

    private static double logNormalize(Pixel[][] pixels, int width, int height) {
        double max = 0.0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pixels[y][x] != null) {
                    pixels[y][x].setNormal(Math.log10(pixels[y][x].getCounter()));

                    if (pixels[y][x].getNormal() > max) {
                        max = pixels[y][x].getNormal();
                    }
                }
            }
        }
        return max;
    }

    private static void applyGammaCorrection(Pixel[][] pixels, double gamma, double max, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pixels[y][x] != null) {
                    pixels[y][x].setNormal(pixels[y][x].getNormal() / max);

                    Color color = pixels[y][x].getColor();
                    double gammaCorrection = Math.pow(pixels[y][x].getNormal(), 1.0 / gamma);
                    color.setRed(color.getRed() * gammaCorrection);
                    color.setGreen(color.getGreen() * gammaCorrection);
                    color.setBlue(color.getBlue() * gammaCorrection);
                }
            }
        }
    }
}
