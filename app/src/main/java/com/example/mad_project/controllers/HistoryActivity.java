package com.example.mad_project.controllers;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mad_project.R;
import com.example.mad_project.models.CompletedOrder;
import com.example.mad_project.models.FoodOrder;
import com.example.mad_project.models.User;
import com.example.mad_project.utils.IntentKeys;

public class HistoryActivity extends AppCompatActivity {

    User currentUser;
    LinearLayout historyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
    }

    private void initialize() {
        currentUser = User.UserList.getUser(getIntent().getStringExtra(IntentKeys.USER_EMAIL));
        historyContainer = findViewById(R.id.historyContainer);

        NavBarControl.initializeNavBarControls(
                this,
                currentUser,
                findViewById(R.id.navMenu),
                findViewById(R.id.navCart),
                findViewById(R.id.navLogout),
                findViewById(R.id.Orders),
                findViewById(R.id.navHistory),
                findViewById(R.id.notificationContainer)
        );

        displayHistory();
    }

    private void displayHistory() {
        historyContainer.removeAllViews();

        if (currentUser.getOrderHistory().isEmpty()) {
            TextView emptyMsg = new TextView(this);
            emptyMsg.setText("No order history found.");
            emptyMsg.setTextSize(18);
            emptyMsg.setTextColor(Color.BLACK); // Make it visible
            emptyMsg.setPadding(10, 10, 10, 10);
            historyContainer.addView(emptyMsg);
            return;
        }

        for (CompletedOrder completed : currentUser.getOrderHistory()) {
            double subtotal = 0.0;

            for (FoodOrder order : completed.getOrders()) {
                TextView tv1 = new TextView(this);
                TextView tv2 = new TextView(this);
                TextView tv3 = new TextView(this);

                double itemTotal = order.getFood().getPrice() * order.getAmount();
                subtotal += itemTotal;

                tv1.setText("Item: " + order.getName());
                tv1.setTextSize(16);
                tv1.setTextColor(Color.BLACK); // visible
                tv1.setPadding(10, 10, 10, 10);
                historyContainer.addView(tv1);

                tv3.setText("Quantity: " + order.getAmount());
                tv3.setTextSize(16);
                tv3.setTextColor(Color.BLACK); // visible
                tv3.setPadding(10, 10, 10, 10);
                historyContainer.addView(tv3);

                tv2.setText("Price: PHP " + String.format("%.2f", itemTotal));
                tv2.setTextSize(16);
                tv2.setTextColor(Color.BLACK); // visible
                tv2.setPadding(10, 10, 10, 10);
                historyContainer.addView(tv2);
            }

            TextView deliveryTXT = new TextView(this);
            deliveryTXT.setText("Delivery Fee: PHP " + String.format("%.2f", completed.getDeliveryFee()));
            deliveryTXT.setTextSize(16);
            deliveryTXT.setTextColor(Color.BLACK); // visible
            deliveryTXT.setPadding(10, 10, 10, 10);
            historyContainer.addView(deliveryTXT);

            TextView totalTXT = new TextView(this);
            totalTXT.setText("Total: PHP " + String.format("%.2f", subtotal + completed.getDeliveryFee()));
            totalTXT.setTextSize(18);
            totalTXT.setTypeface(null, android.graphics.Typeface.BOLD);
            totalTXT.setTextColor(Color.BLACK); // visible
            totalTXT.setPadding(10, 20, 10, 10);
            historyContainer.addView(totalTXT);

            TextView separator = new TextView(this);
            separator.setText("_______________________________________________");
            separator.setTextColor(Color.BLACK); // visible
            separator.setPadding(0, 10, 0, 10);
            historyContainer.addView(separator);
        }
    }
}
