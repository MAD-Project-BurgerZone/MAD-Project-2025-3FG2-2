package com.example.mad_project.controllers;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mad_project.R;
import com.example.mad_project.models.Cart;
import com.example.mad_project.models.FoodOrder;
import com.example.mad_project.models.User;
import com.example.mad_project.utils.IntentKeys;

import java.util.HashMap;

public class CartpageActivity extends AppCompatActivity {

    User currentUser;
    Cart userCart;
    TextView totalPriceTXT;
    Button checkoutBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cartpage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }

    private void initialize(){

        Intent intent = getIntent();
        currentUser = User.UserList.getUser(intent.getStringExtra(IntentKeys.USER_EMAIL));
        LinearLayout cartContainer = findViewById(R.id.cartContainer);
        totalPriceTXT = findViewById(R.id.tvTotalPrice);
        checkoutBTN = findViewById(R.id.btnCheckout);

        NavBarControl.initializeNavBarControls(this, currentUser,
                findViewById(R.id.navMenu),
                findViewById(R.id.navCart),
                findViewById(R.id.navLogout),
                findViewById(R.id.notificationContainer));

        //Put total price
        totalPriceTXT.setText(String.format("Total: PHP%.2f", currentUser.getUserCart().calculateTotalPrice()));

        if(currentUser.getUserCart().calculateTotalPrice() <= 0){
            //Disable Checkout Button if cart is empty
            checkoutBTN.setEnabled(false);
            checkoutBTN.setAlpha(0.5f);
        } else {
            checkoutBTN.setAlpha(1f);
        }

        cartContainer.removeAllViews();

        if(currentUser.getUserCart().getCart().isEmpty()){
            displayNoCartMessage();
            return;
        }

        checkoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartpageActivity.this, CheckoutActivity.class);
                intent.putExtra(IntentKeys.USER_EMAIL, currentUser.getEmail());
                startActivity(intent);
            }
        });

        // Iterate through cart items and display them
        for (FoodOrder order : currentUser.getUserCart().getCart().values()) {

            // Parent layout for each cart item
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setGravity(Gravity.CENTER_VERTICAL);
            int pad = dpToPx(12);
            itemLayout.setPadding(pad, pad, pad, pad);
            itemLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_background));
            itemLayout.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lightgray));
            itemLayout.setElevation(dpToPx(3));

            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            itemParams.setMargins(0, 0, 0, dpToPx(8));
            itemLayout.setLayoutParams(itemParams);

            // ----- IMAGE -----
            ImageView itemImage = new ImageView(this);
            itemImage.setImageResource(order.getFood().getImageResourceId());
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dpToPx(60), dpToPx(60));
            imgParams.setMarginEnd(dpToPx(12));
            itemImage.setLayoutParams(imgParams);
            itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            itemLayout.addView(itemImage);

            // ----- TEXT INFO -----
            LinearLayout infoLayout = new LinearLayout(this);
            infoLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            infoLayout.setLayoutParams(infoParams);

            TextView itemName = new TextView(this);
            itemName.setText(order.getName());
            itemName.setTextSize(16);
            itemName.setTypeface(null, Typeface.BOLD);
            itemName.setTextColor(ContextCompat.getColor(this, R.color.black)); // ✅ fixed color

            TextView itemPrice = new TextView(this);
            itemPrice.setText(String.format("PHP%.2f", order.getFood().getPrice()));
            itemPrice.setTextSize(14);
            itemPrice.setTextColor(ContextCompat.getColor(this, R.color.black));

            infoLayout.addView(itemName);
            infoLayout.addView(itemPrice);
            itemLayout.addView(infoLayout);

            // ----- QUANTITY CONTROLS -----
            LinearLayout qtyLayout = new LinearLayout(this);
            qtyLayout.setOrientation(LinearLayout.HORIZONTAL);
            qtyLayout.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams qtyParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            qtyParams.setMargins(dpToPx(8), 0, 0, 0);
            qtyLayout.setLayoutParams(qtyParams);

            // Decrease Button
            ImageButton btnDecrease = new ImageButton(this);
            btnDecrease.setImageResource(R.drawable.minus_white);
            btnDecrease.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_background));
            btnDecrease.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkgreen));
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(dpToPx(35), dpToPx(35));
            btnDecrease.setLayoutParams(btnParams);
            btnDecrease.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            btnDecrease.setPadding(dpToPx(6), dpToPx(6), dpToPx(6), dpToPx(6));

            // Quantity text
            TextView tvQuantity = new TextView(this);
            tvQuantity.setText(String.valueOf(order.getAmount()));
            tvQuantity.setGravity(Gravity.CENTER);
            tvQuantity.setTextSize(16);
            tvQuantity.setTypeface(null, Typeface.BOLD);
            LinearLayout.LayoutParams qtyTextParams = new LinearLayout.LayoutParams(dpToPx(40), LinearLayout.LayoutParams.WRAP_CONTENT);
            qtyTextParams.setMargins(dpToPx(6), 0, dpToPx(6), 0);
            tvQuantity.setLayoutParams(qtyTextParams);
            tvQuantity.setTextColor(ContextCompat.getColor(this, R.color.black));

            // Increase Button
            ImageButton btnIncrease = new ImageButton(this);
            btnIncrease.setImageResource(R.drawable.add_white);
            btnIncrease.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_background));
            btnIncrease.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkgreen));
            btnIncrease.setLayoutParams(btnParams);
            btnIncrease.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            btnIncrease.setPadding(dpToPx(6), dpToPx(6), dpToPx(6), dpToPx(6));

            // Add to layout
            qtyLayout.addView(btnDecrease);
            qtyLayout.addView(tvQuantity);
            qtyLayout.addView(btnIncrease);
            itemLayout.addView(qtyLayout);

            // ----- BUTTON LOGIC -----
            btnIncrease.setOnClickListener(v -> {
                order.setAmount(order.getAmount() + 1);
                Toast.makeText(this, "Increased " + order.getName() + " to " + order.getAmount(), Toast.LENGTH_SHORT).show();
                tvQuantity.setText(String.valueOf(order.getAmount()));
                currentUser.getUserCart().refreshCartView(this, findViewById(R.id.notificationContainer), currentUser);
                updateTotalPrice();
            });

            btnDecrease.setOnClickListener(v -> {
                if (order.getAmount() > 1) {
                    order.setAmount(order.getAmount() - 1);
                    Toast.makeText(this, "Decreased " + order.getName() + " to " + order.getAmount(), Toast.LENGTH_SHORT).show();
                    tvQuantity.setText(String.valueOf(order.getAmount()));
                    currentUser.getUserCart().refreshCartView(this, findViewById(R.id.notificationContainer), currentUser);
                    updateTotalPrice();
                } else {
                    Toast.makeText(this, "Decreased " + order.getName() + " to " + order.getAmount(), Toast.LENGTH_SHORT).show();
                    currentUser.getUserCart().getCart().remove(order.getName());
                    cartContainer.removeView(itemLayout);
                    currentUser.getUserCart().refreshCartView(this, findViewById(R.id.notificationContainer), currentUser);
                    updateTotalPrice();
                }
            });

            // ----- ADD TO CONTAINER -----
            cartContainer.addView(itemLayout);
        }

    }

    private void updateTotalPrice(){
        double totalPrice = currentUser.getUserCart().calculateTotalPrice();
        totalPriceTXT.setText(String.format("Total: PHP%.2f", totalPrice));
        if(totalPrice <= 0){
            checkoutBTN.setEnabled(false);
            checkoutBTN.setAlpha(0.5f);
            displayNoCartMessage();
        } else {
            checkoutBTN.setEnabled(true);
            checkoutBTN.setAlpha(1f);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void displayNoCartMessage() {
        LinearLayout cartContainer = findViewById(R.id.cartContainer);
        cartContainer.removeAllViews();

        TextView noCartMessage = new TextView(this);
        noCartMessage.setText("Your cart is empty.");
        noCartMessage.setTextSize(18);
        noCartMessage.setTypeface(null, Typeface.BOLD);
        noCartMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
        noCartMessage.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams msgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        msgParams.setMargins(0, dpToPx(20), 0, 0);
        noCartMessage.setLayoutParams(msgParams);

        cartContainer.addView(noCartMessage);
    }

}