package com.example.mad_project.controllers;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import com.example.mad_project.utils.AlertDialogBuilder;
import com.example.mad_project.utils.IntentKeys;

public class CheckoutActivity extends AppCompatActivity {

    //Elements
    Button checkoutBTN;
    LinearLayout itemContainer;
    RadioButton cashRBTN, otherRBTN;
    String selectedPaymentMethod;
    String selectedDeliveryOption;
    User currentUser;
    TextView subtotalTXT, deliveryFeeTXT, totalPriceTXT;
    LinearLayout priorityOption, standardOption, saverOption;
    TextView priorityTXT, standardTXT, saverTXT;
    TextView priorityPriceTXT, standardPriceTXT, saverPriceTXT;
    boolean isPrioritySelected = false;
    boolean isStandardSelected = false;
    boolean isSaverSelected = false;
    double totalPrice;
    LinearLayout otherChoice, cashChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.checkout);
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

        itemContainer = findViewById(R.id.checkoutContainer);
        checkoutBTN = findViewById(R.id.checkoutBTN);
        otherRBTN = findViewById(R.id.otherRBTN);
        cashRBTN = findViewById(R.id.cashRBTN);
        subtotalTXT = findViewById(R.id.subtotalTXT);
        deliveryFeeTXT = findViewById(R.id.deliveryFeeTXT);
        totalPriceTXT = findViewById(R.id.totalPriceTXT);
        totalPrice = currentUser.getUserCart().calculateTotalPrice();
        priorityOption = findViewById(R.id.priorityOption);
        standardOption = findViewById(R.id.standardOption);
        saverOption = findViewById(R.id.saverOption);
        priorityTXT = findViewById(R.id.priorityTXT);
        standardTXT = findViewById(R.id.standardTXT);
        saverTXT = findViewById(R.id.saverTXT);
        priorityPriceTXT = findViewById(R.id.priorityPriceTXT);
        standardPriceTXT = findViewById(R.id.standardPriceTXT);
        saverPriceTXT = findViewById(R.id.saverPriceTXT);
        otherChoice = findViewById(R.id.otherChoice);
        cashChoice = findViewById(R.id.cashChoice);

        //Clickable Layouts for Payment Method
        cashChoice.setOnClickListener(view -> cashRBTN.setChecked(true));
        otherChoice.setOnClickListener(view -> otherRBTN.setChecked(true));

        //Manual RadioButton Behavior, Since each radio button is in different containers
        cashRBTN.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedPaymentMethod = "Cash on Delivery";
            if (isChecked) otherRBTN.setChecked(false);
        });
        otherRBTN.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedPaymentMethod = "Other Payment Methods";
            if (isChecked) cashRBTN.setChecked(false);
        });

        checkoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckoutSummaryDialog();
            }
        });

        //Get Selected Delivery Option
        getSelectedDeliveryOptionLinearLayout();

        //Initialize the Navbar
        NavBarControl.initializeNavBarControls(this, currentUser,
                findViewById(R.id.navMenu),
                findViewById(R.id.navCart),
                findViewById(R.id.navLogout),
                findViewById(R.id.notificationContainer));

        //Generate Checkout Items
        generateCheckoutItems(currentUser.getUserCart());
        subtotalTXT.setText(String.format("PHP %.2f", totalPrice));
        updateCheckoutButtonState();

    }

    private void generateCheckoutItems(Cart cart) {
        LinearLayout checkoutContainer = findViewById(R.id.checkoutContainer);
        checkoutContainer.removeAllViews();

        for (FoodOrder order : cart.getCart().values()) {
            // ----- PARENT CARD -----
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
            LinearLayout.LayoutParams infoParams =
                    new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            infoLayout.setLayoutParams(infoParams);

            // Item Name
            TextView itemName = new TextView(this);
            itemName.setText(order.getName());
            itemName.setTextSize(15);
            itemName.setTypeface(null, Typeface.BOLD);
            itemName.setTextColor(ContextCompat.getColor(this, R.color.black));

            // Quantity text (e.g., "x2")
            TextView itemQuantity = new TextView(this);
            itemQuantity.setText(String.format("x%d", order.getAmount()));
            itemQuantity.setTextSize(14);
            itemQuantity.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));
            itemQuantity.setTypeface(null, Typeface.BOLD);

            infoLayout.addView(itemName);
            infoLayout.addView(itemQuantity);
            itemLayout.addView(infoLayout);

            // ----- TOTAL PRICE -----
            TextView itemTotalPrice = new TextView(this);
            double total = order.getFood().getPrice() * order.getAmount();
            itemTotalPrice.setText(String.format("PHP %.2f", total));
            itemTotalPrice.setTextSize(14);
            itemTotalPrice.setTypeface(null, Typeface.BOLD);
            itemTotalPrice.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));
            itemTotalPrice.setGravity(Gravity.END);

            LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            priceParams.setMargins(dpToPx(8), 0, 0, 0);
            itemTotalPrice.setLayoutParams(priceParams);

            itemLayout.addView(itemTotalPrice);

            // ----- ADD TO CONTAINER -----
            checkoutContainer.addView(itemLayout);
        }

    }

    // Utility
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private boolean isAnyDeliveryOptionSelected() {
        return isPrioritySelected || isStandardSelected || isSaverSelected;
    }

    private void getSelectedDeliveryOptionLinearLayout() {

        priorityOption.setOnClickListener(view -> {

            resetDeliveryOptions();

            isPrioritySelected = true;
            isStandardSelected = false;
            isSaverSelected = false;
            selectedDeliveryOption = "Priority Delivery";
            deliveryFeeTXT.setText("PHP 100.00");
            totalPriceTXT.setText(String.format("PHP %.2f", totalPrice + 100.00));

            priorityOption.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkgreen));
            priorityTXT.setTextColor(ContextCompat.getColor(this, R.color.white));
            priorityPriceTXT.setTextColor(ContextCompat.getColor(this, R.color.white));

            updateCheckoutButtonState();
        });

        standardOption.setOnClickListener(view -> {
            resetDeliveryOptions();

            isPrioritySelected = false;
            isStandardSelected = true;
            isSaverSelected = false;
            selectedDeliveryOption = "Standard Delivery";
            deliveryFeeTXT.setText("PHP 50.00");
            totalPriceTXT.setText(String.format("PHP %.2f", totalPrice + 50.00));

            standardOption.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkgreen));
            standardTXT.setTextColor(ContextCompat.getColor(this, R.color.white));
            standardPriceTXT.setTextColor(ContextCompat.getColor(this, R.color.white));

            updateCheckoutButtonState();
        });

        saverOption.setOnClickListener(view -> {
            resetDeliveryOptions();

            isPrioritySelected = false;
            isStandardSelected = false;
            isSaverSelected = true;
            selectedDeliveryOption = "Saver Delivery";
            deliveryFeeTXT.setText("PHP 20.00");
            totalPriceTXT.setText(String.format("PHP %.2f", totalPrice + 20.00));

            saverOption.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkgreen));
            saverTXT.setTextColor(ContextCompat.getColor(this, R.color.white));
            saverPriceTXT.setTextColor(ContextCompat.getColor(this, R.color.white));

            updateCheckoutButtonState();
        });
    }

    private void resetDeliveryOptions() {
        // Reset backgrounds
        priorityOption.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lightgray));
        standardOption.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lightgray));
        saverOption.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lightgray));

        // Reset text colors
        priorityTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgray));
        priorityPriceTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));

        standardTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgray));
        standardPriceTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));

        saverTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgray));
        saverPriceTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));
    }

    private void updateCheckoutButtonState(){
        if(isAnyDeliveryOptionSelected()){
            checkoutBTN.setEnabled(true);
            checkoutBTN.setAlpha(1.0f);
        } else {
            checkoutBTN.setEnabled(false);
            checkoutBTN.setAlpha(0.5f);
        }
    }

    private void showCheckoutSummaryDialog() {
        // === Build Scrollable Custom View ===
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));

        // Title
        TextView title = new TextView(this);
        title.setText("Order Summary");
        title.setTypeface(null, Typeface.BOLD);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        title.setTextColor(ContextCompat.getColor(this, R.color.black));
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setPadding(0, 0, 0, dpToPx(10));
        mainLayout.addView(title);

        // Scroll container for items
        android.widget.ScrollView scrollView = new android.widget.ScrollView(this);
        LinearLayout itemListLayout = new LinearLayout(this);
        itemListLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(itemListLayout);

        // Populate items from cart
        for (FoodOrder order : currentUser.getUserCart().getCart().values()) {
            LinearLayout itemRow = new LinearLayout(this);
            itemRow.setOrientation(LinearLayout.HORIZONTAL);
            itemRow.setPadding(0, dpToPx(8), 0, dpToPx(8));
            itemRow.setGravity(Gravity.CENTER_VERTICAL);

            // === IMAGE ===
            ImageView itemImage = new ImageView(this);
            itemImage.setImageResource(order.getFood().getImageResourceId());
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dpToPx(45), dpToPx(45));
            imgParams.setMarginEnd(dpToPx(10));
            itemImage.setLayoutParams(imgParams);
            itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            itemRow.addView(itemImage);

            // === NAME, QUANTITY, TOTAL ===
            TextView itemName = new TextView(this);
            itemName.setText(order.getName());
            itemName.setTextColor(ContextCompat.getColor(this, R.color.black));
            itemName.setTextSize(14);
            itemName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            TextView itemAmount = new TextView(this);
            itemAmount.setText(String.format("x%d", order.getAmount()));
            itemAmount.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));
            itemAmount.setTypeface(null, Typeface.BOLD);
            itemAmount.setPadding(dpToPx(4), 0, dpToPx(4), 0);

            TextView itemTotal = new TextView(this);
            double total = order.getFood().getPrice() * order.getAmount();
            itemTotal.setText(String.format("PHP %.2f", total));
            itemTotal.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));
            itemTotal.setTypeface(null, Typeface.BOLD);
            itemTotal.setPadding(dpToPx(8), 0, 0, 0);

            itemRow.addView(itemName);
            itemRow.addView(itemAmount);
            itemRow.addView(itemTotal);

            // Add item row to list
            itemListLayout.addView(itemRow);
        }

        mainLayout.addView(scrollView);

        // --- Delivery Info ---
        TextView deliveryInfo = new TextView(this);
        deliveryInfo.setTypeface(null, Typeface.BOLD);
        deliveryInfo.setText("Delivery Option: " + selectedDeliveryOption);
        deliveryInfo.setTextColor(ContextCompat.getColor(this, R.color.black));
        deliveryInfo.setPadding(0, dpToPx(10), 0, 0);
        mainLayout.addView(deliveryInfo);

        // --- Payment Info ---
        TextView paymentInfo = new TextView(this);
        paymentInfo.setTypeface(null, Typeface.BOLD);
        paymentInfo.setText("Payment Method: " + selectedPaymentMethod);
        paymentInfo.setTextColor(ContextCompat.getColor(this, R.color.black));
        paymentInfo.setPadding(0, dpToPx(6), 0, 0);
        mainLayout.addView(paymentInfo);

        // --- Total Price ---
        TextView totalTXT = new TextView(this);
        totalTXT.setText(String.format("Total Price: %s", totalPriceTXT.getText()));
        totalTXT.setTypeface(null, Typeface.BOLD);
        totalTXT.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        totalTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));
        totalTXT.setPadding(0, dpToPx(10), 0, 0);
        mainLayout.addView(totalTXT);

        //
        AlertDialogBuilder build = new AlertDialogBuilder(
                this,
                "Confirm Purchase",
                "Please review your order below:",
                true,
                (dialog, which) -> {
                    currentUser.getUserCart().clearCart(); // clear cart
                    currentUser.getUserCart().refreshCartView(this, findViewById(R.id.navCart), currentUser);
                    currentUser.getUserCart().refreshCartView(this, findViewById(R.id.notificationContainer), currentUser);
                    dialog.dismiss();

                    // Success + Redirect to Home
                    android.widget.Toast.makeText(this, "Order placed successfully!", android.widget.Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra(IntentKeys.USER_EMAIL, currentUser.getEmail());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                },
                (dialog, which) -> dialog.dismiss(), // Cancel
                mainLayout,
                ContextCompat.getColor(this, R.color.white),       // bgColor
                ContextCompat.getColor(this, R.color.darkgreen),   // titleColor
                ContextCompat.getColor(this, R.color.black),       // messageColor
                ContextCompat.getColor(this, R.color.green),       // positiveButtonColor
                ContextCompat.getColor(this, R.color.darkgray)     // negativeButtonColor
        );
    }


}