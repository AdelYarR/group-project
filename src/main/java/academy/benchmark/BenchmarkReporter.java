package academy.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

public class BenchmarkReporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkReporter.class);

    private final Path outputFile;

    public BenchmarkReporter(Path outputFile) {
        this.outputFile = outputFile;
    }

    public void write(List<BenchmarkResult> results) {
        StringBuilder sb = new StringBuilder();

        sb.append("Benchmark report\n");
        sb.append("Generated: ")
            .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .append("\n");
        sb.append("CPU cores: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
        sb.append("JVM: ").append(System.getProperty("java.version")).append("\n");
        sb.append("Max heap: ").append(Runtime.getRuntime().maxMemory() / (1024 * 1024)).append(" MB\n");
        sb.append("Warmup runs: 3, Measure runs: 7\n");
        sb.append("\n");

        sb.append("RESULTS\n");
        appendTable(sb, results, r -> true);
        sb.append("\n");

        sb.append("Experiment #1: fixed size (1920x1080, spherical, iter=10_000_000)\n");
        appendTable(sb, results, r -> r.label().startsWith("fixed-size"));
        sb.append("\n");

        for (int[] s : new int[][]{{640, 360}, {1280, 720}, {1920, 1080}, {2560, 1440}}) {
            String prefix = String.format("size=%dx%d", s[0], s[1]);
            sb.append("Experiment #2: ").append(prefix).append("\n");
            appendTable(sb, results, r -> r.label().startsWith(prefix));
            sb.append("\n");
        }

        for (String f : new String[]{"linear", "sinusoidal", "spherical", "swirl", "horseshoe"}) {
            String prefix = "func=" + f;
            sb.append("Experiment #3: ").append(prefix).append(" (1920x1080, iter=10_000_000)\n");
            appendTable(sb, results, r -> r.label().startsWith(prefix));
            sb.append("\n");
        }

        for (int iter : new int[]{10_000, 50_000, 100_000, 250_000, 500_000}) {
            String prefix = "iter=" + iter;
            sb.append("Experiment #4: ").append(prefix).append(" (1920x1080, spherical)\n");
            appendTable(sb, results, r -> r.label().startsWith(prefix));
            sb.append("\n");
        }

        try {
            if (outputFile.getParent() != null) {
                Files.createDirectories(outputFile.getParent());
            }
            Files.writeString(outputFile, sb.toString(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            LOGGER.info("Отчёт сохранён: {}", outputFile.toAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("Не удалось сохранить отчёт в {}", outputFile, e);
            throw new RuntimeException(e);
        }
    }

    private void appendTable(StringBuilder sb, List<BenchmarkResult> all, Predicate<BenchmarkResult> filter) {
        String header = String.format(
            "%-28s %-8s %-12s %-10s %-8s %-10s %-10s %-10s %-10s %-10s %-10s",
            "label", "threads", "size", "func", "iter", "min_ms", "max_ms", "avg_ms", "median_ms", "speedup", "efficiency");
        sb.append(header).append("\n");
        sb.append("-".repeat(header.length())).append("\n");

        for (BenchmarkResult r : all) {
            if (!filter.test(r)) continue;
            sb.append(formatRow(all, r)).append("\n");
        }
    }

    private String formatRow(List<BenchmarkResult> all, BenchmarkResult r) {
        BenchmarkResult base = findBase(all, r);
        String speedup;
        String efficiency;
        if (base == null) {
            speedup = "n/a";
            efficiency = "n/a";
        } else {
            double s = base.getMedianMs() / r.getMedianMs();
            double e = s / r.threadCount();
            speedup = String.format("%.2f", s);
            efficiency = String.format("%.2f", e);
        }

        return String.format(
            "%-28s %-8d %-12s %-10s %-8d %-10s %-10s %-10s %-10s %-10s %-10s",
            r.label(),
            r.threadCount(),
            r.width() + "x" + r.height(),
            r.function(),
            r.iterationCount(),
            String.format("%.1f", r.getMinMs()),
            String.format("%.1f", r.getMaxMs()),
            String.format("%.1f", r.getAvgMs()),
            String.format("%.1f", r.getMedianMs()),
            speedup,
            efficiency);
    }

    private static BenchmarkResult findBase(List<BenchmarkResult> results, BenchmarkResult r) {
        if (r.threadCount() == 1) return r;
        for (BenchmarkResult cand : results) {
            if (cand.threadCount() == 1
                && cand.width() == r.width()
                && cand.height() == r.height()
                && cand.function().equals(r.function())
                && cand.iterationCount() == r.iterationCount()) {
                return cand;
            }
        }
        return null;
    }
}
