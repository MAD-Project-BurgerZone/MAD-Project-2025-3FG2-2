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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mad_project.R;
import com.example.mad_project.models.Cart;
import com.example.mad_project.models.CompletedOrder;
import com.example.mad_project.models.FoodOrder;
import com.example.mad_project.models.User;
import com.example.mad_project.utils.AlertDialogBuilder;
import com.example.mad_project.utils.IntentKeys;

import java.util.ArrayList;
import java.util.List;

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
    double deliveryFee = 50.0; // default fee
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

        NavBarControl.initializeNavBarControls(this, currentUser,
                findViewById(R.id.navMenu),
                findViewById(R.id.navCart),
                findViewById(R.id.navLogout),
                findViewById(R.id.Orders),
                findViewById(R.id.navHistory),
                findViewById(R.id.notificationContainer));
    }

    private void setupDeliveryOptionSelection() {
        priorityOption.setOnClickListener(v -> selectDeliveryOption("Priority Delivery", 100.0, priorityOption, priorityTXT, priorityPriceTXT));
        standardOption.setOnClickListener(v -> selectDeliveryOption("Standard Delivery", 50.0, standardOption, standardTXT, standardPriceTXT));
        saverOption.setOnClickListener(v -> selectDeliveryOption("Saver Delivery", 25.0, saverOption, saverTXT, saverPriceTXT));
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

        // Populate items for summary
        ScrollView scrollView = new ScrollView(this);
        LinearLayout itemListLayout = new LinearLayout(this);
        itemListLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(itemListLayout);

        for (FoodOrder order : currentUser.getUserCart().getCart().values()) {
            LinearLayout itemRow = new LinearLayout(this);
            itemRow.setOrientation(LinearLayout.HORIZONTAL);
            itemRow.setPadding(0, dpToPx(8), 0, dpToPx(8));
            itemRow.setGravity(Gravity.CENTER_VERTICAL);

            ImageView itemImage = new ImageView(this);
            itemImage.setImageResource(order.getFood().getImageResourceId());
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dpToPx(45), dpToPx(45));
            imgParams.setMarginEnd(dpToPx(10));
            itemImage.setLayoutParams(imgParams);
            itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            itemRow.addView(itemImage);

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

            itemListLayout.addView(itemRow);
        }

        mainLayout.addView(scrollView);

        // Delivery and payment info
        TextView deliveryInfo = new TextView(this);
        deliveryInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        deliveryInfo.setText("Delivery Option: " + selectedDeliveryOption);
        deliveryInfo.setTextColor(ContextCompat.getColor(this, R.color.darkgray));
        deliveryInfo.setPadding(0, dpToPx(10), 0, 0);
        mainLayout.addView(deliveryInfo);

        TextView paymentInfo = new TextView(this);
        paymentInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        paymentInfo.setText("Payment Method: " + selectedPaymentMethod);
        paymentInfo.setTextColor(ContextCompat.getColor(this, R.color.darkgray));
        paymentInfo.setPadding(0, dpToPx(6), 0, 0);
        mainLayout.addView(paymentInfo);

        TextView totalTXT = new TextView(this);
        double subtotal = currentUser.getUserCart().calculateTotalPrice();
        double totalPrice = subtotal + getSelectedDeliveryFee();
        totalTXT.setText(String.format("Total Price: PHP %.2f", totalPrice));
        totalTXT.setTypeface(null, Typeface.BOLD);
        totalTXT.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        totalTXT.setTextColor(ContextCompat.getColor(this, R.color.darkgreen));
        totalTXT.setPadding(0, dpToPx(10), 0, 0);
        mainLayout.addView(totalTXT);

        // === Checkout confirmation ===
        new AlertDialogBuilder(
                this,
                "Confirm Purchase",
                "Please review your order below:",
                true,
                (dialog, which) -> {
                    // Get cart items
                    List<FoodOrder> ordersToAdd = new ArrayList<>(currentUser.getUserCart().getCart().values());

                    // ✅ Add orders to tracking only
                    currentUser.addOrdersToTracking(ordersToAdd);

                    // ✅ Save delivery fee in user
                    currentUser.setLastDeliveryFee(getSelectedDeliveryFee());

                    // Clear the cart
                    currentUser.getUserCart().clearCart();
                    currentUser.getUserCart().refreshCartView(this, findViewById(R.id.navCart), currentUser);
                    currentUser.getUserCart().refreshCartView(this, findViewById(R.id.notificationContainer), currentUser);

                    dialog.dismiss();

                    // Success dialog (same as before)
                    LinearLayout successLayout = new LinearLayout(this);
                    successLayout.setOrientation(LinearLayout.VERTICAL);
                    successLayout.setGravity(Gravity.CENTER);
                    successLayout.setPadding(dpToPx(24), dpToPx(24), dpToPx(24), dpToPx(24));

                    ImageView checkIcon = new ImageView(this);
                    checkIcon.setImageResource(R.drawable.ic_check_green);
                    LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dpToPx(80), dpToPx(80));
                    iconParams.bottomMargin = dpToPx(16);
                    checkIcon.setLayoutParams(iconParams);
                    successLayout.addView(checkIcon);

                    TextView successText = new TextView(this);
                    successText.setText("Your order has been placed successfully!");
                    successText.setTextColor(ContextCompat.getColor(this, R.color.black));
                    successText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    successText.setGravity(Gravity.CENTER);
                    successLayout.addView(successText);

                    new AlertDialogBuilder(
                            this,
                            "Success",
                            "Order Placed Successfully!",
                            false,
                            (successDialog, which1) -> {
                                successDialog.dismiss();

                                Intent home = new Intent(this, HomeActivity.class);
                                home.putExtra(IntentKeys.USER_EMAIL, currentUser.getEmail());
                                startActivity(home);
                                finish();
                            },
                            null,
                            successLayout,
                            ContextCompat.getColor(this, R.color.white),
                            ContextCompat.getColor(this, R.color.darkgreen),
                            ContextCompat.getColor(this, R.color.black),
                            ContextCompat.getColor(this, R.color.green),
                            ContextCompat.getColor(this, R.color.darkgray),
                            "OK",
                            null
                    );

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

        private double getSelectedDeliveryFee() {
        if (isPrioritySelected) return 100.0;
        if (isStandardSelected) return 50.0;
        if (isSaverSelected) return 25.0;
        return 50.0;
    }
}
