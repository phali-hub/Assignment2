package skool.assignment22;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

public class Tools {
    private boolean isBrush = true;
    private boolean isEraser = false;
    private boolean isDrawingLine = false;
    private boolean isDrawingRectangle = false;
    private boolean isDrawingCircle = false;
    private Color brushColor = Color.BLACK;
    private double brushSize = 5;
    private GraphicsContext gc;
    private double lastX = -1, lastY = -1; // Track previous positions for smooth drawing

    // Constructor that accepts the GraphicsContext of the canvas
    public Tools(GraphicsContext gc) {
        this.gc = gc;
        gc.setLineCap(StrokeLineCap.ROUND); // Ensure smooth lines
    }

    // Activate Brush Tool
    public void activateBrushTool() {
        resetTools();
        isBrush = true;
        gc.setStroke(brushColor);
        gc.setLineWidth(brushSize);
    }

    // Activate Eraser Tool
    public void activateEraserTool() {
        resetTools();
        isEraser = true;
        gc.setStroke(Color.WHITE); // Erasing with white color
        gc.setLineWidth(brushSize * 2); // Make the eraser bigger
    }

    public boolean isDrawingLine() {
        return isDrawingLine;
    }

    public boolean isDrawingRectangle() {
        return isDrawingRectangle;
    }

    public boolean isDrawingCircle() {
        return isDrawingCircle;
    }

    // Activate Line Tool
    public void activateLineTool() {
        resetTools();
        isDrawingLine = true;
    }
    // Activate Rectangle Tool
    public void activateRectangleTool() {
        resetTools();
        isDrawingRectangle = true;
    }
    // Activate Circle Tool
    public void activateCircleTool() {
        resetTools();
        isDrawingCircle = true;
    }
    public Color getBrushColor() {
        return brushColor;
    }

    // Getter for brush size
    public double getBrushSize() {
        return brushSize;
    }

    // Set Brush Color
    public void setBrushColor(Color color) {
        this.brushColor = color;
        isEraser = false; // Ensure it's not in eraser mode
    }

    // Set Brush Size
    public void setBrushSize(double size) {
        this.brushSize = size;
    }

    // Reset the last drawn position (used on mouse press)
    public void resetLastPosition() {
        lastX = -1;
        lastY = -1;
    }

    public void handleDrawing(double x, double y, double startX, double startY) {
        if (isBrush || isEraser) {
            if (lastX == -1 || lastY == -1) {
                lastX = x;
                lastY = y;
            }

            gc.setStroke(isEraser ? Color.WHITE : brushColor);
            gc.setLineWidth(brushSize);
            gc.setLineCap(StrokeLineCap.ROUND);
            gc.strokeLine(lastX, lastY, x, y);

            lastX = x;
            lastY = y;
        } else if (isDrawingLine || isDrawingRectangle || isDrawingCircle) {
            gc.setStroke(brushColor);
            gc.setLineWidth(brushSize);

            if (isDrawingLine) {
                gc.strokeLine(startX, startY, x, y);
            } else if (isDrawingRectangle) {
                double width = Math.abs(x - startX);
                double height = Math.abs(y - startY);
                gc.strokeRect(Math.min(startX, x), Math.min(startY, y), width, height);
            } else if (isDrawingCircle) {
                double radius = Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2));
                gc.strokeOval(startX - radius, startY - radius, 2 * radius, 2 * radius);
            }
        }
    }

    // Reset all tools (to prevent multiple tools being active at once)
    private void resetTools() {
        isBrush = false;
        isEraser = false;
        isDrawingLine = false;
        isDrawingRectangle = false;
        isDrawingCircle = false;
    }

    public void handleShapeDrawing(GraphicsContext gc, double startX, double startY, double endX, double endY) {
        if (isDrawingLine) {
            gc.strokeLine(startX, startY, endX, endY);
        } else if (isDrawingRectangle) {
            double width = Math.abs(endX - startX);
            double height = Math.abs(endY - startY);
            gc.strokeRect(Math.min(startX, endX), Math.min(startY, endY), width, height);
        } else if (isDrawingCircle) {
            double radius = Math.hypot(endX - startX, endY - startY);
            gc.strokeOval(startX - radius, startY - radius, radius * 2, radius * 2);
        }
    }
}
