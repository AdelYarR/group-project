package academy.function.impl;

import academy.function.Function;
import academy.models.image.Point;

public class LinearFunction implements Function {

    public Point apply(Point point) {
        return new Point(point.x(), point.y());
    }
}
