package academy.benchmark;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BenchmarkMain {

    public static void main(String[] args) {
        List<BenchmarkRunner.Case> all = new ArrayList<>();
        all.addAll(BenchmarkScenarios.threadScalingFixedSize());
        all.addAll(BenchmarkScenarios.resolutionThreadMatrix());
        all.addAll(BenchmarkScenarios.functionThreadMatrix());
        all.addAll(BenchmarkScenarios.iterationThreadMatrix());

        List<BenchmarkResult> results = BenchmarkRunner.runAll(all);

        Path reportFile = Path.of(args.length > 0 ? args[0] : "benchmark-report.txt");
        new BenchmarkReporter(reportFile).write(results);
    }
}
