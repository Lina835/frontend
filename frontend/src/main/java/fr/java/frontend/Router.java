package fr.java.frontend;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe utilitaire gérant la navigation entre les différents écrans de l'application.
 * Utilise un Stage statique pour permettre le changement de vue depuis n'importe quel point du programme.
 */
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