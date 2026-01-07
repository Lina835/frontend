package fr.java.frontend.view;

import fr.java.frontend.Router;
import fr.java.frontend.cart.Cart;
import fr.java.frontend.cart.CartItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class CartView {

    public static Scene build() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;");

        // HEADER
        HBox header = new HBox();
        header.setPadding(new Insets(25, 30, 10, 30));

        Button back = new Button("← Continuer mes achats");
        back.setStyle("-fx-background-color: transparent; -fx-font-size: 24px; -fx-font-weight: bold; -fx-cursor: hand;");
        back.setOnAction(e -> Router.setScene(CatalogueView.build()));

        header.getChildren().add(back);
        root.setTop(header);

        // CENTRE
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

        // FOOTER
        VBox bottom = new VBox(20);
        bottom.setPadding(new Insets(20, 50, 40, 50));
        bottom.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, -5);");
        bottom.setAlignment(Pos.CENTER);

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
    // On bascule vers l'écran de récapitulatif pour saisir le nom/table
    Router.setScene(RecapulatifView.build());
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

        // ✅ Infos produit
        VBox info = new VBox(6);

        String safeName = getSafeDishName(it);
        Label name = new Label(safeName);
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");
        name.setWrapText(true);

        double unit = (it != null && it.dish != null) ? it.dish.price : 0.0;
        Label unitPrice = new Label(String.format("%.2f € / unité", unit));
        unitPrice.setStyle("-fx-text-fill: #636e72; -fx-font-size: 13px;");

        // ✅ options (si présentes seulement)
        String optLine = buildOptionsLine(it);
        Label options = new Label(optLine);
        options.setStyle("-fx-text-fill: #636e72; -fx-font-size: 12px;");
        options.setWrapText(true);
        options.setVisible(!optLine.isBlank());
        options.setManaged(!optLine.isBlank());

        // ✅ IMPORTANT: on ajoute BIEN le nom (en premier)
        info.getChildren().addAll(name, unitPrice, options);
        info.setPrefWidth(420);

        // ✅ Quantité
        HBox qtyCtrl = new HBox(15);
        qtyCtrl.setAlignment(Pos.CENTER);

        Button minus = new Button("-");
        minus.setStyle("-fx-background-color: #f1f2f6; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        minus.setOnAction(e -> {
            Cart.decrease(it.dish.id);
            Router.setScene(CartView.build());
        });

        Label qty = new Label(String.valueOf(it.quantity));
        qty.setStyle("-fx-text-fill: #2d3436; -fx-font-size: 18px; -fx-font-weight: bold; -fx-min-width: 30; -fx-alignment: center;");

        Button plus = new Button("+");
        plus.setStyle("-fx-background-color: #f1f2f6; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        plus.setOnAction(e -> {
            Cart.increase(it.dish.id);
            Router.setScene(CartView.build());
        });

        qtyCtrl.getChildren().addAll(minus, qty, plus);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label price = new Label(String.format("%.2f €", it.totalPrice()));
        price.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");
        price.setPrefWidth(120);
        price.setAlignment(Pos.CENTER_RIGHT);

        line.getChildren().addAll(info, qtyCtrl, spacer, price);
        return line;
    }

    private static String getSafeDishName(CartItem it) {
        if (it == null || it.dish == null) return "PRODUIT";
        String n = it.dish.name;
        if (n != null && !n.trim().isEmpty()) return n.trim();
        return "PRODUIT #" + it.dish.id;
    }

    private static String buildOptionsLine(CartItem it) {
        StringBuilder sb = new StringBuilder();

        if (it != null && it.spice != null && !it.spice.isBlank()) {
            sb.append("Épice: ").append(it.spice.trim());
        }
        if (it != null && it.side != null && !it.side.isBlank()) {
            if (sb.length() > 0) sb.append(" • ");
            sb.append("Accompagnement: ").append(it.side.trim());
        }

        return sb.toString();
    }
}
