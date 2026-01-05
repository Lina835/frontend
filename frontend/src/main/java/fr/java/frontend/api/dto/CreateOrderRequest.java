package fr.java.frontend.api.dto;

import java.util.List;

public class CreateOrderRequest {
    public String customerRef;
    public List<Item> items;

    public static class Item {
        public int dishId;
        public int quantity;
        public List<String> options; // on mettra vide pour l’instant
    }
}
