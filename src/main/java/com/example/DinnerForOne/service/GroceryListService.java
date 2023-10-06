package com.example.DinnerForOne.service;

import com.example.DinnerForOne.model.ExtendedIngredients;
import com.example.DinnerForOne.model.Ingredient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GroceryListService {
    @Value("${apiKey}")
    private String apiKey;
    private final WebClient webClient;

    public GroceryListService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.spoonacular.com")
                .build();
    }

    public List<Ingredient> fetchIngredientsForRecipe(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        String urlRegex = "^(https?|ftp)://[\\w\\d\\-.]+(:\\d+)?(/\\S*)?$";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(url);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid URL format.");

        }

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
            return Collections.emptyList();
        }
    }

}