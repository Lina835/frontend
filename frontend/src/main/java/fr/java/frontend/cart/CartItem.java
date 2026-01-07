package fr.java.frontend.cart;

import fr.java.frontend.model.Dish;
import java.util.Objects;

public class CartItem {
    public Dish dish;
    public int quantity;

    // options (null si pas d'options)
    public String spice;   // "Doux", "Moyen", "Fort"
    public String side;    // "Riz", "Nouilles"

    public CartItem(Dish dish) {
        this(dish, 1, null, null); // ✅ plus de valeurs par défaut
    }

    public CartItem(Dish dish, int quantity, String spice, String side) {
        this.dish = dish;
        this.quantity = quantity;
        this.spice = normalize(spice);
        this.side  = normalize(side);
    }

    public double totalPrice() {
        if (dish == null) return 0.0;
        return dish.price * quantity;
    }

    // ✅ compare produit + options (pour empiler correctement)
    public boolean sameProductAndOptions(Dish otherDish, String otherSpice, String otherSide) {
        if (otherDish == null || this.dish == null) return false;
        if (this.dish.id != otherDish.id) return false;

        String s1 = normalize(this.spice);
        String s2 = normalize(otherSpice);

        String a1 = normalize(this.side);
        String a2 = normalize(otherSide);

        return Objects.equals(s1, s2) && Objects.equals(a1, a2);
    }

    private static String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
