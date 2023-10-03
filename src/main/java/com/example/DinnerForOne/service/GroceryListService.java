package com.example.DinnerForOne.service;

import com.example.DinnerForOne.model.ExtendedIngredients;
import com.example.DinnerForOne.model.Ingredient;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GroceryListService {
    @Value("${apiKey}")
    private String apiKey;
    private final WebClient webClient;
    private final Gson gson;

    public GroceryListService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.spoonacular.com")
                .build();
        this.gson = new Gson();
    }

    public List<Ingredient> fetchIngredientsForRecipe(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", apiKey);

        Mono<ExtendedIngredients> responseMono = webClient.get()
                .uri("/recipes/extract?url=" + url)
                .headers(h -> h.addAll(headers))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ExtendedIngredients.class);

        ExtendedIngredients extendedIngredients = responseMono.block();

        if (extendedIngredients != null && extendedIngredients.getExtendedIngredients() != null) {
            return extendedIngredients.getExtendedIngredients();
        } else {
            return null;
        }
    }

    private List<Ingredient> calculateIngredientRatio() {
      return null;
    }
}