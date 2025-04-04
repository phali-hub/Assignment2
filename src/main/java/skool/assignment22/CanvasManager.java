package skool.assignment22;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.Stack;

public class CanvasManager {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private Tools tools;

    // Stores starting position for shapes
    private double startX, startY;

    // Undo/Redo stacks
    private final Stack<CanvasState> undoStack = new Stack<>();
    private final Stack<CanvasState> redoStack = new Stack<>();

    private TextItem currentTextItem;

    public CanvasManager(Canvas canvas) {
        if (canvas == null) {
            throw new IllegalArgumentException("Canvas cannot be null.");
        }
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.tools = new Tools(gc);
        clearCanvas();

        // Mouse Events for Freehand Drawing (Brush & Eraser)
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);

        // Mouse Events for Shapes
        canvas.setOnMousePressed(e -> {
            startX = e.getX();  // Store start position for shapes
            startY = e.getY();
            tools.resetLastPosition();
        });

        canvas.setOnMouseReleased(e -> {
            double endX = e.getX();
            double endY = e.getY();
            saveState();
            tools.handleShapeDrawing(gc, startX, startY, endX, endY);
        });

    }

    // Mouse Pressed Event (Start Drawing)
    private void onMousePressed(MouseEvent event) {
        tools.resetLastPosition();  // Reset for smooth strokes
        tools.handleDrawing(event.getX(), event.getY(),startX, startY);
    }

    // Mouse Dragged Event (Continue Drawing)
    private void onMouseDragged(MouseEvent event) {
        if (tools.isDrawingLine() || tools.isDrawingRectangle() || tools.isDrawingCircle()) {
            restoreLastState(); //
            tools.handleShapeDrawing(gc, startX, startY, event.getX(), event.getY());
        } else {
            tools.handleDrawing(event.getX(), event.getY(), startX, startY);
        }
    }
    private void restoreLastState() {
        if (!undoStack.isEmpty()) {
            CanvasState lastState = undoStack.peek(); // Get last saved state
            gc.drawImage(lastState.getImage(), 0, 0);
        }
    }



    // Getters
    public Canvas getCanvas() {
        return canvas;
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    // Brush Settings
    public void setBrushColor(Color color) {
        tools.setBrushColor(color);
    }

    public void setBrushSize(double size) {
        tools.setBrushSize(size);
    }
    public Color getBrushColor() {
        return tools.getBrushColor();
    }

    public double getBrushSize() {
        return tools.getBrushSize();
    }

    // Activate Tools
    public void activateBrushTool() {
        tools.activateBrushTool();
    }

    public void activateEraserTool() {
        tools.activateEraserTool();
    }

    public void activateLineTool() {
        tools.activateLineTool();
    }

    public void activateRectangleTool() {
        tools.activateRectangleTool();
    }

    public void activateCircleTool() {
        tools.activateCircleTool();
    }


    // Clear Canvas
    public void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        saveState(); // Save state for undo
    }

    // Save Canvas State for Undo/Redo
    private void saveState() {
        undoStack.push(new CanvasState(canvas)); // Save current state
        redoStack.clear();  // Clear redo stack as new action invalidates redo history
    }

    // Undo Action
    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(new CanvasState(canvas)); // Save current state to redo stack
            CanvasState previousState = undoStack.pop();
            gc.drawImage(previousState.getImage(), 0, 0); // Restore previous state
        }
    }

    // Redo Action
    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(new CanvasState(canvas)); // Save current state to undo stack
            CanvasState nextState = redoStack.pop();
            gc.drawImage(nextState.getImage(), 0, 0); // Restore next state
        }
    }

    // Add Text to Canvas
    public void addTextToCanvas() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Text Input");
        dialog.setHeaderText("Enter the text to add:");
        dialog.setContentText("Text:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(content -> {
            double x = 100;
            double y = 100;
            int fontSize = 20;
            Color color = Color.BLACK;

            currentTextItem = new TextItem(content, x, y, fontSize, color);
            currentTextItem.draw(gc);

            makeTextDraggable();
        });
    }
    private void makeTextDraggable() {
        if (currentTextItem == null) return;

        canvas.setOnMousePressed(event -> {
            if (isMouseOverText(event.getX(), event.getY())) {
                startX = event.getX();
                startY = event.getY();
            }
        });

        canvas.setOnMouseDragged(event -> {
            if (isMouseOverText(startX, startY)) {
                double offsetX = event.getX() - startX;
                double offsetY = event.getY() - startY;

                startX = event.getX();
                startY = event.getY();

                moveText(offsetX, offsetY);
            }
        });

        canvas.setOnMouseClicked(event -> {
            if (isMouseOverText(event.getX(), event.getY())) {
                editText();
            }
        });
    }

    private boolean isMouseOverText(double mouseX, double mouseY) {
        if (currentTextItem == null) return false;

        double textWidth = currentTextItem.getContent().length() * currentTextItem.getFontSize() * 0.5;
        double textHeight = currentTextItem.getFontSize();

        return mouseX >= currentTextItem.getX() && mouseX <= currentTextItem.getX() + textWidth &&
                mouseY >= currentTextItem.getY() - textHeight && mouseY <= currentTextItem.getY();
    }

    private void moveText(double offsetX, double offsetY) {
        currentTextItem.setX(currentTextItem.getX() + offsetX);
        currentTextItem.setY(currentTextItem.getY() + offsetY);

        clearCanvas();
        currentTextItem.draw(gc);
    }

    private void editText() {
        if (currentTextItem == null) return;

        TextInputDialog dialog = new TextInputDialog(currentTextItem.getContent());
        dialog.setTitle("Edit Text");
        dialog.setHeaderText("Edit the text:");
        dialog.setContentText("Text:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(content -> {
            currentTextItem.setContent(content);
            clearCanvas();
            currentTextItem.draw(gc);
        });
    }

}
