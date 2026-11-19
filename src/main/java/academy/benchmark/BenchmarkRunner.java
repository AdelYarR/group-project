package academy.benchmark;

import academy.models.config.Configuration;
import academy.models.image.Image;
import academy.service.FractalFlameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BenchmarkRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkRunner.class);

    private static final int WARMUP_RUNS = 3;
    private static final int MEASURE_RUNS = 7;

    public record Case(String label, String function, Configuration config) {}

    public static List<BenchmarkResult> runAll(List<Case> cases) {
        List<BenchmarkResult> results = new ArrayList<>();

        for (Case c : cases) {
            for (int i = 0; i < WARMUP_RUNS; i++) {
                renderOnce(c.config());
            }
            ProgressLoggerReaper.reap();

            long[] times = new long[MEASURE_RUNS];
            for (int i = 0; i < MEASURE_RUNS; i++) {
                System.gc();
                times[i] = renderOnce(c.config());
            }
            ProgressLoggerReaper.reap();

            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;
            long sum = 0L;
            for (long t : times) {
                if (t < min) min = t;
                if (t > max) max = t;
                sum += t;
            }
            long avg = sum / times.length;

            long[] sorted = times.clone();
            Arrays.sort(sorted);
            long median = sorted[sorted.length / 2];

            BenchmarkResult r = new BenchmarkResult(
                c.label(),
                c.config().getThreadCount(),
                c.config().getWidth(),
                c.config().getHeight(),
                c.function(),
                c.config().getIterationCount(),
                min, max, avg, median
            );
            results.add(r);

            LOGGER.info("Замер [{}]: iter={}, min={} мс, max={} мс, avg={} мс, median={} мс",
                r.label(),
                r.iterationCount(),
                String.format("%.1f", r.getMinMs()),
                String.format("%.1f", r.getMaxMs()),
                String.format("%.1f", r.getAvgMs()),
                String.format("%.1f", r.getMedianMs()));
        }

        return results;
    }

    private static long renderOnce(Configuration config) {
        FractalFlameService service = new FractalFlameService(config);
        long t0 = System.nanoTime();
        Image image;
        try {
            image = service.startAndGet();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("benchmark interrupted", e);
        }
        long t1 = System.nanoTime();
        if (image == null) {
            throw new IllegalStateException("image is null");
        }
        return t1 - t0;
    }
}
