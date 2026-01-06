package fr.java.frontend.view;

import fr.java.frontend.Router;
import fr.java.frontend.cart.Cart;
import fr.java.frontend.cart.CartItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ConfirmationView {

    public static Scene build(int orderId) {
        // --- ENTÊTE ---
        Label title = new Label("Confirmation de commande");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label icon = new Label("✅");
        icon.setStyle("-fx-font-size: 50px;");

        Label orderLabel = new Label("Commande N° #" + orderId);
        orderLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");

        // --- ZONE RÉCAPITULATIF (Le cadre noir du dessin) ---
        VBox recapBox = new VBox(10);
        recapBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 20;");
        recapBox.setMaxWidth(500);

        Label recapTitle = new Label("Récapitulatif");
        recapTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        recapBox.getChildren().add(recapTitle);

        // On boucle sur les items du panier comme dans ta CartView
        for (CartItem it : Cart.getItems()) {
            HBox line = new HBox();
            Label nameQty = new Label(it.quantity + "x " + it.dish.name);
            Label price = new Label(String.format("%.2f €", it.totalPrice()));
            
            // Astuce pour pousser le prix à droite
            HBox.setHgrow(nameQty, Priority.ALWAYS);
            nameQty.setMaxWidth(Double.MAX_VALUE);
            
            line.getChildren().addAll(nameQty, price);
            recapBox.getChildren().add(line);
        }

        recapBox.getChildren().add(new Separator());

        // Ligne du Total
        HBox totalLine = new HBox();
        Label totalLabel = new Label("Total");
        totalLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label totalAmount = new Label(String.format("%.2f €", Cart.total()));
        totalAmount.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        HBox.setHgrow(totalLabel, Priority.ALWAYS);
        totalLabel.setMaxWidth(Double.MAX_VALUE);
        totalLine.getChildren().addAll(totalLabel, totalAmount);
        recapBox.getChildren().add(totalLine);

        // --- BAS DE PAGE ---
        Label footer = new Label("Veuillez patienter, votre commande est en préparation");
        footer.setStyle("-fx-text-fill: #666666; -fx-italic: true;");

        Button btnNew = new Button("Nouvelle commande");
        btnNew.setStyle("-fx-padding: 10 30; -fx-font-size: 16px; -fx-background-radius: 20;");
        btnNew.setOnAction(e -> {
            Cart.clear(); // On vide le panier pour le client suivant
            Router.setScene(HomeView.build());
        });

        // Mise en page globale
        VBox root = new VBox(20, title, icon, orderLabel, recapBox, footer, btnNew);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: white;");

        return new Scene(root, 900, 700);
    }
}