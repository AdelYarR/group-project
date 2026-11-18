package academy.models.config;

import academy.utils.Validator;
import java.util.List;

public class ConfigurationBuilder {

    private Configuration config;

    public ConfigurationBuilder() {
        this.config = new Configuration();
    }

    public ConfigurationBuilder width(int width) {
        Validator.validatePositive(width);
        config.getSize().setWidth(width);
        return this;
    }

    public ConfigurationBuilder height(int height) {
        Validator.validatePositive(height);
        config.getSize().setHeight(height);
        return this;
    }

    public ConfigurationBuilder seed(double seed) {
        Validator.validatePositive(seed);
        config.setSeed(seed);
        return this;
    }

    public ConfigurationBuilder outputPath(String outputPath) {
        Validator.validatePath(outputPath);
        config.setOutputPath(outputPath);
        return this;
    }

    public ConfigurationBuilder threadCount(int threadCount) {
        Validator.validatePositive(threadCount);
        config.setThreadCount(threadCount);
        return this;
    }

    public ConfigurationBuilder affineParameters(String affineParametersString) {
        List<AffineParameters> validatedAffineParams = Validator.validateAndGetAffineParams(affineParametersString);
        config.setAffineParameters(validatedAffineParams);
        return this;
    }

    public ConfigurationBuilder functions(String functionsString) {
        List<TransformationFunction> validatedFunctions = Validator.validateAndGetFunctions(functionsString);
        config.setFunctions(validatedFunctions);
        return this;
    }

    public ConfigurationBuilder iterationCount(int iterationCount) {
        Validator.validatePositive(iterationCount);
        config.setIterationCount(iterationCount);
        return this;
    }

    public ConfigurationBuilder gamma(double gamma) {
        Validator.validatePositive(gamma);
        config.setGamma(gamma);
        return this;
    }

    public ConfigurationBuilder symmetryLevel(int symmetryLevel) {
        Validator.validatePositive(symmetryLevel);
        config.setSymmetryLevel(symmetryLevel);
        return this;
    }

    public Configuration build() {
        return config;
    }
}
