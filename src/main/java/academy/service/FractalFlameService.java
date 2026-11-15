package academy.service;

import academy.function.Function;
import academy.function.FunctionFactory;
import academy.models.AffineParameters;
import academy.models.Color;
import academy.models.Configuration;
import academy.models.Image;
import academy.models.Pixel;
import academy.models.Point;
import academy.models.TransformationFunction;
import java.util.List;
import java.util.Random;

public class FractalFlameService {

    private static final double XMAX = 1.777;
    private static final double XMIN = -1.777;
    private static final double YMAX = 1;
    private static final double YMIN = -1;

    private Configuration config;

    public FractalFlameService(Configuration config) {
        this.config = config;
    }

    public Image startAndGet() {
        Random random = new Random(config.getSeed().hashCode());

        int width = config.getSize().getWidth();
        int height = config.getSize().getHeight();

        Image image = new Image(width, height);
        Pixel[][] pixels = image.getPixels();

        // Инициализация начальной точки
        double x = XMIN + (XMAX - XMIN) * random.nextDouble();
        double y = YMIN + (YMAX - YMIN) * random.nextDouble();

        // Получаем список аффинных преобразований и функций с весами
        List<TransformationFunction> functions = config.getFunctions();
        List<AffineParameters> affineParameters = config.getAffineParameters();

        // Массивы для цветов функций
        Color[] functionColors = new Color[functions.size()];
        for (int i = 0; i < functions.size(); i++) {
            double red = random.nextDouble();
            double green = random.nextDouble();
            double blue = random.nextDouble();
            functionColors[i] = new Color(red, green, blue);
        }

        for (int i = 0; i < config.getIterationCount(); i++) {
            int functionIndex = getRandomFunctionIndex(random, config.getFunctions());
            TransformationFunction function = functions.get(functionIndex);
            AffineParameters affine = affineParameters.get(functionIndex);

            // Применяем аффинное преобразование
            double affineX = affine.a() * x + affine.b() * y + affine.c();
            double affineY = affine.d() * x + affine.e() * y + affine.f();

            // Применяем нелинейную вариацию
            Function variation = FunctionFactory.getFunctionByName(function.name());
            Point newPoint = variation.apply(new Point(affineX, affineY));

            // Обновляем текущую точку
            x = newPoint.x();
            y = newPoint.y();

            if (i >= 20) {
                Point normPoint = normalizePoint(newPoint, width, height);
                int normX = (int) normPoint.x();
                int normY = (int) normPoint.y();

                if (normX < width && normY < height) {
                    Color newColor;
                    if (pixels[normY][normX] == null) {
                        newColor = new Color(
                            functionColors[functionIndex].getRed(),
                            functionColors[functionIndex].getGreen(),
                            functionColors[functionIndex].getBlue());

                        pixels[normY][normX] = new Pixel(newColor);
                    } else {
                        Color oldColor = pixels[normY][normX].getColor();
                        newColor = new Color(
                            (oldColor.getRed() + functionColors[functionIndex].getRed()) / 2,
                            (oldColor.getGreen() + functionColors[functionIndex].getGreen()) / 2,
                            (oldColor.getBlue() + functionColors[functionIndex].getBlue()) / 2);

                        pixels[normY][normX].setColor(newColor);
                    }

                    pixels[normY][normX].increaseCounter();
                }
            }
        }

        gammaCorrect(image, config.getGamma());

        Image coloredImage = colorToRgb(image);
        coloredImage.printInfo();
        return coloredImage;
    }

    private int getRandomFunctionIndex(Random random, List<TransformationFunction> functions) {
        double allWeights = functions.stream()
            .mapToDouble(TransformationFunction::weight)
            .sum();

        double randomValue = random.nextDouble() * allWeights;

        double cumulative = 0;
        for (int i = 0; i < functions.size(); i++) {
            cumulative += functions.get(i).weight();
            if (randomValue < cumulative) {
                return i;
            }
        }

        return functions.size() - 1;
    }

    private Point normalizePoint(Point point, int width, int height) {
        //В массиве отчёт начинается с 0, поэтому отнимаем единицу у ширины и высоты
        int w = width - 1;
        int h = height - 1;

        double oldX = point.x();
        double oldY = point.y();

        double xRatio = (XMAX - oldX) / (XMAX - XMIN);
        int x1 = width - (int) Math.floor(xRatio * width);

        double yRatio = (YMAX - oldY) / (YMAX - YMIN);
        int y1 = height - (int) Math.floor(yRatio * height);

        x1 = Math.max(0, Math.min(w, x1));
        y1 = Math.max(0, Math.min(h, y1));

        return new Point(x1, y1);
    }

    private Image colorToRgb(Image image) {
        Pixel[][] pixels = image.getPixels();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Pixel pixel = pixels[y][x];
                if (pixel != null) {
                    Color oldColor = pixel.getColor();
                    int r = (int) (oldColor.getRed() * 255);
                    int g = (int) (oldColor.getGreen() * 255);
                    int b = (int) (oldColor.getBlue() * 255);
                    pixel.setColor(new Color(r, g, b));
                }
            }
        }
        return image;
    }

    private void gammaCorrect(Image image, double gamma) {
        int width = image.getWidth();
        int height = image.getHeight();
        Pixel[][] pixels = image.getPixels();
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

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pixels[y][x] != null) {
                    pixels[y][x].setNormal(pixels[y][x].getNormal() / max);

                    Color color = pixels[y][x].getColor();
                    double gammaCorrection = Math.pow(pixels[y][x].getNormal(), (1.0 / gamma));
                    color.setRed(color.getRed() * gammaCorrection);
                    color.setGreen(color.getGreen() * gammaCorrection);
                    color.setBlue(color.getBlue() * gammaCorrection);
                }
            }
        }
    }
}
