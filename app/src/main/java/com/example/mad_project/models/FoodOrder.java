package com.example.mad_project.models;

//Represents an order of a specific food item with a certain amount in the user's cart
public class FoodOrder {

    private int amount;
    private FoodItem food;
    private String name;//Serves as a key for HashMap in Cart

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
