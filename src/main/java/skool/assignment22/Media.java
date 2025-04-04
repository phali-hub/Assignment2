package skool.assignment22;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Media {
    private final GraphicsContext gc;
    private final VBox toolbar;

    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;
    private MediaView mediaView;
    private ImageView imageView;

    public Media(GraphicsContext gc) {
        this.gc = gc;
        this.toolbar = new VBox(10);
        init();
    }

    private void init() {
        // Initialize buttons and progress bar
        Button addImageButton = new Button("Add Image");
        Button addVideoButton = new Button("Add Video");
        Button addAudioButton = new Button("Add Audio");

        // Add actions to the buttons
        addImageButton.setOnAction(e -> addImage());
        addVideoButton.setOnAction(e -> addVideo());
        addAudioButton.setOnAction(e -> addAudio());

        // Add the buttons to the toolbar
        toolbar.getChildren().addAll(addImageButton, addVideoButton, addAudioButton);

        // Initialize the progress bar for video/audio
        progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(200);
        progressBar.setVisible(false); // Hide initially
        toolbar.getChildren().add(progressBar);
    }

    // Get the toolbar to add it to the GUI
    public VBox getToolBar() {
        return toolbar;
    }

    public VBox getVerticalMediaToolbar() {
        return toolbar;
    }

    // Add image and make it draggable
    private void addImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
            imageView = new ImageView(image);

            // Set image size to match the video size (400x300)
            imageView.setFitWidth(400);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);

            // Make image draggable
            enableDrag(imageView);

            // Create a close button for image
            Button closeButton = new Button("X");
            closeButton.setOnAction(e -> closeMediaImage());

            // Add image and close button to the toolbar in a container
            Pane imageContainer = new Pane(imageView);
           // closeButton.setLayoutX(380);
           // closeButton.setLayoutY(5);

            toolbar.getChildren().addAll(imageContainer,closeButton);
        }
    }

    // Add video and make it draggable
    private void addVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mov", "*.avi"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            javafx.scene.media.Media media = new javafx.scene.media.Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView = new MediaView(mediaPlayer);

            // Set video size and position
            mediaView.setFitWidth(400);
            mediaView.setFitHeight(300);

            // Make video draggable
            enableDrag(mediaView);

            // Play video
            mediaPlayer.play();

            // Progress bar updates
            mediaPlayer.setOnReady(() -> {
                progressBar.setMaxWidth(mediaPlayer.getMedia().getDuration().toSeconds());
                mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
                    progressBar.setProgress(newTime.toSeconds() / mediaPlayer.getMedia().getDuration().toSeconds());
                });
            });

            // Buttons for media controls
            Button playPauseButton = new Button("Pause");
            playPauseButton.setOnAction(e -> togglePlayPause());

            Button stopButton = new Button("Stop");
            stopButton.setOnAction(e -> stopMedia());

            Button closeButton = new Button("X");
            closeButton.setOnAction(e -> closeMedia());

            toolbar.getChildren().addAll(mediaView, playPauseButton, stopButton, closeButton);
        }
    }

    // Add audio
    private void addAudio() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.ogg"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            javafx.scene.media.Media media = new javafx.scene.media.Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            // Progress bar updates
            mediaPlayer.setOnReady(() -> {
                progressBar.setMaxWidth(mediaPlayer.getMedia().getDuration().toSeconds());
                mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
                    progressBar.setProgress(newTime.toSeconds() / mediaPlayer.getMedia().getDuration().toSeconds());
                });
            });

            // Play audio
            mediaPlayer.play();

            // Buttons for audio controls
            Button playPauseButton = new Button("Pause");
            playPauseButton.setOnAction(e -> togglePlayPause());

            Button stopButton = new Button("Stop");
            stopButton.setOnAction(e -> stopMedia());

            Button closeButton = new Button("X");
            closeButton.setOnAction(e -> closeMedia());

            toolbar.getChildren().addAll(playPauseButton, stopButton, closeButton);
        }
    }

    // Make ImageView and MediaView draggable
    private void enableDrag(javafx.scene.Node node) {
        final double[] offsetX = {0};
        final double[] offsetY = {0};

        node.setOnMousePressed(event -> {
            offsetX[0] = event.getSceneX() - node.getLayoutX();
            offsetY[0] = event.getSceneY() - node.getLayoutY();
        });

        node.setOnMouseDragged(event -> {
            node.setLayoutX(event.getSceneX() - offsetX[0]);
            node.setLayoutY(event.getSceneY() - offsetY[0]);
        });
    }

    // Toggle play/pause for media
    private void togglePlayPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        }
    }

    // Stop media
    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            progressBar.setProgress(0);
        }
    }

    // Close media and remove controls
    public void closeMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        // Remove only media-related controls (video/audio + buttons)
        toolbar.getChildren().removeIf(node ->
                node instanceof MediaView || node instanceof ProgressBar ||
                        (node instanceof Button && !((Button) node).getText().contains("Add"))
        );
    }

    // Close image and remove its controls
    private void closeMediaImage() {
        // Remove image and its close button
        toolbar.getChildren().removeIf(node ->
                node instanceof Pane && ((Pane) node).getChildren().contains(imageView));
    }
}
