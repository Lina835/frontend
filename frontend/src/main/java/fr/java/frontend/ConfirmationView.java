package fr.java.frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ConfirmationView {

    public static Scene build(int orderId) {

        Label title = new Label("✅ Commande confirmée !");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Label number = new Label("Numéro de commande : " + orderId);
        number.setStyle("-fx-font-size: 20px;");

        Button backHome = new Button("🏠 Retour Accueil");
        backHome.setOnAction(e -> Router.setScene(HomeView.build()));

        Button newOrder = new Button("🍜 Nouvelle commande");
        newOrder.setOnAction(e -> Router.setScene(CatalogueView.build()));

        VBox root = new VBox(18, title, number, newOrder, backHome);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        return new Scene(root, 900, 600);
    }
}
