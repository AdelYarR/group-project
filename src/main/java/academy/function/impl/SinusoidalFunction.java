package academy.function.impl;

import academy.function.Function;
import academy.models.image.Point;

public class SinusoidalFunction implements Function {

    public Point apply(Point point) {
        double affineX = point.x();
        double affineY = point.y();

        double newX = Math.sin(affineX);
        double newY = Math.sin(affineY);

        return new Point(newX, newY);
    }
}
