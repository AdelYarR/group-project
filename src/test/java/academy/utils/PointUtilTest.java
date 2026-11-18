package academy.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.models.image.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PointUtilTest {

    @Test
    @DisplayName("Точка в центре диапазона, центр изображения")
    void normalizePoint_whenCenterPoint_returnsImageCenter() {
        Point point = new Point(0, 0);
        Point result = PointUtil.normalizePoint(point, 1.777, -1.777, 1, -1, 1920, 1080);

        assertEquals(960, result.x(), 0.5);
        assertEquals(540, result.y(), 0.5);
    }

    @Test
    @DisplayName("Точка в правом верхнем углу → правый верхний пиксель")
    void normalizePoint_whenTopRightCorner_returnsTopRightPixel() {
        Point point = new Point(1.777, 1);
        Point result = PointUtil.normalizePoint(point, 1.777, -1.777, 1, -1, 1920, 1080);

        System.out.println("Result: " + result.x() + ", " + result.y());
        assertEquals(1919, result.x(), 0.5);
    }

    @Test
    @DisplayName("Точка в левом нижнем углу, левый нижний пиксель")
    void normalizePoint_whenBottomLeftCorner_returnsBottomLeftPixel() {
        Point point = new Point(-1.777, -1);
        Point result = PointUtil.normalizePoint(point, 1.777, -1.777, 1, -1, 1920, 1080);

        assertEquals(0, result.x(), 0.5);
    }

    @Test
    @DisplayName("Минимальное изображение 1x1, возвращает (0,0)")
    void normalizePoint_whenMinimalImage_returnsSinglePixel() {
        Point point = new Point(0.5, 0.3);
        Point result = PointUtil.normalizePoint(point, 1, -1, 1, -1, 1, 1);

        assertEquals(0, result.x());
        assertEquals(0, result.y());
    }

    @Test
    @DisplayName("Вращение на 0 градусов, та же точка")
    void rotatePoint_whenZeroDegrees_returnsSamePoint() {
        Point point = new Point(5, 3);
        Point result = PointUtil.rotatePoint(point, 0);

        assertEquals(5, result.x(), 0.0001);
        assertEquals(3, result.y(), 0.0001);
    }

    @Test
    @DisplayName("Вращение на 90 градусов, (-y, x)")
    void rotatePoint_whenNinetyDegrees_rotatesCorrectly() {
        Point point = new Point(5, 3);
        Point result = PointUtil.rotatePoint(point, 90);

        assertEquals(-3, result.x(), 0.0001);
        assertEquals(5, result.y(), 0.0001);
    }

    @Test
    @DisplayName("Вращение на 180 градусов, (-x, -y)")
    void rotatePoint_whenOneEightyDegrees_rotatesCorrectly() {
        Point point = new Point(5, 3);
        Point result = PointUtil.rotatePoint(point, 180);

        assertEquals(-5, result.x(), 0.0001);
        assertEquals(-3, result.y(), 0.0001);
    }

    @Test
    @DisplayName("Вращение на 360 градусов, та же точка")
    void rotatePoint_whenFullCircle_returnsSamePoint() {
        Point point = new Point(5, 3);
        Point result = PointUtil.rotatePoint(point, 360);

        assertEquals(5, result.x(), 0.0001);
        assertEquals(3, result.y(), 0.0001);
    }

    @Test
    @DisplayName("Вращение точки в начале координат, остаётся (0,0)")
    void rotatePoint_whenOrigin_returnsOrigin() {
        Point point = new Point(0, 0);
        Point result = PointUtil.rotatePoint(point, 45);

        assertEquals(0, result.x(), 0.0001);
        assertEquals(0, result.y(), 0.0001);
    }

    @Test
    @DisplayName("Вращение на отрицательный угол → вращение по часовой стрелке")
    void rotatePoint_negativeAngle_rotatesClockwise() {
        Point point = new Point(5, 0);
        Point result = PointUtil.rotatePoint(point, -90);

        assertEquals(0, result.x(), 0.0001);
        assertEquals(-5, result.y(), 0.0001);
    }
}
