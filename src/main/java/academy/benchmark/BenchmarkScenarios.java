package academy.benchmark;

import academy.models.config.Configuration;
import academy.models.config.Size;
import academy.utils.Validator;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkScenarios {

    private BenchmarkScenarios() {}

    private static final int BASE_ITERATIONS = 10_000_000;
    private static final int[] THREADS = {1, 2, 4, 8, 16};
    private static final int[][] SIZES = {{640, 360}, {1280, 720}, {1920, 1080}, {2560, 1440}};
    private static final String[] FUNCTIONS = {"linear", "sinusoidal", "spherical", "swirl", "horseshoe"};
    private static final int[] ITERATIONS = {1_000_000, 5_000_000, 10_000_000, 25_000_000, 50_000_000};

    public static Configuration base() {
        return Configuration.builder()
            .width(1920)
            .height(1080)
            .seed(5.1234)
            .iterationCount(BASE_ITERATIONS)
            .outputPath("bench_out.png")
            .threadCount(1)
            .affineParameters("<0.8>,<-0.1>,<0.0>,<0.1>,<0.7>,<0.0>")
            .functions("spherical:1.0")
            .gamma(2.2)
            .symmetryLevel(1)
            .build();
    }

    public static List<BenchmarkRunner.Case> threadScalingFixedSize() {
        List<BenchmarkRunner.Case> cases = new ArrayList<>();
        for (int t : THREADS) {
            Configuration c = copy(base());
            c.setThreadCount(t);
            cases.add(new BenchmarkRunner.Case(
                "fixed-size threads=" + t, "spherical", c));
        }
        return cases;
    }

    public static List<BenchmarkRunner.Case> resolutionThreadMatrix() {
        List<BenchmarkRunner.Case> cases = new ArrayList<>();
        for (int[] s : SIZES) {
            for (int t : THREADS) {
                Configuration c = copy(base());
                c.setSize(new Size(s[0], s[1]));
                c.setThreadCount(t);
                cases.add(new BenchmarkRunner.Case(
                    String.format("size=%dx%d threads=%d", s[0], s[1], t), "spherical", c));
            }
        }
        return cases;
    }

    public static List<BenchmarkRunner.Case> functionThreadMatrix() {
        List<BenchmarkRunner.Case> cases = new ArrayList<>();
        for (String f : FUNCTIONS) {
            for (int t : THREADS) {
                Configuration c = copy(base());
                c.setFunctions(Validator.validateAndGetFunctions(f + ":1.0"));
                c.setThreadCount(t);
                cases.add(new BenchmarkRunner.Case(
                    String.format("func=%s threads=%d", f, t), f, c));
            }
        }
        return cases;
    }

    public static List<BenchmarkRunner.Case> iterationThreadMatrix() {
        List<BenchmarkRunner.Case> cases = new ArrayList<>();
        for (int iter : ITERATIONS) {
            for (int t : THREADS) {
                Configuration c = copy(base());
                c.setIterationCount(iter);
                c.setThreadCount(t);
                cases.add(new BenchmarkRunner.Case(
                    String.format("iter=%d threads=%d", iter, t), "spherical", c));
            }
        }
        return cases;
    }

    private static Configuration copy(Configuration b) {
        Configuration c = new Configuration();
        c.setSize(new Size(b.getWidth(), b.getHeight()));
        c.setSeed(b.getSeed());
        c.setIterationCount(b.getIterationCount());
        c.setOutputPath(b.getOutputPath());
        c.setThreadCount(b.getThreadCount());
        c.setAffineParameters(b.getAffineParameters());
        c.setFunctions(b.getFunctions());
        c.setGamma(b.getGamma());
        c.setSymmetryLevel(b.getSymmetryLevel());
        return c;
    }
}
