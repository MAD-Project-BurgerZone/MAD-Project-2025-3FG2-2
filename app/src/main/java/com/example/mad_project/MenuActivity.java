package com.example.mad_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);


        View card1 = findViewById(R.id.card1);
        ((TextView) card1.findViewById(R.id.txtTitle)).setText("Cheese Burger");
        ((TextView) card1.findViewById(R.id.txtDesc)).setText("Juicy beef patty with melted cheese.");
        ((TextView) card1.findViewById(R.id.price)).setText("₱129.00");


        View card2 = findViewById(R.id.card2);
        ((TextView) card2.findViewById(R.id.txtTitle)).setText("Chicken Sandwich");
        ((TextView) card2.findViewById(R.id.txtDesc)).setText("Crispy chicken fillet with mayo and lettuce.");
        ((TextView) card2.findViewById(R.id.price)).setText("₱115.00");


        View card3 = findViewById(R.id.card3);
        ((TextView) card3.findViewById(R.id.txtTitle)).setText("Pepperoni Pizza");
        ((TextView) card3.findViewById(R.id.txtDesc)).setText("Cheesy pizza topped with pepperoni.");
        ((TextView) card3.findViewById(R.id.price)).setText("₱89.00");


        View card4 = findViewById(R.id.card4);
        ((TextView) card4.findViewById(R.id.txtTitle)).setText("French Fries");
        ((TextView) card4.findViewById(R.id.txtDesc)).setText("Golden, crispy and lightly salted.");
        ((TextView) card4.findViewById(R.id.price)).setText("₱59.00");


        View card5 = findViewById(R.id.card5);
        ((TextView) card5.findViewById(R.id.txtTitle)).setText("Fried Chicken");
        ((TextView) card5.findViewById(R.id.txtDesc)).setText("Crispy skin with juicy flavored meat.");
        ((TextView) card5.findViewById(R.id.price)).setText("₱149.00");


        View card6 = findViewById(R.id.card6);
        ((TextView) card6.findViewById(R.id.txtTitle)).setText("Chocolate Shake");
        ((TextView) card6.findViewById(R.id.txtDesc)).setText("Creamy ice-cold chocolate blend.");
        ((TextView) card6.findViewById(R.id.price)).setText("₱79.00");

   
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
