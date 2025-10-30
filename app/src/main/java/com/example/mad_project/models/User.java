package com.example.mad_project.models;

import android.content.Context;

import com.example.mad_project.utils.AlertDialogBuilder;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {

    public static class UserList{

        private static HashMap<String, User> userList;

        private static void checkUserListInitialized(){
            if(userList == null){
                userList = new HashMap<>();
            }
        }

        //Check if correct credentials
        public static boolean checkCredentials(String email, String password, Context context){

            checkUserListInitialized();

            if(!userList.containsKey(email)){
                AlertDialogBuilder dialog = new AlertDialogBuilder(context, "Incorrect Credentials", "Incorrect Email or Password, Try Again", true, null);
                return false;
            }

            if(!userList.get(email).getPassword().equals(password)){
                AlertDialogBuilder dialog = new AlertDialogBuilder(context, "Incorrect Credentials", "Incorrect Email or Password, Try Again", true, null);
                return false;
            }

            return true;

        }

        public static void addUser(String email, User newUser){
            checkUserListInitialized();
            if(!userList.containsKey(email)){
                userList.put(email, newUser);
            }
        }

        public static boolean checkUser(String email){
            checkUserListInitialized();
            return userList.containsKey(email);
        }

        public static User getUser(String email){
            checkUserListInitialized();
            return userList.get(email);
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
        if(userCart == null){
            userCart = new Cart(this);
        }
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
