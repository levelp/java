package fxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX FXML example.
 * Need: jfxrt.jar from jre
 */
public class FXML extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.printf("JavaFX version: %s%n", com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
        System.out.println("javafx.runtime.version: " + System.getProperties().get("javafx.runtime.version"));

        Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
