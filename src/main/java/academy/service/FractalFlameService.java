package academy.service;

import academy.function.Function;
import academy.function.FunctionFactory;
import academy.logger.ProgressLogger;
import academy.models.FractalRenderState;
import academy.models.config.AffineParameters;
import academy.models.config.Configuration;
import academy.models.image.Color;
import academy.models.image.Image;
import academy.models.image.Point;
import academy.utils.FunctionUtil;
import academy.utils.PointUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FractalFlameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FractalFlameService.class);

    private static final double MAX_X = 1.777;
    private static final double MIN_X = -1.777;
    private static final double MAX_Y = 1;
    private static final double MIN_Y = -1;

    private static final int SCALE = 10000;
    private static final int CHUNK_SIZE = 10_000;

    private final Configuration config;
    private final Color[] functionColors;
    private final Function[] cachedVariations;

    public FractalFlameService(Configuration config) {
        this.config = config;
        this.functionColors = generateFunctionColors(config.getFunctions().size());
        this.cachedVariations = cacheVariations(config);
    }

    public Image startAndGet() throws InterruptedException {
        FractalRenderState state = new FractalRenderState(config.getWidth(), config.getHeight());

        Thread progressLogger = new Thread(new ProgressLogger(state, config.getIterationCount()));
        progressLogger.start();

        executeMultithreading(state);
        LOGGER.info("Потоки завершили генерацию фрактального пламени.");

        Image image = ImageCreator.createImageFromState(state, SCALE, config.getWidth(), config.getHeight());
        LOGGER.info("Изображение фрактального пламени успешно создано.");

        GammaCorrector.correct(image, config.getGamma());
        LOGGER.info("Гамма-коррекция изображения успешно добавлена.");
        return image;
    }

    private static Function[] cacheVariations(Configuration config) {
        Function[] variations = new Function[config.getFunctions().size()];
        for (int i = 0; i < variations.length; i++) {
            variations[i] = FunctionFactory.getFunctionByName(config.getFunctions().get(i).name());
        }
        return variations;
    }

    private void executeMultithreading(FractalRenderState state) throws InterruptedException {
        int totalPixels = config.getWidth() * config.getHeight();
        int threadCount = config.getThreadCount();

        List<int[]> localCounters = new ArrayList<>();
        List<int[]> localReds = new ArrayList<>();
        List<int[]> localGreens = new ArrayList<>();
        List<int[]> localBlues = new ArrayList<>();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;

            final int[] counter = new int[totalPixels];
            final int[] red = new int[totalPixels];
            final int[] green = new int[totalPixels];
            final int[] blue = new int[totalPixels];

            localCounters.add(counter);
            localReds.add(red);
            localGreens.add(green);
            localBlues.add(blue);

            Thread thread = new Thread(() ->
                generateFractalFlame(state, threadIndex, counter, red, green, blue));
            thread.start();
            threads.add(thread);
            LOGGER.info("Запуск потока {} для генерации фрактального пламени.", thread.getName());
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (int t = 0; t < threadCount; t++) {
            int[] counter = localCounters.get(t);
            int[] red = localReds.get(t);
            int[] green = localGreens.get(t);
            int[] blue = localBlues.get(t);

            for (int idx = 0; idx < totalPixels; idx++) {
                int localCount = counter[idx];
                if (localCount > 0) {
                    int globalCount = state.getCounterArray().get(idx);

                    if (globalCount == 0) {
                        state.getRedColorArray().set(idx, red[idx]);
                        state.getGreenColorArray().set(idx, green[idx]);
                        state.getBlueColorArray().set(idx, blue[idx]);
                    } else {
                        int oldRed = state.getRedColorArray().get(idx);
                        int oldGreen = state.getGreenColorArray().get(idx);
                        int oldBlue = state.getBlueColorArray().get(idx);

                        int newRed = (oldRed * globalCount + red[idx] * localCount) / (globalCount + localCount);
                        int newGreen = (oldGreen * globalCount + green[idx] * localCount) / (globalCount + localCount);
                        int newBlue = (oldBlue * globalCount + blue[idx] * localCount) / (globalCount + localCount);

                        state.getRedColorArray().set(idx, newRed);
                        state.getGreenColorArray().set(idx, newGreen);
                        state.getBlueColorArray().set(idx, newBlue);
                    }
                    state.getCounterArray().addAndGet(idx, localCount);
                }
            }
        }
    }

    private void generateFractalFlame(FractalRenderState state,
                                      int threadIndex,
                                      int[] localCounter,
                                      int[] localRed,
                                      int[] localGreen,
                                      int[] localBlue) {
        Random random = new Random(config.getSeed().hashCode() + threadIndex);

        double currentX = MIN_X + (MAX_X - MIN_X) * random.nextDouble();
        double currentY = MIN_Y + (MAX_Y - MIN_Y) * random.nextDouble();

        while (true) {
            int start = state.getAtomicIterationCount().getAndAdd(CHUNK_SIZE);
            if (start >= config.getIterationCount()) {
                break;
            }
            int end = Math.min(start + CHUNK_SIZE, config.getIterationCount());

            for (int currentIteration = start; currentIteration < end; currentIteration++) {
                int functionIndex = FunctionUtil.getRandomFunctionIndex(random, config.getFunctions());
                int affineIndex = random.nextInt(config.getAffineParameters().size());
                AffineParameters affine = config.getAffineParameters().get(affineIndex);

                double affineX = affine.a() * currentX + affine.b() * currentY + affine.c();
                double affineY = affine.d() * currentX + affine.e() * currentY + affine.f();

                Function variation = cachedVariations[functionIndex];
                Point newPoint = variation.apply(new Point(affineX, affineY));

                currentX = newPoint.x();
                currentY = newPoint.y();

                if (currentX < MIN_X || currentX > MAX_X || currentY < MIN_Y || currentY > MAX_Y) {
                    continue;
                }

                if (currentIteration >= 20) {
                    applySymmetry(functionIndex, config.getWidth(), config.getHeight(), newPoint,
                        localCounter, localRed, localGreen, localBlue);
                }
            }
        }
    }

    private void applySymmetry(int functionIndex, int width, int height, Point newPoint,
                               int[] localCounter, int[] localRed, int[] localGreen, int[] localBlue) {
        int symmetryLevel = config.getSymmetryLevel();

        if (symmetryLevel == 1) {
            processIteration(functionIndex, width, height, newPoint,
                localCounter, localRed, localGreen, localBlue);
            return;
        }

        double angle = 360.0 / symmetryLevel;
        for (int i = 0; i < symmetryLevel; i++) {
            double rotationAngle = i * angle;
            Point rotatedPoint = PointUtil.rotatePoint(newPoint, rotationAngle);
            processIteration(functionIndex, width, height, rotatedPoint,
                localCounter, localRed, localGreen, localBlue);
        }
    }

    private void processIteration(int functionIndex, int width, int height, Point newPoint,
                                  int[] localCounter, int[] localRed, int[] localGreen, int[] localBlue) {
        Point normPoint = PointUtil.normalizePoint(newPoint, MAX_X, MIN_X, MAX_Y, MIN_Y, width, height);
        int normX = (int) normPoint.x();
        int normY = (int) normPoint.y();

        if (normX >= 0 && normX < width && normY >= 0 && normY < height) {
            int index = normY * width + normX;

            int red = (int) (functionColors[functionIndex].getRed() * SCALE);
            int green = (int) (functionColors[functionIndex].getGreen() * SCALE);
            int blue = (int) (functionColors[functionIndex].getBlue() * SCALE);

            if (localCounter[index] == 0) {
                localRed[index] = red;
                localGreen[index] = green;
                localBlue[index] = blue;
            } else {
                localRed[index] = (localRed[index] + red) / 2;
                localGreen[index] = (localGreen[index] + green) / 2;
                localBlue[index] = (localBlue[index] + blue) / 2;
            }
            localCounter[index]++;
        }
    }

    private Color[] generateFunctionColors(int functionSize) {
        Random random = new Random(config.getSeed().hashCode());
        Color[] colors = new Color[functionSize];
        for (int i = 0; i < functionSize; i++) {
            double red = random.nextDouble();
            double green = random.nextDouble();
            double blue = random.nextDouble();

            colors[i] = new Color(red, green, blue);
        }

        return colors;
    }
}
