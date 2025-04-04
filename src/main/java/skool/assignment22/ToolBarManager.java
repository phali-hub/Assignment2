package skool.assignment22;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;

public class ToolBarManager {
    private final HBox toolbar;
    private final CanvasManager canvasManager;
    private final SessionManager sessionManager;
    private final VBox toolsMenu;
    private final Media mediaManager;  // Declare MediaManager

    public ToolBarManager(CanvasManager canvasManager, SessionManager sessionManager) {
        this.canvasManager = canvasManager;
        this.sessionManager = sessionManager;
        this.toolbar = new HBox(10);
        this.toolbar.setPadding(new Insets(10));

        // Initialize MediaManager with GraphicsContext from CanvasManager
        this.mediaManager = new Media(canvasManager.getGraphicsContext());

        // Create buttons
        Button brushButton = new Button("Brush");
        Button eraserButton = new Button("Eraser");
        Button clearButton = new Button("Clear");
        Button saveButton = new Button("Save");
        Button loadButton = new Button("Load");
        Button undoButton = new Button("Undo");
        Button redoButton = new Button("Redo");
        Button toolsButton = new Button("Tools");
        Button addText = new Button("Text");

        // Tools menu (hidden by default)
        toolsMenu = new VBox(10);
        toolsMenu.setVisible(false); // Initially hidden
        Button lineButton = new Button("Line");
        Button rectangleButton = new Button("Rectangle");
        Button circleButton = new Button("Circle");

        // Add action to tools
        lineButton.setOnAction(e -> canvasManager.activateLineTool());
        rectangleButton.setOnAction(e -> canvasManager.activateRectangleTool());
        circleButton.setOnAction(e -> canvasManager.activateCircleTool());

        toolsMenu.getChildren().addAll(lineButton, rectangleButton, circleButton);

        // Toggle tools menu visibility when clicking Tools button
        toolsButton.setOnAction(e -> toolsMenu.setVisible(!toolsMenu.isVisible()));

        // Color picker
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setOnAction(e -> canvasManager.setBrushColor(colorPicker.getValue()));

        // Brush size slider
        Slider brushSizeSlider = new Slider(1, 20, 2);
        brushSizeSlider.setShowTickMarks(true);
        brushSizeSlider.setShowTickLabels(true);
        brushSizeSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                canvasManager.setBrushSize(newVal.doubleValue()));

        // Button actions
        brushButton.setOnAction(e -> canvasManager.activateBrushTool());
        eraserButton.setOnAction(e -> canvasManager.activateEraserTool());
        clearButton.setOnAction(e -> canvasManager.clearCanvas());
        addText.setOnAction(e -> canvasManager.addTextToCanvas());


        // Save and Load actions with FileChooser
        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                sessionManager.saveSessionData(file);
            }
        });

        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                sessionManager.loadSessionData(file);
            }
        });

        // Undo and Redo actions
        undoButton.setOnAction(e -> canvasManager.undo());
        redoButton.setOnAction(e -> canvasManager.redo());

        // Add elements to toolbar
        toolbar.getChildren().addAll(brushButton, eraserButton, colorPicker, brushSizeSlider, clearButton, saveButton, loadButton, undoButton, redoButton,addText,toolsButton);

        // Add tools menu
        toolbar.getChildren().add(toolsMenu); // Add the tools menu to the toolbar

        // Add Media controls toolbar to the main toolbar
        toolbar.getChildren().add(mediaManager.getToolBar());  // Add MediaManager toolbar

    }

    // Provide access to the toolbar
    public HBox getToolBar() {

        return toolbar;
    }

    public Node getMediaToolbox() {

        return mediaManager.getVerticalMediaToolbar();
    }
}

