package com.example.mad_project.models;

import android.graphics.drawable.Drawable;

public class FoodItem {

    private String food;
    private double price;
    private String description;

    public FoodItem(String food, double price, String description){

        this.food = food;
        this.price = price;
        this.description = description;

    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
