package academy.service;

import academy.models.FractalRenderState;
import academy.models.image.Color;
import academy.models.image.Image;
import academy.models.image.Pixel;

public class ImageCreator {

    private ImageCreator() {}

    public static Image createImageFromState(FractalRenderState state, double scale, int width, int height) {
        Image image = new Image(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                if (state.getCounterArray().get(index) != 0) {
                    int oldRed = state.getRedColorArray().get(index);
                    int oldGreen = state.getGreenColorArray().get(index);
                    int oldBlue = state.getBlueColorArray().get(index);

                    int r = (int) (oldRed / scale * 255);
                    int g = (int) (oldGreen / scale * 255);
                    int b = (int) (oldBlue / scale * 255);

                    Pixel pixel = new Pixel(new Color(r, g, b));
                    pixel.setCounter(state.getCounterArray().get(index));
                    image.getPixels()[y][x] = pixel;
                }
            }
        }

        return image;
    }
}
