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
    public static void initializeNavBarControls(Context context, User currentUser, LinearLayout homeBTN, LinearLayout cartBTN, LinearLayout logoutBTN, LinearLayout NotificationContainer) {

        //Initialize NavBar Buttons
        currentUser.getUserCart().refreshCartView(context, NotificationContainer, currentUser);

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.removeExtra(IntentKeys.USER);

                AlertDialogBuilder dialog = new AlertDialogBuilder(
                        context,
                        "Log Out?",
                        "Are you sure you want to log out?",
                        false,
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface dialog, int which) {
                                AlertDialogBuilder confirm = new AlertDialogBuilder(
                                        context,
                                        "Success!",
                                        "You have been logged out.",
                                        false,
                                        new android.content.DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(android.content.DialogInterface dialog, int which) {
                                                context.startActivity(intent);
                                                if (context instanceof android.app.Activity) {
                                                    ((Activity) context).finish();
                                                }
                                            }
                                        });
                            }
                        },
                        null
                );
            }
        });
    }

}
