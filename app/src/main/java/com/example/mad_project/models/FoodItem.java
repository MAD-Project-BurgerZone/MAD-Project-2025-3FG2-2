package com.example.mad_project.models;

import android.graphics.drawable.Drawable;

public class FoodItem {

    private String food;
    private double price;
    private String description;
    private int imageResourceId;

    public FoodItem(String food, double price, String description, int imageResourceId) {

        this.food = food;
        this.price = price;
        this.description = description;
        this.imageResourceId = imageResourceId;

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
    public int getImageResourceId() {
        return imageResourceId;
    }
    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

}
