package fr.java.frontend.view;

import fr.java.frontend.Router;
import fr.java.frontend.api.ApiClient;
import fr.java.frontend.cart.Cart;
import fr.java.frontend.model.Category;
import fr.java.frontend.model.Dish;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.InputStream;
import java.util.List;

public class CatalogueView {

    private static Category selectedCategory = null;

    public static Scene build() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;");
        root.setPadding(new Insets(20));

        // HEADER
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("CATALOGUE");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: 900;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button cartBtn = new Button(String.format("Panier • €%.2f", Cart.total()));
        cartBtn.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        cartBtn.setOnAction(e -> Router.setScene(CartView.build()));

        header.getChildren().addAll(title, spacer, cartBtn);
        root.setTop(header);

        // PLACEHOLDER pendant chargement
        Label loading = new Label("Chargement du catalogue...");
        loading.setStyle("-fx-font-size: 18px; -fx-text-fill: #636e72; -fx-font-weight: bold;");
        root.setCenter(new StackPane(loading));

        Task<Void> task = new Task<>() {
            List<Category> categories;
            List<Dish> dishes;

            @Override
            protected Void call() {
                categories = ApiClient.getCategories();

                if (selectedCategory == null && categories != null && !categories.isEmpty()) {
                    selectedCategory = categories.get(0);
                }
                if (selectedCategory != null) {
                    dishes = ApiClient.getDishes(selectedCategory.id);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                // LEFT categories
                VBox categoriesBox = new VBox(10);
                categoriesBox.setPadding(new Insets(20, 20, 20, 0));
                categoriesBox.setPrefWidth(240);

                if (categories != null) {
                    for (Category c : categories) {
                        Button b = new Button(c.name);
                        b.setMaxWidth(Double.MAX_VALUE);

                        boolean selected = (selectedCategory != null && c.id == selectedCategory.id);
                        b.setStyle(selected
                                ? "-fx-background-color: black; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;"
                                : "-fx-background-color: white; -fx-border-color: #ddd; -fx-font-weight: bold; -fx-cursor: hand;");

                        b.setOnAction(e -> {
                            selectedCategory = c;
                            Router.setScene(CatalogueView.build());
                        });

                        categoriesBox.getChildren().add(b);
                    }
                }

                root.setLeft(categoriesBox);

                // Centre dishes grid
                FlowPane grid = new FlowPane();
                grid.setHgap(20);
                grid.setVgap(20);
                grid.setPadding(new Insets(20));
                grid.setPrefWrapLength(900);

                if (dishes != null) {
                    for (Dish d : dishes) {
                        grid.getChildren().add(dishCard(d));
                    }
                }

                ScrollPane scroll = new ScrollPane(grid);
                scroll.setFitToWidth(true);
                scroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                root.setCenter(scroll);
            }

            @Override
            protected void failed() {
                Throwable ex = getException();
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Impossible de charger le catalogue");
                a.setContentText(ex != null ? ex.getMessage() : "Erreur inconnue");
                a.show();

                root.setCenter(new StackPane(new Label("Erreur de chargement.")));
            }
        };

        new Thread(task).start();
        return new Scene(root, 1280, 720);
    }

    private static VBox dishCard(Dish dish) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(12));
        card.setPrefWidth(240);

        boolean disabled = dish != null && !dish.available;

        card.setStyle(disabled
                ? "-fx-background-color: #f0f0f0; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ddd;"
                : "-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #eee; -fx-cursor: hand;");

        ImageView img = loadDishImage(dish != null ? dish.icon : null, 220, 140);
        if (disabled) img.setOpacity(0.45);

        String safeName = (dish != null && dish.name != null) ? dish.name.trim() : "";
        Label name = new Label(safeName);
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");

        Label price = new Label(String.format("%.2f €", dish != null ? dish.price : 0.0));
        price.setStyle("-fx-font-weight: 900; -fx-font-size: 14px;");

        card.getChildren().addAll(img, name, price);

        if (disabled) {
            Label badge = new Label("INDISPONIBLE");
            badge.setStyle("-fx-text-fill: #d63031; -fx-font-weight: bold;");
            card.getChildren().add(badge);
        } else {
            card.setOnMouseClicked(e -> {
                boolean showOptions = shouldShowOptionsForSelectedCategory();
                Router.setScene(DishDetailView.build(dish, showOptions));
            });
        }

        return card;
    }

    private static boolean shouldShowOptionsForSelectedCategory() {
        if (selectedCategory == null || selectedCategory.name == null) return true;
        String c = selectedCategory.name.toLowerCase();
        return !(c.contains("dessert") || c.contains("boisson") || c.contains("drink") || c.contains("beverage"));
    }

    private static ImageView loadDishImage(String iconFileName, double w, double h) {
        ImageView iv = new ImageView();
        iv.setFitWidth(w);
        iv.setFitHeight(h);
        iv.setPreserveRatio(false);
        iv.setSmooth(true);

        if (iconFileName == null || iconFileName.isBlank()) return iv;

        String path = "/images/" + iconFileName;
        try (InputStream is = CatalogueView.class.getResourceAsStream(path)) {
            if (is != null) iv.setImage(new Image(is));
        } catch (Exception ignored) {}

        return iv;
    }
}
