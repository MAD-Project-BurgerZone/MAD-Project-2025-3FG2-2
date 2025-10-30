package com.example.mad_project.controllers;

import android.content.Context;
import android.content.DialogInterface;
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

import com.example.mad_project.R;
import com.example.mad_project.data.DataProvider;
import com.example.mad_project.models.User;
import com.example.mad_project.utils.AlertDialogBuilder;
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

        Context context = this;

        TextView email = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);
        Button button = findViewById(R.id.signinBTN);
        TextView register = findViewById(R.id.registerTXT);

        //Button Handle
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    if(email.getText().toString().isEmpty()){
                        email.setError("Email is required");
                    }
                    if(password.getText().toString().isEmpty()){
                        password.setError("Password is required");
                    }
                    return;
                }

                //Check credentials
                if(User.UserList.checkCredentials(email.getText().toString(), password.getText().toString(), LoginActivity.this)){

                    //Save the user logged in and the login state from the HashMap
                    AlertDialogBuilder dialog = new AlertDialogBuilder(context, "Success!", "Going to the Homepage...", false,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Create intent to go to MainActivity
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra(IntentKeys.USER_EMAIL, email.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }
                            });
                } else {
                    //Show errors
                    email.setError("Incorrect Credentials");
                    password.setError("Incorrect Credentials");
                }
            }

        });

        //Register Button Handle
        register.setOnClickListener(v -> {

            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);

        });

    }
}