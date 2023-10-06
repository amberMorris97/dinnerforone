package com.example.DinnerForOne.model;

import java.util.List;

public class ExtendedIngredients {
    private int servings;
    private List<Ingredient> extendedIngredients;

    public List<Ingredient> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<Ingredient> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }
    public int getServings() {
        return servings;
    }
    public void setServings(int servings) {
        this.servings = servings;
    }
}
