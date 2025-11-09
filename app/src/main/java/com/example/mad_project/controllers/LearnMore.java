package com.example.mad_project.controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mad_project.R;
import com.example.mad_project.models.User;
import com.example.mad_project.utils.IntentKeys;

public class LearnMore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_learn_more);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }

    private void initialize(){

        Intent intent = getIntent();
        User currentUser = User.UserList.getUser(intent.getStringExtra(IntentKeys.USER_EMAIL));
        //Load Navigation Bar
        NavBarControl.initializeNavBarControls(this, currentUser,
                findViewById(R.id.navMenu),
                findViewById(R.id.navCart),
                findViewById(R.id.navLogout),
                findViewById(R.id.Orders),
                findViewById(R.id.notificationContainer));
    }
}