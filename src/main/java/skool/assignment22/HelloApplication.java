package skool.assignment22;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloApplication extends Application {
    private ToolBarManager toolbarManager;

    @Override
    public void start(Stage primaryStage) {
        // Create Canvas
        Canvas canvas = new Canvas(1000, 600);

        // Initialize CanvasManager correctly
        CanvasManager canvasManager = new CanvasManager(canvas);

        // Initialize SessionManager
        SessionManager sessionManager = new SessionManager(canvasManager);

        // Initialize ToolBarManager
        toolbarManager = new ToolBarManager(canvasManager, sessionManager);

        // Set up layout
        BorderPane root = new BorderPane();
        root.setTop(toolbarManager.getToolBar());  // Toolbar at the top
        root.setLeft(toolbarManager.getMediaToolbox()); // Media buttons on the left
        root.setCenter(canvas); // Canvas at the center

        // Create Scene
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        primaryStage.setTitle("Interactive Digital Whiteboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
