package fr.java.frontend.cart;

import fr.java.frontend.model.Dish;

public class CartItem {
    public Dish dish;
    public int quantity;

    // Constructeur par défaut (pour le catalogue : ajoute 1)
    public CartItem(Dish dish) {
        this.dish = dish;
        this.quantity = 1;
    }

    // NOUVEAU : Constructeur avec quantité (pour la ProductDetailView)
    public CartItem(Dish dish, int quantity) {
        this.dish = dish;
        this.quantity = quantity;
    }

    public double totalPrice() {
        return dish.price * quantity;
    }
}