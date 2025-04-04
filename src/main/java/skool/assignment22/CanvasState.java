package skool.assignment22;

import javafx.scene.image.WritableImage;
import javafx.scene.canvas.Canvas;

public class CanvasState {
    private final WritableImage image;

    public CanvasState(Canvas canvas) {
        // Take a snapshot of the canvas as a WritableImage
        this.image = canvas.snapshot(null, null);
    }

    public WritableImage getImage() {
        return image;
    }
}
