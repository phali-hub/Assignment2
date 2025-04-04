package skool.assignment22;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class SessionData implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Image image;
    private final Color brushColor;
    private final double brushSize;

    public SessionData(Image image, Color brushColor, double brushSize) {
        this.image = image;
        this.brushColor = brushColor;
        this.brushSize = brushSize;
    }

    public Image getImage() {
        return image;
    }

    public Color getBrushColor() {
        return brushColor;
    }

    public double getBrushSize() {
        return brushSize;
    }


}