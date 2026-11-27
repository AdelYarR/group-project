package academy;

import academy.exceptions.ExceptionHandler;
import academy.models.config.Configuration;
import academy.models.image.Image;
import academy.service.FractalFlameService;
import academy.utils.ConfigLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "Log Analyzer", version = "Example 1.0", mixinStandardHelpOptions = true)
public class Application implements Runnable {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(Application.class);

    @Option(names = {"-w", "--width"})
    private int width = 1920;

    @Option(names = {"-h", "--height"})
    private int height = 1080;

    @Option(names = {"--seed"})
    private double seed = 5.1234;

    @Option(names = {"-i", "--iteration-count"})
    private int iterationCount = 2500;

    @Option(names = {"-o", "--output-path"})
    private String outputPath = "result.png";

    @Option(names = {"-t", "--threads"})
    private int threadCount = 1;

    @Option(names = {"-ap", "--affine-params"})
    private String affineParametersString =
        "<0.8>,<-0.1>,<0.0>,<0.1>,<0.7>,<0.0>";

    @Option(names = {"-f", "--functions"})
    private String functionsString = "spherical:1.0";

    @Option(names = "--config")
    private String configPath;

    @Option(names = {"-g", "--gamma-correction"})
    private double gamma = 2.2;

    @Option(names = {"-s", "--symmetry-level"})
    private int symmetryLevel = 1;

    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new Application())
            .setParameterExceptionHandler(new ExceptionHandler());

        int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        try {
            Configuration config;

            if (configPath != null) {
                config = ConfigLoad.loadFromJson(configPath);
            } else {
                config = Configuration.builder()
                    .width(width)
                    .height(height)
                    .seed(seed)
                    .iterationCount(iterationCount)
                    .outputPath(outputPath)
                    .threadCount(threadCount)
                    .affineParameters(affineParametersString)
                    .functions(functionsString)
                    .gamma(gamma)
                    .symmetryLevel(symmetryLevel)
                    .build();
            }

            LOGGER.info(
                "Запуск программы со следующими параметрами: width: {}, height: {}, seed: {}, "
                    + "iteration count: {}, threads: {}, affine parameters: {}, "
                    + "functions: {}, gamma: {}, symmetry level: {}.",
                config.getSize().getWidth(),
                config.getSize().getHeight(),
                config.getSeed(),
                config.getIterationCount(),
                config.getThreadCount(),
                config.getAffineParameters(),
                config.getFunctions(),
                config.getGamma(),
                config.getSymmetryLevel()
            );

            // Замер времени генерации фрактала
            long generationStartTime = System.nanoTime();

            FractalFlameService service = new FractalFlameService(config);
            Image image = service.startAndGet();

            long generationEndTime = System.nanoTime();

            double generationTime =
                (generationEndTime - generationStartTime) / 1_000_000_000.0;

            LOGGER.info("Время генерации фрактала: {} сек.",
                String.format("%.3f", generationTime));

            LOGGER.info("========================================");
            LOGGER.info("РЕЗУЛЬТАТЫ ИЗМЕРЕНИЯ ВРЕМЕНИ");
            LOGGER.info("Генерация фрактала: {} сек.",
                String.format("%.3f", generationTime));
            LOGGER.info("========================================");

        } catch (Exception e) {
            throw new CommandLine.ParameterException(
                new CommandLine(this),
                e.getMessage()
            );
        }
    }
}
