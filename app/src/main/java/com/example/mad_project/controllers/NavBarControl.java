package com.example.mad_project.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import com.example.mad_project.models.User;
import com.example.mad_project.utils.AlertDialogBuilder;
import com.example.mad_project.utils.IntentKeys;

public class NavBarControl {
    public static void initializeNavBarControls(
            Context context,
            User currentUser,
            LinearLayout homeBTN,
            LinearLayout cartBTN,
            LinearLayout logoutBTN,
            LinearLayout ordersBTN,    // Tracking
            LinearLayout historyBTN,   // History
            LinearLayout NotificationContainer
    ) {

        // Refresh cart badge/notification
        currentUser.getUserCart().refreshCartView(context, NotificationContainer, currentUser);

        // LOGOUT BUTTON
        logoutBTN.setOnClickListener(v -> {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.removeExtra(IntentKeys.USER);
            new AlertDialogBuilder(
                    context,
                    "Log Out?",
                    "Are you sure you want to log out?",
                    false,
                    (dialog, which) -> new AlertDialogBuilder(
                            context,
                            "Success!",
                            "You have been logged out.",
                            false,
                            (confirmDialog, which1) -> {
                                context.startActivity(intent);
                                intent.removeExtra(IntentKeys.USER_EMAIL);
                                if (context instanceof Activity) {
                                    ((Activity) context).finish();
                                }
                            }
                    ),
                    null
            );
        });

        // CART BUTTON
        cartBTN.setOnClickListener(v -> {
            Intent intent = new Intent(context, CartpageActivity.class);
            intent.putExtra(IntentKeys.USER_EMAIL, currentUser.getEmail());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        // HOME BUTTON
        homeBTN.setOnClickListener(v -> {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.putExtra(IntentKeys.USER_EMAIL, currentUser.getEmail());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        // ORDERS BUTTON → TRACKING
        ordersBTN.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderTrackingActivity.class);
            intent.putExtra(IntentKeys.USER_EMAIL, currentUser.getEmail());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        // HISTORY BUTTON → HISTORY PAGE
        historyBTN.setOnClickListener(v -> {
            Intent intent = new Intent(context, HistoryActivity.class);
            intent.putExtra(IntentKeys.USER_EMAIL, currentUser.getEmail());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }
}
