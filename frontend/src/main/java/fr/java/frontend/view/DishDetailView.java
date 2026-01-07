package fr.java.frontend.view;

import fr.java.frontend.Router;
import fr.java.frontend.cart.Cart;
import fr.java.frontend.model.Dish;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.InputStream;

/**
 * Vue détaillée d'un plat permettant la personnalisation (épices, accompagnements)
 * et le choix de la quantité avant l'ajout au panier.
 */
public class DishDetailView {

    private static int quantity = 1;

    // Compat si jamais tu l'appelles quelque part
    public static Scene build(Dish dish) {
        return build(dish, true);
    }

    // showOptions = true pour PLATS, false pour DESSERT/BOISSON
    public static Scene build(Dish dish, boolean showOptions) {
        quantity = 1;

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f8f9fa;");

        // --- HEADER
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

        // --- MAIN
        HBox mainContent = new HBox(50);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(40, 0, 0, 0));

        // GAUCHE : Image
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(450, 350);

        Rectangle bg = new Rectangle(450, 350, Color.LIGHTGRAY);
        bg.setArcWidth(20);
        bg.setArcHeight(20);

        ImageView photo = loadDishImage(dish != null ? dish.icon : null, 450, 350);

        Label imgLabel = new Label("image\nplat");
        imgLabel.setStyle("-fx-text-alignment: center; -fx-text-fill: #555;");

        imageContainer.getChildren().addAll(bg, photo);
        if (photo.getImage() == null) imageContainer.getChildren().add(imgLabel);

        // DROITE : Infos
        VBox rightDetails = new VBox(20);
        rightDetails.setPrefWidth(450);

        String safeName = (dish != null && dish.name != null) ? dish.name : "";
        String safeDesc = (dish != null && dish.description != null) ? dish.description : "";
        double safePrice = (dish != null) ? dish.price : 0.0;

        Label nameLabel = new Label(safeName);
        nameLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: 900;");

        Label descLabel = new Label(safeDesc);
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");

        Label priceLabel = new Label(String.format("%.2f €", safePrice));
        priceLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        // OPTIONS seulement si showOptions=true
        ToggleGroup spiceGroup = new ToggleGroup();
        RadioButton rbDoux = new RadioButton("Doux");
        RadioButton rbMoyen = new RadioButton("Moyen");
        RadioButton rbFort = new RadioButton("Fort");
        rbDoux.setToggleGroup(spiceGroup);
        rbMoyen.setToggleGroup(spiceGroup);
        rbFort.setToggleGroup(spiceGroup);
        rbMoyen.setSelected(true);

        VBox spiceBox = new VBox(10);
        spiceBox.setVisible(showOptions);
        spiceBox.setManaged(showOptions);
        Label spiceTitle = new Label("Options\nÉpice : doux / moyen / fort");
        spiceTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        HBox spiceOptions = new HBox(20, rbDoux, rbMoyen, rbFort);
        spiceBox.getChildren().addAll(spiceTitle, spiceOptions);

        ToggleGroup sideGroup = new ToggleGroup();
        RadioButton rbRiz = new RadioButton("Riz");
        RadioButton rbNouilles = new RadioButton("Nouilles");
        rbRiz.setToggleGroup(sideGroup);
        rbNouilles.setToggleGroup(sideGroup);
        rbRiz.setSelected(true);

        VBox sideBox = new VBox(10);
        sideBox.setVisible(showOptions);
        sideBox.setManaged(showOptions);
        Label sideTitle = new Label("Accompagnement : riz / nouilles");
        sideTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        HBox sideOptions = new HBox(20, rbRiz, rbNouilles);
        sideBox.getChildren().addAll(sideTitle, sideOptions);

        // --- Quantité + Ajouter
        VBox footerAction = new VBox(15);
        footerAction.setPadding(new Insets(20, 0, 0, 0));

        Label qtyTitle = new Label("Quantité");
        qtyTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox actionRow = new HBox(20);
        actionRow.setAlignment(Pos.CENTER_LEFT);

        HBox qtySelector = new HBox(10);
        qtySelector.setAlignment(Pos.CENTER);
        qtySelector.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: white; -fx-padding: 5;");

        Button btnMinus = new Button("-");
        Label lblQty = new Label("1");
        lblQty.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 0 15;");
        Button btnPlus = new Button("+");

        btnMinus.setOnAction(e -> {
            if (quantity > 1) {
                quantity--;
                lblQty.setText(String.valueOf(quantity));
            }
        });
        btnPlus.setOnAction(e -> {
            quantity++;
            lblQty.setText(String.valueOf(quantity));
        });

        qtySelector.getChildren().addAll(btnMinus, lblQty, btnPlus);

        Button btnAdd = new Button("Ajouter au panier");
        btnAdd.setPrefWidth(300);
        btnAdd.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-radius: 30; -fx-cursor: hand;");

        btnAdd.setOnAction(e -> {
            if (dish == null) return;

            // SI DESSERT/BOISSON => options null (donc pas d'accompagnement dans le panier)
            if (!showOptions) {
                Cart.add(dish, quantity, null, null);
            } else {
                String spice = ((RadioButton) spiceGroup.getSelectedToggle()).getText();
                String side = ((RadioButton) sideGroup.getSelectedToggle()).getText();
                Cart.add(dish, quantity, spice, side);
            }

            Router.setScene(CatalogueView.build());
        });

        actionRow.getChildren().addAll(qtySelector, btnAdd);
        footerAction.getChildren().addAll(qtyTitle, actionRow);

        rightDetails.getChildren().addAll(nameLabel, descLabel, priceLabel, spiceBox, sideBox, footerAction);

        mainContent.getChildren().addAll(imageContainer, rightDetails);
        root.setCenter(mainContent);

        return new Scene(root, 1280, 720);
    }

    private static ImageView loadDishImage(String iconFileName, double w, double h) {
        ImageView iv = new ImageView();
        iv.setFitWidth(w);
        iv.setFitHeight(h);
        iv.setPreserveRatio(false);
        iv.setSmooth(true);

        if (iconFileName == null || iconFileName.isBlank()) return iv;

        String path = "/images/" + iconFileName;
        try (InputStream is = DishDetailView.class.getResourceAsStream(path)) {
            if (is != null) iv.setImage(new Image(is));
        } catch (Exception ignored) {}

        return iv;
    }
}
