package com.example.mad_project.controllers;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

import static com.example.mad_project.utils.TextScaler.dpToPx;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.ScrollView;
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
import java.util.concurrent.atomic.AtomicInteger;

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

        Button LearnMoreBTN = findViewById(R.id.learnMoreBTN);
        Button OrderNowBTN = findViewById(R.id.orderNowBTN);

        //Direct to Learn More
        LearnMoreBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LearnMore.class);
                intent.putExtra(IntentKeys.USER_EMAIL, currentUser.getEmail());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //Click Order Now to Scroll to Food Items
        OrderNowBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scrollView = findViewById(R.id.foodItemsScroll);
                scrollView.smoothScrollTo(0, flex.getTop() - dpToPx(80, HomeActivity.this));
            }
        });

        //Get Food Items
        List<FoodItem> foodItems = DataProvider.provideFoodItems();

        NavBarControl.initializeNavBarControls(this, currentUser,
                findViewById(R.id.navMenu),
                findViewById(R.id.navCart),
                findViewById(R.id.navLogout),
                findViewById(R.id.Orders),
                findViewById(R.id.navHistory),
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
                dpToPx(300,this),
                dpToPx(355,this)
        );
        mainParams.setMargins(dpToPx(5,this), 0, dpToPx(5,this), dpToPx(10,this));
        mainLayout.setLayoutParams(mainParams);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedrect));
        mainLayout.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkgreen));

        // ImageView (placeholder)
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(160, this)
        );
        imageParams.setMargins(0, 0, 0, dpToPx(20, this));
        imageView.setLayoutParams(imageParams);
        //placeholder
        imageView.setImageDrawable(ContextCompat.getDrawable(this, item.getImageResourceId()));
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
        textContainerParams.setMargins(dpToPx(40, this), 0, dpToPx(40, this), 0);
        textContainer.setLayoutParams(textContainerParams);
        textContainer.setOrientation(LinearLayout.VERTICAL);

        // Title TextView
        TextView titleText = new TextView(this);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, dpToPx(5, this));
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
                dpToPx(50, this)
        );
        bottomParams.setMargins(0, 0, 0, dpToPx(10, this));
        bottomContainer.setLayoutParams(bottomParams);
        bottomContainer.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        bottomContainer.setPadding(dpToPx(40, this), 0, dpToPx(40, this), 0);

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
                dpToPx(38, this),
                dpToPx(38, this)
        );
        addButton.setLayoutParams(buttonParams);
        addButton.setBackground(ContextCompat.getDrawable(this, R.drawable.round));
        addButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
        addButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.add));
        addButton.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        addButton.setOnClickListener(v -> {
            // Handle add button click
            showAddToCartDialog(this, item);
        });
        bottomContainer.addView(addButton);

        mainLayout.addView(bottomContainer);

        return mainLayout;
    }

    private void showAddToCartDialog(Context context, FoodItem item) {
        //Build Custom View
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(dpToPx(20, this), dpToPx(20,this), dpToPx(20,this), dpToPx(20,this));
        mainLayout.setBackground(ContextCompat.getDrawable(context, R.color.white));

        //Image
        ImageView itemImage = new ImageView(context);
        itemImage.setImageResource(item.getImageResourceId());
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dpToPx(120, this), dpToPx(120, this));
        imgParams.gravity = Gravity.CENTER_HORIZONTAL;
        itemImage.setLayoutParams(imgParams);
        itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mainLayout.addView(itemImage);

        //Name
        TextView itemName = new TextView(context);
        itemName.setText(item.getFood());
        itemName.setTextSize(18);
        itemName.setTypeface(null, Typeface.BOLD);
        itemName.setTextColor(ContextCompat.getColor(context, R.color.black));
        itemName.setGravity(Gravity.CENTER_HORIZONTAL);
        itemName.setPadding(0, dpToPx(12, this), 0, dpToPx(8, this));
        mainLayout.addView(itemName);

        //Quantity Layout
        LinearLayout qtyLayout = new LinearLayout(context);
        qtyLayout.setOrientation(LinearLayout.HORIZONTAL);
        qtyLayout.setGravity(Gravity.CENTER);
        qtyLayout.setPadding(0, dpToPx(8,this), 0, dpToPx(8,this));

        ImageButton minusBTN = new ImageButton(context);
        minusBTN.setImageResource(R.drawable.minus_white);
        minusBTN.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_background));
        minusBTN.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.darkgreen));
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(dpToPx(35, this), dpToPx(35,this));
        minusBTN.setLayoutParams(btnParams);

        TextView amountTXT = new TextView(context);
        amountTXT.setText("1");
        amountTXT.setTextSize(16);
        amountTXT.setTypeface(null, Typeface.BOLD);
        amountTXT.setTextColor(ContextCompat.getColor(context, R.color.black));
        amountTXT.setPadding(dpToPx(10, this), 0, dpToPx(10, this), 0);

        ImageButton plusBTN = new ImageButton(context);
        plusBTN.setImageResource(R.drawable.add_white);
        plusBTN.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_background));
        plusBTN.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.darkgreen));
        plusBTN.setLayoutParams(btnParams);

        qtyLayout.addView(minusBTN);
        qtyLayout.addView(amountTXT);
        qtyLayout.addView(plusBTN);
        mainLayout.addView(qtyLayout);

        //Quantity control logic
        final AtomicInteger currentAmount = new AtomicInteger(1);

        minusBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentAmount.get() > 1) {
                    currentAmount.decrementAndGet();
                    amountTXT.setText(String.valueOf(currentAmount.get()));
                }
            }
        });
        plusBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAmount.incrementAndGet();
                amountTXT.setText(String.valueOf(currentAmount.get()));
            }
        });

        //Show the Dialog
        new AlertDialogBuilder(
                context,
                "Add to Cart",
                "Select amount for " + item.getFood(),
                true,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentUser.getUserCart().addFoodItem(item, currentAmount.get(), context);
                        currentUser.getUserCart().refreshCartView(
                                context,
                                ((AppCompatActivity) context).findViewById(R.id.notificationContainer),
                                currentUser
                        );
                        dialog.dismiss();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                },
                mainLayout, // customview
                ContextCompat.getColor(context, R.color.white),
                ContextCompat.getColor(context, R.color.darkgreen),
                ContextCompat.getColor(context, R.color.black),
                ContextCompat.getColor(context, R.color.green),
                ContextCompat.getColor(context, R.color.darkgray),
                "Add to Cart",
                "Cancel"
        );
    }

}

