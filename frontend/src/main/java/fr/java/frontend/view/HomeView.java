package fr.java.frontend.view;

import fr.java.frontend.Router;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import fr.java.frontend.util.StoreStatus;

public class HomeView {

    public static Scene build() {
        // --- FOND ÉLÉGANT ---
        boolean isOpen = StoreStatus.isCurrentlyOpen(); 
        VBox root = new VBox(50); // Espacement entre logo, texte et bouton
        root.setAlignment(Pos.CENTER);
        // Dégradé très léger pour un aspect premium
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f8f9fa);");

        // --- LOGO ---
        // On essaie de charger le logo, sinon on met un texte stylé pour ne pas faire crash le code
        ImageView logo;
        try {
            logo = new ImageView(new Image(HomeView.class.getResourceAsStream("/logo.png")));
            logo.setFitWidth(350); // Plus grand pour l'accueil
            logo.setPreserveRatio(true);
        } catch (Exception e) {
            // Fallback si l'image n'est pas trouvée
            logo = new ImageView(); 
            System.out.println("Logo non trouvé, vérifie le dossier resources.");
        }

        // --- TITRE DE BIENVENUE ---
        VBox welcomeText = new VBox(10);
        welcomeText.setAlignment(Pos.CENTER);
        
        Label brandName = new Label("ASIATIK EXPRESS");
        brandName.setOnMouseClicked(e -> showAdminLogin()); 
        brandName.setCursor(javafx.scene.Cursor.HAND);
        brandName.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #2d3436; -fx-letter-spacing: 3px;");
        
        Label slogan = new Label("Le meilleur de l'Asie, à portée de main.");
        slogan.setStyle("-fx-font-size: 18px; -fx-text-fill: #636e72; -fx-font-style: italic;");
        
        welcomeText.getChildren().addAll(brandName, slogan);

        // --- BOUTON START STYLE BORNE ---
        Button start = new Button();

if (isOpen) {
    // SI OUVERT : Ton style actuel
    start.setText("COMMENCER MA COMMANDE");
    start.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold; -fx-padding: 20 50; -fx-background-radius: 40; -fx-cursor: hand;");
    start.setOnAction(e -> Router.setScene(CatalogueView.build()));
} else {
    // SI FERMÉ : Style grisé et bouton bloqué
    start.setText("BORNE FERMÉE (OUVERTURE 11H)");
    start.setDisable(true); // Bloque le clic
    start.setStyle("-fx-background-color: #636e72; -fx-text-fill: #dfe6e9; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 20 50; -fx-background-radius: 40;");
}
        
        // Animation simple au clic
        start.setOnMousePressed(e -> start.setStyle(start.getStyle() + "-fx-scale-x: 0.95; -fx-scale-y: 0.95;"));
        start.setOnMouseReleased(e -> start.setStyle(start.getStyle() + "-fx-scale-x: 1.0; -fx-scale-y: 1.0;"));

        // Navigation
        start.setOnAction(e -> Router.setScene(CatalogueView.build()));

        // Ajout des éléments
        root.getChildren().addAll(logo, welcomeText, start);

        // On ajuste la taille pour une borne (souvent 1080p ou 720p)
        return new Scene(root, 1280, 720);
    }

    private static void showAdminLogin() {
    // Création d'une boîte de dialogue pour saisir le texte
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Accès Administration");
    dialog.setHeaderText("Borne verrouillée (Hors horaires)");
    dialog.setContentText("Entrez le code PIN gérant :");

    // On récupère le résultat
    dialog.showAndWait().ifPresent(code -> {
        if (code.equals(StoreStatus.ADMIN_CODE)) {
            // Si le code est 1234, on force l'entrée vers le catalogue
            Router.setScene(CatalogueView.build());
        } else {
            // Sinon, petit message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR, "Code incorrect.");
            alert.show();
        }
    });
}
}

