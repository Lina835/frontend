package fr.java.frontend.api.dto;

import java.util.List;
/**
 * DTO (Data Transfer Object) utilisé pour envoyer une commande au serveur.
 * Cette classe structure les données pour la conversion en JSON.
 */
public class CreateOrderRequest {
    // Référence ou nom du client pour identifier la commande
    public String customerRef;
    // Liste des articles contenus dans la commande
    public List<Item> items;
    // Représente un article individuel dans le panier.
    public static class Item {
        public int dishId;
        public int quantity;
        public List<String> options; // on mettra vide pour l’instant
    }
}
