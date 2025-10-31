package com.example.mad_project.data;
import android.graphics.drawable.Drawable;

import com.example.mad_project.R;
import com.example.mad_project.models.FoodItem;
import com.example.mad_project.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataProvider {

    public static HashMap<String, User> provideUsers(){

        HashMap<String, User> users = new HashMap<>();

        users.put("user1@example.com", new User("user1@example.com", "UserOne", "password1"));
        users.put("user2", new User("user2@example.com", "UserTwo", "password2"));
        users.put("user3", new User("user3@example.com", "UserThree", "password3"));
        users.put("user4", new User("user4@example.com", "UserFour", "password4"));
        users.put("user5", new User("user5@example.com", "UserFive", "password5"));
        users.put("user6", new User("user6@example.com", "UserSix", "password6"));
        users.put("user7", new User("user7@example.com", "UserSeven", "password7"));
        users.put("user8", new User("user8@example.com", "UserEight", "password8"));
        users.put("user9", new User("user9@example.com", "UserNine", "password9"));
        users.put("user10", new User("user10@example.com", "UserTen", "password10"));

        return users;
    }

    public static List<FoodItem> provideFoodItems(){

        List<FoodItem> foods = new ArrayList<>();

        foods.add(new FoodItem("Classic Cheeseburger", 150, "A timeless burger with fresh ingredients.", R.drawable.burgerimage_classiccheeseburger));
        foods.add(new FoodItem("Bacon Burger", 180, "Juicy beef patty topped with crispy bacon.", R.drawable.burgerimage_baconburger));
        foods.add(new FoodItem("Double Cheeseburger", 220, "Two beef patties stacked with melted cheese.", R.drawable.burgerimage_doublecheeseburger));
        foods.add(new FoodItem("Mushroom Swiss Burger", 200, "Savory mushrooms and Swiss cheese on a beef patty.", R.drawable.burgerimage_mushroomswissburger));
        foods.add(new FoodItem("BBQ Burger", 190, "Smoky BBQ sauce and crispy onion rings.", R.drawable.burgerimage_bbqburger));
        foods.add(new FoodItem("Spicy Chicken Burger", 170, "A chicken burger with a kick of spice.", R.drawable.burgerimage_spicychickenburger));
        foods.add(new FoodItem("Veggie Burger", 160, "Healthy and delicious vegetarian option.", R.drawable.burgerimage_veggieburger));
        foods.add(new FoodItem("Hawaiian Burger", 200, "Sweet pineapple and ham on a juicy burger.", R.drawable.burgerimage_hawaiianburger));
        foods.add(new FoodItem("Cheese Lover's Burger", 210, "Loaded with three kinds of cheese.", R.drawable.burgerimage_cheeseloversburger));
        foods.add(new FoodItem("Bacon & Egg Burger", 220, "Perfect breakfast burger with bacon and egg.", R.drawable.burgerimage_baconandeggburger));
        foods.add(new FoodItem("Buffalo Chicken Burger", 180, "Spicy buffalo chicken with creamy blue cheese.", R.drawable.burgerimage_buffalochickenburger));
        foods.add(new FoodItem("Avocado Burger", 200, "Creamy avocado topping on a juicy beef patty.", R.drawable.burgerimage_avocadoburger));
        foods.add(new FoodItem("Teriyaki Burger", 190, "Sweet and savory Japanese-inspired burger.", R.drawable.burgerimage_teriyakiburger));
        foods.add(new FoodItem("Double Bacon Burger", 240, "Extra meaty burger with double bacon and patties.", R.drawable.burgerimage_doublebaconburger));
        foods.add(new FoodItem("Classic Hamburger", 140, "Simple and classic hamburger with fresh veggies.", R.drawable.burgerimage_classichamburger));


        return foods;

    }

}
