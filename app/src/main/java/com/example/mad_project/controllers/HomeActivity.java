package com.example.mad_project.controllers;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mad_project.R;
import com.example.mad_project.data.DataProvider;
import com.example.mad_project.models.Cart;
import com.example.mad_project.models.FoodItem;
import com.example.mad_project.models.User;
import com.example.mad_project.utils.AlertDialogBuilder;
import com.example.mad_project.utils.IntentKeys;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Intent intent;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }

    private void initialize(){

        TextView welcomeTXT = findViewById(R.id.welcomeTXT);
        FlexboxLayout flex = findViewById(R.id.flexboxlayout);

        //Get Current User
        intent = getIntent();
        currentUser = User.UserList.getUser(intent.getStringExtra(IntentKeys.USER_EMAIL));
        welcomeTXT.setText("Welcome, " + currentUser.getUsername() + "!");

        List<FoodItem> foodItems = DataProvider.provideFoodItems();

        NavBarControl.initializeNavBarControls(this, currentUser,
                findViewById(R.id.navMenu),
                findViewById(R.id.navCart),
                findViewById(R.id.navLogout),
                findViewById(R.id.notificationContainer));

        //Generate Food Cards
        for(FoodItem item : foodItems){
            LinearLayout foodCard = createFoodCard(item);
            flex.addView(foodCard);
        }

    }

    private LinearLayout createFoodCard(FoodItem item) {
        // Main card container
        LinearLayout mainLayout = new LinearLayout(this);
        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(
                dpToPx(300),
                dpToPx(355)
        );
        mainParams.setMargins(dpToPx(5), 0, dpToPx(5), dpToPx(10));
        mainLayout.setLayoutParams(mainParams);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedrect));
        mainLayout.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkgreen));

        // ImageView (placeholder)
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(160)
        );
        imageParams.setMargins(0, 0, 0, dpToPx(20));
        imageView.setLayoutParams(imageParams);
        //placeholder
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.cheeseburg));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBackground(ContextCompat.getDrawable(this, R.drawable.imagetopradius));
        imageView.setClipToOutline(true);
        mainLayout.addView(imageView);

        // Text container with weight
        LinearLayout textContainer = new LinearLayout(this);
        LinearLayout.LayoutParams textContainerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1.0f
        );
        textContainerParams.setMargins(dpToPx(40), 0, dpToPx(40), 0);
        textContainer.setLayoutParams(textContainerParams);
        textContainer.setOrientation(LinearLayout.VERTICAL);

        // Title TextView
        TextView titleText = new TextView(this);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, dpToPx(5));
        titleText.setLayoutParams(titleParams);
        titleText.setText(item.getFood());
        titleText.setTextColor(ContextCompat.getColor(this, R.color.white));
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        titleText.setGravity(Gravity.CENTER);
        titleText.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
        titleText.setTypeface(null, android.graphics.Typeface.BOLD);
        textContainer.addView(titleText);

        // Description TextView
        TextView descText = new TextView(this);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        descText.setLayoutParams(descParams);
        descText.setText(item.getDescription());
        descText.setTextColor(ContextCompat.getColor(this, R.color.white));
        descText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        descText.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
        textContainer.addView(descText);

        mainLayout.addView(textContainer);

        // Bottom container (price and button)
        LinearLayout bottomContainer = new LinearLayout(this);
        LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(50)
        );
        bottomParams.setMargins(0, 0, 0, dpToPx(10));
        bottomContainer.setLayoutParams(bottomParams);
        bottomContainer.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        bottomContainer.setPadding(dpToPx(40), 0, dpToPx(40), 0);

        // Price TextView
        TextView priceText = new TextView(this);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        priceText.setLayoutParams(priceParams);
        priceText.setText(String.format("PHP%.2f", item.getPrice()));
        priceText.setTextColor(ContextCompat.getColor(this, R.color.white));
        priceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        priceText.setTypeface(null, android.graphics.Typeface.BOLD);
        bottomContainer.addView(priceText);

        // Add Button
        ImageButton addButton = new ImageButton(this);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                dpToPx(38),
                dpToPx(38)
        );
        addButton.setLayoutParams(buttonParams);
        addButton.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedrect));
        addButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
        addButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.add));
        addButton.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        addButton.setOnClickListener(v -> {
            // Handle add button click
            onAddButtonClick(item);
        });
        bottomContainer.addView(addButton);

        mainLayout.addView(bottomContainer);

        return mainLayout;
    }

    private void onAddButtonClick(FoodItem item) {
        //Add to the User Cart
        currentUser.getUserCart().addFoodItem(item, 1, HomeActivity.this);
        currentUser.getUserCart().refreshCartView(this, findViewById(R.id.notificationContainer), currentUser);
    }

    // Helper method to convert dp to pixels
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}

