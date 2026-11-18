package academy.function.impl;

import academy.function.Function;
import academy.models.image.Point;

public class SwirlFunction implements Function {

    public Point apply(Point point) {
        double affineX = point.x();
        double affineY = point.y();

        double r = Math.sqrt(affineX * affineX + affineY * affineY);

        if (r == 0) {
            return new Point(0, 0);
        }

        double newX = affineX * Math.sin(r * r) - affineY * Math.cos(r * r);
        double newY = affineX * Math.cos(r * r) + affineY * Math.sin(r * r);

        return new Point(newX, newY);
    }
}
