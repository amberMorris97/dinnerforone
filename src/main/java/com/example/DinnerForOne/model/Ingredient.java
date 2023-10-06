package com.example.DinnerForOne.model;

public class Ingredient {
    private String name;
    private String amount;
    private String unit;
    private String aisle;
    private double originalServingSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public double getOriginalServingSize() {
        return originalServingSize;
    }

    public void setOriginalServingSize(double originalServingSize) {
        this.originalServingSize = originalServingSize;
    }
}
