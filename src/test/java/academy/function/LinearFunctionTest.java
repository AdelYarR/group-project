package academy.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.function.impl.LinearFunction;
import academy.models.image.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LinearFunctionTest {

    @Test
    @DisplayName("Функция должна возвращать те же координаты")
    void apply_shouldReturnSamePoint() {
        Function function = new LinearFunction();
        Point input = new Point(1.5, -2.3);
        Point result = function.apply(input);

        assertEquals(input.x(), result.x(), 0.0001);
        assertEquals(input.y(), result.y(), 0.0001);
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "1.5, 2.5", "-3.2, 4.7", "0.001, -0.002"})
    @DisplayName("Различные входные точки")
    void apply_whenVariousInputs_shouldReturnSamePoint(double x, double y) {
        Function function = new LinearFunction();
        Point input = new Point(x, y);
        Point result = function.apply(input);

        assertEquals(x, result.x(), 0.0001);
        assertEquals(y, result.y(), 0.0001);
    }
}
