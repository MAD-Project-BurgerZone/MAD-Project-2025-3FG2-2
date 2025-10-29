package com.example.mad_project.models;

//Inner Food Class for to put in cart
public class FoodOrder {

    private int amount;
    private FoodItem food;
    private String name;

    public FoodOrder(FoodItem food, int amount){
        this.amount = amount;
        this.food = food;
        name = food.getFood();
    }

    public FoodItem getFood() {
        return food;
    }

    public void setFood(FoodItem food) {
        this.food = food;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
