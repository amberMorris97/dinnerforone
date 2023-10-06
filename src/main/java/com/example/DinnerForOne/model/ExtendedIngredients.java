package com.example.DinnerForOne.model;

import java.util.List;

public class ExtendedIngredients {
    private double servings;
    private List<Ingredient> extendedIngredients;

    public List<Ingredient> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<Ingredient> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }
    public double getServings() {
        return servings;
    }
    public void setServings(double servings) {
        this.servings = servings;
    }
}
