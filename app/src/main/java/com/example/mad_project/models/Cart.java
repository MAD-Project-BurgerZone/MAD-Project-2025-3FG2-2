package com.example.mad_project.models;

import android.content.Context;

import com.example.mad_project.utils.AlertDialogBuilder;

import java.io.Serializable;
import java.util.HashMap;

public class Cart implements Serializable {

    private HashMap<String, FoodOrder> cart;

    public Cart(User owner){

        //Link Cart to User if not already linked
        if(owner.getUserCart() == null){
            owner.setUserCart(this);
        }
        //Initialize Cart HashMap if Null
        if(cart == null){
            cart = new HashMap<>();
        }

    }

    public void addFoodItem(FoodItem food, int amount, Context context){

        //If cart doesn't have the food yet
        if(!cart.containsKey(food.getFood())){
            cart.put(food.getFood(), new FoodOrder(food, amount));
            AlertDialogBuilder dialog = new AlertDialogBuilder(context, "Success!","Added " + amount + " " + food.getFood() + " to the Cart!", true, null);
            return;
        }

        //Get Existing Food from Cart
        FoodOrder currFood = cart.get(food.getFood());
        currFood.setAmount(currFood.getAmount() + amount);

    }

    public void reduceFoodItem(FoodItem food, int amount, Context context) {

        //Error Handle
        if (!cart.containsKey(food.getFood())) {
            AlertDialogBuilder dialog = new AlertDialogBuilder(context, "Invalid!", "Can't Remove A Non-Existing Order!", true, null);
        } else {

            //If User Input is more than the current amount
            if (cart.get(food.getFood()).getAmount() < amount) {
                AlertDialogBuilder dialog = new AlertDialogBuilder(context, "Invalid!", "Can't Remove Amount greater than the current amount", true, null);
                return;
            }
            //Reduce item amount
            FoodOrder currFood = cart.get(food.getFood());
            currFood.setAmount(currFood.getAmount() - amount);

        }
    }

    public HashMap<String, FoodOrder> getCart() {
        return cart;
    }

}
