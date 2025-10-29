package com.example.mad_project.models;
public class User {

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
