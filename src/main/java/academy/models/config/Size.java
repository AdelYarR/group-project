package academy.models.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Size {

    private static final int DEFAULT_WIDTH = 1920;
    private static final int DEFAULT_HEIGHT = 1080;

    private int width;
    private int height;

    public Size() {
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
    }

    @JsonCreator
    public Size(@JsonProperty("width") int width, @JsonProperty("height") int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
