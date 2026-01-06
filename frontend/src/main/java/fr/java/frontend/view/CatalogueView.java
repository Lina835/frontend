package fr.java.frontend.view;

import fr.java.frontend.Router;
import fr.java.frontend.api.ApiClient;
import fr.java.frontend.cart.Cart;
import fr.java.frontend.model.Category;
import fr.java.frontend.model.Dish;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.List;

public class CatalogueView {

    public static Scene build() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;"); // Fond gris perle

        // --- TOP BAR (Menu + Total Panier) ---
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(20, 30, 20, 30));
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        Label menuTitle = new Label("☰  MENU");
        menuTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 900; -fx-text-fill: #2d3436;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Petit badge prix en haut à droite (comme ton schéma)
        Label cartPriceBadge = new Label(String.format("🛒 %.2f €", Cart.total()));
        cartPriceBadge.setStyle(
            "-fx-background-color: white; -fx-border-color: #2d3436; -fx-border-radius: 5; " +
            "-fx-padding: 8 15; -fx-font-weight: bold; -fx-font-size: 18px;"
        );
        
        topBar.getChildren().addAll(menuTitle, spacer, cartPriceBadge);
        root.setTop(topBar);

        // --- CENTRE (Onglets + Grille) ---
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        // Style CSS pour les onglets
        tabPane.setStyle("-fx-tab-min-width: 120px; -fx-tab-min-height: 40px;");

        try {
            List<Category> categories = ApiClient.getCategories();
            for (Category c : categories) {
                Tab tab = new Tab(c.name.toUpperCase());
                
                // Grille de produits (TilePane)
                TilePane grid = new TilePane();
                grid.setHgap(25);
                grid.setVgap(25);
                grid.setPadding(new Insets(30));
                grid.setAlignment(Pos.TOP_LEFT);
                grid.setPrefColumns(3);

                List<Dish> dishes = ApiClient.getDishes(c.id);
                for (Dish d : dishes) {
                    grid.getChildren().add(dishCard(d));
                }

                ScrollPane scroll = new ScrollPane(grid);
                scroll.setFitToWidth(true);
                scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
                tab.setContent(scroll);
                
                tabPane.getTabs().add(tab);
            }
        } catch (Exception ex) {
            root.setCenter(new Label("Erreur de chargement : " + ex.getMessage()));
        }
        root.setCenter(tabPane);

        // --- BOTTOM BAR (Navigation) ---
        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(20, 30, 20, 30));
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setSpacing(400); // Espace entre Accueil et Panier

        Button btnHome = new Button("ACCUEIL");
        btnHome.setStyle("-fx-background-color: white; -fx-border-color: #2d3436; -fx-padding: 12 30; -fx-font-weight: bold; -fx-background-radius: 5; -fx-border-radius: 5;");
        btnHome.setOnAction(e -> Router.setScene(HomeView.build()));

        Button btnCart = new Button("PANIER");
        btnCart.setStyle("-fx-background-color: #2d3436; -fx-text-fill: white; -fx-padding: 12 40; -fx-font-weight: bold; -fx-background-radius: 5;");
        btnCart.setOnAction(e -> Router.setScene(CartView.build()));

        bottomBar.getChildren().addAll(btnHome, btnCart);
        root.setBottom(bottomBar);

        return new Scene(root, 1280, 800);
    }

    // --- CARTE PRODUIT STYLE "PRO" ---
    private static VBox dishCard(Dish d) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setPrefWidth(300);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 15; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );

        // Placeholder Image (Gris comme sur ton schéma)
        StackPane imgPlaceholder = new StackPane();
        Rectangle rect = new Rectangle(270, 180, Color.web("#ecf0f1"));
        rect.setArcWidth(15);
        rect.setArcHeight(15);
        Label imgTxt = new Label("PHOTO PLAT");
        imgTxt.setStyle("-fx-text-fill: #bdc3c7;");
        imgPlaceholder.getChildren().addAll(rect, imgTxt);

        // Infos
        Label name = new Label(d.name);
        name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");
        
        Label price = new Label(String.format("%.2f €", d.price));
        price.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: #e74c3c;");

        // Bouton AJOUTER
        Button btnAdd = new Button("AJOUTER");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setStyle(
            "-fx-background-color: white; -fx-border-color: #2d3436; " +
            "-fx-font-weight: bold; -fx-padding: 8; -fx-cursor: hand;"
        );
        
        // Clic sur AJOUTER -> Va vers le détail du plat
        btnAdd.setOnAction(e -> Router.setScene(DishDetailView.build(d)));

        if (!d.available) {
            btnAdd.setText("INDISPONIBLE");
            btnAdd.setDisable(true);
            card.setOpacity(0.6);
        }

        card.getChildren().addAll(imgPlaceholder, name, price, btnAdd);
        return card;
    }
}