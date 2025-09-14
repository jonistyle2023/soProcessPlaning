package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/ui/main-view.fxml"));
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ui/icon.png"))));
        Scene scene = new Scene(fxmlLoader.load(), 800, 400);
        stage.setTitle("Software para Planificación de Procesos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}