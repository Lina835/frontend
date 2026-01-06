package fr.java.frontend.view;

import fr.java.frontend.Router;
import fr.java.frontend.api.ApiClient;
import fr.java.frontend.cart.Cart;
import fr.java.frontend.cart.CartItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CartView {

    public static Scene build() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        Button back = new Button("← Retour Catalogue");
        back.setOnAction(e -> Router.setScene(CatalogueView.build()));
        root.setTop(back);

        VBox center = new VBox(12);
        center.setPadding(new Insets(20));
        center.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("🛒 Votre Panier");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        center.getChildren().add(title);

        if (Cart.getItems().isEmpty()) {
            Label empty = new Label("Votre panier est vide.");
            empty.setStyle("-fx-font-size: 16px;");
            center.getChildren().add(empty);
        } else {
            for (CartItem it : Cart.getItems()) {
                center.getChildren().add(cartLine(it));
            }

            Label total = new Label("TOTAL : " + String.format("%.2f €", Cart.total()));
            total.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            center.getChildren().add(total);
        }

        VBox bottom = new VBox(10);
        bottom.setAlignment(Pos.CENTER);

        Button clear = new Button("🗑 Vider le panier");
        clear.setOnAction(e -> {
            Cart.clear();
            Router.setScene(CartView.build());
        });

        Button confirm = new Button("✅ Confirmer la commande");
        confirm.setDisable(Cart.getItems().isEmpty());
        confirm.setOnAction(e -> {
    try {
        // 1. Désactiver le bouton pour éviter les doubles clics
        confirm.setDisable(true);

        // 2. Appel au backend
        int newId = ApiClient.createOrder("BORNE_01", Cart.getItems());

        // 3. Succès -> Page de confirmation
        Router.setScene(ConfirmationView.build(newId));

    } catch (Exception ex) {
        // En cas d'erreur (serveur éteint par exemple)
        confirm.setDisable(false); // Réactiver le bouton
        
        // Créer une petite popup d'alerte
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText("Impossible d'envoyer la commande");
        alert.setContentText("Le serveur ne répond pas. Veuillez appeler un membre du personnel.");
        alert.showAndWait();
        
        ex.printStackTrace();
    }
});

        bottom.getChildren().addAll(clear, confirm);

        root.setCenter(center);
        root.setBottom(bottom);

        return new Scene(root, 900, 600);
    }

    private static HBox cartLine(CartItem it) {
        HBox line = new HBox(12);
        line.setAlignment(Pos.CENTER);
        line.setPadding(new Insets(8));
        line.setStyle("-fx-border-color: #dddddd; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label name = new Label(it.dish.name);
        name.setPrefWidth(260);
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button minus = new Button("-");
        minus.setOnAction(e -> {
            Cart.decrease(it.dish.id);
            Router.setScene(CartView.build());
        });

        Label qty = new Label(String.valueOf(it.quantity));
        qty.setMinWidth(30);
        qty.setAlignment(Pos.CENTER);

        Button plus = new Button("+");
        plus.setOnAction(e -> {
            Cart.increase(it.dish.id);
            Router.setScene(CartView.build());
        });

        Label price = new Label(String.format("%.2f €", it.totalPrice()));
        price.setPrefWidth(120);
        price.setAlignment(Pos.CENTER_RIGHT);

        line.getChildren().addAll(name, minus, qty, plus, price);
        return line;
    }
}
