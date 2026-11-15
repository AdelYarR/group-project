package academy.models;

import academy.utils.Validator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Configuration {

    private static final int DEFAULT_WIDTH = 1920;
    private static final int DEFAULT_HEIGHT = 1080;
    private static final Double DEFAULT_SEED = 5.1234;
    private static final int DEFAULT_ITERATION_COUNT = 2500;
    private static final String DEFAULT_OUTPUT_PATH = "result.png";
    private static final int DEFAULT_THREAD_COUNT = 1;
    private static final double DEFAULT_GAMMA = 2.2;

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

    @JsonProperty(value = "affine_params", required = true)
    private List<AffineParameters> affineParameters;

    @JsonProperty(value = "functions", required = true)
    private List<TransformationFunction> functions;

    @JsonProperty(value="gamma_correction")
    private double gamma;

    public Configuration() {
        this.size = new Size();
        this.seed = DEFAULT_SEED;
        this.iterationCount = DEFAULT_ITERATION_COUNT;
        this.outputPath = DEFAULT_OUTPUT_PATH;
        this.threadCount = DEFAULT_THREAD_COUNT;
        this.gamma = DEFAULT_GAMMA;
    }

    public Configuration(Size size, double seed, int iterationCount, String outputPath, int threadCount,
                         List<AffineParameters> affineParameters, List<TransformationFunction> functions,
                         double gamma) {
        this.size = size;
        this.seed = seed;
        this.iterationCount = iterationCount;
        this.outputPath = outputPath;
        this.threadCount = threadCount;
        this.affineParameters = affineParameters;
        this.functions = functions;
        this.gamma = gamma;
    }

    public static class Size {

        @JsonProperty("width")
        private int width;

        @JsonProperty("height")
        private int height;

        public Size() {
            this.width = DEFAULT_WIDTH;
            this.height = DEFAULT_HEIGHT;
        }

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public static class Builder {
        private Size size;
        private double seed;
        private int iterationCount;
        private String outputPath;
        private int threadCount;
        private List<AffineParameters> affineParameters;
        private List<TransformationFunction> functions;
        private double gamma;

        public Builder() {
            this.size = new Size();
            this.seed = DEFAULT_SEED;
            this.iterationCount = DEFAULT_ITERATION_COUNT;
            this.outputPath = DEFAULT_OUTPUT_PATH;
            this.threadCount = DEFAULT_THREAD_COUNT;
            this.gamma = DEFAULT_GAMMA;
        }

        public Builder width(int width) {
            Validator.validatePositive(width);
            size.setWidth(width);
            return this;
        }

        public Builder height(int height) {
            Validator.validatePositive(height);
            size.setHeight(height);
            return this;
        }

        public Builder seed(double seed) {
            Validator.validatePositive(seed);
            this.seed = seed;
            return this;
        }

        public Builder outputPath(String outputPath) {
            Validator.validatePath(outputPath);
            this.outputPath = outputPath;
            return this;
        }

        public Builder threadCount(int threadCount) {
            Validator.validatePositive(threadCount);
            this.threadCount = threadCount;
            return this;
        }

        public Builder affineParameters(String affineParametersString) {
            this.affineParameters = Validator.validateAndGetAffineParams(affineParametersString);
            return this;
        }

        public Builder functions(String functionsString) {
            this.functions = Validator.validateAndGetFunctions(functionsString);
            return this;
        }

        public Builder iterationCount(int iterationCount) {
            Validator.validatePositive(iterationCount);
            this.iterationCount = iterationCount;
            return this;
        }

        public Builder gamma(double gamma) {
            Validator.validatePositive(gamma);
            this.gamma = gamma;
            return this;
        }

        public Configuration build() {
            return new Configuration(size, seed, iterationCount, outputPath,
                threadCount, affineParameters, functions, gamma);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Size getSize() {
        return size;
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
}
