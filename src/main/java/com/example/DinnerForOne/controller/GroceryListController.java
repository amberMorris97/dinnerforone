package com.example.DinnerForOne.controller;

import com.example.DinnerForOne.model.Ingredient;
import com.example.DinnerForOne.service.GroceryListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GroceryListController {
    private final GroceryListService groceryListService;

    @Autowired
    public GroceryListController(GroceryListService groceryListService) {
        this.groceryListService = groceryListService;
    }

    @GetMapping("/grocery-list")
    @ResponseBody
    public ResponseEntity<?> getGroceryList(@RequestParam String url) {
        try {
            List<Ingredient> groceryList = groceryListService.fetchIngredientsForRecipe(url);
            return ResponseEntity.ok(groceryList);
        } catch (Exception e) {
            return null;
        }
    }

}

