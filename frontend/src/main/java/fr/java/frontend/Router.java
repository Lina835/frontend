package fr.java.frontend;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Router {

    private static Stage stage;

    public static void init(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Asiatik Express");
        stage.setWidth(900);
        stage.setHeight(600);
    }

    public static void setScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }
}
