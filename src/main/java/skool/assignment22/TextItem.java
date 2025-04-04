package skool.assignment22;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TextItem {
    private String content;
    private double x, y;
    private int fontSize;
    private Color color;

    public TextItem(String content, double x, double y, int fontSize, Color color) {
        this.content = content;
        this.x = x;
        this.y = y;
        this.fontSize = fontSize;
        this.color = color;
    }
    // Getter and Setter methods for content and color
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Method to draw the text on the canvas
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.setFont(javafx.scene.text.Font.font(fontSize));
        gc.fillText(content, x, y);
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public int getFontSize() {

        return fontSize;
    }

}
