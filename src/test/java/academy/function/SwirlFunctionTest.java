package academy.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.function.impl.SwirlFunction;
import academy.models.image.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SwirlFunctionTest {

    @Test
    @DisplayName("Точка в начале координат")
    void apply_whenAtOrigin() {
        Function function = new SwirlFunction();

        Point input = new Point(0, 0);
        Point result = function.apply(input);

        assertEquals(0.0, result.x(), 0.0001);
        assertEquals(0.0, result.y(), 0.0001);
    }

    @Test
    @DisplayName("Вращение вокруг начала координат")
    void apply_whenRotation() {
        Function function = new SwirlFunction();

        Point input = new Point(1, 0);
        Point result = function.apply(input);

        double r = 1.0;
        double sinR2 = Math.sin(r * r);
        double cosR2 = Math.cos(r * r);
        double expectedX = 1 * sinR2 - 0 * cosR2;
        double expectedY = 1 * cosR2 + 0 * sinR2;

        assertEquals(expectedX, result.x(), 0.0001);
        assertEquals(expectedY, result.y(), 0.0001);
    }
}
