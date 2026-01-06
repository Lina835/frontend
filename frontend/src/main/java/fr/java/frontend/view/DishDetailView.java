package fr.java.frontend.view;

import fr.java.frontend.Router;
import fr.java.frontend.cart.Cart;
import fr.java.frontend.model.Dish; // Assure-toi d'avoir ton modèle Dish
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DishDetailView {

    private static int quantity = 1; // Quantité locale pour la vue

    public static Scene build(Dish dish) {
        quantity = 1; // Reset à chaque ouverture

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f8f9fa;");

        // --- HEADER : Retour et Panier ---
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        Button btnBack = new Button("← Détail du plat");
        btnBack.setStyle("-fx-background-color: transparent; -fx-font-size: 24px; -fx-font-weight: bold; -fx-cursor: hand;");
        btnBack.setOnAction(e -> Router.setScene(CatalogueView.build()));
        
        Region spacerHeader = new Region();
        HBox.setHgrow(spacerHeader, Priority.ALWAYS);
        
        Label cartInfo = new Label(String.format("€%.2f", Cart.total()));
        cartInfo.setStyle("-fx-border-color: black; -fx-padding: 10 20; -fx-font-weight: bold; -fx-font-size: 18px;");

        header.getChildren().addAll(btnBack, spacerHeader, cartInfo);
        root.setTop(header);

        // --- CONTENU PRINCIPAL (Split 50/50) ---
        HBox mainContent = new HBox(50);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(40, 0, 0, 0));

        // GAUCHE : Image du plat
        StackPane imageContainer = new StackPane();
        Rectangle placeholder = new Rectangle(450, 350, Color.LIGHTGRAY);
        placeholder.setArcWidth(20);
        placeholder.setArcHeight(20);
        
        Label imgLabel = new Label("image\nplat"); // Comme sur ton schéma
        imgLabel.setStyle("-fx-text-alignment: center;");
        
        imageContainer.getChildren().addAll(placeholder, imgLabel);

        // DROITE : Infos et Options
        VBox rightDetails = new VBox(20);
        rightDetails.setPrefWidth(450);

        Label nameLabel = new Label(dish.name);
        nameLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: 900;");

        Label descLabel = new Label(dish.description);
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");

        Label priceLabel = new Label(String.format("%.2f €", dish.price));
        priceLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        // OPTIONS (Épices)
        VBox spiceBox = new VBox(10);
        Label spiceTitle = new Label("Options\nÉpice : doux / moyen / fort");
        spiceTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        ToggleGroup spiceGroup = new ToggleGroup();
        RadioButton rbDoux = new RadioButton("Doux");
        RadioButton rbMoyen = new RadioButton("Moyen");
        RadioButton rbFort = new RadioButton("Fort");
        rbDoux.setToggleGroup(spiceGroup);
        rbMoyen.setToggleGroup(spiceGroup);
        rbFort.setToggleGroup(spiceGroup);
        rbMoyen.setSelected(true);
        
        HBox spiceOptions = new HBox(20, rbDoux, rbMoyen, rbFort);
        spiceBox.getChildren().addAll(spiceTitle, spiceOptions);

        // ACCOMPAGNEMENT
        VBox sideBox = new VBox(10);
        Label sideTitle = new Label("Accompagnement : riz / nouilles");
        sideTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        ToggleGroup sideGroup = new ToggleGroup();
        RadioButton rbRiz = new RadioButton("Riz");
        RadioButton rbNouilles = new RadioButton("Nouilles");
        rbRiz.setToggleGroup(sideGroup);
        rbNouilles.setToggleGroup(sideGroup);
        rbRiz.setSelected(true);
        
        HBox sideOptions = new HBox(20, rbRiz, rbNouilles);
        sideBox.getChildren().addAll(sideTitle, sideOptions);

        // --- SÉLECTEUR DE QUANTITÉ ET BOUTON AJOUTER ---
        VBox footerAction = new VBox(15);
        footerAction.setPadding(new Insets(20, 0, 0, 0));
        
        Label qtyTitle = new Label("Quantité");
        qtyTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox actionRow = new HBox(20);
        actionRow.setAlignment(Pos.CENTER_LEFT);

        // Sélecteur +/-
        HBox qtySelector = new HBox(10);
        qtySelector.setAlignment(Pos.CENTER);
        qtySelector.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: white; -fx-padding: 5;");
        
        Button btnMinus = new Button("-");
        Label lblQty = new Label("1");
        lblQty.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 0 15;");
        Button btnPlus = new Button("+");
        
        btnMinus.setOnAction(e -> { if(quantity > 1) { quantity--; lblQty.setText(String.valueOf(quantity)); } });
        btnPlus.setOnAction(e -> { quantity++; lblQty.setText(String.valueOf(quantity)); });
        
        qtySelector.getChildren().addAll(btnMinus, lblQty, btnPlus);

        // Bouton Ajouter au Panier
        Button btnAdd = new Button("Ajouter au panier");
        btnAdd.setPrefWidth(300);
        btnAdd.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-radius: 30; -fx-cursor: hand;");
        
        btnAdd.setOnAction(e -> {
            Cart.add(dish, quantity); // On ajoute avec la quantité sélectionnée
            Router.setScene(CatalogueView.build());
        });

        actionRow.getChildren().addAll(qtySelector, btnAdd);
        footerAction.getChildren().addAll(qtyTitle, actionRow);

        rightDetails.getChildren().addAll(nameLabel, descLabel, priceLabel, spiceBox, sideBox, footerAction);
        mainContent.getChildren().addAll(imageContainer, rightDetails);

        root.setCenter(mainContent);

        return new Scene(root, 1280, 720);
    }
}