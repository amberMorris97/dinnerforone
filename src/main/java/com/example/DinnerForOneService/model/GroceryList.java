package com.example.DinnerForOneService.model;

import java.util.Map;

public class GroceryList {

    private final String id;
    private Map<String, String> groceries;

    public GroceryList(final String id, Map<String, String> groceries) {
        this.id = id;
        this.groceries = groceries;
    }


}
