package fr.java.frontend.view;

import java.util.ArrayList;
import java.util.List;

import fr.java.frontend.Router;
import fr.java.frontend.api.ApiClient;
import fr.java.frontend.cart.Cart;
import fr.java.frontend.cart.CartItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CartView {

    public static Scene build() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;"); // Fond gris perle uniforme

        // --- HEADER : Retour ---
        HBox header = new HBox();
        header.setPadding(new Insets(25, 30, 10, 30));
        Button back = new Button("← Continuer mes achats");
        back.setStyle("-fx-background-color: transparent; -fx-font-size: 24px; -fx-font-weight: bold; -fx-cursor: hand;");
        back.setOnAction(e -> Router.setScene(CatalogueView.build()));
        header.getChildren().add(back);
        root.setTop(header);

        // --- CENTRE : Liste des produits ---
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(10, 50, 20, 50));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("MON PANIER");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: #2d3436;");
        mainContainer.getChildren().add(title);

        if (Cart.getItems().isEmpty()) {
            VBox emptyBox = new VBox(20);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(100, 0, 0, 0));
            Label empty = new Label("Votre panier est actuellement vide.");
            empty.setStyle("-fx-font-size: 18px; -fx-text-fill: #636e72;");
            emptyBox.getChildren().add(empty);
            mainContainer.getChildren().add(emptyBox);
        } else {
            VBox listItems = new VBox(15);
            for (CartItem it : Cart.getItems()) {
                listItems.getChildren().add(cartLine(it));
            }
            
            ScrollPane scroll = new ScrollPane(listItems);
            scroll.setFitToWidth(true);
            scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
            mainContainer.getChildren().add(scroll);
        }
        root.setCenter(mainContainer);

        // --- FOOTER : Recap & Validation ---
        VBox bottom = new VBox(20);
        bottom.setPadding(new Insets(20, 50, 40, 50));
        bottom.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, -5);");
        bottom.setAlignment(Pos.CENTER);

        // Ligne du Total
        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER);
        totalRow.setMaxWidth(600);
        Label totalLabel = new Label("TOTAL À RÉGLER");
        totalLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label totalVal = new Label(String.format("%.2f €", Cart.total()));
        totalVal.setStyle("-fx-font-size: 28px; -fx-font-weight: 900; -fx-text-fill: #e74c3c;");
        totalRow.getChildren().addAll(totalLabel, spacer, totalVal);

        // Boutons d'action
        HBox actions = new HBox(20);
        actions.setAlignment(Pos.CENTER);

        Button clear = new Button("VIDER");
        clear.setStyle("-fx-background-color: white; -fx-border-color: #d63031; -fx-text-fill: #d63031; -fx-padding: 12 25; -fx-font-weight: bold; -fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand;");
        clear.setOnAction(e -> {
            Cart.clear();
            Router.setScene(CartView.build());
        });

        Button confirm = new Button("COMMANDER ET PAYER");
        confirm.setPrefWidth(400);
        confirm.setStyle("-fx-background-color: #2d3436; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 15; -fx-background-radius: 5; -fx-cursor: hand;");
        confirm.setDisable(Cart.getItems().isEmpty());
        
        confirm.setOnAction(e -> {
            try {
                confirm.setDisable(true);
                double finalAmount = Cart.total();
                List<CartItem> itemsOrdered = new ArrayList<>(Cart.getItems());
                int newId = ApiClient.createOrder("BORNE_01", itemsOrdered);
                Router.setScene(ConfirmationView.build(newId, finalAmount, itemsOrdered));
            } catch (Exception ex) {
                confirm.setDisable(false);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Le serveur ne répond pas.");
                alert.showAndWait();
            }
        });

        actions.getChildren().addAll(clear, confirm);
        bottom.getChildren().addAll(totalRow, actions);
        root.setBottom(bottom);

        return new Scene(root, 1280, 720);
    }

    private static HBox cartLine(CartItem it) {
        HBox line = new HBox(20);
        line.setAlignment(Pos.CENTER_LEFT);
        line.setPadding(new Insets(15, 25, 15, 25));
        line.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 5, 0, 0, 2);");

        // Infos produit
        VBox info = new VBox(5);
        Label name = new Label(it.dish.name.toUpperCase());
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label unitPrice = new Label(String.format("%.2f € / unité", it.dish.price));
        unitPrice.setStyle("-fx-text-fill: #636e72; -fx-font-size: 13px;");
        info.getChildren().addAll(name, unitPrice);
        info.setPrefWidth(300);

        // Sélecteur quantité
        HBox qtyCtrl = new HBox(15);
        qtyCtrl.setAlignment(Pos.CENTER);
        
        Button minus = new Button("-");
        minus.setStyle("-fx-background-color: #f1f2f6; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        minus.setOnAction(e -> { Cart.decrease(it.dish.id); Router.setScene(CartView.build()); });

        Label qty = new Label(String.valueOf(it.quantity));
        qty.setStyle("-fx-text-fill: #2d3436; -fx-font-size: 18px; -fx-font-weight: bold; -fx-min-width: 30; -fx-alignment: center;");

        Button plus = new Button("+");
        plus.setStyle("-fx-background-color: #f1f2f6; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        plus.setOnAction(e -> { Cart.increase(it.dish.id); Router.setScene(CartView.build()); });

        qtyCtrl.getChildren().addAll(minus, qty, plus);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Sous-total
        Label price = new Label(String.format("%.2f €", it.totalPrice()));
        price.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");
        price.setPrefWidth(120);
        price.setAlignment(Pos.CENTER_RIGHT);

        line.getChildren().addAll(info, qtyCtrl, spacer, price);
        return line;
    }
}