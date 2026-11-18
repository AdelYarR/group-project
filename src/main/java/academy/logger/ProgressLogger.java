package academy.logger;

import academy.models.FractalRenderState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressLogger implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgressLogger.class);
    private static final long INTERVAL = 100;

    private FractalRenderState state;
    private int step;

    public ProgressLogger(FractalRenderState state, int maxIterations) {
        this.state = state;
        this.step = Math.max(1, maxIterations / 10);
    }

    @Override
    public void run() {
        try {
            int stepIteration = step;
            int currentPercentage = 10;

            while (!Thread.currentThread().isInterrupted()) {
                int currentIteration = state.getAtomicIterationCount().get();

                if (currentIteration > stepIteration) {
                    LOGGER.info("Прогресс генерации фрактального пламени: {}%", currentPercentage);
                    currentPercentage += 10;
                    stepIteration += step;

                    if (currentPercentage >= 100) {
                        LOGGER.info("Прогресс генерации фрактального пламени: 100%");
                        break;
                    }
                }

                Thread.sleep(INTERVAL);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.debug("ProgressLogger прерван");
        }
    }
}
