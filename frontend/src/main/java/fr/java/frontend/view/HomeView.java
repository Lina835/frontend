package fr.java.frontend.view;

import fr.java.frontend.Router;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class HomeView {

    public static Scene build() {

        ImageView logo = new ImageView(new Image(HomeView.class.getResourceAsStream("/logo.png")));
        logo.setFitWidth(260);
        logo.setPreserveRatio(true);

        Button start = new Button("TOUCHEZ POUR COMMENCER");
        start.setStyle("-fx-font-size: 18px; -fx-padding: 14 26; -fx-background-radius: 10;");

        // ✅ Quand on clique -> on va au catalogue
        start.setOnAction(e -> Router.setScene(CatalogueView.build()));

        VBox root = new VBox(40, logo, start);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: white;");

        return new Scene(root, 900, 600);
    }
}
