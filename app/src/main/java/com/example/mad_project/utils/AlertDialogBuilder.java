package com.example.mad_project.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.mad_project.R;

public class AlertDialogBuilder {

    public AlertDialogBuilder(Context context, String title, String message, boolean cancellable, DialogInterface.OnClickListener callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancellable)
                .setPositiveButton("OK", callback)
                .show();
    }

    public AlertDialogBuilder(Context context, String title, String message, boolean cancellable, DialogInterface.OnClickListener callback, DialogInterface.OnClickListener negativeCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancellable)
                .setPositiveButton("Yes", callback)
                .setNegativeButton("No", negativeCallback)
                .show();
    }

    public AlertDialogBuilder(Context context, String title, String message, boolean cancellable, DialogInterface.OnClickListener callback, DialogInterface.OnClickListener negativeCallback, View customView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancellable)
                .setPositiveButton("Yes", callback)
                .setNegativeButton("No", negativeCallback)
                .setView(customView)
                .show();
    }

    public AlertDialogBuilder(Context context, String title, String message, boolean cancellable, DialogInterface.OnClickListener callback, DialogInterface.OnClickListener negativeCallback, View customView, int bgColor, int titleColor, int messageColor, int positiveButtonColor, int negativeButtonColor) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancellable)
                .setPositiveButton("Submit", callback);

        if (negativeCallback != null) {
            builder.setNegativeButton("Cancel", negativeCallback);
        }

        if (customView != null) {
            builder.setView(customView);
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        //Set background color
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(bgColor));
        }

        //Change title and message colors
        TextView titleView = dialog.findViewById(androidx.appcompat.R.id.alertTitle);
        TextView messageView = dialog.findViewById(android.R.id.message);

        if (titleView != null) titleView.setTextColor(titleColor);
        if (messageView != null) messageView.setTextColor(messageColor);

        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(positiveButtonColor);
        }

        if (dialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(negativeButtonColor);
        }

    }

}
