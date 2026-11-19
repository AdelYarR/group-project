package academy.benchmark;

public record BenchmarkResult(String label, int threadCount, int width, int height, String function, int iterationCount,
                              long minNanos, long maxNanos, long avgNanos, long medianNanos) {

    public double getMinMs() {
        return minNanos / 1_000_000.0;
    }

    public double getMaxMs() {
        return maxNanos / 1_000_000.0;
    }

    public double getAvgMs() {
        return avgNanos / 1_000_000.0;
    }

    public double getMedianMs() {
        return medianNanos / 1_000_000.0;
    }
}
