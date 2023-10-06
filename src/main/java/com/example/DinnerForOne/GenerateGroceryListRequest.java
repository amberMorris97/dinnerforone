package com.example.DinnerForOne;

public class GenerateGroceryListRequest {
    private String recipeUrl;
    private int newServingSize;

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public int getNewServingSize() {
        return newServingSize;
    }

    public void setNewServingSize(int servingSize) {
        this.newServingSize = servingSize;
    }
}
