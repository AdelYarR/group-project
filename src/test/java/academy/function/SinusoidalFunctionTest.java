package academy.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.function.impl.SinusoidalFunction;
import academy.models.image.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SinusoidalFunctionTest {

    @Test
    @DisplayName("Функция должна применить синуса к координатам")
    void apply_shouldApplySinus() {
        Function function = new SinusoidalFunction();
        Point input = new Point(Math.PI / 2, 0);
        Point result = function.apply(input);

        assertEquals(1.0, result.x(), 0.0001);
        assertEquals(0.0, result.y(), 0.0001);
    }

    @Test
    @DisplayName("Точка в начале координат")
    void apply_whenAtOrigin() {
        Function function = new SinusoidalFunction();
        Point input = new Point(0, 0);
        Point result = function.apply(input);

        assertEquals(0.0, result.x(), 0.0001);
        assertEquals(0.0, result.y(), 0.0001);
    }
}
