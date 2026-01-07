package fr.java.frontend.view;

import fr.java.frontend.Router;
import fr.java.frontend.api.ApiClient;
import fr.java.frontend.cart.Cart;
import fr.java.frontend.cart.CartItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;

public class RecapulatifView {

    public static Scene build() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;"); // Fond gris perle pro

        // --- HEADER (Récapitulatif de commande) ---
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25, 0, 25, 0));
        header.setStyle("-fx-background-color: #2d3436;"); // Bandeau anthracite
        
        Label title = new Label("RÉCAPITULATIF DE COMMANDE");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        header.getChildren().add(title);
        root.setTop(header);

        // --- CENTRE : LISTE DES ARTICLES (Scrollable) ---
        VBox listContainer = new VBox(15);
        listContainer.setPadding(new Insets(30, 60, 30, 60));
        listContainer.setAlignment(Pos.TOP_CENTER);
        
        // On boucle sur les items du panier pour créer les lignes
        for (CartItem it : Cart.getItems()) {
            listContainer.getChildren().add(createSummaryRow(it));
        }

        ScrollPane scroll = new ScrollPane(listContainer);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        root.setCenter(scroll);

        // --- FOOTER : SAISIE CLIENT + TOTAL + BOUTONS ---
        VBox footer = new VBox(25);
        footer.setPadding(new Insets(30, 60, 40, 60));
        footer.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, -5);");
        footer.setAlignment(Pos.CENTER);

        // Section Saisie (Table / Client)
        VBox inputSection = new VBox(10);
        inputSection.setAlignment(Pos.CENTER);
        Label inputLabel = new Label("Numéro de Table ou Nom du Client :");
        inputLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #2d3436;");
        
        TextField customerRefField = new TextField();
        customerRefField.setPromptText("Ex: Table 12 / Sophie");
        customerRefField.setMaxWidth(350);
        customerRefField.setStyle("-fx-padding: 12; -fx-font-size: 16px; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");
        inputSection.getChildren().addAll(inputLabel, customerRefField);

        // Affichage du Total
        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER);
        totalRow.setMaxWidth(500);
        Label totalTxt = new Label("TOTAL À PAYER :");
        totalTxt.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label totalVal = new Label(String.format("%.2f €", Cart.total()));
        totalVal.setStyle("-fx-font-size: 28px; -fx-font-weight: 900; -fx-text-fill: #e74c3c;");
        totalRow.getChildren().addAll(totalTxt, spacer, totalVal);

        // Boutons de navigation
        HBox btnBox = new HBox(40);
        btnBox.setAlignment(Pos.CENTER);

        Button btnBack = new Button("← MODIFIER");
        btnBack.setStyle("-fx-background-color: white; -fx-border-color: #2d3436; -fx-padding: 12 35; -fx-font-weight: bold; -fx-cursor: hand;");
        btnBack.setOnAction(e -> Router.setScene(CartView.build()));

        Button btnConfirm = new Button("CONFIRMER LA COMMANDE →");
        btnConfirm.setStyle("-fx-background-color: #2d3436; -fx-text-fill: white; -fx-padding: 14 50; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand; -fx-background-radius: 5;");
        
        // Action finale : Appel à ton ApiClient
        btnConfirm.setOnAction(e -> {
            String ref = customerRefField.getText().trim();
            if (ref.isEmpty()) {
                customerRefField.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2; -fx-padding: 12;");
                return;
            }

            try {
                List<CartItem> items = new ArrayList<>(Cart.getItems());
                double amount = Cart.total();
                
                // C'est ici qu'on envoie la ref vers ton backend (customer_ref)
                int orderId = ApiClient.createOrder(ref, items);

                if (orderId > 0) {
                    Cart.clear();
                    Router.setScene(ConfirmationView.build(orderId, amount, items));
                }
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Erreur API : " + ex.getMessage()).show();
            }
        });

        btnBox.getChildren().addAll(btnBack, btnConfirm);
        footer.getChildren().addAll(inputSection, totalRow, btnBox);
        root.setBottom(footer);

        return new Scene(root, 1280, 800);
    }

    // Méthode pour créer une carte d'article propre
    private static HBox createSummaryRow(CartItem it) {
        HBox row = new HBox(0); // On utilise 0 pour gérer l'alignement par colonnes
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(15, 25, 15, 25));
        row.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 8, 0, 0, 2);");
        row.setMaxWidth(950);

        // --- COLONNE 1 : NOM & OPTIONS (Gauche) ---
        VBox col1 = new VBox(5);
        Label name = new Label(it.dish.name.toUpperCase());
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");
        
        // On affiche les épices et accompagnements directement sous le titre
        String options = "";
        if (it.spice != null && !it.spice.isEmpty()) options += "🌶 Epice : " + it.spice;
        if (it.side != null && !it.side.isEmpty()) options += (options.isEmpty() ? "" : " | ") + "🍚 Accomp : " + it.side;
        
        Label optLabel = new Label(options.isEmpty() ? "Options : Standard" : options);
        optLabel.setStyle("-fx-text-fill: #e67e22; -fx-font-size: 13px; -fx-font-weight: bold;");
        
        col1.getChildren().addAll(name, optLabel);
        col1.setPrefWidth(350); // Largeur fixe pour la première colonne

        // --- COLONNE 2 : QUANTITÉ ---
        Label col2Qty = new Label("x" + it.quantity);
        col2Qty.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");
        col2Qty.setPrefWidth(120);
        col2Qty.setAlignment(Pos.CENTER);

        // --- COLONNE 3 : PRIX UNITAIRE ---
        Label col3Unit = new Label(String.format("%.2f €", it.dish.price));
        col3Unit.setStyle("-fx-font-size: 14px; -fx-text-fill: #95a5a6;");
        col3Unit.setPrefWidth(150);
        col3Unit.setAlignment(Pos.CENTER);

        // --- COLONNE 4 : PRIX TOTAL (Droite) ---
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label col4Total = new Label(String.format("%.2f €", it.totalPrice()));
        col4Total.setStyle("-fx-font-size: 18px; -fx-font-weight: 900; -fx-text-fill: #e74c3c;");
        col4Total.setPrefWidth(120);
        col4Total.setAlignment(Pos.CENTER_RIGHT);

        row.getChildren().addAll(col1, col2Qty, col3Unit, spacer, col4Total);
        return row;
    }
}