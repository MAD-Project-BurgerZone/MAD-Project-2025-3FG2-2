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

import com.example.mad_project.MainActivity;
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

        User.UserList.userList = DataProvider.provideUsers();
        Context context = this;

        TextView email = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);
        Button button = findViewById(R.id.signinBTN);
        TextView register = findViewById(R.id.registerTXT);

        //Button Handle
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(User.UserList.checkCredentials(email.getText().toString(), password.getText().toString(), LoginActivity.this)){

                    //Save the user logged in and the login state from the HashMap
                    User loggedInUser = User.UserList.userList.get(email.getText().toString());
                    AlertDialogBuilder dialog = new AlertDialogBuilder(context, "Success!", "Going to the Homepage...", false,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Create intent to go to MainActivity
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra(IntentKeys.IS_LOGGED_IN, true);
                                    intent.putExtra(IntentKeys.USER, loggedInUser);
                                    startActivity(intent);
                                    finish();
                                }
                            });
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