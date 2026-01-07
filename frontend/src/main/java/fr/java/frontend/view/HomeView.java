package fr.java.frontend.view;

import fr.java.frontend.Router;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class HomeView {

    public static Scene build() {
        // --- FOND ÉLÉGANT ---
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
        brandName.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #2d3436; -fx-letter-spacing: 3px;");
        
        Label slogan = new Label("Le meilleur de l'Asie, à portée de main.");
        slogan.setStyle("-fx-font-size: 18px; -fx-text-fill: #636e72; -fx-font-style: italic;");
        
        welcomeText.getChildren().addAll(brandName, slogan);

        // --- BOUTON START STYLE BORNE ---
        Button start = new Button("COMMENCER MA COMMANDE");
        
        // Style CSS Pro : Noir, coins arrondis, effet au survol
        start.setStyle(
            "-fx-background-color: #e74c3c; " + // Rouge Impérial
            "-fx-text-fill: white; " +
            "-fx-font-size: 22px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 20 50; " +
            "-fx-background-radius: 40; " + // Très arrondi
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(231, 76, 60, 0.4), 15, 0, 0, 8);"
        );

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
}

