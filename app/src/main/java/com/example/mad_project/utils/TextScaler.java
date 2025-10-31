package com.example.mad_project.utils;

import android.content.Context;

public class TextScaler {

    public static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}
