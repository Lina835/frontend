package fr.java.frontend.cart;

import fr.java.frontend.model.Dish;
import java.util.ArrayList;
import java.util.List;

public class Cart {

    private static final List<CartItem> items = new ArrayList<>();

    public static List<CartItem> getItems() {
        return items;
    }

    public static double total() {
        double sum = 0;
        for (CartItem it : items) sum += it.totalPrice();
        return sum;
    }

    public static void clear() {
        items.clear();
    }

    // ajout simple (dessert/boisson typiquement)
    public static void add(Dish dish) {
        add(dish, 1, null, null);
    }

    // ✅ ajout avec options
    public static void add(Dish dish, int quantity, String spice, String side) {
        for (CartItem it : items) {
            if (it.sameProductAndOptions(dish, spice, side)) {
                it.quantity += quantity;
                return;
            }
        }
        items.add(new CartItem(dish, quantity, spice, side));
    }

    public static void increase(int dishId) {
        for (CartItem it : items) {
            if (it.dish != null && it.dish.id == dishId) {
                it.quantity++;
                return;
            }
        }
    }

    public static void decrease(int dishId) {
        for (int i = 0; i < items.size(); i++) {
            CartItem it = items.get(i);
            if (it.dish != null && it.dish.id == dishId) {
                it.quantity--;
                if (it.quantity <= 0) items.remove(i);
                return;
            }
        }
    }
}
