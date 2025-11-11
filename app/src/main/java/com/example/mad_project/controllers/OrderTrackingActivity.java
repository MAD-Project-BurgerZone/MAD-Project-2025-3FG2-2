package com.example.mad_project.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mad_project.R;
import com.example.mad_project.models.FoodOrder;
import com.example.mad_project.models.User;
import com.example.mad_project.utils.IntentKeys;

import java.util.ArrayList;

public class OrderTrackingActivity extends AppCompatActivity {

    User currentUser;
    LinearLayout checkoutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.order_tracking_activity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
    }

    private void initialize() {
        currentUser = User.UserList.getUser(getIntent().getStringExtra(IntentKeys.USER_EMAIL));
        checkoutContainer = findViewById(R.id.checkoutContainer);

        NavBarControl.initializeNavBarControls(this, currentUser,
                findViewById(R.id.navMenu),
                findViewById(R.id.navCart),
                findViewById(R.id.navLogout),
                findViewById(R.id.Orders),
                findViewById(R.id.navHistory),
                findViewById(R.id.notificationContainer));

        displayOrders();

        findViewById(R.id.receivedBTN).setOnClickListener(v -> confirmOrderReceived());
    }

    private void displayOrders() {
        checkoutContainer.removeAllViews();

        if (currentUser.getTrackingOrders().isEmpty()) {
            TextView emptyMsg = new TextView(this);
            emptyMsg.setText("No orders found.");
            emptyMsg.setTextSize(18);
            emptyMsg.setPadding(10, 10, 10, 10);
            checkoutContainer.addView(emptyMsg);
            return;
        }

        double subtotal = 0.0;

        for (FoodOrder order : currentUser.getTrackingOrders()) {
            TextView tv1 = new TextView(this);
            TextView tv2 = new TextView(this);
            TextView tv3 = new TextView(this);

            double itemTotal = order.getFood().getPrice() * order.getAmount();
            subtotal += itemTotal;

            tv1.setText("Item name: " + order.getName());
            tv1.setTextSize(16);
            tv1.setPadding(10, 10, 10, 10);
            checkoutContainer.addView(tv1);

            tv3.setText("Quantity: " + order.getAmount());
            tv3.setTextSize(16);
            tv3.setPadding(10, 10, 10, 10);
            checkoutContainer.addView(tv3);

            tv2.setText("Item price: PHP " + String.format("%.2f", itemTotal));
            tv2.setTextSize(16);
            tv2.setPadding(10, 10, 10, 10);
            checkoutContainer.addView(tv2);
        }

        TextView deliveryTXT = new TextView(this);
        deliveryTXT.setText("Delivery Fee: PHP " + String.format("%.2f", currentUser.getLastDeliveryFee()));
        deliveryTXT.setTextSize(16);
        deliveryTXT.setPadding(10, 10, 10, 10);
        checkoutContainer.addView(deliveryTXT);

        TextView totalTXT = new TextView(this);
        totalTXT.setText("Total: PHP " + String.format("%.2f", subtotal + currentUser.getLastDeliveryFee()));
        totalTXT.setTextSize(18);
        totalTXT.setTypeface(null, android.graphics.Typeface.BOLD);
        totalTXT.setPadding(10, 20, 10, 10);
        checkoutContainer.addView(totalTXT);
    }

    private void confirmOrderReceived() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Receipt")
                .setMessage("Have you received your order?")
                .setPositiveButton("Yes", (dialog, which) -> handleOrderReceived())
                .setNegativeButton("No", null)
                .show();
    }

    private void handleOrderReceived() {
        if (!currentUser.getTrackingOrders().isEmpty()) {
            currentUser.addCompletedOrder(
                    new ArrayList<>(currentUser.getTrackingOrders()),
                    currentUser.getLastDeliveryFee()
            );
            currentUser.clearTrackingOrders();
            currentUser.setLastDeliveryFee(0.0);
        }

        Intent Intent = new Intent(this, OrderTrackingActivity.class);
        Intent.putExtra(IntentKeys.USER_EMAIL, currentUser.getEmail());
        startActivity(Intent);
        finish();
    }
}
