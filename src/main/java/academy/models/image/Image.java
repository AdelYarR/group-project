package academy.models.image;

public class Image {

    private int width;
    private int height;
    private Pixel[][] pixels;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new Pixel[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Pixel[][] getPixels() {
        return pixels;
    }
}
