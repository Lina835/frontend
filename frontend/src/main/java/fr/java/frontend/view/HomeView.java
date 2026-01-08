package fr.java.frontend.view;

import fr.java.frontend.Router;
import fr.java.frontend.util.StoreStatus;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class HomeView {

    public static Scene build() {
        boolean isOpen = StoreStatus.isCurrentlyOpen();

        VBox root = new VBox(50);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f8f9fa);");

        ImageView logo;
        try {
            logo = new ImageView(new Image(HomeView.class.getResourceAsStream("/logo.png")));
            logo.setFitWidth(350);
            logo.setPreserveRatio(true);
        } catch (Exception e) {
            logo = new ImageView();
            System.out.println("Logo non trouvé, vérifie le dossier resources.");
        }

        VBox welcomeText = new VBox(10);
        welcomeText.setAlignment(Pos.CENTER);

        Label brandName = new Label("ASIATIK EXPRESS");
        brandName.setOnMouseClicked(e -> showAdminLogin());
        brandName.setCursor(javafx.scene.Cursor.HAND);
        brandName.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #2d3436; -fx-letter-spacing: 3px;");

        Label slogan = new Label("Le meilleur de l'Asie, à portée de main.");
        slogan.setStyle("-fx-font-size: 18px; -fx-text-fill: #636e72; -fx-font-style: italic;");

        welcomeText.getChildren().addAll(brandName, slogan);

        Button start = new Button();

        if (isOpen) {
            start.setText("COMMENCER MA COMMANDE");
            start.setDisable(false);
            start.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold; -fx-padding: 20 50; -fx-background-radius: 40; -fx-cursor: hand;");
            start.setOnAction(e -> Router.setScene(CatalogueView.build()));
        } else {
            start.setText("BORNE FERMÉE (OUVERTURE 11H)");
            start.setDisable(true);
            start.setStyle("-fx-background-color: #636e72; -fx-text-fill: #dfe6e9; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 20 50; -fx-background-radius: 40;");
        }

        // Animation simple (sans casser le style)
        start.setOnMousePressed(e -> start.setScaleX(0.97));
        start.setOnMousePressed(e -> start.setScaleY(0.97));
        start.setOnMouseReleased(e -> {
            start.setScaleX(1.0);
            start.setScaleY(1.0);
        });

        root.getChildren().addAll(logo, welcomeText, start);
        return new Scene(root, 1280, 720);
    }

    private static void showAdminLogin() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Accès Administration");
        dialog.setHeaderText("Borne verrouillée (Hors horaires)");
        dialog.setContentText("Entrez le code PIN gérant :");

        dialog.showAndWait().ifPresent(code -> {
            if (code.equals(StoreStatus.ADMIN_CODE)) {
                Router.setScene(CatalogueView.build());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Code incorrect.");
                alert.show();
            }
        });
    }
}
