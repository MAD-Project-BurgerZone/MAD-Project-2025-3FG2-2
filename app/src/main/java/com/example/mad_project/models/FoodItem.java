package com.example.mad_project.models;

public class FoodItem {

    private String food;
    private int price;
    private String[] details;
    private String description;

    public FoodItem(String food, int price, String[] details, String description){

        this.food = food;
        this.price = price;
        this.details = details;
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

    public String[] getDetails() {
        return details;
    }

    public void setDetails(String[] details) {
        this.details = details;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
