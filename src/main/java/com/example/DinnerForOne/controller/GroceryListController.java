package com.example.DinnerForOne.controller;

import com.example.DinnerForOne.model.Ingredient;
import com.example.DinnerForOne.service.GroceryListService;
import com.example.DinnerForOne.util.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroceryListController {
    private final GroceryListService groceryListService;
    private final UrlValidator urlValidator;

    @Autowired
    public GroceryListController(GroceryListService groceryListService, UrlValidator urlValidator) {
        this.groceryListService = groceryListService;
        this.urlValidator = urlValidator;
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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + e.getMessage());
  }
}

