package academy.models.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Configuration {

    private static final Double DEFAULT_SEED = 5.1234;
    private static final int DEFAULT_ITERATION_COUNT = 2500;
    private static final String DEFAULT_OUTPUT_PATH = "result.png";
    private static final int DEFAULT_THREAD_COUNT = 1;
    private static final List<AffineParameters> DEFAULT_AFFINE =
            List.of(new AffineParameters(0.8, -0.1, 0.0, 0.1, 0.7, 0.0));
    private static final List<TransformationFunction> DEFAULT_FUNCTION =
            List.of(new TransformationFunction("spherical", 1.0));
    private static final double DEFAULT_GAMMA = 2.2;
    private static final int DEFAULT_SYMMETRY_LEVEL = 1;

    @JsonProperty("size")
    private Size size;

    @JsonProperty("seed")
    private Double seed;

    @JsonProperty("iteration_count")
    private int iterationCount;

    @JsonProperty("output_path")
    private String outputPath;

    @JsonProperty("threads")
    private int threadCount;

    @JsonProperty(value = "affine_params")
    private List<AffineParameters> affineParameters;

    @JsonProperty(value = "functions")
    private List<TransformationFunction> functions;

    @JsonProperty(value = "gamma_correction")
    private double gamma;

    @JsonProperty(value = "symmetry_level")
    private int symmetryLevel;

    public Configuration() {
        this.size = new Size();
        this.seed = DEFAULT_SEED;
        this.iterationCount = DEFAULT_ITERATION_COUNT;
        this.outputPath = DEFAULT_OUTPUT_PATH;
        this.threadCount = DEFAULT_THREAD_COUNT;
        this.functions = DEFAULT_FUNCTION;
        this.affineParameters = DEFAULT_AFFINE;
        this.gamma = DEFAULT_GAMMA;
        this.symmetryLevel = DEFAULT_SYMMETRY_LEVEL;
    }

    public static ConfigurationBuilder builder() {
        return new ConfigurationBuilder();
    }

    public Size getSize() {
        return size;
    }

    public int getWidth() {
        return size.getWidth();
    }

    public int getHeight() {
        return size.getHeight();
    }

    public Double getSeed() {
        return seed;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public List<AffineParameters> getAffineParameters() {
        return affineParameters;
    }

    public List<TransformationFunction> getFunctions() {
        return functions;
    }

    public double getGamma() {
        return gamma;
    }

    public int getSymmetryLevel() {
        return symmetryLevel;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public void setSeed(Double seed) {
        this.seed = seed;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public void setAffineParameters(List<AffineParameters> affineParameters) {
        this.affineParameters = affineParameters;
    }

    public void setFunctions(List<TransformationFunction> functions) {
        this.functions = functions;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public void setSymmetryLevel(int symmetryLevel) {
        this.symmetryLevel = symmetryLevel;
    }
}
