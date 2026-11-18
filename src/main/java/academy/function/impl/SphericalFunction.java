package academy.function.impl;

import academy.function.Function;
import academy.models.image.Point;

public class SphericalFunction implements Function {

    public Point apply(Point point) {
        double affineX = point.x();
        double affineY = point.y();

        double r = Math.sqrt(affineX * affineX + affineY * affineY);

        if (r == 0) {
            return new Point(0, 0);
        }

        double newX = 1 / (r * r) * affineX;
        double newY = 1 / (r * r) * affineY;

        return new Point(newX, newY);
    }
}
