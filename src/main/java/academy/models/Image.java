package academy.models;

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

    public void printInfo() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixel pixel = pixels[y][x];
                if (pixel != null) {
                    Color color = pixel.getColor();
                    System.out.println(String.format("Pixel: %d,%d; color: %f,%f,%f", x, y, color.getRed(), color.getGreen(), color.getBlue()));
                }
            }
        }
    }
}
