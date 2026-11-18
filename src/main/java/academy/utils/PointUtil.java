package academy.utils;

import academy.models.image.Point;

public class PointUtil {

    private PointUtil() {}

    public static Point normalizePoint(
            Point point, double maxX, double minX, double maxY, double minY, int width, int height) {
        // В массиве отчёт начинается с 0, поэтому отнимаем единицу у ширины и высоты
        int w = width - 1;
        int h = height - 1;

        double oldX = point.x();
        double oldY = point.y();

        double xRatio = (maxX - oldX) / (maxX - minX);
        int newX = width - (int) Math.floor(xRatio * width);

        double yRatio = (maxY - oldY) / (maxY - minY);
        int newY = height - (int) Math.floor(yRatio * height);

        newX = Math.clamp(newX, 0, w);
        newY = Math.clamp(newY, 0, h);

        return new Point(newX, newY);
    }

    public static Point rotatePoint(Point point, double angleDegrees) {
        double angleRad = Math.toRadians(angleDegrees);

        double x = point.x();
        double y = point.y();

        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);

        double newX = x * cos - y * sin;
        double newY = x * sin + y * cos;

        return new Point(newX, newY);
    }
}
