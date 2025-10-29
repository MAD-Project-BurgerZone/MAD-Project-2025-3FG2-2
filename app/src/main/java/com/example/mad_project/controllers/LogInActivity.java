package com.example.mad_project.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.models.User;
import com.example.mad_project.utils.IntentKeys;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }

    private void initialize(){

        TextView email = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);
        Button button = findViewById(R.id.signinBTN);
        TextView register = findViewById(R.id.registerTXT);

        Boolean isLoggedIn = getIntent().getBooleanExtra(IntentKeys.IS_LOGGED_IN, false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(User.UserList.checkCredentials(email.getText().toString(), password.getText().toString(), LoginActivity.this)){

                    //Save the user logged in and the login state from the HashMap
                    User loggedInUser = User.UserList.userList.get(email.getText().toString());

                    //Create intent to go to MainActivity
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra(IntentKeys.IS_LOGGED_IN, true);
                    intent.putExtra(IntentKeys.USER, loggedInUser);
                    startActivity(intent);
                    finish();
                }
            }

        });

        register.setOnClickListener(v -> {

            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();

        });


        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        //intent.putExtra(IntentKeys.USER, )

    }
}