import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class YPlayApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Stage primaryStage;

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(YPlayApp.class.getClassLoader().getResource("yplay.fxml"));
            BorderPane rootLayout = loader.load();
            loader.getController();
            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add(YPlayApp.class.getClassLoader().getResource("hide-scrollbars.css").toExternalForm());
            scene.getStylesheets().add(YPlayApp.class.getClassLoader().getResource("style.css").toExternalForm());

            primaryStage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(0);
            });
            primaryStage.getIcons().add(new Image(YPlayApp.class.getClassLoader().getResourceAsStream("icon.png")));
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Y-Player");
        initRootLayout();
    }
}
