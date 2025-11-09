package com.example.mad_project.controllers;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

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
    double subtotalPrice;
    double deliveryFee = 50.0; // default fee for Standard Delivery
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

    private void initialize() {
        currentUser = User.UserList.getUser(getIntent().getStringExtra(IntentKeys.USER_EMAIL));

        itemContainer = findViewById(R.id.checkoutContainer);
        checkoutBTN = findViewById(R.id.checkoutBTN);
        otherRBTN = findViewById(R.id.otherRBTN);
        cashRBTN = findViewById(R.id.cashRBTN);
        subtotalTXT = findViewById(R.id.subtotalTXT);
        deliveryFeeTXT = findViewById(R.id.deliveryFeeTXT);
        totalPriceTXT = findViewById(R.id.totalPriceTXT);
        subtotalPrice = currentUser.getUserCart().calculateTotalPrice();

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

        // Default selections
        selectedPaymentMethod = "Cash on Delivery";
        selectedDeliveryOption = "Standard Delivery";
        isStandardSelected = true;

        deliveryFeeTXT.setText(String.format("PHP %.2f", deliveryFee));
        totalPriceTXT.setText(String.format("PHP %.2f", subtotalPrice + deliveryFee));

        standardOption.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkgreen));
        standardTXT.setTextColor(ContextCompat.getColor(this, R.color.white));
        standardPriceTXT.setTextColor(ContextCompat.getColor(this, R.color.white));

        updateCheckoutButtonState();

        cashChoice.setOnClickListener(v -> cashRBTN.setChecked(true));
        otherChoice.setOnClickListener(v -> otherRBTN.setChecked(true));

        cashRBTN.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                otherRBTN.setChecked(false);
                selectedPaymentMethod = "Cash on Delivery";
            }
        });

        otherRBTN.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cashRBTN.setChecked(false);
                selectedPaymentMethod = "Other Payment Method";
            }
        });

        checkoutBTN.setOnClickListener(v -> showCheckoutSummaryDialog());

        setupDeliveryOptionSelection();
        generateCheckoutItems(currentUser.getUserCart());
        subtotalTXT.setText(String.format("PHP %.2f", subtotalPrice));

        // Initialize Navbar
        NavBarControl.initializeNavBarControls(this, currentUser,
                findViewById(R.id.navMenu),
                findViewById(R.id.navCart),
                findViewById(R.id.navLogout),
                findViewById(R.id.Orders),
                findViewById(R.id.notificationContainer));
    }

    private void setupDeliveryOptionSelection() {
        priorityOption.setOnClickListener(v -> selectDeliveryOption("Priority Delivery", 100.00, priorityOption, priorityTXT, priorityPriceTXT));
        standardOption.setOnClickListener(v -> selectDeliveryOption("Standard Delivery", 50.00, standardOption, standardTXT, standardPriceTXT));
        saverOption.setOnClickListener(v -> selectDeliveryOption("Saver Delivery", 25.00, saverOption, saverTXT, saverPriceTXT));
    }

    private void selectDeliveryOption(String option, double fee, LinearLayout layout, TextView txt, TextView priceTxt) {
        resetDeliveryOptions();
        selectedDeliveryOption = option;
        deliveryFee = fee;

        deliveryFeeTXT.setText(String.format("PHP %.2f", deliveryFee));
        totalPriceTXT.setText(String.format("PHP %.2f", subtotalPrice + deliveryFee));

        layout.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkgreen));
        txt.setTextColor(ContextCompat.getColor(this, R.color.white));
        priceTxt.setTextColor(ContextCompat.getColor(this, R.color.white));

        isPrioritySelected = option.equals("Priority Delivery");
        isStandardSelected = option.equals("Standard Delivery");
        isSaverSelected = option.equals("Saver Delivery");

        updateCheckoutButtonState();
    }

    private void resetDeliveryOptions() {
        priorityOption.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lightgray));
        standardOption.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lightgray));
        saverOption.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lightgray));

        priorityTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgray));
        standardTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgray));
        saverTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgray));

        priorityPriceTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));
        standardPriceTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));
        saverPriceTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));
    }

    private void updateCheckoutButtonState() {
        boolean enabled = isPrioritySelected || isStandardSelected || isSaverSelected;
        checkoutBTN.setEnabled(enabled);
        checkoutBTN.setAlpha(enabled ? 1.0f : 0.5f);
    }

    private void generateCheckoutItems(Cart cart) {
        itemContainer.removeAllViews();
        for (FoodOrder order : cart.getCart().values()) {
            LinearLayout itemRow = new LinearLayout(this);
            itemRow.setOrientation(LinearLayout.HORIZONTAL);
            itemRow.setGravity(Gravity.CENTER_VERTICAL);
            itemRow.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
            itemRow.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_background));
            itemRow.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lightgray));
            itemRow.setElevation(dpToPx(3));

            ImageView img = new ImageView(this);
            img.setImageResource(order.getFood().getImageResourceId());
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dpToPx(60), dpToPx(60));
            imgParams.setMarginEnd(dpToPx(12));
            img.setLayoutParams(imgParams);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            itemRow.addView(img);

            LinearLayout infoLayout = new LinearLayout(this);
            infoLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            infoLayout.setLayoutParams(infoParams);

            TextView itemName = new TextView(this);
            itemName.setText(order.getName());
            itemName.setTextSize(15);
            itemName.setTypeface(null, Typeface.BOLD);
            itemName.setTextColor(ContextCompat.getColor(this, R.color.black));

            TextView itemQuantity = new TextView(this);
            itemQuantity.setText(String.format("x%d", order.getAmount()));
            itemQuantity.setTextSize(14);
            itemQuantity.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));
            itemQuantity.setTypeface(null, Typeface.BOLD);

            infoLayout.addView(itemName);
            infoLayout.addView(itemQuantity);
            itemRow.addView(infoLayout);

            TextView itemTotalPrice = new TextView(this);
            double total = order.getFood().getPrice() * order.getAmount();
            itemTotalPrice.setText(String.format("PHP %.2f", total));
            itemTotalPrice.setTextSize(14);
            itemTotalPrice.setTypeface(null, Typeface.BOLD);
            itemTotalPrice.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));
            itemTotalPrice.setGravity(Gravity.END);
            itemRow.addView(itemTotalPrice);

            itemContainer.addView(itemRow);
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    // === ALERT DIALOG INTEGRATION ===
    private void showCheckoutSummaryDialog() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));

        TextView title = new TextView(this);
        title.setText("Order Summary");
        title.setTypeface(null, Typeface.BOLD);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setPadding(0, 0, 0, dpToPx(10));
        mainLayout.addView(title);

        LinearLayout itemListLayout = new LinearLayout(this);
        itemListLayout.setOrientation(LinearLayout.VERTICAL);

        for (FoodOrder order : currentUser.getUserCart().getCart().values()) {
            LinearLayout itemRow = new LinearLayout(this);
            itemRow.setOrientation(LinearLayout.HORIZONTAL);
            itemRow.setPadding(0, dpToPx(8), 0, dpToPx(8));
            itemRow.setGravity(Gravity.CENTER_VERTICAL);

            ImageView img = new ImageView(this);
            img.setImageResource(order.getFood().getImageResourceId());
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dpToPx(45), dpToPx(45));
            imgParams.setMarginEnd(dpToPx(10));
            img.setLayoutParams(imgParams);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            itemRow.addView(img);

            TextView name = new TextView(this);
            name.setText(order.getName());
            name.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            itemRow.addView(name);

            TextView qty = new TextView(this);
            qty.setText("x" + order.getAmount());
            itemRow.addView(qty);

            TextView total = new TextView(this);
            total.setText(String.format("PHP %.2f", order.getFood().getPrice() * order.getAmount()));
            itemRow.addView(total);

            itemListLayout.addView(itemRow);
        }

        mainLayout.addView(itemListLayout);

        // Delivery info
        TextView deliveryInfo = new TextView(this);
        deliveryInfo.setText("Delivery: " + selectedDeliveryOption);
        mainLayout.addView(deliveryInfo);

        // Payment info
        TextView paymentInfo = new TextView(this);
        paymentInfo.setText("Payment: " + selectedPaymentMethod);
        mainLayout.addView(paymentInfo);

        // Total Price
        TextView totalPriceView = new TextView(this);
        totalPriceView.setText(String.format("Total: PHP %.2f", subtotalPrice + deliveryFee));
        totalPriceView.setTypeface(null, Typeface.BOLD);
        mainLayout.addView(totalPriceView);

        // Show the AlertDialog
        new AlertDialogBuilder(
                this,
                "Confirm Purchase",
                "Please review your order below:",
                true,
                (dialog, which) -> {
                    // On Confirm
                    currentUser.addOrdersToHistory(new ArrayList<>(currentUser.getUserCart().getCart().values()));
                    currentUser.getUserCart().clearCart();
                    currentUser.getUserCart().refreshCartView(this, findViewById(R.id.navCart), currentUser);
                    currentUser.getUserCart().refreshCartView(this, findViewById(R.id.notificationContainer), currentUser);

                    dialog.dismiss();

                    // Redirect to OrderTrackingActivity
                    Intent trackingIntent = new Intent(this, OrderTrackingActivity.class);
                    trackingIntent.putExtra(IntentKeys.USER_EMAIL, currentUser.getEmail());
                    trackingIntent.putExtra("DELIVERY_FEE", deliveryFee);
                    trackingIntent.putExtra("DELIVERY_OPTION", selectedDeliveryOption);
                    startActivity(trackingIntent);
                    finish();
                },
                (dialog, which) -> dialog.dismiss(),
                mainLayout,
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.darkgreen),
                ContextCompat.getColor(this, R.color.black),
                ContextCompat.getColor(this, R.color.green),
                ContextCompat.getColor(this, R.color.darkgray),
                "CONFIRM",
                "CANCEL"
        );
    }
}
