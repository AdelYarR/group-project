package academy.service;

import academy.function.Function;
import academy.function.FunctionFactory;
import academy.models.AffineParameters;
import academy.models.Color;
import academy.models.Configuration;
import academy.models.FractalRenderState;
import academy.models.Image;
import academy.models.Point;
import academy.models.TransformationFunction;
import academy.utils.FunctionUtil;
import academy.utils.GammaCorrector;
import academy.utils.ImageCreator;
import academy.utils.PointUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FractalFlameService {

    /** Максимальные и минимальные значения точек до нормализации, взятые со статьи. */
    private static final double MAX_X = 1.777;
    private static final double MIN_X = -1.777;
    private static final double MAX_Y = 1;
    private static final double MIN_Y = -1;

    /** По статье приложенной в ТЗ, цвета RGB хранятся в виде десятичных чисел в диапазоне [0, 1].
     * Поэтому, чтобы можно было атомарно (AtomicInteger) хранить данные о цветах,
     * будем умножать число на SCALE и отрезать значение после точки.
     * Пример: 0.345687 * 10000 = 3456.87 -> 3456 */
    private static final int SCALE = 10000;

    private final Configuration config;
    private final Color[] functionColors;

    public FractalFlameService(Configuration config) {
        this.config = config;
        this.functionColors = generateFunctionColors(config.getSeed(), config.getFunctions().size());
    }

    public Image startAndGet() throws InterruptedException {
        // Храним атомарные буферы для цветов и счётчика плотности в классе состояния рендера
        FractalRenderState state = new FractalRenderState(config.getWidth(), config.getHeight());

        // Обрабатываем все итерации в разных потоках и сохраняем информацию в state
        executeMultithreading(state);

        // Строим изображение на основе state
        Image image = ImageCreator.createImageFromState(state, SCALE, config.getWidth(), config.getHeight());

        GammaCorrector.correct(image, config.getGamma());
        return image;
    }

    private void executeMultithreading(FractalRenderState state) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < config.getThreadCount(); i++) {
            Thread thread = new Thread(() -> generateFractalFlame(state));
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    private void generateFractalFlame(FractalRenderState state) {
        Random random = new Random(config.getSeed().hashCode());

        // Инициализация начальной точки
        double currentX = MIN_X + (MAX_X - MIN_X) * random.nextDouble();
        double currentY = MIN_Y + (MAX_Y - MIN_Y) * random.nextDouble();

        while (true) {
            int currentIteration = state.getAtomicIterationCount().getAndIncrement();
            if (currentIteration >= config.getIterationCount()) {
                break;
            }

            // Получаем рандомно аффинное преобразование и функцию в зависимости от её веса
            int functionIndex = FunctionUtil.getRandomFunctionIndex(random, config.getFunctions());
            AffineParameters affine = config.getAffineParameters().get(functionIndex);
            TransformationFunction function = config.getFunctions().get(functionIndex);

            // Применяем аффинное преобразование
            double affineX = affine.a() * currentX + affine.b() * currentY + affine.c();
            double affineY = affine.d() * currentX + affine.e() * currentY + affine.f();

            // Применяем нелинейную вариацию функции
            Function variation = FunctionFactory.getFunctionByName(function.name());
            Point newPoint = variation.apply(new Point(affineX, affineY));

            // Обновляем текущую точку
            currentX = newPoint.x();
            currentY = newPoint.y();

            if (currentIteration >= 20) {
                processIteration(state, functionIndex, config.getWidth(), config.getHeight(), newPoint);
            }
        }
    }

    private void processIteration(FractalRenderState state, int functionIndex, int width, int height, Point newPoint) {
        // Переводим координаты точки из десятичных значений диапазона [0, 1] в целые значения разрешения изображения
        Point normPoint = PointUtil.normalizePoint(newPoint, MAX_X, MIN_X, MAX_Y, MIN_Y, width, height);
        int normX = (int) normPoint.x();
        int normY = (int) normPoint.y();

        if (normX < width && normY < height) {
            // Считаем индекс для AtomicIntegerArray переменных
            int index = normY * width + normX;

            int red = (int) (functionColors[functionIndex].getRed() * SCALE);
            int green = (int) (functionColors[functionIndex].getGreen() * SCALE );
            int blue = (int) (functionColors[functionIndex].getBlue() * SCALE);

            // Если в точку ещё не попадали, то устанавливаем изначально сгенерированный цвет
            if (state.getCounterArray().get(index) == 0) {

                state.getRedColorArray().set(index, red);
                state.getGreenColorArray().set(index, green);
                state.getBlueColorArray().set(index, blue);
            }
            // Если в точку уже попадали, то усредняем новый цвет с предыдущим
            else {
                int oldRed = state.getRedColorArray().get(index);
                int oldGreen = state.getGreenColorArray().get(index);
                int oldBlue = state.getBlueColorArray().get(index);

                state.getRedColorArray().set(index, (oldRed + red) / 2);
                state.getGreenColorArray().set(index, (oldGreen + green) / 2);
                state.getBlueColorArray().set(index, (oldBlue + blue) / 2);
            }

            state.getCounterArray().incrementAndGet(index);
        }
    }

    private Color[] generateFunctionColors(Double seed, int functionSize) {
        Random colorRandom = new Random(seed.hashCode());

        Color[] colors = new Color[functionSize];
        for (int i = 0; i < functionSize; i++) {
            double red = colorRandom.nextDouble();
            double green = colorRandom.nextDouble();
            double blue = colorRandom.nextDouble();

            colors[i] = new Color(red, green, blue);
        }

        return colors;
    }
}
