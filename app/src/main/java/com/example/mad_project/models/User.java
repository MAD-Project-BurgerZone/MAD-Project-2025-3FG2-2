package com.example.mad_project.models;

import android.content.Context;

import com.example.mad_project.utils.AlertDialogBuilder;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {

    public static class UserList{

        public static HashMap<String, User> userList;

        //Check if correct credentials
        public static boolean checkCredentials(String email, String password, Context context){

            if(!userList.containsKey(email)){
                AlertDialogBuilder dialog = new AlertDialogBuilder(context, "Incorrect Credentials", "Incorrect Email or Password, Try Again", true);
                return false;
            }

            if(!userList.get(email).getPassword().equals(password)){
                AlertDialogBuilder dialog = new AlertDialogBuilder(context, "Incorrect Credentials", "Incorrect Email or Password, Try Again", true);
                return false;
            }

            AlertDialogBuilder dialog = new AlertDialogBuilder(context, "Success!", "Going to the Homepage...", false);
            return true;

        }

    }

    private String email;
    private String username;
    private String password;

    private Cart userCart;

    public User(String email, String username, String password){
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Cart getUserCart(){
        return userCart;
    }

    public void setUserCart(Cart userCart){
        this.userCart = userCart;
    }

}
