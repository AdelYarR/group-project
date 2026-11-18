package academy.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.function.impl.SphericalFunction;
import academy.models.image.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SphericalFunctionTest {

    @Test
    @DisplayName("Точка на единичной окружности")
    void apply_whenOnUnitCircle() {
        Function function = new SphericalFunction();

        Point input = new Point(1, 0);
        Point result = function.apply(input);

        assertEquals(1.0, result.x(), 0.0001);
        assertEquals(0.0, result.y(), 0.0001);
    }

    @Test
    @DisplayName("Точка в начале координат")
    void apply_whenAtOrigin() {
        Function function = new SphericalFunction();

        Point input = new Point(0, 0);
        Point result = function.apply(input);

        assertEquals(0.0, result.x(), 0.0001);
        assertEquals(0.0, result.y(), 0.0001);
    }

    @Test
    @DisplayName("Произвольная точка")
    void apply_whenArbitraryPoint() {
        Function function = new SphericalFunction();

        Point input = new Point(2, 3);
        Point result = function.apply(input);

        double r = Math.sqrt(2 * 2 + 3 * 3);
        double expectedX = 2 / (r * r);
        double expectedY = 3 / (r * r);

        assertEquals(expectedX, result.x(), 0.0001);
        assertEquals(expectedY, result.y(), 0.0001);
    }
}
