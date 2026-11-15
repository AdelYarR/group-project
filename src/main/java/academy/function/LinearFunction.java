package academy.function;

import academy.models.Point;

public class LinearFunction implements Function {

    public Point apply(Point point) {
        return new Point(point.x(), point.y());
    }
}
