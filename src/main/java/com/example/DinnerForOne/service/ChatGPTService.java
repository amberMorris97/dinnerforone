package com.example.DinnerForOne.service;

import com.example.DinnerForOne.model.Ingredient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ChatGPTService {

    @Value("@openapi.apiKey")
    private String apiKey;

    private final WebClient webClient;

    public ChatGPTService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat")
                .defaultHeader("Authorization", "Bearer", apiKey)
                .build();
    }

    public Mono<String> generateGroceryList(List<Ingredient> extendedIngredients, int newServingSize) {
            String chatGptInput = "Generate a grocery list for " + newServingSize + " servings based on the ingredients:\n";

            for (Ingredient ingredient : extendedIngredients) {
                chatGptInput += ingredient.getName() + " - " + ingredient.getAmount() + " " + ingredient.getUnit() + "\n";
            }

            Mono<String> responseMono = webClient.post()
                    .uri("/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue("{\"prompt\":\"" + chatGptInput + "\"}"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnNext(response -> {
                        System.out.print("Received GPT response: " + response);
                    })
                    .onErrorResume(e -> {
                        e.printStackTrace();
                        return Mono.just("Error occurred: " + e.getMessage());
                    })
                    .map(this::transformChatGptResponse)
                    .doOnNext(transformedResponse -> {
                        System.out.print("Transformed response: " + transformedResponse);
                    });

            return responseMono;
    }

    private String transformChatGptResponse(String chatGptResponse) {
        // todo: ensure chatGptResponse is not an error message

        String[] lines = chatGptResponse.split("\n");

        StringBuilder formattedGroceryList = new StringBuilder();

        if (lines.length > 0) {
            formattedGroceryList.append("Generated Grocery List:\n");

            for (String line : lines) {
                String[] parts = line.split("-");

                if (parts.length == 2) {
                    String ingredientName = parts[0].trim();
                    String ingredientAmount = parts[1].trim();

                    formattedGroceryList.append(ingredientName).append(" - ").append(ingredientAmount);
                }
            }
        } else {
            formattedGroceryList.append("No grocery list generated");
        }

        return formattedGroceryList.toString();
    }
}
