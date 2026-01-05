package fr.java.frontend;

import fr.java.frontend.api.ApiClient;
import fr.java.frontend.model.Category;
import fr.java.frontend.model.Dish;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import fr.java.frontend.DishDetailView;


import java.util.List;

public class CatalogueView {

    public static Scene build() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: white;");

        Button cartBtn = new Button("🛒 Voir le panier");
        cartBtn.setOnAction(e -> Router.setScene(CartView.build()));

cartBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        root.setBottom(cartBtn);
BorderPane.setAlignment(cartBtn, Pos.CENTER);
BorderPane.setMargin(cartBtn, new Insets(10));

        // ✅ Bouton retour
        Button back = new Button("← Retour Accueil");
        back.setOnAction(e -> Router.setScene(HomeView.build()));
        root.setTop(back);
        BorderPane.setMargin(back, new Insets(0, 0, 10, 0));

        // ✅ Onglets catégories
        TabPane tabPane = new TabPane();
        root.setCenter(tabPane);

        try {
            List<Category> categories = ApiClient.getCategories();

            for (Category c : categories) {
                Tab tab = new Tab(c.name);
                tab.setClosable(false);

                // Grille de plats
                TilePane grid = new TilePane();
                grid.setHgap(15);
                grid.setVgap(15);
                grid.setPadding(new Insets(10));
                grid.setPrefColumns(3);

                List<Dish> dishes = ApiClient.getDishes(c.id);

                for (Dish d : dishes) {
                    grid.getChildren().add(dishCard(d));
                }

                ScrollPane scroll = new ScrollPane(grid);
                scroll.setFitToWidth(true);
                tab.setContent(scroll);

                tabPane.getTabs().add(tab);
            }

        } catch (Exception ex) {
            // Si backend OFF ou erreur : on affiche un message clair
            VBox errBox = new VBox(10);
            errBox.setAlignment(Pos.CENTER);
            errBox.getChildren().addAll(
                    new Text("❌ Impossible de charger le catalogue."),
                    new Text("Vérifie que le backend est lancé sur http://localhost:7000"),
                    new Text("Erreur : " + ex.getMessage())
            );
            root.setCenter(errBox);
        }

        return new Scene(root, 1000, 650);
    }

    // ✅ Une "carte" plat (simple)
    private static VBox dishCard(Dish d) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        box.setPrefWidth(260);
        box.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;"
        );

        Label name = new Label(d.name);
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label price = new Label(String.format("%.2f €", d.price));

        Button details = new Button("Détails");
        details.setDisable(!d.available);

        // ✅ Quand on clique -> écran détail (on fera juste après)
      details.setOnAction(e -> {
    Router.setScene(DishDetailView.build(d));
});


        if (!d.available) {
            Label soldOut = new Label("Indisponible");
            soldOut.setStyle("-fx-text-fill: red;");
            box.getChildren().addAll(name, price, soldOut, details);
        } else {
            box.getChildren().addAll(name, price, details);
        }

        return box;
    }
}
