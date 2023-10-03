package com.example.DinnerForOne.model;

import java.util.List;

public class GroceryList {
    private Long id;
    private String name;
    private List<GroceryItem> items;
    public GroceryList(Long id, String name, List<GroceryItem> items) {
        this.id = id;
        this.name = name;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GroceryItem> getItems() {
        return items;
    }

    public void setItems(List<GroceryItem> items) {
        this.items = items;
    }
}
