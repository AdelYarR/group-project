package academy.utils;

import academy.models.TransformationFunction;
import java.util.List;
import java.util.Random;

public class FunctionUtil {

    private FunctionUtil() {}

    public static int getRandomFunctionIndex(Random random, List<TransformationFunction> functions) {
        double allWeights = functions.stream()
            .mapToDouble(TransformationFunction::weight)
            .sum();

        double randomValue = random.nextDouble() * allWeights;

        double cumulative = 0;
        for (int i = 0; i < functions.size(); i++) {
            cumulative += functions.get(i).weight();
            if (randomValue < cumulative) {
                return i;
            }
        }

        return functions.size() - 1;
    }
}
