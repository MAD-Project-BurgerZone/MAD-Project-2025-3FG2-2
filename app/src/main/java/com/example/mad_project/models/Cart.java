package com.example.mad_project.models;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.mad_project.R;
import com.example.mad_project.utils.AlertDialogBuilder;

import java.io.Serializable;
import java.util.HashMap;

public class Cart implements Serializable {

    private HashMap<String, FoodOrder> cart;
    private double totalPrice;

    public Cart(User owner) {
        if (owner.getUserCart() == null) {
            owner.setUserCart(this);
            totalPrice = 0;
            if (cart == null) {
                cart = new HashMap<>();
            }
        }
    }

    public void addFoodItem(FoodItem food, int amount, Context context) {
        if (!cart.containsKey(food.getFood())) {
            cart.put(food.getFood(), new FoodOrder(food, amount));
            totalPrice += food.getPrice() * amount;
            Toast.makeText(context, "Added " + food.getFood() + " to the Cart!", Toast.LENGTH_SHORT).show();
            return;
        }
        FoodOrder currFood = cart.get(food.getFood());
        currFood.setAmount(currFood.getAmount() + amount);
        totalPrice += food.getPrice() * amount;
        Toast.makeText(context, "Added " + food.getFood() + " to the Cart!", Toast.LENGTH_SHORT).show();
    }

    public void reduceFoodItem(FoodItem food, int amount, Context context) {
        if (!cart.containsKey(food.getFood())) {
            AlertDialogBuilder dialog = new AlertDialogBuilder(context, "Invalid!", "Can't Remove A Non-Existing Order!", true, null);
        } else {
            if (cart.get(food.getFood()).getAmount() < amount) {
                AlertDialogBuilder dialog = new AlertDialogBuilder(context, "Invalid!", "Can't Remove Amount greater than the current amount", true, null);
                return;
            }
            FoodOrder currFood = cart.get(food.getFood());
            currFood.setAmount(currFood.getAmount() - amount);
            Toast.makeText(context, "Removed " + amount + " " + food.getFood() + " from the Cart!", Toast.LENGTH_SHORT).show();
            totalPrice -= food.getPrice() * amount;
        }
    }

    public HashMap<String, FoodOrder> getCart() {
        return cart;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (FoodOrder order : cart.values()) {
            total += order.getFood().getPrice() * order.getAmount();
        }
        return total;
    }

    public int getTotalItems() {
        int total = 0;
        for (FoodOrder order : cart.values()) {
            total += order.getAmount();
        }
        return total;
    }

    public void refreshCartView(Context context, LinearLayout cartBTN, User currentUser) {
        if (cartBTN == null) {
            return;
        }
        if (currentUser.getUserCart().getTotalItems() > 0) {
            View existing = cartBTN.findViewWithTag("cart_red_dot");
            if (existing != null) {
                cartBTN.removeView(existing);
            }
            int total = currentUser.getUserCart().getTotalItems();
            String display = total > 99 ? "99+" : String.valueOf(total);
            TextView redDot = new TextView(context);
            redDot.setTag("cart_red_dot");
            redDot.setText(display);
            redDot.setTextColor(ContextCompat.getColor(context, R.color.white));
            redDot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            redDot.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
            redDot.setGravity(Gravity.CENTER);
            android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
            bg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            bg.setColor(android.graphics.Color.parseColor("#FF3B30"));
            bg.setSize(dpToPx(15), dpToPx(15));
            redDot.setBackground(bg);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dpToPx(18), dpToPx(18));
            lp.gravity = Gravity.START;
            cartBTN.addView(redDot, lp);
        } else {
            View existing = cartBTN.findViewWithTag("cart_red_dot");
            if (existing != null) {
                cartBTN.removeView(existing);
            }
        }
    }

    private int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
