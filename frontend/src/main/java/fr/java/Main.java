package fr.java;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Image logo = new Image(
                getClass().getResourceAsStream("/logo.png")
        );

        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(250);
        logoView.setPreserveRatio(true);

        Button btn = new Button("TOUCHEZ POUR COMMENCER");
        btn.setOnAction(e -> System.out.println("Bouton cliqué !"));

        VBox root = new VBox(30, logoView, btn);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Asiatik Express");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
