package fr.java.frontend.api;
import fr.java.frontend.api.dto.CreateOrderRequest;
import fr.java.frontend.api.dto.CreateOrderResponse;
import fr.java.frontend.cart.CartItem;

import java.util.ArrayList;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.java.frontend.model.Category;
import fr.java.frontend.model.Dish;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:7000";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Category> getCategories() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/categories"))
                    .GET()
                    .build();

            String json = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
            return mapper.readValue(json, new TypeReference<List<Category>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Erreur API categories", e);
        }
    }

    public static List<Dish> getDishes(int categoryId) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/dishes?categoryId=" + categoryId))
                    .GET()
                    .build();

            String json = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
            return mapper.readValue(json, new TypeReference<List<Dish>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Erreur API dishes", e);
        }
    }


public static int createOrder(String customerRef, java.util.List<CartItem> cartItems) {
    try {
        // Construire la requête pour le backend
        CreateOrderRequest reqBody = new CreateOrderRequest();
        reqBody.customerRef = customerRef;
        reqBody.items = new ArrayList<>();

        for (CartItem it : cartItems) {
            CreateOrderRequest.Item item = new CreateOrderRequest.Item();
            item.dishId = it.dish.id;
            item.quantity = it.quantity;
            item.options = new ArrayList<>(); // options vides pour l’instant
            reqBody.items.add(item);
        }

        String jsonBody = mapper.writeValueAsString(reqBody);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/orders"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() != 201) {
            throw new RuntimeException("Erreur backend (" + resp.statusCode() + "): " + resp.body());
        }

        CreateOrderResponse created = mapper.readValue(resp.body(), CreateOrderResponse.class);
        return created.id;

    } catch (Exception e) {
        throw new RuntimeException("Erreur API createOrder", e);
    }
}


}
