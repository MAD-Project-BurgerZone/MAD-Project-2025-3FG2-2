package com.example.mad_project.utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class AlertDialogBuilder {

    public AlertDialogBuilder(Context context, String title, String message, boolean cancellable){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancellable)
                .setPositiveButton("OK", null)
                .show();
    }

}
