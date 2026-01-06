package fr.java.frontend.view;

import fr.java.frontend.Router;
import fr.java.frontend.cart.Cart;
import fr.java.frontend.cart.CartItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.util.List;

public class ConfirmationView {

    public static Scene build(int orderId, double totalAmount, List<CartItem> items) {
        // Fond gris clair (comme les écrans McDo/BK)
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f0f0f0;");

        // --- LE TICKET BLANC ---
        VBox ticket = new VBox(15);
        ticket.setMaxWidth(400);
        ticket.setAlignment(Pos.TOP_CENTER);
        ticket.setPadding(new Insets(30));
        ticket.setStyle(
            "-fx-background-color: white; " + 
            "-fx-background-radius: 10; " + 
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );

        // En-tête simple
        Label restaurantName = new Label("ASIATIK EXPRESS");
        restaurantName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label thankYou = new Label("Merci pour votre commande !");
        thankYou.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        // LE NUMÉRO (L'élément le plus important)
        VBox idBox = new VBox(5);
        idBox.setAlignment(Pos.CENTER);
        idBox.setPadding(new Insets(10, 0, 10, 0));
        
        Label idTitle = new Label("VOTRE NUMÉRO :");
        idTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        Label idValue = new Label(String.valueOf(orderId));
        idValue.setStyle("-fx-font-size: 60px; -fx-font-weight: 900; -fx-text-fill: #e74c3c;"); // Rouge BK/McDo
        
        idBox.getChildren().addAll(idTitle, idValue);

        // Séparateur pointillé
        Line line = new Line(0, 0, 320, 0);
        line.setStroke(Color.LIGHTGRAY);
        line.getStrokeDashArray().addAll(5d, 5d);

        // LISTE DES PRODUITS (Simple et propre)
        VBox itemsList = new VBox(8);
        for (CartItem it : items) {
            HBox row = new HBox();
            Label qtyName = new Label(it.quantity + " x " + it.dish.name);
            qtyName.setStyle("-fx-font-size: 15px;");
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            Label price = new Label(String.format("%.2f €", it.totalPrice()));
            price.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
            
            row.getChildren().addAll(qtyName, spacer, price);
            itemsList.getChildren().add(row);
        }

        Line line2 = new Line(0, 0, 320, 0);
        line2.setStroke(Color.BLACK);

        // TOTAL
        HBox totalRow = new HBox();
        Label totalLabel = new Label("TOTAL");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        
        Label totalValue = new Label(String.format("%.2f €", totalAmount));
        totalValue.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        totalRow.getChildren().addAll(totalLabel, spacer2, totalValue);

        ticket.getChildren().addAll(restaurantName, thankYou, idBox, line, itemsList, line2, totalRow);

        // --- BOUTON DE SORTIE ---
        Button btnFinish = new Button("TERMINER");
        btnFinish.setPrefWidth(400);
        btnFinish.setStyle(
            "-fx-background-color: #2d3436; " + 
            "-fx-text-fill: white; " + 
            "-fx-font-size: 18px; " + 
            "-fx-font-weight: bold; " + 
            "-fx-padding: 15; " + 
            "-fx-background-radius: 10;"
        );
        btnFinish.setOnAction(e -> {
            Cart.clear();
            Router.setScene(CatalogueView.build());
        });

        root.getChildren().addAll(ticket, btnFinish);

        return new Scene(root, 900, 700);
    }
}