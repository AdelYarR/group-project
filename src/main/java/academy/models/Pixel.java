package academy.models;

public class Pixel {
    private Color color;
    private int counter;
    private double normal;

    public Pixel(Color color) {
        this.color = color;
        this.counter = 0;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public double getNormal() {
        return normal;
    }

    public void setNormal(double normal) {
        this.normal = normal;
    }
}
