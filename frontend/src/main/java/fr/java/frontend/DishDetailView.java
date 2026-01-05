package fr.java.frontend;

import fr.java.frontend.model.Dish;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DishDetailView {

    public static Scene build(Dish dish) {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        // 🔙 Bouton retour
        Button back = new Button("← Retour Catalogue");
        back.setOnAction(e -> Router.setScene(CatalogueView.build()));
        root.setTop(back);

        // Contenu central
        VBox center = new VBox(15);
        center.setAlignment(Pos.CENTER);

        Label name = new Label(dish.name);
        name.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Label desc = new Label(dish.description);
        desc.setWrapText(true);
        desc.setStyle("-fx-font-size: 16px;");

        Label price = new Label(String.format("%.2f €", dish.price));
        price.setStyle("-fx-font-size: 20px;");

        Button add = new Button("Ajouter à la commande");
        add.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        add.setDisable(!dish.available);

      add.setOnAction(e -> {
    fr.java.frontend.cart.Cart.add(dish);
    System.out.println("Panier : " + fr.java.frontend.cart.Cart.getItems().size() + " article(s)");
});

        if (!dish.available) {
            Label unavailable = new Label("Indisponible");
            unavailable.setStyle("-fx-text-fill: red;");
            center.getChildren().addAll(name, desc, price, unavailable, add);
        } else {
            center.getChildren().addAll(name, desc, price, add);
        }

        root.setCenter(center);

        return new Scene(root, 900, 600);
    }
}
