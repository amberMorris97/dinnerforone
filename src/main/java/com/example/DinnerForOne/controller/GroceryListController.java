package com.example.DinnerForOne.controller;

import com.example.DinnerForOne.GenerateGroceryListRequest;
import com.example.DinnerForOne.model.Ingredient;
import com.example.DinnerForOne.service.ChatGPTService;
import com.example.DinnerForOne.service.GroceryListService;
import com.example.DinnerForOne.util.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class GroceryListController {
    private final GroceryListService groceryListService;
    private final UrlValidator urlValidator;

    private ChatGPTService chatGPTService;

    @Autowired
    public GroceryListController(GroceryListService groceryListService, ChatGPTService chatGPTService, UrlValidator urlValidator) {
        this.groceryListService = groceryListService;
        this.urlValidator = urlValidator;
        this.chatGPTService = chatGPTService;
    }

    @GetMapping("/fetch-ingredients")
    @ResponseBody
    public ResponseEntity<?> fetchIngredientsForRecipe(@RequestParam String url) {
        UrlValidator.validateUrl(url);

        try {
            List<Ingredient> ingredientsForRecipe = groceryListService.fetchIngredientsForRecipe(url);
            return ResponseEntity.ok(ingredientsForRecipe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching ingredients: " + e.getMessage());
        }
    }

    @PostMapping("/generate-grocery-list")
    public ResponseEntity<Mono<String>> generateGroceryList(@RequestBody GenerateGroceryListRequest request) {
        String recipeUrl = request.getRecipeUrl();
        int newServingSize = request.getNewServingSize();
        List<Ingredient> ingredientsForRecipe = groceryListService.fetchIngredientsForRecipe(recipeUrl);

        Mono<String> responseBody = chatGPTService.generateGroceryList(ingredientsForRecipe, newServingSize);

        return ResponseEntity.ok(responseBody);

//        try {
//            List<Ingredient> ingredientsForRecipe = groceryListService.fetchIngredientsForRecipe(recipeUrl);
//
//            if (ingredientsForRecipe.size() > 0) {
//                try {
//                    chatGPTService.generateGroceryList(ingredientsForRecipe, newServingSize);
//                } catch()
//            }
//        }

    }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + e.getMessage());
  }
}

