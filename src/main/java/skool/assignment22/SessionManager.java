package skool.assignment22;

import java.io.*;

public class SessionManager {
    private final CanvasManager canvasManager;

    public SessionManager(CanvasManager canvasManager) {
        this.canvasManager = canvasManager;
    }

    // Save session to an image file
    public void saveSessionData(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            // Get current session data
            SessionData sessionData = new SessionData(
                    canvasManager.getCanvas().snapshot(null, null),
                    canvasManager.getBrushColor(),
                    canvasManager.getBrushSize()
            );
            oos.writeObject(sessionData);  // Save session data
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load session data (restore canvas + brush properties)
    public void loadSessionData(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            SessionData sessionData = (SessionData) ois.readObject();

            // Restore canvas image
            canvasManager.getCanvas().getGraphicsContext2D().drawImage(sessionData.getImage(), 0, 0);

            // Restore brush properties
            canvasManager.setBrushColor(sessionData.getBrushColor());
            canvasManager.setBrushSize(sessionData.getBrushSize());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
