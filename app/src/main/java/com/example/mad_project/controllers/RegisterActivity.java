package com.example.mad_project.controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mad_project.R;
import com.example.mad_project.models.User;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }

    private void initialize(){

        //Init Elements
        EditText username = findViewById(R.id.user);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText confirmPassword = findViewById(R.id.passwordconf);
        RadioButton terms = findViewById(R.id.agreeTerms);
        Button registerBTN = findViewById(R.id.register);
        TextView loginRedirect = findViewById(R.id.loginRedirect);

        //Handle Register Button
        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String errors = "";

                if (username.getText().toString().trim().isEmpty()
                        || email.getText().toString().trim().isEmpty()
                        || password.getText().toString().trim().isEmpty()
                        || confirmPassword.getText().toString().trim().isEmpty())
                {

                    if(username.getText().toString().isEmpty()){
                        username.setError("Username is required");
                    }
                    if(email.getText().toString().isEmpty()){
                        email.setError("Email is required");
                    }
                    if(password.getText().toString().isEmpty()) {
                        password.setError("Password is required");
                    }
                    if(confirmPassword.getText().toString().isEmpty()) {
                        confirmPassword.setError("Please confirm your password");
                    }

                    return;

                }

                //If email already exists
                if(User.UserList.userList.containsKey(email.getText().toString())){
                    email.setError("An account with this email already exists");
                    return;
                }

                //If password do not match
                if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                    confirmPassword.setError("Passwords do not match");
                    return;
                }

                //If terms not agreed
                if(!terms.isChecked()){
                    terms.setError("You must agree to the terms and conditions");
                    return;
                }

                //If no errors, proceed to register the user
                User.UserList.userList.put(email.getText().toString(),
                        new User(email.getText().toString(),
                                username.getText().toString(),
                                password.getText().toString()));
                finish();
            }
        });

        //Handle Login Redirect
        loginRedirect.setOnClickListener(v -> {
            finish();
        });

    }
}