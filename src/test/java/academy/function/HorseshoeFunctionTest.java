package academy.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.function.impl.HorseshoeFunction;
import academy.models.image.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HorseshoeFunctionTest {

    @Test
    @DisplayName("Точка в начале координат")
    void apply_whenAtOrigin() {
        Function function = new HorseshoeFunction();

        Point input = new Point(0, 0);
        Point result = function.apply(input);

        assertEquals(0.0, result.x(), 0.0001);
        assertEquals(0.0, result.y(), 0.0001);
    }

    @Test
    @DisplayName("Преобразование подковы")
    void apply_whenHorseshoeTransformation() {
        Function function = new HorseshoeFunction();

        Point input = new Point(1, 1);
        Point result = function.apply(input);

        double r = Math.sqrt(2);
        double expectedX = 0 / r;
        double expectedY = 2 / r;

        assertEquals(expectedX, result.x(), 0.0001);
        assertEquals(expectedY, result.y(), 0.0001);
    }
}
