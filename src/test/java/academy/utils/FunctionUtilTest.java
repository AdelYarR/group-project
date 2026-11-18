package academy.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.models.config.TransformationFunction;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FunctionUtilTest {

    @Test
    @DisplayName("Одна функция, всегда индекс 0")
    void singleFunction_whenAlwaysZero() {
        List<TransformationFunction> functions = List.of(new TransformationFunction("linear", 1.0));
        Random random = new Random(42);

        for (int i = 0; i < 5; i++) {
            assertEquals(0, FunctionUtil.getRandomFunctionIndex(random, functions));
        }
    }

    @Test
    @DisplayName("Равные веса, равномерное распределение")
    void equalWeights_whenUniformDistribution() {
        List<TransformationFunction> functions = List.of(
                new TransformationFunction("a", 1.0),
                new TransformationFunction("b", 1.0),
                new TransformationFunction("c", 1.0));

        Random random = new Random(123);
        int[] counts = new int[3];

        for (int i = 0; i < 300; i++) {
            counts[FunctionUtil.getRandomFunctionIndex(random, functions)]++;
        }

        for (int count : counts) {
            assertTrue(count > 80 && count < 120);
        }
    }

    @Test
    @DisplayName("Разные веса, пропорциональное распределение")
    void differentWeights_whenProportionalDistribution() {
        List<TransformationFunction> functions =
                List.of(new TransformationFunction("small", 1.0), new TransformationFunction("large", 9.0));

        Random random = new Random(456);
        int smallCount = 0;

        for (int i = 0; i < 100; i++) {
            if (FunctionUtil.getRandomFunctionIndex(random, functions) == 0) {
                smallCount++;
            }
        }

        assertTrue(smallCount > 5 && smallCount < 15);
    }

    @Test
    @DisplayName("Нулевой вес, никогда не выбирается")
    void zeroWeight_whenNeverSelected() {
        List<TransformationFunction> functions =
                List.of(new TransformationFunction("normal", 1.0), new TransformationFunction("zero", 0.0));

        Random random = new Random(789);

        for (int i = 0; i < 50; i++) {
            assertEquals(0, FunctionUtil.getRandomFunctionIndex(random, functions));
        }
    }

    @Test
    @DisplayName("Пустой список бросаем исключение")
    void emptyList_throwsException() {
        assertThrows(Exception.class, () -> FunctionUtil.getRandomFunctionIndex(new Random(), List.of()));
    }
}
