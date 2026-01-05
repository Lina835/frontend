package fr.java.frontend.cart;

import fr.java.frontend.model.Dish;

public class CartItem {
    public Dish dish;
    public int quantity;

    public CartItem(Dish dish) {
        this.dish = dish;
        this.quantity = 1;
    }

    public double totalPrice() {
        return dish.price * quantity;
    }
}
